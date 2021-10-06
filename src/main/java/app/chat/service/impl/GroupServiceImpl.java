package app.chat.service.impl;

import app.chat.entity.group.Group;
import app.chat.entity.group.GroupUser;
import app.chat.entity.group.GroupUserPermission;
import app.chat.entity.user.User;
import app.chat.entity.user.Username;
import app.chat.enums.*;
import app.chat.helpers.UserSession;
import app.chat.helpers.Utils;
import app.chat.mapper.MapstructMapper;
import app.chat.model.req.group.GroupReqDto;
import app.chat.model.resp.group.GroupRespDto;
import app.chat.repository.ChatRoleRepo;
import app.chat.repository.GroupUserPermissionRepo;
import app.chat.repository.group.GroupRepo;
import app.chat.repository.group.GroupUserRepo;
import app.chat.service.ErrorService;
import app.chat.service.GroupService;
import app.chat.service.UsernameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static app.chat.model.ApiResponse.response;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupUserPermissionRepo groupUserPermissionRepo;
    private final UsernameService usernameService;
    private final GroupUserRepo groupMemberRepo;
    private final ChatRoleRepo chatRoleRepo;
    private final MapstructMapper mapper;
    private final GroupRepo groupRepo;
    private final UserSession session;
    private final ErrorService error;


    @Override
    public ResponseEntity<?> getGroup(Long groupId) {
        User activeUser = session.getUser();
        Optional<Group> optionalGroup = groupRepo.findById(groupId);
        if (optionalGroup.isEmpty()) {
            return response(error.message(ErrorEnum.GROUP_NOT_FOUND.getCode(), Lang.RU, groupId), HttpStatus.NOT_FOUND);
        }

//        //todo check existing group
//        Optional<Group> groupOptional = groupRepo.findById(groupId);
//        if (groupOptional.isEmpty()) {
//            return response(error.message(ErrorEnum.GROUP_NOT_FOUND.getCode(), Lang.RU, groupId), HttpStatus.NOT_FOUND);
//        }
//        //todo check am I a member of this group
//        Optional<GroupUser> memberOptional = groupMemberRepo.findByUser_IdAndGroup_IdAndActiveTrue(activeUser.getId(), groupId);
//        if (memberOptional.isEmpty()) {
//            return response(error.message(ErrorEnum.YOU_NOT_MEMBER_IN_GROUP.getCode(), Lang.RU, groupId), HttpStatus.BAD_REQUEST);
//        }

        Group group = optionalGroup.get();

        GroupRespDto groupRespDto = mapper.toGroupRespDto(group);
        groupRespDto.setMemberCount((long) groupMemberRepo.findAllByGroup_IdAndActiveTrue(group.getId()).size());

        return response(groupRespDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAll() {
        List<Group> allGroups = groupRepo.findAll();
        List<GroupRespDto> allGroupRespDto = mapper.toGroupRespDtoList(allGroups);
        allGroupRespDto.forEach(groupRespDto -> {
            groupRespDto.setMemberCount((long) groupMemberRepo.findAllByGroup_IdAndActiveTrue(groupRespDto.getId()).size());
        });
        return response(allGroupRespDto, HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<?> createGroup(GroupReqDto dto) {
        if (!Utils.isEmpty(dto.getDescription())) {
            if (dto.getDescription().length() > 255) {
                return response(error.message(ErrorEnum.LARGE_DESCRIPTION.getCode(), Lang.RU, 255L), HttpStatus.BAD_REQUEST);
            }
        }

        Username username = null;
        if (!Utils.isEmpty(dto.getUsername())) {
            username = usernameService.create(dto.getUsername());
            if (Utils.isEmpty(username)) {
                return response(error.message(ErrorEnum.USERNAME_ALREADY_EXIST.getCode(), Lang.RU, dto.getUsername()));
            }
        }

        Group group = new Group();
        group.setChatType(ChatType.GROUP);
        group.setUsername(username);
        group.setSecurityType(Utils.isEmpty(group.getUsername()) ? SecurityType.PRIVATE : SecurityType.PUBLIC);
        group.setTitle(dto.getTitle());
        group.setDescription(dto.getDescription());
        group = groupRepo.save(group);
        saveUserToGroup(session.getUser(), group, ChatRoleEnum.OWNER);

        GroupRespDto groupRespDto = mapper.toGroupRespDto(group);
        groupRespDto.setMemberCount((long) groupMemberRepo.findAllByGroup_IdAndActiveTrue(group.getId()).size());

        return response(groupRespDto, HttpStatus.CREATED);
    }

    @Transactional
    @Override
    public ResponseEntity<?> updateGroup(Long groupId, GroupReqDto dto) {
        User activeUser = session.getUser();
        Optional<Group> optionalGroup = groupRepo.findById(groupId);
        if (optionalGroup.isEmpty()) {
            return response(error.message(ErrorEnum.GROUP_NOT_FOUND.getCode(), Lang.RU, groupId), HttpStatus.NOT_FOUND);
        }

        //todo check permission update info
        List<String> groupUserPermission = groupUserPermissionRepo.getPermissions(activeUser.getId(), groupId);
        if (!groupUserPermission.contains(ChatPermissions.CHANGE_INFO.name())) {
            return response(error.message(ErrorEnum.NO_PERMISSION.getCode(), Lang.RU, groupId));
        }

        Group group = optionalGroup.get();

        if (!Utils.isEmpty(dto.getDescription())) {
            if (dto.getDescription().length() > 255) {
                return response(error.message(ErrorEnum.LARGE_DESCRIPTION.getCode(), Lang.RU, 255L), HttpStatus.BAD_REQUEST);
            }
        }

        Username newUsername = null;
        if (!Utils.isEmpty(dto.getUsername())) {
            newUsername = usernameService.create(dto.getUsername());
            if (Utils.isEmpty(newUsername)) {
                return response(error.message(ErrorEnum.USERNAME_ALREADY_EXIST.getCode(), Lang.RU, dto.getUsername()));
            } else {
                if (!Utils.isEmpty(group.getUsername())) {
                    usernameService.delete(group.getUsername().getId());
                }
            }
        }

        group.setSecurityType(Utils.isEmpty(group.getUsername()) ? SecurityType.PRIVATE : SecurityType.PUBLIC);
        group.setTitle(Utils.isEmpty(dto.getTitle()) ? group.getTitle() : dto.getTitle());
        group.setDescription(Utils.isEmpty(dto.getDescription()) ? group.getDescription() : dto.getDescription());
        group.setUsername(Utils.isEmpty(dto.getUsername()) ? group.getUsername() : newUsername);
        group = groupRepo.save(group);

        GroupRespDto groupRespDto = mapper.toGroupRespDto(group);
        groupRespDto.setMemberCount((long) groupMemberRepo.findAllByGroup_IdAndActiveTrue(group.getId()).size());

        return response(group, HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<?> deleteGroup(Long id) {
        Optional<Group> optionalGroup = groupRepo.findById(id);
        if (optionalGroup.isEmpty()) {
            return response(error.message(ErrorEnum.GROUP_NOT_FOUND.getCode(), Lang.RU, id), HttpStatus.NOT_FOUND);
        }
        Group group = optionalGroup.get();
        group.setActive(false);
        groupRepo.save(group);
        if (!Utils.isEmpty(group.getUsername())){
            usernameService.delete(group.getUsername().getId());
        }

        return response(true);
    }

    @Override
    public List<String> getPermissions(Long userId, Long groupId) {
        return groupUserPermissionRepo.getPermissions(userId, groupId);
    }

    @Transactional
    public void saveUserToGroup(User user, Group group, ChatRoleEnum role) {
        GroupUser groupUser = new GroupUser(group);
        groupUser.setUser(user);
        groupUser.setChatRole(chatRoleRepo.findByRole(role).get());
        attachPermissions(groupMemberRepo.save(groupUser), role);
    }

    @Transactional
    public void attachPermissions(GroupUser groupUser, ChatRoleEnum role) {
        List<GroupUserPermission> permissions = new ArrayList<>();

        Set<ChatPermissions> permission = ChatPermissions.getPermission(role);

        permission.forEach(chatPermission -> {
            permissions.add(new GroupUserPermission(groupUser, chatPermission));
        });
        groupUserPermissionRepo.saveAll(permissions);
    }

}


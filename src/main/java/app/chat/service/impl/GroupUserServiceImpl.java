package app.chat.service.impl;

import app.chat.entity.group.Group;
import app.chat.entity.group.GroupUser;
import app.chat.entity.group.GroupUserPermission;
import app.chat.entity.personal.Personal;
import app.chat.entity.user.User;
import app.chat.enums.ChatPermissions;
import app.chat.enums.ChatRoleEnum;
import app.chat.enums.ErrorEnum;
import app.chat.enums.Lang;
import app.chat.helpers.UserSession;
import app.chat.helpers.Utils;
import app.chat.model.dto.MemberDto;
import app.chat.model.req.group.GroupMemberReqDto;
import app.chat.model.resp.group.GroupMemberRespDto;
import app.chat.repository.ChatRoleRepo;
import app.chat.repository.GroupUserPermissionRepo;
import app.chat.repository.group.GroupRepo;
import app.chat.repository.group.GroupUserRepo;
import app.chat.repository.user.UserRepo;
import app.chat.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@Transactional
@RequiredArgsConstructor
public class GroupUserServiceImpl implements GroupUserService {

    private final GroupUserPermissionRepo groupUserPermissionRepo;
    private final UserContactService userContactService;
    private final ChannelUserService channelUserService;
    private final GroupUserRepo groupMemberRepo;
    private final GroupUserRepo groupUserRepo;
    private final ChatRoleRepo chatRoleRepo;
    private final UserSession session;
    private final GroupRepo groupRepo;
    private final ErrorService error;
    private final UserRepo userRepo;
    private final GroupService groupService;
    @Autowired
    PersonalService personalService;


    @Override
    public ResponseEntity<?> addMembers(GroupMemberReqDto dto) {
        User activeUser = session.getUser();

        //todo check existing group
        Optional<Group> groupOptional = groupRepo.findById(dto.getGroupId());
        if (groupOptional.isEmpty()) {
            return response(error.message(ErrorEnum.GROUP_NOT_FOUND.getCode(), Lang.RU, dto.getGroupId()));
        }
        Group group = groupOptional.get();
        //todo check am I a member of this group
        Optional<GroupUser> memberOptional = groupMemberRepo.findByUser_IdAndGroup_IdAndActiveTrue(activeUser.getId(), dto.getGroupId());
        if (memberOptional.isEmpty()) {
            return response(error.message(ErrorEnum.YOU_NOT_MEMBER_IN_GROUP.getCode(), Lang.RU, dto.getGroupId()), HttpStatus.BAD_REQUEST);
        }
        //todo check permission for add member
        List<String> permissions = groupUserPermissionRepo.getPermissions(activeUser.getId(), group.getId());
        if (!permissions.contains(ChatPermissions.ADD_MEMBER.name())) {
            return response(error.message(ErrorEnum.NO_PERMISSION.getCode(), Lang.RU, dto.getGroupId()));
        }

        //todo check joining member not delete account
        List<GroupMemberRespDto> newMembers = new ArrayList<>();

        dto.getMemberIdList().forEach(memberId -> {
            Optional<User> userOptional = userRepo.findById(memberId);
            if (userOptional.isEmpty()) {
                newMembers.add(new GroupMemberRespDto(memberId, false, error.message(ErrorEnum.USER_NOT_FOUND.getCode(), Lang.RU, memberId)));
            } else {
                User newMember = userOptional.get();
                //todo find my group mate for add user validation
                boolean itsMyFriend = !Utils.isEmpty(findMyGroupMate(memberId));

                //todo find in my contacts
                if (!Utils.isEmpty(userContactService.findInMyContacts(memberId)) && !itsMyFriend) {
                    itsMyFriend = true;
                }
                //todo men admin yoki owner bo`lgan kanallardan tekshirish
                if (!Utils.isEmpty(channelUserService.findMyAuthorityChannelFollower(memberId)) && !itsMyFriend) {
                    itsMyFriend = true;
                }
                //todo find in my personals
               if (!itsMyFriend){
                   for (Personal personal : personalService.getMyPersonals()) {
                       if (personal.getUser1().getId().equals(memberId) || personal.getUser2().getId().equals(memberId)) {
                           itsMyFriend = true;
                           break;
                       }
                   }
               }

                if (!userOptional.get().isActive()) {
                    newMembers.add(new GroupMemberRespDto(memberId, false, error.message(ErrorEnum.USER_NOT_FOUND.getCode(), Lang.RU, memberId)));
                } else if (!itsMyFriend) {
                    newMembers.add(new GroupMemberRespDto(memberId, false, error.message(ErrorEnum.CHAT_NOT_FOUND.getCode(), Lang.RU, memberId)));
                } else {
                    //todo already exist in group
                    if (groupUserRepo.existsByUserAndGroupAndActiveTrue(newMember, group)) {
                        newMembers.add(new GroupMemberRespDto(memberId, false, error.message(ErrorEnum.MEMBER_ALREADY_EXIST.getCode(), Lang.RU, memberId)));
                    } else if (false/**todo check the privacy policy of joining members**/) {
                        //todo userni gurux yoki kanalga qo`shish ximoyasini tikshirish qismi
                        //todo user settings service kutilmoqda
                    } else {
                        newMembers.add(new GroupMemberRespDto(memberId, true, ""));
                    }
                }
            }
        });

        newMembers.forEach(newMember -> {
            if (newMember.getAdded()) {
                GroupUser groupUser = new GroupUser();
                groupUser.setGroup(group);
                groupUser.setUser(userRepo.getById(newMember.getUserId()));
                groupUser.setIsMute(false);
                groupUser.setIsPinned(false);
                groupUser.setChatRole(chatRoleRepo.findByRole(ChatRoleEnum.MEMBER).get());
                attachPermissions(groupUserRepo.save(groupUser), ChatRoleEnum.MEMBER);
                groupUserRepo.save(groupUser);
            }
        });

        return response(newMembers, (long) newMembers.size(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getSortedMembers(Long groupId) {
        Optional<Group> optionalGroup = groupRepo.findById(groupId);
        if (optionalGroup.isEmpty()) {
            return response(error.message(30, Lang.RU, groupId), HttpStatus.NOT_FOUND);
        }
        List<GroupUser> groupMembers = groupUserRepo.findAllByGroup_IdAndActiveTrue(groupId);//, Sort.by("user.firstName").descending()
        List<MemberDto> responseGroupMembersDto = new ArrayList<>();
        groupMembers.forEach(groupUser -> {
            MemberDto memberDto = new MemberDto();
            memberDto.setMember(groupUser.getUser());
            memberDto.setChatId(groupId);
            memberDto.setRole(groupUser.getChatRole().getRole());
            responseGroupMembersDto.add(memberDto);
        });
        return response(responseGroupMembersDto, (long) responseGroupMembersDto.size(), HttpStatus.OK);
    }

    @Override
    public GroupUser findMyGroupMate(Long searchedFriendId) {

        //todo get i`m follow groups

        List<Group> groups = userRepo.getGroup(session.getUserId());
        for (Group group : groups) {
            if (group.isActive()) {
                //todo get i`m follow group members
                List<GroupUser> members = groupUserRepo.findByGroup_IdAndActiveTrue(group.getId());

                //todo for each group members
                for (GroupUser member : members) {
                    if (member.getUser().isActive()) {
                        if (member.getUser().getId().equals(searchedFriendId)) {
                            return member;
                        }
                    }
                }

            }
        }
        return null;
    }

    public void attachPermissions(GroupUser groupUser, ChatRoleEnum role) {
        List<GroupUserPermission> permissions = new ArrayList<>();
        Set<ChatPermissions> permission = ChatPermissions.getPermission(role);
        permission.forEach(chatPermission -> {
            permissions.add(new GroupUserPermission(groupUser, chatPermission));
        });
        groupUserPermissionRepo.saveAll(permissions);
    }
}

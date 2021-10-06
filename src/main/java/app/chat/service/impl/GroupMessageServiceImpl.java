package app.chat.service.impl;

import app.chat.entity.Attachment;
import app.chat.entity.group.*;
import app.chat.entity.user.User;
import app.chat.enums.ChatPermissions;
import app.chat.enums.ErrorEnum;
import app.chat.enums.Lang;
import app.chat.enums.SendState;
import app.chat.helpers.UserSession;
import app.chat.helpers.Utils;
import app.chat.mapper.MapstructMapper;
import app.chat.model.resp.group.GroupMessageRespDto;
import app.chat.repository.GroupUserPermissionRepo;
import app.chat.repository.group.*;
import app.chat.service.AttachmentService;
import app.chat.service.ErrorService;
import app.chat.service.GroupMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

import static app.chat.model.ApiResponse.response;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupMessageServiceImpl implements GroupMessageService {

    private final GroupMessageAttachmentRepo groupMessageAttachmentRepo;
    private final GroupUserPermissionRepo groupUserPermissionRepo;
    private final GroupUserMessageRepo groupUserMessageRepo;
    private final GroupBlockListRepo groupBlockListRepo;
    private final AttachmentService attachmentService;
    private final GroupMessageRepo groupMessageRepo;
    private final GroupUserRepo groupMemberRepo;
    private final MapstructMapper mapper;
    private final GroupRepo groupRepo;
    private final UserSession session;
    private final ErrorService error;


    @Override
    public ResponseEntity<?> sendMessage(Long groupId, String text, MultipartHttpServletRequest files) {
        User activeUser = session.getUser();

        //todo check existing group
        Optional<Group> groupOptional = groupRepo.findById(groupId);
        if (groupOptional.isEmpty()) {
            return response(error.message(ErrorEnum.GROUP_NOT_FOUND.getCode(), Lang.RU, groupId), HttpStatus.NOT_FOUND);
        }
        //todo check am I a member of this group
        Optional<GroupUser> memberOptional = groupMemberRepo.findByUser_IdAndGroup_IdAndActiveTrue(activeUser.getId(), groupId);
        if (memberOptional.isEmpty()) {
            return response(error.message(ErrorEnum.YOU_NOT_MEMBER_IN_GROUP.getCode(), Lang.RU, groupId), HttpStatus.BAD_REQUEST);
        }
        //todo check you are blocked in this group
        Optional<GroupBlockList> blockUser = groupBlockListRepo.findByUser_IdAndGroup_IdAndActiveTrue(activeUser.getId(), groupOptional.get().getId());
        if (blockUser.isPresent()) {
            return response(error.message(ErrorEnum.USER_BLOCKED_IN_GROUP.getCode(), Lang.RU, groupId), HttpStatus.BAD_REQUEST);
        }
        //todo check permission send message
        List<String> groupUserPermission = groupUserPermissionRepo.getPermissions(activeUser.getId(), groupId);
        if (!groupUserPermission.contains(ChatPermissions.SEND_MESSAGE.name())) {
            return response(error.message(ErrorEnum.NO_PERMISSION.getCode(), Lang.RU, groupId));
        }


        GroupMessage groupMessage = new GroupMessage();
        groupMessage.setGroup(groupOptional.get());
        if (Utils.isEmpty(text)) {
            groupMessage.setText(text);
        }
        groupMessage.setSendDate(LocalDateTime.now());
        groupMessage.setSender(activeUser);
        groupMessage.setIsPinned(false);
        groupMessage.setSendState(SendState.PROGRESS);
        groupMessage = groupMessageRepo.save(groupMessage);

        //todo set view message
        setView(activeUser, groupMessage);


        //todo stringList for response dto
        List<String> stringList = new ArrayList<>();

        Iterator<String> iterator = files.getFileNames();

        //todo check or create download folders
        attachmentService.intiFileSystem();

        while (iterator.hasNext()) {
            MultipartFile file = Objects.requireNonNull(files.getFile(iterator.next()));

            stringList.add(file.getOriginalFilename());

            Attachment attachment = attachmentService.upload(file);
            groupMessageAttachmentRepo.save(new GroupMessageAttachment(groupMessage, attachment));
        }
        groupMessage.setSendState(SendState.SEND);
        groupMessage = groupMessageRepo.save(groupMessage);

        GroupMessageRespDto groupMessageRespDto = mapper.toGroupMessageDto(groupMessage);
        groupMessageRespDto.setFiles(stringList);
        //todo set viewed count
        groupMessageRespDto.setViewCount((long) groupUserMessageRepo.findAllByGroupMessage_IdAndIsViewedTrue(groupMessageRespDto.getId()).size());

        return response(groupMessageRespDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getMessages(Integer page, Long groupId) {
        User activeUser = session.getUser();
        //todo check existing group
        Optional<Group> groupOptional = groupRepo.findById(groupId);
        if (groupOptional.isEmpty()) {
            return response(error.message(ErrorEnum.GROUP_NOT_FOUND.getCode(), Lang.RU, groupId));
        }
        //todo check am I a member of this group
        Optional<GroupUser> groupMember = groupMemberRepo.findByUser_IdAndGroup_IdAndActiveTrue(activeUser.getId(), groupId);
        if (groupMember.isEmpty()) {
            return response(error.message(ErrorEnum.YOU_NOT_MEMBER_IN_GROUP.getCode(), Lang.RU, groupId));
        }

        Pageable hundred = PageRequest.of(page, 100, Sort.by("sendDate").descending());
        List<GroupMessage> messages = groupMessageRepo.findAllByGroup_IdAndActiveTrue(groupId, hundred);

        List<GroupMessageRespDto> groupMessageRespDtos = mapper.toGroupMessageDtoList(messages);
        //todo set messageFiles and viewCount for response dto
        for (GroupMessageRespDto groupMessageRespDto : groupMessageRespDtos) {
            List<GroupMessageAttachment> messageFiles = groupMessageAttachmentRepo.findAllByGroupMessage_IdAndActiveTrue(groupMessageRespDto.getId());
            List<String> fileNames = new ArrayList<>();
            for (GroupMessageAttachment messageFile : messageFiles) {
                fileNames.add(messageFile.getAttachment().getName());
            }
            groupMessageRespDto.setFiles(fileNames);
            //todo set viewed count
            groupMessageRespDto.setViewCount((long) groupUserMessageRepo.findAllByGroupMessage_IdAndIsViewedTrue(groupMessageRespDto.getId()).size());
        }
        return response(groupMessageRespDtos, (long) groupMessageRespDtos.size(), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> view(Long messageId) {
        //todo check existing message
        Optional<GroupMessage> groupMessageOptional = groupMessageRepo.findById(messageId);
        if (groupMessageOptional.isEmpty()) {
            return response(error.message(ErrorEnum.MESSAGE_NOT_FOUND.getCode(), Lang.RU, messageId));
        }

        User activeUser = session.getUser();
        GroupMessage groupMessage = groupMessageOptional.get();
        Group group = groupMessage.getGroup();

        //todo check am I a member of this group
        Optional<GroupUser> groupMember = groupMemberRepo.findByUser_IdAndGroup_IdAndActiveTrue(activeUser.getId(), group.getId());
        if (groupMember.isEmpty()) {
            return response(error.message(ErrorEnum.YOU_NOT_MEMBER_IN_GROUP.getCode(), Lang.RU, messageId));
        }
        //todo check that the message is sender
        if (activeUser.getId().equals(groupMessage.getSender().getId())) {
            return response(error.message(ErrorEnum.CANT_VIEW.getCode(), Lang.RU, messageId));
        }
        //todo check already view
        Optional<GroupUserMessage> alreadyView = groupUserMessageRepo.findByGroupMessage_IdAndUser_IdAndIsViewedTrue(groupMessage.getId(), activeUser.getId());
        if (alreadyView.isPresent()) {
            return response(true, HttpStatus.OK);
        }
        GroupUserMessage groupUserMessage = setView(activeUser, groupMessage);
        return response(true, HttpStatus.OK);
    }

    private GroupUserMessage setView(User user, GroupMessage groupMessage) {
        GroupUserMessage groupUserMessage = new GroupUserMessage();
        groupUserMessage.setGroupMessage(groupMessage);
        groupUserMessage.setUser(user);
        groupUserMessage.setIsViewed(true);
        return groupUserMessageRepo.save(groupUserMessage);
    }
}

package app.chat.service.impl;

import app.chat.entity.Attachment;
import app.chat.entity.channel.Channel;
import app.chat.entity.channel.ChannelMessage;
import app.chat.entity.channel.ChannelMessageAttachment;
import app.chat.entity.channel.ChannelUserMessage;
import app.chat.entity.template.AbsMain;
import app.chat.entity.user.User;
import app.chat.enums.*;
import app.chat.helpers.UserSession;
import app.chat.helpers.Utils;
import app.chat.mapper.MapstructMapper;
import app.chat.model.ApiResponse;
import app.chat.model.dto.ChannelMessageDto;
import app.chat.repository.ChannelMessageAttachmentRepo;
import app.chat.repository.ChannelUserPermissionRepo;
import app.chat.repository.channel.ChannelMessageRepo;
import app.chat.repository.channel.ChannelRepo;
import app.chat.repository.channel.ChannelUserMessageRepo;
import app.chat.repository.channel.ChannelUserRepo;
import app.chat.repository.user.UserRepo;
import app.chat.service.AttachmentService;
import app.chat.service.ChannelMessageService;
import app.chat.service.ChannelService;
import app.chat.service.ErrorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static app.chat.helpers.Utils.isEmpty;
import static app.chat.model.ApiResponse.response;

@Service
@RequiredArgsConstructor
public class ChannelMessageServiceImpl implements ChannelMessageService {

    private final ChannelMessageRepo channelMessageRepo;
    private final ChannelUserRepo channelUserRepo;
    private final UserSession session;
    private final ChannelRepo channelRepo;
    private final ErrorService errorService;
    private final MapstructMapper mapper;
    private final ChannelMessageAttachmentRepo channelMessageAttachmentRepo;
    private final AttachmentService attachmentService;
    private final ChannelService channelService;
    private final ChannelUserMessageRepo channelUserMessageRepo;
    private final UserRepo userRepo;

    @Override
    public ResponseEntity<?> getMessages(Integer page, Long channelId) {
        Optional<Channel> optionalChannel = channelRepo.findActiveById(channelId);
        if (optionalChannel.isEmpty()) {
            return response(errorService.message(ErrorEnum.CHANNEL_NOT_FOUND.getCode(), Lang.RU, channelId), HttpStatus.NOT_FOUND);
        }
        Long userId = session.getUserId();
        if (isEmpty(userId)) {
            return response(errorService.message(ErrorEnum.USER_NOT_FOUND.getCode(), Lang.RU, userId), HttpStatus.NOT_FOUND);
        }
        Channel channel = optionalChannel.get();
        if (!channelUserRepo.existsChannelUser(userId, channelId) && channel.getSecurityType().equals(SecurityType.PRIVATE)) {
            return response(errorService.message(ErrorEnum.YOU_NOT_MEMBER_IN_CHANNEL.getCode(), Lang.RU, channelId), HttpStatus.BAD_REQUEST);
        }

        List<ChannelMessage> messages = channelMessageRepo.findAllActiveByChannelId(channelId, 50, page * 50);

        return response(toMessageDto(messages), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getUserMessages(Integer page, Long channelId) {
        Optional<Channel> optionalChannel = channelRepo.findActiveById(channelId);
        if (optionalChannel.isEmpty()) {
            return response(errorService.message(ErrorEnum.CHANNEL_NOT_FOUND.getCode(), Lang.RU, channelId), HttpStatus.NOT_FOUND);
        }
        Long userId = session.getUserId();
        if (isEmpty(userId)) {
            return response(errorService.message(ErrorEnum.USER_NOT_FOUND.getCode(), Lang.RU, userId), HttpStatus.NOT_FOUND);
        }
        Channel channel = optionalChannel.get();
        if (!channelUserRepo.existsChannelUser(userId, channelId) && channel.getSecurityType().equals(SecurityType.PRIVATE)) {
            return response(errorService.message(ErrorEnum.YOU_NOT_MEMBER_IN_CHANNEL.getCode(), Lang.RU, channelId), HttpStatus.BAD_REQUEST);
        }

        List<ChannelUserMessage> messages = channelUserMessageRepo.findAllActiveByChannelId(channelId, userId, 50, page * 50);

        return response(toMessagesDto(messages), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getUnreadMessages(Integer page, Long channelId) {
        Long userId = session.getUserId();
        if (isEmpty(userId)) {
            return response(errorService.message(ErrorEnum.USER_NOT_FOUND.getCode(), Lang.RU, userId), HttpStatus.NOT_FOUND);
        }
        List<ChannelMessage> messages = channelMessageRepo.getUnreadMessages(channelId, userId, 25, 25 * page);
        return response(toMessageDto(messages), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> sendMessage(Long channelId, String text, MultipartHttpServletRequest files) {
        User user = session.getUser();
        if (isEmpty(user)) {
            return response(errorService.message(ErrorEnum.USER_NOT_FOUND.getCode(), Lang.RU), HttpStatus.NOT_FOUND);
        }
        //todo check existing channel
        Optional<Channel> channelOptional = channelRepo.findActiveById(channelId);
        if (channelOptional.isEmpty()) {
            return response(errorService.message(ErrorEnum.CHANNEL_NOT_FOUND.getCode(), Lang.RU, channelId), HttpStatus.NOT_FOUND);
        }
        //todo check permission send message
        if (!channelService.getPermissions(user.getId(), channelId).contains(ChatPermissions.POST_MESSAGE.name())) {
            return response(errorService.message(ErrorEnum.NO_PERMISSION.getCode(), Lang.RU, channelId), HttpStatus.FORBIDDEN);
        }

        ChannelMessage channelMessage = new ChannelMessage();
        channelMessage.setChannel(channelOptional.get());
        channelMessage.setText(text);
        channelMessage.setSender(user);
        channelMessage.setSendState(SendState.PROGRESS);
        channelMessage = channelMessageRepo.save(channelMessage);

        // files
        List<String> stringList = new ArrayList<>();
        if (!isEmpty(files)) {
            Iterator<String> iterator = files.getFileNames();
            attachmentService.intiFileSystem();

            while (iterator.hasNext()) {
                MultipartFile file = Objects.requireNonNull(files.getFile(iterator.next()));

                stringList.add(file.getOriginalFilename());

                Attachment attachment = attachmentService.upload(file);
                channelMessageAttachmentRepo.save(new ChannelMessageAttachment(channelMessage, attachment));
            }
        }
        List<User> members = userRepo.getMembers(channelId);

        channelMessage.setSendState(SendState.SEND);
        channelMessage = channelMessageRepo.save(channelMessage);

        sendToMembers(channelMessage, members);

        view(channelMessage.getId());

        ChannelMessageDto channelMessageDto = mapper.toChannelMessageDto(channelMessage);
        channelMessageDto.setFiles(stringList);
        return response(channelMessageDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> editMessage(Long messageId, String text) {
        Long userId = session.getUserId();
        if (isEmpty(userId)) {
            return response(errorService.message(ErrorEnum.USER_NOT_FOUND.getCode(), Lang.RU), HttpStatus.NOT_FOUND);
        }
        Optional<ChannelMessage> optionalChannelMessage = channelMessageRepo.findActiveById(messageId);
        if (optionalChannelMessage.isEmpty()){
            return response(errorService.message(ErrorEnum.CHANNEl_MESSAGE_NOT_FOUND.getCode(), Lang.RU, messageId), HttpStatus.NOT_FOUND);
        }
        ChannelMessage channelMessage = optionalChannelMessage.get();

        if (!channelService.getPermissions(userId, channelMessage.getChannel().getId()).contains(ChatPermissions.EDIT_MESSAGE.name())) {
            return response(errorService.message(ErrorEnum.NO_PERMISSION.getCode(), Lang.RU), HttpStatus.FORBIDDEN);
        }
        channelMessage.setText(text);
        channelMessage.setSendState(SendState.PROGRESS);
        channelMessageRepo.save(channelMessage);
        channelMessage.setSendState(SendState.SEND);
        channelMessageRepo.save(channelMessage);
        return response("Edited!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> view(Long channelMessageId) {
        Long userId = session.getUserId();
        if (isEmpty(userId)) {
            return response(errorService.message(ErrorEnum.USER_NOT_FOUND.getCode(), Lang.RU, userId), HttpStatus.NOT_FOUND);
        }
        Optional<ChannelUserMessage> optionalChannelUserMessage = channelUserMessageRepo.findChannelMessage(channelMessageId, userId);
        if (optionalChannelUserMessage.isEmpty()) {
            return response(errorService.message(ErrorEnum.CHANNEl_MESSAGE_NOT_FOUND.getCode(), Lang.RU), HttpStatus.NOT_FOUND);
        }
        ChannelUserMessage channelUserMessage = optionalChannelUserMessage.get();
        channelUserMessage.setIsViewed(true);
        channelUserMessageRepo.save(channelUserMessage);
        return response(new ApiResponse<>());
    }

    @Override
    public ResponseEntity<?> delete(Long messageId) {
        User user = session.getUser();
        if (isEmpty(user)) {
            return response(errorService.message(ErrorEnum.USER_NOT_FOUND.getCode(), Lang.RU), HttpStatus.NOT_FOUND);
        }
        Optional<ChannelMessage> optionalChannelMessage = channelMessageRepo.findActiveById(messageId);
        if (optionalChannelMessage.isEmpty()) {
            return response(errorService.message(ErrorEnum.CHANNEl_MESSAGE_NOT_FOUND.getCode(), Lang.RU, messageId), HttpStatus.NOT_FOUND);
        }
        ChannelMessage channelMessage = optionalChannelMessage.get();
        if (!channelService.getPermissions(user.getId(), channelMessage.getChannel().getId()).contains(ChatPermissions.DELETE_MESSAGE_OF_OTHERS.name())) {
            return response(errorService.message(ErrorEnum.NO_PERMISSION.getCode(), Lang.RU, ChatPermissions.POST_MESSAGE.name()), HttpStatus.FORBIDDEN);
        }
        channelMessage.setActive(false);
        channelMessage = channelMessageRepo.save(channelMessage);
        deleteFromMembers(channelMessage, userRepo.getMembers(channelMessage.getChannel().getId()));
        return response("Deleted!", HttpStatus.OK);
    }

    public void sendToMember(ChannelMessage channelMessage, User user) {
        ChannelUserMessage channelUserMessage = new ChannelUserMessage();
        channelUserMessage.setChannelMessage(channelMessage);
        channelUserMessage.setUser(user);
        channelUserMessageRepo.save(channelUserMessage);
    }

    public void sendToMembers(ChannelMessage channelMessage, List<User> users) {
        List<ChannelUserMessage> userMessages = new ArrayList<>();
        users.forEach(user -> {
            ChannelUserMessage channelUserMessage = new ChannelUserMessage();
            channelUserMessage.setChannelMessage(channelMessage);
            channelUserMessage.setUser(user);
            userMessages.add(channelUserMessage);
        });
        channelUserMessageRepo.saveAll(userMessages);
    }

    public void deleteFromMembers(ChannelMessage channelMessage, List<User> users) {
        Set<Long> collect = users.stream().map(AbsMain::getId).collect(Collectors.toSet());
        List<ChannelUserMessage> userMessages = channelUserMessageRepo.findMessageInUser(channelMessage.getId(), new ArrayList<>(collect));
        userMessages.forEach(channelUserMessage -> channelUserMessage.setActive(false));
        channelUserMessageRepo.saveAll(userMessages);
    }

    private List<ChannelMessageDto> toMessageDto(List<ChannelMessage> messages) {
        List<ChannelMessageDto> channelMessageDtos = mapper.toChannelMessageDto(messages);
        for (ChannelMessageDto channelMessageDto : channelMessageDtos) {
            List<ChannelMessageAttachment> messageFiles = channelMessageAttachmentRepo.findAllByChannelMessage_IdAndActiveTrue(channelMessageDto.getId());
            List<String> fileNames = new ArrayList<>();
            for (ChannelMessageAttachment messageFile : messageFiles) {
                fileNames.add(messageFile.getAttachment().getName());
            }
            channelMessageDto.setFiles(fileNames);
        }
        return channelMessageDtos;
    }

    private List<ChannelMessageDto> toMessagesDto(List<ChannelUserMessage> messages) {
        List<ChannelMessageDto> channelMessageDtos = mapper.toChannelMessagesDto(messages);
        for (ChannelMessageDto channelMessageDto : channelMessageDtos) {
            List<ChannelMessageAttachment> messageFiles = channelMessageAttachmentRepo.findAllByChannelMessage_IdAndActiveTrue(channelMessageDto.getId());
            List<String> fileNames = new ArrayList<>();
            for (ChannelMessageAttachment messageFile : messageFiles) {
                fileNames.add(messageFile.getAttachment().getName());
            }
            channelMessageDto.setFiles(fileNames);
        }
        return channelMessageDtos;
    }
}

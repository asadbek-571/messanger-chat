package app.chat.service.impl;

import app.chat.entity.Attachment;
import app.chat.entity.personal.Personal;
import app.chat.entity.personal.PersonalMessage;
import app.chat.entity.personal.PersonalMessageAttachment;
import app.chat.entity.personal.UserPersonalMessage;
import app.chat.entity.user.User;
import app.chat.enums.ErrorEnum;
import app.chat.enums.Lang;
import app.chat.enums.SendState;
import app.chat.helpers.UserSession;
import app.chat.helpers.Utils;
import app.chat.mapper.MapstructMapper;
import app.chat.model.dto.PersonalMessageDto;
import app.chat.repository.PersonalMessageAttachmentRepo;
import app.chat.repository.personal.PersonalMessageRepo;
import app.chat.repository.personal.PersonalRepo;
import app.chat.repository.user.UserPersonalMessageRepo;
import app.chat.repository.user.UserRepo;
import app.chat.service.AttachmentService;
import app.chat.service.ErrorService;
import app.chat.service.PersonalMessageService;
import app.chat.service.UserBlockService;
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
public class PersonalMessageServiceImpl implements PersonalMessageService {

    private final PersonalRepo personalRepo;
    private final UserRepo userRepo;
    private final ErrorService error;
    private final UserSession session;
    private final PersonalMessageRepo personalMessageRepo;
    private final AttachmentService attachmentService;
    private final PersonalMessageAttachmentRepo personalMessageAttachmentRepo;
    private final MapstructMapper mapper;
    private final UserBlockService userBlockService;
    private final UserPersonalMessageRepo userPersonalMessageRepo;


    /**
     *Send messege method
     */
    @Override
    public ResponseEntity<?> sendMessage(Long receiverId, String text, MultipartHttpServletRequest files) {
        Optional<User> receiverOptional = userRepo.findActiveById(receiverId);
        if (receiverOptional.isEmpty()) {
            return response(error.message(10, Lang.RU, receiverId));
        }
        User receiver = receiverOptional.get();
        User sender = session.getUser();

        Optional<Personal> personalOptional = personalRepo.findPersonal(receiver.getId(), sender.getId());
        if (personalOptional.isEmpty()) {
            return response(error.message(70, Lang.RU, receiverId));
        }
        Personal personal = personalOptional.get();

        if (userBlockService.isMyBlockUser(receiver.getId())) {
            return response(error.message(14, Lang.RU, receiver.getId()));
        }
        if (userBlockService.isThisUserBlockedYou(receiver.getId())) {
            return response(error.message(15, Lang.RU, receiver.getId()));
        }

        PersonalMessage personalMessage = new PersonalMessage();
        personalMessage.setPersonal(personal);
        personalMessage.setSender(sender);
        personalMessage.setSendState(SendState.PROGRESS);
        personalMessage.setIsPinned(false);
        personalMessage.setSendDate(LocalDateTime.now());
        if (!Utils.isEmpty(text)) {
            personalMessage.setText(text);
        }
        personalMessage = personalMessageRepo.save(personalMessage);

        List<String> stringList = new ArrayList<>();
        Iterator<String> iterator = files.getFileNames();

        attachmentService.intiFileSystem();

        while (iterator.hasNext()) {
            MultipartFile file = Objects.requireNonNull(files.getFile(iterator.next()));
            stringList.add(file.getOriginalFilename());
            Attachment attachment = attachmentService.upload(file);
            personalMessageAttachmentRepo.save(new PersonalMessageAttachment(personalMessage, attachment));
        }
        personalMessage.setSendState(SendState.SEND);
        personalMessage = personalMessageRepo.save(personalMessage);

        PersonalMessageDto personalMessageDto = mapper.toPersonalMessageDto(personalMessage);
        personalMessageDto.setFiles(stringList);


        return response(personalMessageDto, HttpStatus.OK);
    }

    /**
     *Get pageable message method
     */
    @Override
    public ResponseEntity<?> getMessages(Integer page, Long personalId) {

        Optional<Personal> personalOptional = personalRepo.findById(personalId);
        if (personalOptional.isEmpty()) {
            return response(error.message(70, Lang.RU, personalId));
        }
        if (!personalOptional.get().isActive()) {
            return response(error.message(70, Lang.RU, personalId));
        }

        if (!personalOptional.get().getUser1().getId().equals(session.getUserId()) &&
                !personalOptional.get().getUser2().getId().equals(session.getUserId())) {
            return response(error.message(70, Lang.RU, personalId));
        }

        Pageable hundred = PageRequest.of(page, 100, Sort.by("sendDate").descending());
        List<PersonalMessage> messages = personalMessageRepo.findAllByPersonal_IdAndActiveTrue(personalId, hundred);

        List<PersonalMessageDto> personalMessageDtos = mapper.toPersonalMessageDtoList(messages);
        for (PersonalMessageDto personalMessageDto : personalMessageDtos) {
            List<PersonalMessageAttachment> messageFiles = personalMessageAttachmentRepo.findAllByPersonalMessage_IdAndActiveTrue(personalMessageDto.getId());
            List<String> fileNames = new ArrayList<>();
            for (PersonalMessageAttachment messageFile : messageFiles) {
                fileNames.add(messageFile.getAttachment().getName());
            }
            personalMessageDto.setFiles(fileNames);
        }

        return response(personalMessageDtos, (long) personalMessageDtos.size());
    }


    @Override
    public ResponseEntity<?> view(Long messageId) {
        //todo check exist message
        Optional<PersonalMessage> messageOptional = personalMessageRepo.findById(messageId);
        if (messageOptional.isEmpty()) {
            return response(error.message(ErrorEnum.MESSAGE_NOT_FOUND.getCode(), Lang.RU, messageId), HttpStatus.NOT_FOUND);
        }

        PersonalMessage personalMessage = messageOptional.get();
        Personal personal = personalMessage.getPersonal();

        //todo check that the message is sender
        if (personalMessage.getSender().getId().equals(session.getUserId())) {
            return response(error.message(ErrorEnum.CANT_VIEW.getCode(), Lang.RU, messageId), HttpStatus.BAD_REQUEST);
        }

        User receiver = null;
        if (personalMessage.getSender().getId().equals(personal.getUser1().getId())) {
            receiver = personal.getUser2();
        } else {
            receiver = personal.getUser1();
        }
        if (receiver.getId().equals(session.getUserId())) {
            UserPersonalMessage userPersonalMessage = new UserPersonalMessage();
            userPersonalMessage.setPersonalMessage(personalMessage);
            userPersonalMessage.setUser(session.getUser());
            userPersonalMessage.setIsViewed(true);
            userPersonalMessageRepo.save(userPersonalMessage);
            return response(true, HttpStatus.OK);
        } else {
            return response(error.message(ErrorEnum.CANT_VIEW.getCode(), Lang.RU, messageId), HttpStatus.BAD_REQUEST);
        }
    }
}

package app.chat.service.impl;

import app.chat.entity.channel.ChannelUser;
import app.chat.entity.group.GroupUser;
import app.chat.entity.personal.Personal;
import app.chat.enums.ErrorEnum;
import app.chat.enums.Lang;
import app.chat.repository.channel.ChannelUserRepo;
import app.chat.repository.group.GroupUserRepo;
import app.chat.repository.personal.PersonalRepo;
import app.chat.service.ErrorService;
import app.chat.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static app.chat.model.ApiResponse.response;


@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final ErrorService error;
    private final PersonalRepo personalRepo;
    private final GroupUserRepo groupUserRepo;
    private final ChannelUserRepo channelUserRepo;


    /**
     * For turn on personal chat notification
     * @param personalId
     * @return  ResponseEntity<?>
     */
    @Override
    public ResponseEntity<?> turnOnPersonalNotificationById(Long personalId) {
        Optional<Personal> optionalPersonal = personalRepo.findById(personalId);

        if (optionalPersonal.isEmpty()) {
            return response(error.message(ErrorEnum.PERSONAL_MESSAGE_NOT_FOUND.getCode(), Lang.RU, personalId), HttpStatus.NOT_FOUND);
        }
        Personal personal = optionalPersonal.get();
        personal.setIsMute(false);
        personalRepo.save(personal);
        return response(personal);
    }

    /**
     * For turn off personal chat notification
     * @param personalId
     * @return  ResponseEntity<?>
     */
    @Override
    public ResponseEntity<?> turnOffPersonalNotificationById(Long personalId) {
        Optional<Personal> optionalPersonal = personalRepo.findById(personalId);

        if (optionalPersonal.isEmpty()) {
            return response(error.message(ErrorEnum.PERSONAL_MESSAGE_NOT_FOUND.getCode(), Lang.RU, personalId), HttpStatus.NOT_FOUND);
        }
        Personal personal = optionalPersonal.get();
        personal.setIsMute(true);
        personalRepo.save(personal);
        return response(personal);
    }

    /**
     * For turn on UserGroup chat notification
     * @param userGroupId
     * @return ResponseEntity<?>
     */
    @Override
    public ResponseEntity<?> turnOnUserGroupNotificationById(Long userGroupId) {
        Optional<GroupUser> optionalUserGroup = groupUserRepo.findById(userGroupId);
        if (optionalUserGroup.isEmpty()) {
            return response(error.message(ErrorEnum.USER_GROUP_NOT_FOUND.getCode(), Lang.RU, userGroupId), HttpStatus.NOT_FOUND);
        }

        GroupUser userGroup = optionalUserGroup.get();
        userGroup.setIsMute(false);
        groupUserRepo.save(userGroup);
        return response((userGroup));
    }

    /**
     * For turn off UserGroup chat notification
     * @param userGroupId
     * @return ResponseEntity<?>
     */
    @Override
    public ResponseEntity<?> turnOffUserGroupNotificationById(Long userGroupId) {
        Optional<GroupUser> optionalUserGroup = groupUserRepo.findById(userGroupId);
        if (optionalUserGroup.isEmpty()) {
            return response(error.message(ErrorEnum.USER_GROUP_NOT_FOUND.getCode(), Lang.RU, userGroupId), HttpStatus.NOT_FOUND);
        }

        GroupUser userGroup = optionalUserGroup.get();
        userGroup.setIsMute(true);
        groupUserRepo.save(userGroup);
        return response((userGroup));
    }

    /**
     * For turn on channel chat notification
     * @param channelUserId
     * @return ResponseEntity<?>
     */
    @Override
    public ResponseEntity<?> turnOnChannelUserNotificationById(Long channelUserId) {
        Optional<ChannelUser> optionalChannelUser = channelUserRepo.findById(channelUserId);
        if (optionalChannelUser.isEmpty()) {
            return response(error.message(ErrorEnum.CHANNEl_MESSAGE_NOT_FOUND.getCode(), Lang.RU, channelUserId), HttpStatus.NOT_FOUND);
        }

        ChannelUser channelUser = optionalChannelUser.get();
        channelUser.setIsMute(false);
        channelUserRepo.save(channelUser);
        return response(channelUser);
    }

    /**
     * For turn off channel chat notification
     * @param channelUserId
     * @return ResponseEntity<?>
     */
    @Override
    public ResponseEntity<?> turnOffChannelUserNotificationById(Long channelUserId) {
        Optional<ChannelUser> optionalChannelUser = channelUserRepo.findById(channelUserId);
        if (optionalChannelUser.isEmpty()) {
            return response(error.message(ErrorEnum.CHANNEl_MESSAGE_NOT_FOUND.getCode(), Lang.RU, channelUserId), HttpStatus.NOT_FOUND);
        }

        ChannelUser channelUser = optionalChannelUser.get();
        channelUser.setIsMute(true);
        channelUserRepo.save(channelUser);

        return response(channelUser);
    }

}

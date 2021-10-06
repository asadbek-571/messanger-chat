package app.chat.controller;

import app.chat.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * For turn on personal chat notification api
     * @param personalId
     * @return  ResponseEntity<?>
     */
    @PutMapping("/turnOnForPersonal/{personalId}")
    public ResponseEntity<?> turnOnPersonalNotificationById(@PathVariable Long personalId){
      return   notificationService.turnOnPersonalNotificationById(personalId);
    }

    /**
     * For turn off personal chat notification api
     * @param personalId
     * @return ResponseEntity<?>
     */
    @PutMapping("/turnOffForPersonal/{personalId}")
    public ResponseEntity<?> turnOffPersonalNotificationById(@PathVariable Long personalId){
        return   notificationService.turnOffPersonalNotificationById(personalId);
    }

    /**
     * For turn on UserGroup chat notification api
     * @param userGroupId
     * @return ResponseEntity<?>
     */
    @PutMapping("/turnOnForUserGroup/{userGroupId}")
    public ResponseEntity<?> turnOnUserGroupNotificationById(@PathVariable Long userGroupId){
        return   notificationService.turnOnUserGroupNotificationById(userGroupId);
    }

    /**
     * For turn off UserGroup chat notification api
     * @param userGroupId
     * @return ResponseEntity<?>
     */
    @PutMapping("/turnOffForUserGroup/{userGroupId}")
    public ResponseEntity<?> turnOffUserGroupNotificationById(@PathVariable Long userGroupId){
        return   notificationService.turnOffUserGroupNotificationById(userGroupId);
    }

    /**
     * For turn on channel chat notification api
     * @param channelUserId
     * @return ResponseEntity<?>
     */
    @PutMapping("/turnOnForChannelUser/{channelUserId}")
    public ResponseEntity<?> turnOnChannelUserNotificationById(@PathVariable Long channelUserId){
        return   notificationService.turnOnChannelUserNotificationById(channelUserId);
    }

    /**
     * For turn off channel chat notification api
     * @param channelUserId
     * @return ResponseEntity<?>
     */
    @PutMapping("/turnOffForChannelUser/{channelUserId}")
    public ResponseEntity<?> turnOffChannelUserNotificationById(@PathVariable Long channelUserId){
        return   notificationService.turnOffChannelUserNotificationById(channelUserId);
    }

}

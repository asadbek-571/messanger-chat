package app.chat.service;


import org.springframework.http.ResponseEntity;

public interface NotificationService {

    ResponseEntity<?> turnOnPersonalNotificationById(Long id);

    ResponseEntity<?> turnOffPersonalNotificationById(Long id);

    ResponseEntity<?> turnOnUserGroupNotificationById(Long id);

    ResponseEntity<?> turnOffUserGroupNotificationById(Long id);

    ResponseEntity<?> turnOnChannelUserNotificationById(Long id);

    ResponseEntity<?> turnOffChannelUserNotificationById(Long id);
}

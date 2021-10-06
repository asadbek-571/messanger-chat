package app.chat.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface GroupMessageService {
    ResponseEntity<?> sendMessage(Long groupId, String text, MultipartHttpServletRequest files);

    ResponseEntity<?> getMessages(Integer page, Long groupId);

    ResponseEntity<?> view(Long messageId);
}

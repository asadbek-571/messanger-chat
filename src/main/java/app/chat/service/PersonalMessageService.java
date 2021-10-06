package app.chat.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.HashMap;
import java.util.List;

public interface PersonalMessageService {
    ResponseEntity<?> sendMessage(Long receiverId, String text, MultipartHttpServletRequest files);

    ResponseEntity<?> getMessages(Integer page, Long personalId);

    ResponseEntity<?> view(Long messageId);
}

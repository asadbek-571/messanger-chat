package app.chat.service;

import app.chat.entity.user.User;
import app.chat.repository.channel.ChannelMessageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface ChannelMessageService {

    ResponseEntity<?> getMessages(Integer page, Long channelId);

    ResponseEntity<?> getUnreadMessages(Integer page, Long channelId);

    ResponseEntity<?> sendMessage(Long channelId, String text, MultipartHttpServletRequest files);

    ResponseEntity<?> view(Long messageId);

    ResponseEntity<?> editMessage(Long messageId, String text);

    ResponseEntity<?> delete(Long messageId);

    ResponseEntity<?> getUserMessages(Integer page, Long channelId);
}

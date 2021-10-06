package app.chat.controller;

import app.chat.service.ChannelMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channel/message")
public class ChannelMessageController {

    private final ChannelMessageService channelMessageService;

    /**
     * @param channelId
     * @param page
     * @return channel messages sort by created date desc
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getMessages(@PathVariable(value = "id") Long channelId,
                                         @RequestParam(value = "page", defaultValue = "0") Integer page) {
        return channelMessageService.getMessages(page, channelId);
    }

    /**
     * @param channelId
     * @param page
     * @return user channelni ochganda unga boradigan xabarlari
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserMessages(@PathVariable(value = "id") Long channelId,
                                             @RequestParam(value = "page", defaultValue = "0") Integer page) {
        return channelMessageService.getUserMessages(page, channelId);
    }

    /**
     * @param channelId
     * @param page
     * @return unread channel messages sort by created date desc
     */
    @GetMapping("/unreadMessages/{channelId}")
    public ResponseEntity<?> getUnreadMessages(@PathVariable(value = "channelId") Long channelId,
                                               @RequestParam(value = "page", defaultValue = "0") Integer page) {
        return channelMessageService.getUnreadMessages(page, channelId);
    }

    /**
     * send posts to channel by user who has POST_MESSAGE permission to channel
     *
     * @param files
     * @param text
     * @param channelId
     * @return
     */
    @PostMapping
    public ResponseEntity<?> sendMessage(MultipartHttpServletRequest files,
                                         @RequestParam(required = false) String text,
                                         @RequestParam Long channelId) {
        return channelMessageService.sendMessage(channelId, text, files);
    }

    @PutMapping("/edit/{messageId}")
    public ResponseEntity<?> editMessage(@PathVariable Long messageId,
                                         @RequestParam(required = false) String text) {
        return channelMessageService.editMessage(messageId, text);
    }

    /**
     * read unread messages by user
     *
     * @param messageId
     * @return
     */
    @PostMapping("/view")
    public ResponseEntity<?> postView(@RequestParam Long messageId) {
        return channelMessageService.view(messageId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long messageId) {
        return channelMessageService.delete(messageId);
    }
}

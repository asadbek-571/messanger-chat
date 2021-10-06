package app.chat.controller;

import app.chat.service.GroupMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/group/message")
public class GroupMessageController {
    private final GroupMessageService groupMessageService;

    @PostMapping()
    public ResponseEntity<?> sendMessage(MultipartHttpServletRequest files,
                                         @RequestParam(required = false) String text,
                                         @RequestParam Long groupId) {

        return groupMessageService.sendMessage(groupId, text, files);
    }

    @GetMapping("list")
    public ResponseEntity<?> getMessages(@RequestParam Integer page, @RequestParam Long groupId) {
        return groupMessageService.getMessages(page, groupId);
    }
    @PostMapping("/view")
    public ResponseEntity<?> postView(@RequestParam Long messageId){
        return groupMessageService.view(messageId);
    }

}

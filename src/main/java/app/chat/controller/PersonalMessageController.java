package app.chat.controller;

import app.chat.service.PersonalMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/personal/message")
public class PersonalMessageController {

    private final PersonalMessageService personalMessageService;

    @PostMapping()
    public ResponseEntity<?> sendMessage(MultipartHttpServletRequest files,
                                         @RequestParam(required = false) String text,
                                         @RequestParam Long receiverId) {

        return personalMessageService.sendMessage(receiverId, text, files);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getMessages(@RequestParam(value = "page") Integer page,@RequestParam Long personalId) {
        return personalMessageService.getMessages(page,personalId);
    }

    @PostMapping("/view")
    public ResponseEntity<?> postView(@RequestParam Long messageId){
        return personalMessageService.view(messageId);
    }

}

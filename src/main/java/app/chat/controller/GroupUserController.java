package app.chat.controller;

import app.chat.model.req.group.GroupMemberReqDto;
import app.chat.service.GroupUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/userGroup")
@RequiredArgsConstructor
public class GroupUserController {

    private final GroupUserService service;

    @PostMapping("/")
    public ResponseEntity<?> addMembers(@RequestBody GroupMemberReqDto dto) {
        return service.addMembers(dto);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<?> getSortedMembers(@PathVariable(value = "groupId") Long groupId) {
        return service.getSortedMembers(groupId);
    }
}

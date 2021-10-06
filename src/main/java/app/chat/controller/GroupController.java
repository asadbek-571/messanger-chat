package app.chat.controller;

import app.chat.model.req.group.GroupReqDto;
import app.chat.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getGroup(@PathVariable(value = "id") Long id) {
        return groupService.getGroup(id);
    }

    /**
     * todo get all groups for server admin
     */
    @GetMapping()
    public ResponseEntity<?> getAll() {
        return groupService.getAll();
    }

    @PostMapping()
    public ResponseEntity<?> createGroup(@RequestBody GroupReqDto dto) {
        return groupService.createGroup(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGroup(@PathVariable(value = "id") Long id, @RequestBody GroupReqDto dto) {
        return groupService.updateGroup(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable(value = "id") Long id) {
        return groupService.deleteGroup(id);
    }
}

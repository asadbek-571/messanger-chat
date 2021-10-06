package app.chat.controller;

import app.chat.model.dto.UserDto;
import app.chat.model.dto.UserEditDto;
import app.chat.service.AuthService;
import app.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable(value = "id") Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/get/list")
    public ResponseEntity<?> getList() {
        return userService.getAll();
    }

    @PostMapping()
    public ResponseEntity<?> add(@RequestBody UserDto dto) {
        return userService.createUser(dto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable(value = "id") Long id, @RequestBody UserEditDto dto) {
        return userService.updateUser(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/contact")
    public ResponseEntity<?> getContacts() {
        return userService.getContact();
    }

    @GetMapping("/personal")
    public ResponseEntity<?> getPersonals() {
        return userService.getPersonal();
    }

    @GetMapping("/group")
    public ResponseEntity<?> getGroups() {
        return userService.getGroup();
    }

    @GetMapping("/channel")
    public ResponseEntity<?> getChannels() {
        return userService.getChannel();
    }

    @GetMapping("/userBlock")
    public ResponseEntity<?> getUserBlocks() {
        return userService.getUserBlock();
    }
}

package app.chat.controller;

import app.chat.service.UsernameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/username")
@RequiredArgsConstructor
public class UsernameController {

    private final UsernameService usernameService;

    @GetMapping("/{id}}")
    public ResponseEntity<?> getUsername(@PathVariable(value = "id") Long id) {
        return usernameService.getUsername(id);
    }

//    @PostMapping()
//    public ResponseEntity<ApiResponse> add(@RequestParam String name) {
//        return usernameService.create(name);
//    }

    @GetMapping("/check/{name}")
    public Boolean checkExist(@PathVariable(value = "name") String username) {
        return usernameService.checkExist(username);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUsername(@PathVariable(value = "id") Long id, @RequestParam String name) {
        return usernameService.updateUsername(id, name);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsername(@PathVariable(value = "id") Long id) {
        return usernameService.delete(id);
    }
}

package app.chat.controller;

import app.chat.model.dto.PersonalDto;
import app.chat.service.PersonalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/personal")
public class PersonalController {

    private final PersonalService personalService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getPersonal(@PathVariable(value = "id") Long id) {
        return personalService.getPersonal(id);
    }

    @GetMapping("/get/list")
    public ResponseEntity<?> getList(@RequestParam(value = "size", defaultValue = "5", required = false) Integer size,
                                               @RequestParam(value = "page", defaultValue = "0", required = false) Integer page) {
        return personalService.getPage(size, page * size);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPersonal(@RequestParam Long user2Id) {
        return personalService.createPersonal(user2Id);
    }
    @PostMapping("/create{username}")
    public ResponseEntity<?> createPersonal(@PathVariable String username) {
        return personalService.createPersonal(username);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> updatePersonal(@PathVariable(value = "id") Long id, @RequestBody PersonalDto dto) {
        return personalService.updatePersonal(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePersonal(@PathVariable(value = "id") Long id) {
        return personalService.deletePersonal(id);
    }



}

package app.chat.service;

import app.chat.entity.personal.Personal;
import app.chat.entity.user.User;
import app.chat.model.dto.PersonalDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PersonalService {
    ResponseEntity<?> createPersonal(Long user2Id);

    ResponseEntity<?> createPersonal(String username);

    ResponseEntity<?> getPage(Integer size, Integer page);

    ResponseEntity<?> getPersonal(Long id);

    ResponseEntity<?> updatePersonal(Long id, PersonalDto dto);

    ResponseEntity<?> deletePersonal(Long id);

    Personal personal(User user1, User user2);

    List<Personal> getMyPersonals();

}

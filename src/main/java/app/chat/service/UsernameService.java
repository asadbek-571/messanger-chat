package app.chat.service;

import app.chat.entity.user.Username;
import org.springframework.http.ResponseEntity;

public interface UsernameService {
    ResponseEntity<?> getUsername(Long id);

    Username create(String name);

    ResponseEntity<?> updateUsername(Long id, String name);

    ResponseEntity<?> delete(Long id);

    ResponseEntity<?> delete(String name);

    boolean checkExist(String username);
}

package app.chat.service;

import app.chat.model.dto.LoginDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {

    ResponseEntity<?> login(LoginDto dto);
}

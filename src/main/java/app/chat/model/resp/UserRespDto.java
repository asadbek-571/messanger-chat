package app.chat.model.resp;

import app.chat.enums.Activity;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Setter
@Getter
public class UserRespDto {
    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String bio;

    private String password;

    private LocalDateTime lastActivity;

    private Activity activity;

    private Timestamp createdAt;

    private boolean isActive;
}

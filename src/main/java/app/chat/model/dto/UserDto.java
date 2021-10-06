package app.chat.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String bio;
    private String password;
}

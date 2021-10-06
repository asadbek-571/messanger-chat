package app.chat.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserEditDto {
    private String firstName;
    private String lastName;
    private String email;
    private String bio;
    private String password;
}

package app.chat.model.dto;

import app.chat.entity.user.User;
import app.chat.enums.ChatRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    private Long chatId;

    private User member;

    private ChatRoleEnum role;
}

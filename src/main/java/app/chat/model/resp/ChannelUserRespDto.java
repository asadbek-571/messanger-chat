package app.chat.model.resp;

import app.chat.enums.ChatRoleEnum;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class ChannelUserRespDto {
    private String name;

    private ChatRoleEnum role;

    private Timestamp joined;
}

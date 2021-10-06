package app.chat.model.resp;

import app.chat.enums.ChatType;
import app.chat.enums.SecurityType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelRespDto {
    private Long id;

    private ChatType chatType;

    private String link;

    // username.name
    private String username;

    private SecurityType securityType;

    private String title;

    private String description;
}

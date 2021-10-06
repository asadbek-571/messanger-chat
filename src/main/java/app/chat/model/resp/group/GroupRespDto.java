package app.chat.model.resp.group;

import app.chat.enums.ChatType;
import app.chat.enums.SecurityType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupRespDto {
    private Long id;
    private String title;
    private String description;
    private String link;
    private String username;
    private ChatType chatType;
    private SecurityType securityType;
    private Long creatorId;
    private Long memberCount;
    private Boolean isActive;
}
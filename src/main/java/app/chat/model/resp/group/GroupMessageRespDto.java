package app.chat.model.resp.group;

import app.chat.enums.SendState;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GroupMessageRespDto {
    private Long id;

    private Long groupId;

    private String text;

    private Long senderId;

    private List<String> files;

    private LocalDateTime sendDate;

    private SendState sendState;

    private Boolean isPinned;

    private Long viewCount;
}

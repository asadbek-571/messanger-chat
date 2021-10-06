package app.chat.model.dto;

import app.chat.entity.user.User;
import app.chat.enums.SendState;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ChannelMessageDto {

    private Long id;

    private Long channelId;

    private String text;

    private Long senderId;

    private LocalDateTime sendDate;

    private SendState sendState;

    private Boolean isPinned;

    private List<String> files;
}
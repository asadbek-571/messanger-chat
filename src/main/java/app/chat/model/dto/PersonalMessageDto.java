package app.chat.model.dto;

import app.chat.enums.SendState;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PersonalMessageDto {
    private Long id;
    private PersonalDto personalDto;
    private Long senderId;
    private String text;
    private List<String> files;
    private LocalDateTime sendDate;
    private SendState sendState;
    private Boolean isPinned;
}

package app.chat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

    private Boolean showSender;

    private Boolean playSound;

    private Boolean showMessagePreview;

    private Boolean contactJoin;

    private Boolean mute;

}

package app.chat.entity.settings;

import app.chat.entity.template.AbsMain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification extends AbsMain {

    @Column(name = "show_sender", nullable = false)
    private Boolean showSender = true;

    @Column(name = "play_sound", nullable = false)
    private Boolean playSound = true;

    @Column(name = "show_message_preview", nullable = false)
    private Boolean showMessagePreview = true;

    @Column(name = "contact_join", nullable = false)
    private Boolean contactJoin = true;

    @Column(name = "mute", nullable = false)
    private Boolean mute = false;

}

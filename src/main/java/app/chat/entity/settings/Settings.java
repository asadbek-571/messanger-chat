package app.chat.entity.settings;

import app.chat.entity.template.AbsMain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "settings")
public class Settings extends AbsMain {

    //todo fill with dataLoader
    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @OneToOne
    @JoinColumn(name = "privacy_security_id", nullable = false)
    private PrivacySecurity privacySecurity;

    @OneToOne
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;
}
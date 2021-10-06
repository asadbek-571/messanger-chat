package app.chat.entity.personal;

import app.chat.entity.user.User;
import app.chat.entity.template.AbsMain;
import app.chat.enums.SendState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "personal_message")
public class PersonalMessage extends AbsMain {

    @ManyToOne(optional = false)
    private Personal personal;

    @Column(length = 500)
    private String text;

    @ManyToOne(optional = false)
    private User sender;

    @Column(nullable = false)
    private LocalDateTime sendDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SendState sendState;

    @Column(nullable = false)
    private Boolean isPinned;

}

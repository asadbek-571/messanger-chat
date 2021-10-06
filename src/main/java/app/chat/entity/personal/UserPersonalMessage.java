package app.chat.entity.personal;

import app.chat.entity.user.User;
import app.chat.entity.template.AbsMain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "user_personal_message")
public class UserPersonalMessage extends AbsMain {

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private PersonalMessage personalMessage;

    @Column(nullable = false)
    private Boolean isViewed;

}

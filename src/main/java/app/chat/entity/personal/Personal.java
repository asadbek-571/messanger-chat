package app.chat.entity.personal;

import app.chat.entity.user.User;
import app.chat.entity.template.AbsMain;
import app.chat.enums.ChatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "personal")
public class Personal extends AbsMain {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChatType chatType;

    @ManyToOne
    private User user1;

    @ManyToOne
    private User user2;

    @Column(name = "is_mute", columnDefinition = "boolean default false")
    private Boolean isMute = false;
}

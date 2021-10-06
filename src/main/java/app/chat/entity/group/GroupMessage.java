package app.chat.entity.group;

import app.chat.entity.template.AbsMain;
import app.chat.entity.template.chat.AbsChatMessage;
import app.chat.entity.user.User;
import app.chat.enums.SendState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "groups_messages")
public class GroupMessage extends AbsChatMessage {

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
}
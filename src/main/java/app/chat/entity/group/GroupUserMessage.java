package app.chat.entity.group;

import app.chat.entity.template.chat.AbsChatUserMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_group_messages")
public class GroupUserMessage extends AbsChatUserMessage {

    @ManyToOne
    @JoinColumn(name = "group_message_id", nullable = false)
    private GroupMessage groupMessage;
}

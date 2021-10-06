package app.chat.entity.group;

import app.chat.entity.Attachment;
import app.chat.entity.template.AbsMain;
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
@Table(name = "group_messages_attachments")
public class GroupMessageAttachment extends AbsMain {

    @ManyToOne
    @JoinColumn(name = "group_message_id", nullable = false)
    private GroupMessage groupMessage;

    @ManyToOne
    @JoinColumn(name = "attachment_id", nullable = false)
    private Attachment attachment;
}

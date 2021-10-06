package app.chat.entity.channel;

import app.chat.entity.Attachment;
import app.chat.entity.template.AbsMain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChannelMessageAttachment extends AbsMain {

    @ManyToOne
    @JoinColumn(name = "channel_message_id", nullable = false)
    private ChannelMessage channelMessage;

    @ManyToOne
    @JoinColumn(name = "attachment_id", nullable = false)
    private Attachment attachment;
}

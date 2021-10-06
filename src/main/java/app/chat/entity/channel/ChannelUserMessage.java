package app.chat.entity.channel;

import app.chat.entity.template.chat.AbsChatUserMessage;
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
public class ChannelUserMessage extends AbsChatUserMessage {

    @ManyToOne
    @JoinColumn(name = "channel_message_id", nullable = false)
    private ChannelMessage channelMessage;
}

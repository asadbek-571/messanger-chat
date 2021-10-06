package app.chat.entity.channel;

import app.chat.entity.template.chat.AbsChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChannelMessage extends AbsChatMessage {

    @ManyToOne
    private Channel channel;
}

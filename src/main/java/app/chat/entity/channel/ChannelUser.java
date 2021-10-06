package app.chat.entity.channel;

import app.chat.entity.template.chat.AbsChatUser;
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
public class ChannelUser extends AbsChatUser {

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    public String getName(){
        return getUser().getFirstName() + " " + getUser().getLastName();
    }
}

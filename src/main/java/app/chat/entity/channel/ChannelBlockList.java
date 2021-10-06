package app.chat.entity.channel;

import app.chat.entity.template.AbsMain;
import app.chat.entity.user.User;
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
public class ChannelBlockList extends AbsMain {

    @ManyToOne
    private Channel channel;

    @ManyToOne
    private User user;
}

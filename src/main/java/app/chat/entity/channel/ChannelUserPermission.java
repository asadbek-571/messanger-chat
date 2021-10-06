package app.chat.entity.channel;

import app.chat.entity.template.AbsMain;
import app.chat.enums.ChatPermissions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChannelUserPermission extends AbsMain {
    @ManyToOne
    private ChannelUser channelUser;

    @Enumerated(EnumType.STRING)
    private ChatPermissions permission;
}

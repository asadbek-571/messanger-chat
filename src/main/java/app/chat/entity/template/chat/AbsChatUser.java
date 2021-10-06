package app.chat.entity.template.chat;

import app.chat.entity.ChatRole;
import app.chat.entity.template.AbsMain;
import app.chat.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@Setter
@Getter
@MappedSuperclass
public abstract class AbsChatUser extends AbsMain {

    @ManyToOne(optional = false)
    private User user;

    @Column(name = "is_mute", columnDefinition = "boolean default false")
    private Boolean isMute = false;

    @ManyToOne(optional = false)
    private ChatRole chatRole;

    @Column(name = "is_pinned", columnDefinition = "boolean default false")
    private Boolean isPinned = false;

    @Transient
    @JsonIgnore
    private String name;
}

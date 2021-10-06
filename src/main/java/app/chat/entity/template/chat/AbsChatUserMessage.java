package app.chat.entity.template.chat;

import app.chat.entity.user.User;
import app.chat.entity.template.AbsMain;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@MappedSuperclass
public abstract class AbsChatUserMessage extends AbsMain {

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_viewed", columnDefinition = "boolean default false")
    private Boolean isViewed = false;
}

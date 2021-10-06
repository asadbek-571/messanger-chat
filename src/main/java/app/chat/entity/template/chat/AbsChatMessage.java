package app.chat.entity.template.chat;

import app.chat.entity.template.AbsMain;
import app.chat.entity.user.User;
import app.chat.enums.SendState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass
public abstract class AbsChatMessage extends AbsMain {

    @Column(name = "text", columnDefinition = "text")
    private String text;

    @ManyToOne(optional = false)
    private User sender;

    // Channelda message yaratilgan vaqt, absMainda esa column yaratilgan vaqt
    @Column(name = "send_date", columnDefinition = "timestamp default now()", updatable = false)
    private LocalDateTime sendDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private SendState sendState;

    @Column(name = "is_pinned", columnDefinition = "boolean default false")
    private Boolean isPinned = false;

    // xabar o'chirilgan yoki o'chirilmaganligi
    @Column(name = "status", columnDefinition = "boolean default true")
    private Boolean status = true;
}

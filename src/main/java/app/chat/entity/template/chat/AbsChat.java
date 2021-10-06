package app.chat.entity.template.chat;

import app.chat.entity.template.AbsMain;
import app.chat.entity.user.Username;
import app.chat.enums.ChatType;
import app.chat.enums.SecurityType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Setter
@Getter
@MappedSuperclass
public abstract class AbsChat extends AbsMain {

    @Enumerated(EnumType.STRING)
    private ChatType chatType;

    @Column(name = "link")
    private String link = UUID.randomUUID().toString();

    @OneToOne
    @JoinColumn(name = "username_id")
    private Username username;

    @Column(nullable = false, name = "security_type")
    @Enumerated(EnumType.STRING)
    private SecurityType securityType;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;
}

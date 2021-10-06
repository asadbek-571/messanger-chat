package app.chat.entity;

import app.chat.entity.template.AbsMain;
import app.chat.enums.ChatPermissions;
import app.chat.enums.ChatRoleEnum;
import app.chat.enums.Permission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatRole extends AbsMain {

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, unique = true)
    private ChatRoleEnum role;

    @Enumerated(value = EnumType.STRING)
    @ElementCollection
    private Set<ChatPermissions> permissions;
}

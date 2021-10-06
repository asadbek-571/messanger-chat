package app.chat.entity.group;

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
public class GroupUserPermission extends AbsMain {
    @ManyToOne
    private GroupUser groupUser;

    @Enumerated(EnumType.STRING)
    private ChatPermissions permission;
}

package app.chat.entity.group;

import app.chat.entity.template.chat.AbsChatUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "group_users")
public class GroupUser extends AbsChatUser {

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
}

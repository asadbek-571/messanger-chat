package app.chat.entity.user;

import app.chat.entity.channel.Channel;
import app.chat.entity.group.Group;
import app.chat.entity.template.AbsMain;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

//@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "usernames")
public class Username extends AbsMain {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public Username(String name) {
        this.name = name;
    }
//
//    @OneToOne(mappedBy = "username")
//    private User user;
//
//    @OneToOne(mappedBy = "username")
//    private Group group;
//
//    @OneToOne(mappedBy = "username")
//    private Channel channel;
}

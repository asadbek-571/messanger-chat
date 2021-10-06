package app.chat.entity.channel;

import app.chat.entity.template.chat.AbsChat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Channel extends AbsChat {

}

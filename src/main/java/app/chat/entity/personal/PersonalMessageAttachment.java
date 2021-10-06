package app.chat.entity.personal;

import app.chat.entity.Attachment;
import app.chat.entity.template.AbsMain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "personal_message_attachment")
public class PersonalMessageAttachment extends AbsMain {

    @ManyToOne(optional = false)
    private PersonalMessage personalMessage;

    @ManyToOne(optional = false)
    private Attachment attachment;
}

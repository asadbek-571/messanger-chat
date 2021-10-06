package app.chat.entity;

import app.chat.entity.template.AbsMain;
import app.chat.enums.ChatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatFolder extends AbsMain {
    @Enumerated(EnumType.STRING)
    private ChatType chatType;

    @Column
    private Long chatId;

    @ManyToOne
    private Folder folder;
}

package app.chat.entity;

import app.chat.entity.template.AbsMain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Folder extends AbsMain {
    @Column
    private String name;
}

package app.chat.entity.settings;

import app.chat.enums.Lang;
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
@Table(name = "languages")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //todo full name language
    @Column(name = "name", nullable = false)
    private String name;

    //todo uz ru en
    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private Lang code;

    public Language(String name, Lang code) {
        this.name = name;
        this.code = code;
    }
}

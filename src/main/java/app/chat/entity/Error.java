package app.chat.entity;

import app.chat.entity.settings.Language;
import app.chat.enums.ErrorSeries;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "errors")
public class Error {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Integer code;

    @ManyToOne(optional = false)
    private Language language;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ErrorSeries series;

    @Column(nullable = false,columnDefinition = "boolean default true")
    private Boolean active = true;

    public Error(String message, Integer code, Language language, ErrorSeries series) {
        this.message = message;
        this.code = code;
        this.language = language;
        this.series = series;
    }
}

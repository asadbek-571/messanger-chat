package app.chat.entity.settings;

import app.chat.entity.template.AbsMain;
import app.chat.enums.AccountSelfDestructionDeadline;
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
@Table(name = "privacy_and_security")
public class PrivacySecurity extends AbsMain {

    @Column(name = "show_last_seen", nullable = false)
    private Boolean showLastSeen = true;

    @Column(name = "show_photo", nullable = false)
    private Boolean showPhoto = true;

    @Column(name = "account_self_destruction_deadline", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AccountSelfDestructionDeadline accountSelfDestructionDeadline = AccountSelfDestructionDeadline.SIX_MONTHS;

    @Column(nullable = false, name = "forward_access")
    private Boolean forwardAccess = true;

    @Column(name = "show_email", nullable = false)
    private Boolean showEmail = true;

    @Column(name = "add_access", nullable = false)
    private Boolean addAccess = true;

    @OneToOne(optional = true)
    @JoinColumn(name = "two_step_verification_id")
    private TwoStepVerification twoStepVerification;
}

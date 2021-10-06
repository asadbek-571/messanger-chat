package app.chat.repository.sittings;

import app.chat.entity.settings.TwoStepVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwoStepVerificationRepo extends JpaRepository<TwoStepVerification, Long> {
}

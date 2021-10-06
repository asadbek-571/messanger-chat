package app.chat.repository.sittings;

import app.chat.entity.settings.PrivacySecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivacySecurityRepo extends JpaRepository<PrivacySecurity,Long> {
}

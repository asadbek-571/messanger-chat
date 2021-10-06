package app.chat.repository.sittings;

import app.chat.entity.settings.Language;
import app.chat.enums.Lang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepo extends JpaRepository<Language, Long> {
    Optional<Language> findByCode(Lang code);

    Language getByCode(Lang code);
}

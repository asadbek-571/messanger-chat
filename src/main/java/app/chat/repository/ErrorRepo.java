package app.chat.repository;

import app.chat.entity.Error;
import app.chat.enums.Lang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ErrorRepo extends JpaRepository<Error, Long> {
    Optional<Error> findByCodeAndActiveAndLanguage_Code(Integer code, boolean active, Lang language_code);
}

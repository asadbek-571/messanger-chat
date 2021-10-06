package app.chat.repository.personal;

import app.chat.entity.personal.PersonalMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonalMessageRepo extends JpaRepository<PersonalMessage, Long> {

    @Query(value = "select * from personal_message pm\n" +
            "where pm.personal_id=:personal_id and active=true\n" +
            "order by ?#{#pageable}", nativeQuery = true)
    List<PersonalMessage> findAllByPersonal_IdAndActiveTrue(Long personal_id, Pageable pageable);
}

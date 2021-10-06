package app.chat.repository.personal;

import app.chat.entity.personal.Personal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalRepo extends JpaRepository<Personal, Long> {

    @Query(value = "SELECT t.* FROM personal t " +
            "WHERE t.user1_id IN (:user1_id , :user2_id) and " +
            "t.user2_id IN (:user1_id , :user2_id) and " +
            "t.active = true",
            nativeQuery = true)
    Optional<Personal> findPersonal(Long user1_id, Long user2_id);

    @Query(value = " SELECT * FROM personal t " +
            " WHERE t.active=true " +
            " LIMIT :size OFFSET :page ", nativeQuery = true)
    List<Personal> getAllByActiveTrue(Integer size, Integer page);

    @Query(value = "select * from personal p\n" +
            "where active=true and p.id=:id", nativeQuery = true)
    Optional<Personal> getAllByActiveTrueAndId(Long id);


    @Query(value = "select *\n" +
            "from personal p\n" +
            "where p.user1_id = :user1_id and p.active = true\n" +
            "   or p.user2_id = :user2_id and p.active = true", nativeQuery = true)
    List<Personal> findAllByUser1_IdAndActiveTrueOrUser2_IdAndActiveTrue(Long user1_id, Long user2_id);

    @Query(value = "select t.* from personal t " +
            " where t.active = true and (t.user1_id = :userId or t.user2_id = :userId)",
            nativeQuery = true)
    List<Personal> getPersonal(Long userId);

}

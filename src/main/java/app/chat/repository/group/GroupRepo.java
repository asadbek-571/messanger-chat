package app.chat.repository.group;

import app.chat.entity.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepo extends JpaRepository<Group, Long> {
    @Query("select g from Group g where g.username.name = ?1 and g.active = true")
    Optional<Group> findByUsername_Name(String username_name);
}

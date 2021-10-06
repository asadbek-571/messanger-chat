package app.chat.repository;

import app.chat.entity.ChatRole;
import app.chat.enums.ChatRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoleRepo extends JpaRepository<ChatRole, Long> {

    @Query("select c from ChatRole c where c.role = ?1 and c.active = true")
    Optional<ChatRole> findByRole(ChatRoleEnum role);

    @Query("select c from ChatRole c where c.id = ?1 and c.active = true")
    Optional<ChatRole> findActiveById(Long id);

}

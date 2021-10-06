package app.chat.repository;

import app.chat.entity.user.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsernameRepo extends JpaRepository<Username, Long> {
    boolean existsByName(String name);

    @Query("select (count(u) > 0) from Username u where u.name = ?1 and u.id <> ?2 and u.active = true")
    boolean existsByName(String name, Long id);

    @Query("select u from Username u where u.id = ?1 and u.active = true")
    Optional<Username> findActiveById(Long id);

    @Query("select u from Username u where u.name = ?1 and u.active = true")
    Optional<Username> findActiveByName(String name);
}

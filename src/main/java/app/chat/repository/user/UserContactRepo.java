package app.chat.repository.user;

import app.chat.entity.user.UserContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserContactRepo extends JpaRepository<UserContact, Long> {
    List<UserContact> findAllByUser1_IdAndActiveTrue(Long user1_id);
}

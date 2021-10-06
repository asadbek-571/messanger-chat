package app.chat.repository.user;

import app.chat.entity.personal.UserPersonalMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPersonalMessageRepo extends JpaRepository<UserPersonalMessage,Long> {
}

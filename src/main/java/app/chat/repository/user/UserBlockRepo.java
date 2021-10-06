package app.chat.repository.user;

import app.chat.entity.user.UserBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBlockRepo extends JpaRepository<UserBlock, Long> {

    List<UserBlock> findAllByUser_Id(Long user_id);
}

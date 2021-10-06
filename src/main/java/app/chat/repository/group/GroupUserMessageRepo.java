package app.chat.repository.group;

import app.chat.entity.group.GroupUserMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupUserMessageRepo extends JpaRepository<GroupUserMessage, Long> {

    List<GroupUserMessage> findAllByGroupMessage_IdAndIsViewedTrue(Long groupMessage_id);

    Optional<GroupUserMessage> findByGroupMessage_IdAndUser_IdAndIsViewedTrue(Long groupMessage_id, Long user_id);
}

package app.chat.repository.group;

import app.chat.entity.group.GroupMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMessageRepo extends JpaRepository<GroupMessage, Long> {

    List<GroupMessage> findAllByGroup_IdAndActiveTrue(Long groupId, Pageable hundred);
}

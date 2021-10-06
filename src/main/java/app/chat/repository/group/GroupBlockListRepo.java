package app.chat.repository.group;

import app.chat.entity.group.GroupBlockList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupBlockListRepo extends JpaRepository<GroupBlockList, Long> {

    Optional<GroupBlockList> findByUser_IdAndGroup_IdAndActiveTrue(Long user_id, Long group_id);
}

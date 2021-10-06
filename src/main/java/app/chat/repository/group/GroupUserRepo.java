package app.chat.repository.group;

import app.chat.entity.group.Group;
import app.chat.entity.group.GroupUser;
import app.chat.entity.user.User;
import app.chat.model.resp.group.GroupUserRespDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupUserRepo extends JpaRepository<GroupUser, Long> {
//    List<GroupUser> findByGroup_ActiveTrueAndUser_ActiveTrueAndUser_Id(Long user_id);

    @Query(value = " SELECT count(t.*) > 0 " +
            " FROM users_groups t " +
            " LEFT  JOIN chat_role cr ON cr.id = t.chat_role_id " +
            "WHERE t.user_id=:userId AND t.group_id in :groupId" +
            " AND (cr.role = 'ADMIN' or cr.role = 'OWNER')", nativeQuery = true)
    boolean existsById_AndChatRole_Role(Long userId, Long groupId);

    boolean existsByUserAndGroupAndActiveTrue(User user, Group group);


    List<GroupUser> findAllByGroup_IdAndActiveTrue(Long group_id, Sort sort);
    List<GroupUser> findAllByGroup_IdAndActiveTrue(Long group_id);

    //todo get group all members
    List<GroupUser> findByGroup_IdAndActiveTrue(Long group_id);

    //todo get GroupMember by GroupId and UserId
    Optional<GroupUser> findByUser_IdAndGroup_IdAndActiveTrue(Long user_id, Long group_id);

    List<GroupUser> findAllByUser_Id(Long user_id);
}

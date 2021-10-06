package app.chat.repository;

import app.chat.entity.group.GroupUserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupUserPermissionRepo extends JpaRepository<GroupUserPermission, Long> {
    @Query(value = "SELECT gup.permission FROM GroupUserPermission gup WHERE gup.groupUser.user.id = ?1 AND gup.groupUser.group.id=?2 AND gup.active = TRUE")
    List<String> getPermissions(Long userId, Long groupId);
}

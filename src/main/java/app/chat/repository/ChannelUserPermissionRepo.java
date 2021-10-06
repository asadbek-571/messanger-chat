package app.chat.repository;

import app.chat.entity.channel.ChannelUserPermission;
import app.chat.enums.ChatPermissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelUserPermissionRepo extends JpaRepository<ChannelUserPermission, Long> {
    @Query(value = "SELECT cup.permission FROM ChannelUserPermission cup WHERE cup.channelUser.channel.id=?2 AND cup.channelUser.user.id=?1 AND cup.active = TRUE")
    List<String> getPermissions(Long userId, Long channelId);

    @Query("delete from ChannelUserPermission cup where cup.channelUser.channel.id = ?1 and cup.channelUser.user.id=?2")
    void deleteByChannelIdAndUserId(Long channelId, Long userId);

    @Query(value = "SELECT cup FROM ChannelUserPermission cup WHERE cup.channelUser.id=?1 and cup.active = TRUE")
    List<ChannelUserPermission> getPermissions(Long id);

    ChannelUserPermission getByChannelUserIdAndPermission(Long channelUser_id, ChatPermissions permission);
}

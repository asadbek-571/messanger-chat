package app.chat.repository;

import app.chat.entity.channel.ChannelBlockList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChannelBlocklistRepo extends JpaRepository<ChannelBlockList, Long> {
    @Query("select (count(c) > 0) from ChannelBlockList c where c.user.id = ?1 and c.channel.id = ?2 and c.active = true")
    boolean isUserBlocked(Long user_id, Long channel_id);

    @Query("select c from ChannelBlockList c where c.channel.id = ?1 and c.user.id = ?2 and c.active = true")
    Optional<ChannelBlockList> findActiveByChannelIdAndUserId(Long channel_id, Long user_id);
}

package app.chat.repository.channel;

import app.chat.entity.channel.ChannelUser;
import app.chat.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelUserRepo extends JpaRepository<ChannelUser, Long> {
    @Query(value = "select c.* from channel_user c where c.channel_id = :channel_id and c.active = true limit :result offset :pageNumber ", nativeQuery = true)
    List<ChannelUser> findAllMembers(Long channel_id, Integer pageNumber, Integer result);

    @Query(value = "select count(c.*) > 0\n" +
            "from channel_user c\n" +
            "         join chat_role cr on cr.id = c.chat_role_id\n" +
            "where c.user_id = :userId\n" +
            "  and c.channel_id = :channelId\n" +
            "  and (cr.role = 'ADMIN' or cr.role = 'OWNER')\n" +
            "  and c.active = true", nativeQuery = true)
    boolean isAdmin(Long userId, Long channelId);

    @Query(value = "select count(c.*) > 0\n" +
            "from channel_user c\n" +
            "         join chat_role cr on cr.id = c.chat_role_id\n" +
            "where c.user_id = :userId\n" +
            "  and c.channel_id = :channelId\n" +
            "  and cr.role = 'OWNER'\n" +
            "  and c.active = true", nativeQuery = true)
    boolean isOwner(Long userId, Long channelId);


    @Query(value = "select count(c) from ChannelUser c where c.channel.id = ?1 and c.active = true")
    Long getMemberNumber(Long channelId);

    @Query("select (count(c) > 0) from ChannelUser c where c.user.id = ?1 and c.channel.id = ?2 and c.active = true")
    boolean existsChannelUser(Long user_id, Long channel_id);

    @Query("select c from ChannelUser c where c.user.id = ?1 and c.channel.id = ?2 and c.active = true")
    Optional<ChannelUser> findActiveChannelUser(Long user_id, Long channel_id);

    @Query("select c from ChannelUser c where c.id = ?1 and c.active = true")
    Optional<ChannelUser> findActiveChannelUser(Long channelUserId);

    List<ChannelUser> findAllByChannel_Id(Long channel_id);
}

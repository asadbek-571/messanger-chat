package app.chat.repository.channel;

import app.chat.entity.channel.ChannelUserMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChannelUserMessageRepo extends JpaRepository<ChannelUserMessage , Long> {
    @Query("select c from ChannelUserMessage c where c.channelMessage.id = ?1 and c.user.id = ?2 and c.active = true")
    Optional<ChannelUserMessage> findChannelMessage(Long channelMessage_id, Long user_id);

    @Query(value = "select cum.* from channel_user_message cum where cum.channel_message_id = :messageId and cum.user_id in :users", nativeQuery = true)
    List<ChannelUserMessage> findMessageInUser(Long messageId, List<Long> users);

    @Query(value = "delete from channel_user_message cum using channel c where cum.user_id = :userId and c.id = :channelId", nativeQuery = true)
    void deleteByUserIdAndChannelId(Long channelId, Long userId);

    @Query(value = "select cum.*\n" +
            "from channel_user_message cum\n" +
            "         join channel_message cm on cm.id = cum.channel_message_id\n" +
            "where cm.channel_id = :channelId and cum.user_id = :userId and cum.active = true order by cum.created_at desc limit :limit offset :offset", nativeQuery = true)
    List<ChannelUserMessage> findAllActiveByChannelId(Long channelId, Long userId, int limit, int offset);
}

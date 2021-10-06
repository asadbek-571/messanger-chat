package app.chat.repository.channel;

import app.chat.entity.channel.ChannelMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChannelMessageRepo extends JpaRepository<ChannelMessage, Long> {

    @Query("select c from ChannelMessage c where c.id = ?1 and c.active = true")
    Optional<ChannelMessage> findActiveById(Long id);

    @Query(value = "select cm.* from channel_message cm where cm.channel_id = :channelId and cm.active=true order by cm.created_at desc limit :number offset :offset ", nativeQuery = true)
    List<ChannelMessage> findAllActiveByChannelId(Long channelId, Integer number, Integer offset);

    @Query(value = "select cm.* from channel_message cm where cm.channel_id = :channelId and cm.active=true", nativeQuery = true)
    List<ChannelMessage> findAllActiveByChannelId(Long channelId);

    @Query(nativeQuery = true, value = "select cm.*\n" +
            "from channel_user_message cum\n" +
            "         join channel_message cm on cm.id = cum.channel_message_id\n" +
            "where cum.user_id = :userId\n" +
            "  and cm.channel_id = :channelId\n" +
            "  and cum.active = true\n" +
            "  and cum.is_viewed = false\n" +
            "order by cm.created_at desc limit :limit offset :offset ")
    List<ChannelMessage> getUnreadMessages(Long channelId, Long userId, Integer limit, Integer offset);


}

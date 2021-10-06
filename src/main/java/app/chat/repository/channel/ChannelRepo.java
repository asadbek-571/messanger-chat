package app.chat.repository.channel;

import app.chat.entity.channel.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelRepo extends JpaRepository<Channel, Long> {

    @Query("select c from Channel c where c.id = ?1 and c.active = true")
    Optional<Channel> findActiveById(Long id);

    @Query("select c from Channel c where c.username.name = ?1 and c.active = true")
    Optional<Channel> findByUsername_Name(String username_name);

    @Query("select c from Channel c where c.link = ?1 and c.active = true")
    Optional<Channel> findByLink(String link);

    @Query(value = "select c.* from channel_user cu " +
            "join channel c on c.id = cu.channel_id where cu.user_id = :userId and cu.active = true limit :number offset :offset", nativeQuery = true)
    List<Channel> getUserChannels(Long userId, int number, int offset);

}

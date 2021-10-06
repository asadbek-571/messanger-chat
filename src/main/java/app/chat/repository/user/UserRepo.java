package app.chat.repository.user;

import app.chat.entity.channel.Channel;
import app.chat.entity.group.Group;
import app.chat.entity.personal.Personal;
import app.chat.entity.user.User;
import app.chat.entity.user.UserBlock;
import app.chat.entity.user.UserContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface UserRepo extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    @Query("select u from users u where u.id = ?1 and u.active = true")
    Optional<User> findActiveById(Long id);

    boolean existsByEmailAndIdNot(String email, Long id);

    @Query("select u from users u where u.userName.name = ?1 and u.active = true ")
    Optional<User> findByUserName_Name(String username_name);

    Optional<User> findByEmailAndActiveTrue(String email);

    @Query(nativeQuery = true, value = "select t.* from users_groups t join users u on u.id = t.user_id where u.id = :id")
    List<Group> getGroup(Long id);

    @Query(nativeQuery = true, value = "select t.* from channel_user t join users u on u.id = t.user_id where u.id = :id")
    List<Channel> getChannel(Long id);   // ok

    @Query(nativeQuery = true, value = "select t.blocked_user_id from user_block t join users u on u.id = t.users_id where u.id = :id")
    List<UserBlock> getBlocks(Long id);  // ok

    @Query(value = "select cu.user from ChannelUser cu where cu.channel.id = ?1 and cu.active = true")
    List<User> getMembers(Long channelId);

    @Query(value = "select u.*\n" +
            "from users u\n" +
            "         join channel_block_list cbl on u.id = cbl.user_id\n" +
            "where cbl.channel_id = :channelId and cbl.active = true and u.active = true order by cbl.created_at desc limit :limit offset :offset", nativeQuery = true)
    List<User> getChannelBlocklist(Long channelId, Integer limit, Integer offset);
}

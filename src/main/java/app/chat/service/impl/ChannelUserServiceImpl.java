package app.chat.service.impl;

import app.chat.entity.channel.ChannelUser;
import app.chat.repository.channel.ChannelUserRepo;
import app.chat.service.ChannelUserService;
import app.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChannelUserServiceImpl implements ChannelUserService {

    private final UserService userService;
    private final ChannelUserRepo channelUserRepo;

    @Override
    public ChannelUser findMyAuthorityChannelFollower(Long followerId) {
        List<ChannelUser> myAuthorityChannel = userService.getMyAuthorityChannel();
        for (ChannelUser channelUser : myAuthorityChannel) {
            List<ChannelUser> followers = channelUserRepo.findAllByChannel_Id(channelUser.getChannel().getId());
            for (ChannelUser follower : followers) {
                if (follower.isActive()) {
                    if (follower.getId().equals(followerId)) {
                        return follower;
                    }
                }
            }
        }
        return null;
    }
}

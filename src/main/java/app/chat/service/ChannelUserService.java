package app.chat.service;

import app.chat.entity.channel.ChannelUser;

public interface ChannelUserService {

    ChannelUser findMyAuthorityChannelFollower(Long followerId);
}

package app.chat.mapper;

import app.chat.entity.Error;
import app.chat.entity.channel.Channel;
import app.chat.entity.channel.ChannelMessage;
import app.chat.entity.channel.ChannelUser;
import app.chat.entity.channel.ChannelUserMessage;
import app.chat.entity.group.Group;
import app.chat.entity.group.GroupMessage;
import app.chat.entity.personal.Personal;
import app.chat.entity.personal.PersonalMessage;
import app.chat.entity.user.User;
import app.chat.enums.ErrorSeries;
import app.chat.model.dto.*;
import app.chat.model.resp.ChannelRespDto;
import app.chat.model.resp.ChannelUserRespDto;
import app.chat.model.resp.group.GroupMessageRespDto;
import app.chat.model.resp.group.GroupRespDto;
import app.chat.model.resp.UserRespDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE, componentModel = "spring")
@Component
public interface MapstructMapper {
    /**
     * User
     */
    @Mapping(source = "userName.name", target = "username")
    UserRespDto toUserDto(User user);

    List<UserRespDto> toUserDto(List<User> users);

    @Mapping(source = "series", target = "errorSeries", qualifiedByName = "getErrorSeriesValue")
    ErrorDto toErrorDto(Error error);

    @Named("getErrorSeriesValue")
    static String getErrorSeries(ErrorSeries errorSeries) {
        return errorSeries.name();
    }

    @Mapping(target = "name", source = "name")
    @Mapping(target = "role", source = "chatRole.role")
    @Mapping(target = "joined", source = "createdAt")
    ChannelUserRespDto toChannelUserDto(ChannelUser channelUser);

    List<ChannelUserRespDto> toChannelUserDto(List<ChannelUser> channelUser);

    @Mapping(target = "personalDto", source = "personal", qualifiedByName = "toPersonalDto")
    @Mapping(target = "senderId", source = "sender.id")
    PersonalMessageDto toPersonalMessageDto(PersonalMessage personalMessage);

    List<PersonalMessageDto> toPersonalMessageDtoList(List<PersonalMessage> personalMessages);

    @Named("toPersonalDto")
    @Mapping(target = "user1Id", source = "user1.id")
    @Mapping(target = "user2Id", source = "user2.id")
    PersonalDto toPersonalDto(Personal personal);

    List<PersonalDto> toPersonalDto(List<Personal> personals);

    @Mapping(target = "channelId" , source = "channel.id")
    @Mapping(target = "senderId", source = "sender.id")
    ChannelMessageDto toChannelMessageDto(ChannelMessage channelMessage);

    List<ChannelMessageDto> toChannelMessageDto(List<ChannelMessage> channelMessages);

    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "senderId", source = "sender.id")
    GroupMessageRespDto toGroupMessageDto(GroupMessage groupMessage);

    List<GroupMessageRespDto> toGroupMessageDtoList(List<GroupMessage> messages);

    @Mapping(target = "creatorId", source = "createdBy.id")
    @Mapping(target = "username", source = "username.name")
    @Mapping(target = "isActive", source = "active")
    GroupRespDto toGroupRespDto(Group group);

    @Mapping(target = "chatId", source = "channel.id")
    @Mapping(target = "role", source = "chatRole.role")
    @Mapping(target = "member", source = "user")
    MemberDto toMemberDto(ChannelUser member);

    List<MemberDto> toMemberDto(List<ChannelUser> members);

    List<GroupRespDto> toGroupRespDtoList(List<Group> groupList);

    @Mapping(source = "username.name", target = "username")
    ChannelRespDto toChannelDto(Channel channel);

    List<ChannelRespDto> toChannelDto(List<Channel> channels);

    @Mapping(target = "channelId" , source = "channelMessage.channel.id")
    @Mapping(target = "senderId", source = "channelMessage.sender.id")
    ChannelMessageDto toChannelMessagesDto(ChannelUserMessage channelUserMessage);

    List<ChannelMessageDto> toChannelMessagesDto(List<ChannelUserMessage> channelUserMessage);
}
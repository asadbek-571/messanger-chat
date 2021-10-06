package app.chat.service.impl;

import app.chat.entity.ChatRole;
import app.chat.entity.channel.*;
import app.chat.entity.user.User;
import app.chat.entity.user.Username;
import app.chat.enums.*;
import app.chat.helpers.UserSession;
import app.chat.helpers.Utils;
import app.chat.mapper.MapstructMapper;
import app.chat.model.ApiResponse;
import app.chat.model.dto.ChannelDto;
import app.chat.model.dto.ChannelMemberDto;
import app.chat.model.req.AttachRoleDto;
import app.chat.repository.ChannelBlocklistRepo;
import app.chat.repository.ChannelUserPermissionRepo;
import app.chat.repository.ChatRoleRepo;
import app.chat.repository.channel.ChannelMessageRepo;
import app.chat.repository.channel.ChannelRepo;
import app.chat.repository.channel.ChannelUserMessageRepo;
import app.chat.repository.channel.ChannelUserRepo;
import app.chat.repository.user.UserRepo;
import app.chat.service.ChannelMessageService;
import app.chat.service.ChannelService;
import app.chat.service.ErrorService;
import app.chat.service.UsernameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.*;

import static app.chat.model.ApiResponse.response;
import static app.chat.helpers.Utils.isEmpty;

@Service
@Transactional
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepo channelRepo;
    private final ChannelUserRepo channelUserRepo;
    private final ChannelMessageRepo channelMessageRepo;
    private final ChannelUserMessageRepo channelUserMessageRepo;
    private final ChannelBlocklistRepo channelBlocklistRepo;
    private final ChannelUserPermissionRepo channelUserPermissionRepo;
    private final UsernameService usernameService;
    private final ErrorService errorService;
    private final UserSession userSession;
    private final UserRepo userRepo;
    private final ChatRoleRepo chatRoleRepo;
    private final EntityManager entityManager;
    private final MapstructMapper mapper;

    @Override
    public ResponseEntity<?> getChannel(Long id) {
        Optional<Channel> channelOptional = channelRepo.findActiveById(id);
        if (channelOptional.isEmpty()) {
            return response(errorService.message(ErrorEnum.CHANNEL_NOT_FOUND.getCode(), Lang.RU, id), HttpStatus.NOT_FOUND);
        }
        return response(new ApiResponse<>(mapper.toChannelDto(channelOptional.get())));
    }

    @Override
    public ResponseEntity<?> getMyChannels(Integer pageNumber) {
        Long userId = userSession.getUserId();
        if (isEmpty(userId)) {
            return response(errorService.message(ErrorEnum.USER_NOT_FOUND.getCode(), Lang.RU, userId), HttpStatus.NOT_FOUND);
        }
        return response(mapper.toChannelDto(channelRepo.getUserChannels(userId, 50, pageNumber * 50)));
    }

    @Override
    public ResponseEntity<?> getMembers(Long channelId, Integer pageNumber) {
        User user = userSession.getUser();
        if (isEmpty(user)) {
            return response(errorService.message(ErrorEnum.USER_NOT_FOUND.getCode(), Lang.RU), HttpStatus.NOT_FOUND);
        }
        Optional<Channel> optionalChannel = channelRepo.findActiveById(channelId);
        if (optionalChannel.isEmpty()) {
            return response(errorService.message(ErrorEnum.CHANNEL_NOT_FOUND.getCode(), Lang.RU, channelId), HttpStatus.NOT_FOUND);
        }
        if (channelUserRepo.isAdmin(user.getId(), channelId)) {
            return response(mapper.toChannelUserDto(channelUserRepo.findAllMembers(channelId, pageNumber * 50, 50)));
        }
        return response(errorService.message(ErrorEnum.NO_PERMISSION.getCode(), Lang.RU), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> createChannel(ChannelDto dto) {
        if (!Utils.isEmpty(dto.getDescription())) {
            if (dto.getDescription().length() > 255) {
                return response(errorService.message(ErrorEnum.LARGE_DESCRIPTION.getCode(), Lang.RU, 255L), HttpStatus.BAD_REQUEST);
            }
        }

        Username username = null;
        if (!Utils.isEmpty(dto.getUsername())) {
            username = usernameService.create(dto.getUsername());
            if (isEmpty(username)) {
                return response(errorService.message(ErrorEnum.USERNAME_ALREADY_EXIST.getCode(), Lang.RU, dto.getUsername()), HttpStatus.CONFLICT);
            }
        }

        Channel channel = new Channel();
        channel.setTitle(dto.getTitle());
        channel.setDescription(dto.getDescription());
        channel.setChatType(ChatType.CHANNEL);
        channel.setUsername(username);
        channel.setSecurityType(Utils.isEmpty(username) ? SecurityType.PRIVATE : SecurityType.PUBLIC);
        channel.setActive(true);
        channel = channelRepo.save(channel);
        saveUserToChannel(userSession.getUser(), channel, ChatRoleEnum.OWNER);

        return response(channel, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> updateChannel(Long id, ChannelDto dto) {
        Long activeUserId = userSession.getUserId();
        if (isEmpty(activeUserId)) {
            return response(errorService.message(ErrorEnum.USER_NOT_FOUND.getCode(), Lang.RU, activeUserId), HttpStatus.NOT_FOUND);
        }

        Optional<Channel> channelOptional = channelRepo.findActiveById(id);
        if (channelOptional.isEmpty()) {
            return response(errorService.message(ErrorEnum.CHANNEL_NOT_FOUND.getCode(), Lang.RU, id), HttpStatus.NOT_FOUND);
        }
        Channel channel = channelOptional.get();

        if (getPermissions(activeUserId, channel.getId()).contains(ChatPermissions.CHANGE_INFO.name())) {
            if (!Utils.isEmpty(dto.getDescription())) {
                if (dto.getDescription().length() > 255) {
                    return response(errorService.message(ErrorEnum.LARGE_DESCRIPTION.getCode(), Lang.RU, 255L), HttpStatus.BAD_REQUEST);
                }
            }
            Username newUsername = null;
            if (!Utils.isEmpty(dto.getUsername())) {
                newUsername = usernameService.create(dto.getUsername());
                usernameService.delete(channel.getUsername().getName());
            }
            channel.setTitle(dto.getTitle());
            channel.setDescription(Utils.isEmpty(dto.getDescription()) ? channel.getDescription() : dto.getDescription());

            if (!Utils.isEmpty(newUsername)) {
                channel.setUsername(newUsername);
                channel.setSecurityType(SecurityType.PUBLIC);
            }
            channel = channelRepo.save(channel);
            return response(mapper.toChannelDto(channel));
        }
        return response(errorService.message(ErrorEnum.NO_PERMISSION.getCode(), Lang.RU), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> deleteChannel(Long id) {
        Long activeUserId = userSession.getUserId();
        if (isEmpty(activeUserId)) {
            return response(errorService.message(ErrorEnum.USER_NOT_FOUND.getCode(), Lang.RU, activeUserId), HttpStatus.NOT_FOUND);
        }

        Optional<Channel> channelOptional = channelRepo.findActiveById(id);
        if (channelOptional.isEmpty()) {
            return response(errorService.message(ErrorEnum.CHANNEL_NOT_FOUND.getCode(), Lang.RU, id), HttpStatus.NOT_FOUND);
        }

        if (channelUserRepo.isOwner(activeUserId, id)) {
            Channel channel = channelOptional.get();
            channel.setActive(false);

            channelRepo.save(channel);
            if (isEmpty(channel.getUsername())) {
                usernameService.delete(channel.getUsername().getId());
            }
            return response(new ApiResponse<>());
        }
        return response(errorService.message(ErrorEnum.NO_PERMISSION.getCode(), Lang.RU), HttpStatus.BAD_REQUEST);
    }

    // Channel member
    @Override
    public ResponseEntity<?> addMember(ChannelMemberDto dto) {
        Optional<Channel> optionalChannel = channelRepo.findActiveById(dto.getChannelId());
        if (optionalChannel.isEmpty()) {
            return response(errorService.message(ErrorEnum.CHANNEL_NOT_FOUND.getCode(), Lang.RU, dto.getChannelId()), HttpStatus.NOT_FOUND);
        }
        Channel channel = optionalChannel.get();
        Long activeUserId = userSession.getUserId();

        if (isEmpty(activeUserId)) {
            return response(errorService.message(ErrorEnum.USER_NOT_FOUND.getCode(), Lang.RU, activeUserId), HttpStatus.NOT_FOUND);
        }

        if (!getPermissions(activeUserId, dto.getChannelId()).contains(ChatPermissions.ADD_MEMBER.name())) {
            return response(errorService.message(ErrorEnum.NO_PERMISSION.getCode(), Lang.RU), HttpStatus.BAD_REQUEST);
        }

        for (Long userId : dto.getUsersId()) {
            Optional<User> optionalUser = userRepo.findActiveById(userId);
            if (optionalUser.isEmpty()) {
                // todo write log
                break;
//                return response(errorService.message(10, Lang.RU, dto.getUserId()), HttpStatus.NOT_FOUND);
            }
            User user = optionalUser.get();
            if (hasPermissionToChannel(user.getId(), channel.getId())) {
                if (channelUserRepo.isAdmin(activeUserId, dto.getChannelId())) {
                    if (channelUserRepo.getMemberNumber(dto.getChannelId()) < 200) {
                        saveUserToChannel(user, channel);
//                        return response("User added to channel", HttpStatus.CREATED);
                    } else {
                        return response(errorService.message(ErrorEnum.LIMIT_MEMBER_REACHED.getCode(), Lang.RU, "Channel member reached limit"));
                    }
                }
                if (!activeUserId.equals(userId) && hasPermissionToChannel(user.getId(), channel.getId())) {
                    saveUserToChannel(user, channel);
//                    return response("User added to channel", HttpStatus.CREATED);
                }
            } else {
                // todo write log
                break;
//                return response(errorService.message(13, Lang.RU));
            }
        }
        return response("Users added to channel", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> addMemberByLink(String link) {
        User user = userSession.getUser();
        if (!isEmpty(user)) {
            Optional<Channel> optionalChannel = channelRepo.findByUsername_Name(link);
            if (optionalChannel.isPresent()) {
                Channel channel = optionalChannel.get();
                if (hasPermissionToChannel(user.getId(), channel.getId())) {
                    saveUserToChannel(user, channel);
                    return response("User added to channel", HttpStatus.CREATED);
                }
                return response(errorService.message(ErrorEnum.USER_BLOCKED_IN_CHANNEL.getCode(), Lang.RU), HttpStatus.BAD_REQUEST);
            } else {
                optionalChannel = channelRepo.findByLink(link);
                if (optionalChannel.isPresent()) {
                    Channel channel = optionalChannel.get();
                    if (hasPermissionToChannel(user.getId(), channel.getId())) {
                        saveUserToChannel(user, channel);
                        return response("User added to channel", HttpStatus.CREATED);
                    }
                    return response(errorService.message(ErrorEnum.USER_BLOCKED_IN_CHANNEL.getCode(), Lang.RU), HttpStatus.BAD_REQUEST);
                }
            }
            return response(errorService.message(ErrorEnum.CHAT_NOT_FOUND.getCode(), Lang.RU), HttpStatus.NOT_FOUND);
        }
        return response(errorService.message(ErrorEnum.USER_NOT_FOUND.getCode(), Lang.RU), HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<?> getMemberSort(Long id, String sortBy, String sortType, Integer pageNumber) {
        User user = userSession.getUser();
        if (isEmpty(user)) {
            return response(errorService.message(ErrorEnum.USER_NOT_FOUND.getCode(), Lang.RU), HttpStatus.NOT_FOUND);
        }
        Optional<Channel> optionalChannel = channelRepo.findActiveById(id);
        if (optionalChannel.isEmpty()) {
            return response(errorService.message(ErrorEnum.CHANNEL_NOT_FOUND.getCode(), Lang.RU, id), HttpStatus.NOT_FOUND);
        }
        Channel channel = optionalChannel.get();
        if (channelUserRepo.isAdmin(user.getId(), id)) {
            return response(mapper.toChannelUserDto(channelUserRepo.findAllMembers(id, pageNumber * 50, 50)));
        }
        StringBuilder query = new StringBuilder("select cu.*\n" +
                "from channel_user cu\n" +
                "         join chat_role cr on cr.id = cu.chat_role_id\n" +
                "         join users u on u.id = cu.user_id\n" +
                "where cu.channel_id = :id\n" +
                "  and cu.active = true\n" +
                "order by ");
        if (sortBy.equals("date")) {
            query.append("cu.created_at ");
        } else query.append("u.first_name ");

        if (sortType.equals("desc")) {
            query.append("desc ");
        }
        query.append("limit 50 offset ").append(pageNumber * 50);
        List<ChannelUser> members = entityManager.createNativeQuery(query.toString(), ChannelUser.class).setParameter("id", id).getResultList();

        return response(mapper.toChannelUserDto(members), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getBlockList(Long id, Integer pageNumber) {
        User user = userSession.getUser();
        if (isEmpty(user)) {
            return response(errorService.message(ErrorEnum.USER_NOT_FOUND.getCode(), Lang.RU), HttpStatus.NOT_FOUND);
        }
        Optional<Channel> optionalChannel = channelRepo.findActiveById(id);
        if (optionalChannel.isEmpty()) {
            return response(errorService.message(ErrorEnum.CHANNEL_NOT_FOUND.getCode(), Lang.RU, id), HttpStatus.NOT_FOUND);
        }
        if (channelUserRepo.isAdmin(user.getId(), id)) {
            return response(mapper.toChannelUserDto(channelUserRepo.findAllMembers(id, pageNumber * 50, 50)));
        }
        List<User> blocklist = userRepo.getChannelBlocklist(id, 50, pageNumber * 50);
        return response(mapper.toUserDto(blocklist));
    }

    @Override
    public List<String> getPermissions(Long userId, Long channelId) {
        return channelUserPermissionRepo.getPermissions(userId, channelId);
    }

    @Override
    public ResponseEntity<?> attachRole(AttachRoleDto dto) {
        Long activeUserId = userSession.getUserId();
        if (isEmpty(activeUserId)) {
            return response(errorService.message(ErrorEnum.USER_NOT_FOUND.getCode(), Lang.RU, activeUserId), HttpStatus.NOT_FOUND);
        }
        Optional<ChannelUser> optionalChannelUser = channelUserRepo.findActiveChannelUser(dto.getChannelUserId());
        if (optionalChannelUser.isEmpty()) {
            return response(errorService.message(ErrorEnum.CHANNEL_USER_NOT_FOUND.getCode(), Lang.RU, dto.getChannelUserId()), HttpStatus.NOT_FOUND);
        }
        ChannelUser channelUser = optionalChannelUser.get();
        if (!getPermissions(activeUserId, channelUser.getChannel().getId()).contains(ChatPermissions.ADD_NEW_ADMINS.name())) {
            return response(errorService.message(ErrorEnum.NO_PERMISSION.getCode(), Lang.RU), HttpStatus.BAD_REQUEST);
        }
        Optional<ChatRole> optionalChatRole = chatRoleRepo.findActiveById(dto.getChatRoleId());
        if (optionalChatRole.isEmpty()) {
            return response(errorService.message(ErrorEnum.CHAT_ROLE_NOT_FOUND.getCode(), Lang.RU, dto.getChatRoleId()), HttpStatus.NOT_FOUND);
        }
        ChatRole chatRole = optionalChatRole.get();
        if (chatRole.getRole().equals(ChatRoleEnum.ADMIN)) {
            channelUser.setChatRole(chatRole);
            channelUser = channelUserRepo.save(channelUser);
            attachPermissions(channelUser, ChatRoleEnum.ADMIN);
        } else if (chatRole.getRole().equals(ChatRoleEnum.MEMBER)) {
            channelUser.setChatRole(chatRole);
            channelUser = channelUserRepo.save(channelUser);
            detachPermissions(channelUser, ChatRoleEnum.MEMBER);
        }
        return response("Attached!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> giveAdmin(Long channelId, Long channelUserId) {
        Long activeUserId = userSession.getUserId();
        if (isEmpty(activeUserId)) {
            return response(errorService.message(ErrorEnum.USER_NOT_FOUND.getCode(), Lang.RU, activeUserId), HttpStatus.NOT_FOUND);
        }
        Optional<ChannelUser> optionalChannelUser = channelUserRepo.findActiveChannelUser(channelUserId);
        if (optionalChannelUser.isEmpty()) {
            return response(errorService.message(ErrorEnum.CHANNEL_USER_NOT_FOUND.getCode(), Lang.RU, channelUserId), HttpStatus.NOT_FOUND);
        }
        Optional<Channel> optionalChannel = channelRepo.findById(channelId);
        if (optionalChannel.isEmpty()) {
            return response(errorService.message(ErrorEnum.CHANNEL_NOT_FOUND.getCode(), Lang.RU, channelId), HttpStatus.NOT_FOUND);
        }
        if (!getPermissions(activeUserId, channelId).contains(ChatPermissions.GIVE_OWNER.name())) {
            return response(errorService.message(ErrorEnum.NO_PERMISSION.getCode(), Lang.RU), HttpStatus.BAD_REQUEST);
        }
        ChannelUser channelUser = optionalChannelUser.get();
        channelUser.setChatRole(chatRoleRepo.findByRole(ChatRoleEnum.OWNER).get());
        channelUserRepo.save(channelUser);
        attachPermissions(channelUser, ChatRoleEnum.OWNER);

        channelUser = channelUserRepo.findActiveChannelUser(activeUserId, channelId).get();
        channelUser.setChatRole(chatRoleRepo.findByRole(ChatRoleEnum.ADMIN).get());
        channelUserRepo.save(channelUser);
        detachPermissions(channelUser, Collections.singleton(ChatPermissions.GIVE_OWNER));

        return response("Admin has changed!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> blockMember(Long id, Long userId) {
        Optional<Channel> optionalChannel = channelRepo.findActiveById(id);
        if (optionalChannel.isEmpty()) {
            return response(errorService.message(ErrorEnum.CHANNEL_NOT_FOUND.getCode(), Lang.RU, id), HttpStatus.NOT_FOUND);
        }
        Long activeUserId = userSession.getUserId();
        if (isEmpty(activeUserId)) {
            return response(errorService.message(ErrorEnum.USER_NOT_FOUND.getCode(), Lang.RU, activeUserId), HttpStatus.NOT_FOUND);
        }
        if (!getPermissions(activeUserId, id).contains(ChatPermissions.BLOCK_MEMBER.name())) {
            return response(errorService.message(ErrorEnum.NO_PERMISSION.getCode(), Lang.RU), HttpStatus.BAD_REQUEST);
        }
        Optional<ChannelUser> optionalChannelUser = channelUserRepo.findActiveChannelUser(userId, id);
        if (optionalChannelUser.isEmpty()) {
            return response(errorService.message(ErrorEnum.CHANNEL_USER_NOT_FOUND.getCode(), Lang.RU), HttpStatus.NOT_FOUND);
        }
        ChannelBlockList channelBlockList = new ChannelBlockList();
        channelBlockList.setChannel(optionalChannel.get());
        channelBlockList.setUser(userRepo.getById(id));
        channelBlocklistRepo.save(channelBlockList);
        removeMember(id, userId);
        return response("Blocked!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> unblockMember(Long id, Long userId) {
        Long activeUserId = userSession.getUserId();
        if (isEmpty(activeUserId)) {
            return response(errorService.message(ErrorEnum.USER_NOT_FOUND.getCode(), Lang.RU, activeUserId), HttpStatus.NOT_FOUND);
        }
        if (!getPermissions(activeUserId, id).contains(ChatPermissions.UNBLOCK_MEMBER.name())) {
            return response(errorService.message(ErrorEnum.NO_PERMISSION.getCode(), Lang.RU), HttpStatus.BAD_REQUEST);
        }
        Optional<ChannelBlockList> optionalChannelBlockList = channelBlocklistRepo.findActiveByChannelIdAndUserId(id, userId);
        if (optionalChannelBlockList.isEmpty()) {
            return response(errorService.message(ErrorEnum.CHANNEL_USER_NOT_FOUND.getCode(), Lang.RU), HttpStatus.NOT_FOUND);
        }
        ChannelBlockList channelBlockList = optionalChannelBlockList.get();
        channelBlockList.setActive(true);
        channelBlocklistRepo.save(channelBlockList);
        return response("Unblocked!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> removeMember(Long id, Long userId) {
        Optional<Channel> optionalChannel = channelRepo.findActiveById(id);
        if (optionalChannel.isEmpty()) {
            return response(errorService.message(ErrorEnum.CHANNEL_NOT_FOUND.getCode(), Lang.RU, id), HttpStatus.NOT_FOUND);
        }
        Long activeUserId = userSession.getUserId();
        if (isEmpty(activeUserId)) {
            return response(errorService.message(ErrorEnum.USER_NOT_FOUND.getCode(), Lang.RU, activeUserId), HttpStatus.NOT_FOUND);
        }
        if (!getPermissions(activeUserId, id).contains(ChatPermissions.REMOVE_MEMBER.name())) {
            return response(errorService.message(ErrorEnum.NO_PERMISSION.getCode(), Lang.RU), HttpStatus.BAD_REQUEST);
        }
        Optional<ChannelUser> optionalChannelUser = channelUserRepo.findActiveChannelUser(userId, id);
        if (optionalChannelUser.isEmpty()) {
            return response(errorService.message(ErrorEnum.CHANNEL_USER_NOT_FOUND.getCode(), Lang.RU), HttpStatus.NOT_FOUND);
        }
        ChannelUser channelUser = optionalChannelUser.get();
        channelUser.setActive(false);
        channelUserRepo.save(channelUser);
        channelUserPermissionRepo.deleteByChannelIdAndUserId(id, userId);

        deleteMessages(id, userId);
        return response("Deleted!", HttpStatus.OK);
    }

    public void attachMessages(Long channelId, User user) {
        List<ChannelMessage> messages = channelMessageRepo.findAllActiveByChannelId(channelId);
        List<ChannelUserMessage> userMessages = new ArrayList<>();
        messages.forEach(message -> {
            ChannelUserMessage channelUserMessage = new ChannelUserMessage();
            channelUserMessage.setChannelMessage(message);
            channelUserMessage.setUser(user);
            channelUserMessage.setIsViewed(true);
            userMessages.add(channelUserMessage);
        });
        channelUserMessageRepo.saveAll(userMessages);
    }

    public void deleteMessages(Long channelId, Long userId) {
        channelUserMessageRepo.deleteByUserIdAndChannelId(channelId, userId);
    }

    public void saveUserToChannel(User user, Channel channel) {
        ChannelUser channelUser = new ChannelUser(channel);
        channelUser.setUser(user);
        channelUser.setChatRole(chatRoleRepo.findByRole(ChatRoleEnum.MEMBER).get());
        attachPermissions(channelUserRepo.save(channelUser), ChatRoleEnum.MEMBER);

        attachMessages(channel.getId(), user);
    }

    public void saveUserToChannel(User user, Channel channel, ChatRoleEnum role) {
        ChannelUser channelUser = new ChannelUser(channel);
        channelUser.setUser(user);
        channelUser.setChatRole(chatRoleRepo.findByRole(role).get());
        attachPermissions(channelUserRepo.save(channelUser), role);
    }

    public void attachPermissions(ChannelUser channelUser, ChatRoleEnum role) {
        Set<ChannelUserPermission> permissions = getPermissions(channelUser);
        Set<ChatPermissions> permission = ChatPermissions.getPermission(role);
        attachHelper(channelUser, permissions, permission);
    }

    public void attachPermissions(ChannelUser channelUser, Set<ChatPermissions> newPermissions) {
        Set<ChannelUserPermission> permissions = getPermissions(channelUser);
        attachHelper(channelUser, permissions, newPermissions);
    }

    private void attachHelper(ChannelUser channelUser, Set<ChannelUserPermission> permissions, Set<ChatPermissions> permission) {
        for (ChatPermissions chatPermission : permission) {
            boolean hasPermission = false;
            for (ChannelUserPermission userPermission : permissions) {
                if (chatPermission.equals(userPermission.getPermission())) {
                    hasPermission = true;
                    break;
                }
            }
            if (!hasPermission) {
                permissions.add(new ChannelUserPermission(channelUser, chatPermission));
            }
        }
        channelUserPermissionRepo.saveAll(permissions);
    }

    private void detachPermissions(ChannelUser channelUser, ChatRoleEnum role) {
        Set<ChannelUserPermission> permissions = getPermissions(channelUser);
        Set<ChannelUserPermission> deletePermissions = new HashSet<>();
        Set<ChatPermissions> permission = ChatPermissions.getPermission(role);
        for (ChannelUserPermission userPermission : permissions) {
            boolean hasPermission = false;
            for (ChatPermissions chatPermission : permission) {
                if (chatPermission.equals(userPermission.getPermission())) {
                    hasPermission = true;
                    break;
                }
            }
            if (!hasPermission) {
                deletePermissions.add(channelUserPermissionRepo.getByChannelUserIdAndPermission(channelUser.getId(), userPermission.getPermission()));
            }
        }
        channelUserPermissionRepo.deleteAll(deletePermissions);
    }

    private void detachPermissions(ChannelUser channelUser, Set<ChatPermissions> removedPermissions) {
        Set<ChannelUserPermission> permissions = getPermissions(channelUser);
        Set<ChannelUserPermission> deletePermissions = new HashSet<>();
        for (ChannelUserPermission userPermission : permissions) {
            boolean hasPermission = false;
            for (ChatPermissions chatPermission : removedPermissions) {
                if (chatPermission.equals(userPermission.getPermission())) {
                    hasPermission = true;
                    break;
                }
            }
            if (hasPermission) {
                deletePermissions.add(channelUserPermissionRepo.getByChannelUserIdAndPermission(channelUser.getId(), userPermission.getPermission()));
            }
        }
        channelUserPermissionRepo.deleteAll(deletePermissions);
    }

    public boolean hasPermissionToChannel(Long userId, Long channelId) {
        return !channelBlocklistRepo.isUserBlocked(userId, channelId) && !channelUserRepo.existsChannelUser(userId, channelId);
    }

    public Set<ChannelUserPermission> getPermissions(ChannelUser channelUser) {
        return new HashSet<>(channelUserPermissionRepo.getPermissions(channelUser.getId()));
    }
}

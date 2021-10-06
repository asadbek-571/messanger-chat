package app.chat.service;

import app.chat.model.dto.ChannelDto;
import app.chat.model.dto.ChannelMemberDto;
import app.chat.model.req.AttachRoleDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ChannelService {

    ResponseEntity<?> getChannel(Long id);

    ResponseEntity<?> getMyChannels(Integer pageNumber);

    ResponseEntity<?> getMembers(Long channelId, Integer pageNumber);

    ResponseEntity<?> createChannel(ChannelDto dto);

    ResponseEntity<?> updateChannel(Long id, ChannelDto dto);

    ResponseEntity<?> deleteChannel(Long id);

    // Channel member
    ResponseEntity<?> addMember(ChannelMemberDto dto);

    ResponseEntity<?> addMemberByLink(String link);

    ResponseEntity<?> getBlockList(Long id, Integer pageNumber);

    ResponseEntity<?> getMemberSort(Long id, String sortBy, String sortType, Integer pageNumber);

    List<String> getPermissions(Long userId, Long channelId);

    ResponseEntity<?> removeMember(Long id, Long userId);

    ResponseEntity<?> attachRole(AttachRoleDto dto);

    ResponseEntity<?> giveAdmin(Long channelId, Long channelUserId);

    ResponseEntity<?> blockMember(Long id, Long userId);

    ResponseEntity<?> unblockMember(Long id, Long userId);
}

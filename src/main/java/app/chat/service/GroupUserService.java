package app.chat.service;

import app.chat.entity.group.GroupUser;
import app.chat.model.req.group.GroupMemberReqDto;
import org.springframework.http.ResponseEntity;

public interface GroupUserService {
    ResponseEntity<?> addMembers(GroupMemberReqDto dto);

    ResponseEntity<?> getSortedMembers(Long groupId);

    GroupUser findMyGroupMate(Long userId);
}

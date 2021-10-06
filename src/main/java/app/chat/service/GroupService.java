package app.chat.service;

import app.chat.model.req.group.GroupReqDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GroupService {
    ResponseEntity<?> getGroup(Long id);

    ResponseEntity<?> getAll();

    ResponseEntity<?> createGroup(GroupReqDto dto);

    ResponseEntity<?> updateGroup(Long id, GroupReqDto dto);

    ResponseEntity<?> deleteGroup(Long id);

    List<String> getPermissions(Long userId, Long groupId);
}

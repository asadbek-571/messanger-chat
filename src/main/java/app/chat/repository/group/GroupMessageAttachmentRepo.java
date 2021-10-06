package app.chat.repository.group;

import app.chat.entity.group.GroupMessageAttachment;
import app.chat.entity.personal.PersonalMessageAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMessageAttachmentRepo extends JpaRepository<GroupMessageAttachment, Long> {

    List<GroupMessageAttachment> findAllByGroupMessage_IdAndActiveTrue(Long id);
}

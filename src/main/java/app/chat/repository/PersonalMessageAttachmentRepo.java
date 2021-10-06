package app.chat.repository;

import app.chat.entity.personal.PersonalMessageAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonalMessageAttachmentRepo extends JpaRepository<PersonalMessageAttachment,Long> {

    List<PersonalMessageAttachment> findAllByPersonalMessage_IdAndActiveTrue(Long personalMessage_id);
}

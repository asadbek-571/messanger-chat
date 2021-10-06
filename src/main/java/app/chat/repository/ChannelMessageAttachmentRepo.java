package app.chat.repository;

import app.chat.entity.channel.ChannelMessageAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelMessageAttachmentRepo extends JpaRepository<ChannelMessageAttachment , Long> {

    List<ChannelMessageAttachment> findAllByChannelMessage_IdAndActiveTrue(Long id);
}

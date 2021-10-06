package app.chat.service;

import app.chat.entity.Attachment;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {

    Attachment upload(MultipartFile file);

    void intiFileSystem();
}

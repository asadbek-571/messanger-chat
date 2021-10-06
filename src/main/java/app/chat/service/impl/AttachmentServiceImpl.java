package app.chat.service.impl;

import app.chat.entity.Attachment;
import app.chat.repository.AttachmentRepo;
import app.chat.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepo attachmentRepo;


    @Value("${upload.path.video}")
    private String videoPath;
    @Value("${upload.path.music}")
    private String musicPath;
    @Value("${upload.path.document}")
    private String documentPath;
    @Value("${upload.path.image}")
    private String imagePath;
    @Value("${upload.path}")
    private String rootPath;

    @Override
    public Attachment upload(MultipartFile file) {

        String uniqueName = UUID.randomUUID() + file.getOriginalFilename();
        uniqueName = uniqueName.replaceAll(" ", "");
        Path root;
        if (Objects.requireNonNull(file.getContentType()).contains("document")) {
            root = Paths.get(documentPath);
        } else if (Objects.requireNonNull(file.getContentType()).contains("image")) {
            root = Paths.get(imagePath);
        } else {
            root = Paths.get(rootPath);
        }

        try {
            Files.copy(file.getInputStream(), root.resolve(uniqueName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Attachment attachment = new Attachment();
        attachment.setName(file.getName());
        attachment.setContentType(file.getContentType());
        attachment.setOriginalName(file.getName());
        attachment.setName(uniqueName);
        attachment.setSize(file.getSize());
        attachment = attachmentRepo.save(attachment);
        return attachment;
    }


    @Override
    public void intiFileSystem() {

        File file = new File(rootPath.substring(0, rootPath.lastIndexOf("/")));
        boolean rootFolderExist = false;
        for (File listFile : Objects.requireNonNull(file.listFiles())) {
            if (listFile.isDirectory()) {
                if (listFile.getName().equals(rootPath.substring(rootPath.lastIndexOf("/") + 1))) {
                    rootFolderExist = true;
                }
            }
        }
        if (!rootFolderExist) {
            new File(rootPath).mkdir();
        }
        file = new File(rootPath);
        HashMap<String, Boolean> hashMap = checkFolders(file);

        if (!hashMap.get(imagePath)) {
            createFolder(imagePath);
        }
        if (!hashMap.get(documentPath)) {
            createFolder(documentPath);
        }
        if (!hashMap.get(videoPath)) {
            createFolder(videoPath);
        }
        if (!hashMap.get(musicPath)) {
            createFolder(musicPath);
        }

    }

    private void createFolder(String path) {
        boolean file1 = new File(path).mkdir();
        if (!file1) {
            try {
                throw new Exception("folder not created : " + path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private HashMap<String, Boolean> checkFolders(File file) {
        HashMap<String, Boolean> hashMap = new HashMap<>();
        hashMap.put(imagePath, false);
        hashMap.put(videoPath, false);
        hashMap.put(musicPath, false);
        hashMap.put(documentPath, false);
        for (File listFile : Objects.requireNonNull(file.listFiles())) {
            if (listFile.isDirectory()) {
                if (listFile.getName().equals(imagePath.substring(imagePath.lastIndexOf("/") + 1))) {
                    hashMap.replace(imagePath, true);
                } else if (listFile.getName().equals(documentPath.substring(documentPath.lastIndexOf("/") + 1))) {
                    hashMap.replace(documentPath, true);
                } else if (listFile.getName().equals(musicPath.substring(musicPath.lastIndexOf("/") + 1))) {
                    hashMap.replace(musicPath, true);
                } else if (listFile.getName().equals(videoPath.substring(videoPath.lastIndexOf("/") + 1))) {
                    hashMap.replace(videoPath, true);
                }
            }
        }
        return hashMap;
    }
}

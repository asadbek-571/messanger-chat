package app.chat.repository.group;

import app.chat.entity.group.GroupPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupPhotoRepo extends JpaRepository<GroupPhoto, Long> {

}

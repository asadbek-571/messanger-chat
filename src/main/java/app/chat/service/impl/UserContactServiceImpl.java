package app.chat.service.impl;

import app.chat.entity.user.User;
import app.chat.entity.user.UserContact;
import app.chat.helpers.UserSession;
import app.chat.repository.user.UserContactRepo;
import app.chat.repository.user.UserRepo;
import app.chat.service.UserContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserContactServiceImpl implements UserContactService {

    private final UserRepo userRepo;
    private final UserContactRepo userContactRepo;
    private final UserSession session;

    @Override
    public UserContact findInMyContacts(Long friendId) {
        User user = session.getUser();
        List<UserContact> contacts = userContactRepo.findAllByUser1_IdAndActiveTrue(user.getId());
        for (UserContact contact : contacts) {
            if (contact.getUser2().getId().equals(friendId) && contact.getUser2().isActive()) {
                return contact;
            }
        }
        return null;
    }
}

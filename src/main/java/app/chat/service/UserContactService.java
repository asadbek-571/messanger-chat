package app.chat.service;

import app.chat.entity.user.UserContact;

public interface UserContactService {
    UserContact findInMyContacts(Long friendId);
}

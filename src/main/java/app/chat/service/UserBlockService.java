package app.chat.service;

public interface UserBlockService {

    boolean isMyBlockUser(Long userId);

    boolean isThisUserBlockedYou(Long userId);
}

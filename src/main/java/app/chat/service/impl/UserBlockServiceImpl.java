package app.chat.service.impl;

import app.chat.entity.user.UserBlock;
import app.chat.helpers.UserSession;
import app.chat.repository.user.UserBlockRepo;
import app.chat.service.UserBlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserBlockServiceImpl implements UserBlockService {

    private final UserSession session;
    private final UserBlockRepo userBlockRepo;

    @Override
    public boolean isMyBlockUser(Long userId) {

        Long myId = session.getUserId();

        List<UserBlock> myBlockList = userBlockRepo.findAllByUser_Id(myId);

        for (UserBlock userBlock : myBlockList) {
            if (userBlock.getBlockedUser().getId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isThisUserBlockedYou(Long userId) {
        Long myId = session.getUserId();

        List<UserBlock> myBlockList = userBlockRepo.findAllByUser_Id(userId);

        for (UserBlock userBlock : myBlockList) {
            if (userBlock.getBlockedUser().getId().equals(myId)) {
                return true;
            }
        }
        return false;
    }
}

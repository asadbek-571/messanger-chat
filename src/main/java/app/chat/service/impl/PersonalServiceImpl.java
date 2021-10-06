package app.chat.service.impl;

import app.chat.entity.channel.ChannelUser;
import app.chat.entity.group.GroupUser;
import app.chat.entity.personal.Personal;
import app.chat.entity.user.User;
import app.chat.entity.user.UserContact;
import app.chat.enums.ChatType;
import app.chat.enums.Lang;
import app.chat.helpers.UserSession;
import app.chat.helpers.Utils;
import app.chat.mapper.MapstructMapper;
import app.chat.model.ApiResponse;
import app.chat.model.dto.PersonalDto;
import app.chat.repository.personal.PersonalRepo;
import app.chat.repository.user.UserRepo;
import app.chat.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static app.chat.model.ApiResponse.response;

@Service
@RequiredArgsConstructor
public class PersonalServiceImpl implements PersonalService {

    private final PersonalRepo personalRepo;
    private final UserRepo userRepo;
    private final ErrorService error;
    private final UserSession session;
    private final MapstructMapper mapper;
    private final UserContactService userContactService;
    private final GroupUserService groupUserService;
    private final ChannelUserService channelUserService;


    @Override
    public ResponseEntity<?> createPersonal(Long user2Id) {

        Optional<User> optionalUser2 = userRepo.findActiveById(user2Id);

        if (optionalUser2.isEmpty()) {
            return response(error.message(10, Lang.RU, user2Id), HttpStatus.NOT_FOUND);
        }

        User user1 = session.getUser();
        User user2 = optionalUser2.get();

        if (user1.getId().equals(user2.getId())) {
            return response(error.message(71, Lang.RU, user2.getId()), HttpStatus.BAD_REQUEST);
        }

        Optional<Personal> personalOptional = personalRepo.findPersonal(user1.getId(), user2.getId());
        if (personalOptional.isEmpty()) {

            UserContact inMyContacts = userContactService.findInMyContacts(user2.getId());
            if (Utils.isEmpty(inMyContacts)) {
                GroupUser inMyFollowGroup = groupUserService.findMyGroupMate(user2.getId());
                if (Utils.isEmpty(inMyFollowGroup)) {
                    ChannelUser inMyAuthorityChannel = channelUserService.findMyAuthorityChannelFollower(user2.getId());
                    if (Utils.isEmpty(inMyAuthorityChannel)) {
                        return response(error.message(71, Lang.RU, user2.getId()), HttpStatus.BAD_REQUEST);
                    } else {
                        //todo if my channel follower
                        Personal personal = personal(user1, inMyAuthorityChannel.getUser());
                        PersonalDto personalDto = mapper.toPersonalDto(personal);
                        return response(personalDto, HttpStatus.CREATED);
                    }
                } else {
                    //todo if my groupMate
                    Personal personal = personal(user1, inMyFollowGroup.getUser());
                    PersonalDto personalDto = mapper.toPersonalDto(personal);
                    return response(personalDto, HttpStatus.CREATED);
                }
            } else {
                //todo if my contact
                Personal personal = personal(user1, inMyContacts.getUser2());
                PersonalDto personalDto = mapper.toPersonalDto(personal);
                return response(personalDto, HttpStatus.CREATED);
            }
        } else {
            return response(mapper.toPersonalDto(personalOptional.get()), HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> createPersonal(String username) {

        Optional<User> userOptional = userRepo.findByUserName_Name(username);
        if (userOptional.isEmpty()) {
            return response(error.message(50, Lang.RU, username));
        }
        User user1 = session.getUser();
        User user2 = userOptional.get();

        if (!user2.isActive()) {
            return response(error.message(50, Lang.RU, username));
        }
        Personal personal = personal(user1, user2);
        return response(personal, HttpStatus.CREATED);
    }


    @Override
    public Personal personal(User user1, User user2) {
        Personal personal = new Personal();
        personal.setChatType(ChatType.PERSONAL);
        personal.setUser1(user1);
        personal.setUser2(user2);
        personal = personalRepo.save(personal);
        return personal;
    }

    @Override
    public List<Personal> getMyPersonals() {
        User activeUser = session.getUser();
        return personalRepo.findAllByUser1_IdAndActiveTrueOrUser2_IdAndActiveTrue(activeUser.getId(), activeUser.getId());
    }

    @Override
    public ResponseEntity<?> updatePersonal(Long id, PersonalDto dto) {
        Optional<Personal> optionalPersonal = personalRepo.getAllByActiveTrueAndId(id);
        if (optionalPersonal.isEmpty()) {
            return response(error.message(70, Lang.RU, id), HttpStatus.NOT_FOUND);
        }

        Optional<User> optionalUser1 = userRepo.findActiveById(dto.getUser1Id());
        Optional<User> optionalUser2 = userRepo.findActiveById(dto.getUser2Id());

        if (optionalUser1.isEmpty()) {
            return response(error.message(10, Lang.RU, dto.getUser1Id()), HttpStatus.NOT_FOUND);
        }
        if (optionalUser2.isEmpty()) {
            return response(error.message(10, Lang.RU, dto.getUser2Id()), HttpStatus.NOT_FOUND);
        }

        User user1 = optionalUser1.get();
        User user2 = optionalUser2.get();

        Personal personal = optionalPersonal.get();
        personal.setChatType(ChatType.PERSONAL);
        personal.setUser1(user1);
        personal.setUser2(user2);
        personal = personalRepo.save(personal);
        return response(personal);
    }

    @Override
    public ResponseEntity<?> getPage(Integer size, Integer page) {
        return response(personalRepo.getAllByActiveTrue(size, page));
    }

    @Override
    public ResponseEntity<?> getPersonal(Long id) {
        Optional<Personal> optionalPersonal = personalRepo.getAllByActiveTrueAndId(id);
        if (optionalPersonal.isEmpty()) {
            return response(error.message(70, Lang.RU, id), HttpStatus.NOT_FOUND);
        }
        return response(optionalPersonal.get());
    }

    @Override
    public ResponseEntity<?> deletePersonal(Long id) {
        Optional<Personal> optionalPersonal = personalRepo.getAllByActiveTrueAndId(id);
        if (optionalPersonal.isEmpty()) {
            return response(error.message(70, Lang.RU, id), HttpStatus.NOT_FOUND);
        }
        Personal personal = optionalPersonal.get();
        personal.setActive(false);
        Personal save = personalRepo.save(personal);
        PersonalDto personalDto = new PersonalDto();
        personalDto.setUser1Id(save.getUser1().getId());
        personalDto.setUser2Id(save.getUser2().getId());
        return response(new ApiResponse<>());
    }

}

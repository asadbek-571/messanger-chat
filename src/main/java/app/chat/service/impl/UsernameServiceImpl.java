package app.chat.service.impl;

import app.chat.entity.channel.Channel;
import app.chat.entity.group.Group;
import app.chat.entity.user.User;
import app.chat.entity.user.Username;
import app.chat.enums.ErrorEnum;
import app.chat.enums.Lang;
import app.chat.model.ApiResponse;
import app.chat.repository.user.UserRepo;
import app.chat.repository.group.GroupRepo;
import app.chat.repository.UsernameRepo;
import app.chat.repository.channel.ChannelRepo;
import app.chat.service.ErrorService;
import app.chat.service.UsernameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static app.chat.model.ApiResponse.response;

@Service
@RequiredArgsConstructor
public class UsernameServiceImpl implements UsernameService {
    private final UsernameRepo usernameRepo;
    private final UserRepo userRepo;
    private final ChannelRepo channelRepo;
    private final GroupRepo groupRepo;
    private final ErrorService error;

    @Override
    public ResponseEntity<?> getUsername(Long id) {
        Optional<Username> optionalUsername = usernameRepo.findActiveById(id);
        if (optionalUsername.isEmpty()) {
            response(error.message(ErrorEnum.USERNAME_NOT_FOUND.getCode(), Lang.RU, id), HttpStatus.NOT_FOUND);
        }
        return response(optionalUsername.get());
    }

    @Override
    public Username create(String name) {
        if (usernameRepo.existsByName(name)) {
            return null;
        }
        Username username = new Username(name);
        return usernameRepo.save(username);
    }

    @Override
    public ResponseEntity<?> updateUsername(Long id, String name) {
        Optional<Username> optionalUsername = usernameRepo.findActiveById(id);
        if (optionalUsername.isEmpty()) {
            return response(error.message(ErrorEnum.USERNAME_NOT_FOUND.getCode(), Lang.RU, id), HttpStatus.NOT_FOUND);
        }
        if (usernameRepo.existsByName(name, id)) {
            return response(error.message(ErrorEnum.USERNAME_ALREADY_EXIST.getCode(), Lang.RU, name), HttpStatus.CONFLICT);
        }

        Username username = optionalUsername.get();
        username.setName(name);
        username = usernameRepo.save(username);
        return response(username);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        Optional<Username> optionalUsername = usernameRepo.findActiveById(id);
        if (optionalUsername.isEmpty()) {
            return response(error.message(ErrorEnum.USERNAME_NOT_FOUND.getCode(), Lang.RU, id), HttpStatus.NOT_FOUND);
        }
        Username username = optionalUsername.get();

        Optional<Channel> optionalChannel = channelRepo.findByUsername_Name(username.getName());
        if (optionalChannel.isPresent()) {
            Channel channel = optionalChannel.get();
            channel.setUsername(null);
            channelRepo.save(channel);
        }
        Optional<User> optionalUser = userRepo.findByUserName_Name(username.getName());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUserName(null);
            userRepo.save(user);
        }
        Optional<Group> optionalGroup = groupRepo.findByUsername_Name(username.getName());
        if (optionalGroup.isPresent()) {
            Group group = optionalGroup.get();
            group.setUsername(null);
            groupRepo.save(group);
        }
        username.setName(username.getName() + "/!deleted!/");
        username.setActive(false);
        usernameRepo.save(username);
        return response(new ApiResponse<>());
    }

    @Override
    public ResponseEntity<?> delete(String name) {
        Optional<Username> optionalUsername = usernameRepo.findActiveByName(name);
        if (optionalUsername.isEmpty()) {
            return response(error.message(ErrorEnum.USERNAME_NOT_FOUND.getCode(), Lang.RU, name), HttpStatus.NOT_FOUND);
        }
        Username username = optionalUsername.get();

        Optional<Channel> optionalChannel = channelRepo.findByUsername_Name(username.getName());
        if (optionalChannel.isPresent()) {
            Channel channel = optionalChannel.get();
            channel.setUsername(null);
            channelRepo.save(channel);
        }
        Optional<User> optionalUser = userRepo.findByUserName_Name(username.getName());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUserName(null);
            userRepo.save(user);
        }
        Optional<Group> optionalGroup = groupRepo.findByUsername_Name(username.getName());
        if (optionalGroup.isPresent()) {
            Group group = optionalGroup.get();
            group.setUsername(null);
            groupRepo.save(group);
        }
        username.setName(username.getName() + "/!deleted!/");
        username.setActive(false);
        usernameRepo.save(username);
        return response(new ApiResponse<>());
    }

    @Override
    public boolean checkExist(String username) {
        return usernameRepo.existsByName(username);
    }
}

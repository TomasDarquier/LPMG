package com.example.user_service.services;

import com.example.user_service.dtos.UserDto;
import com.example.user_service.entities.User;
import com.example.user_service.exceptions.UserAlreadyExistsException;
import com.example.user_service.exceptions.UserNotRegisteredException;
import com.example.user_service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //TODO validate user
    @Override
    public User register(UserDto user) {
        if(existsByEmail(user.email())) {
            throw new UserAlreadyExistsException(
                    "There is already a registered user with that email"
            );
        }
        User userToSave = createUserFromDto(user);

        Optional<User> registeredUser = Optional.of(userRepository.save(userToSave));
        return registeredUser.orElseThrow(() -> new UserNotRegisteredException(
                "There was an error persisting the new user in the database"
        ));
    }

    private User createUserFromDto(UserDto user) {
        User newUser = new User();
        newUser.setEmail(user.email());
        newUser.setName(user.name());
        newUser.setOauthId(user.oauthId());
        newUser.setOauthProvider(user.oauthProvider());
        newUser.setCreatedAt(new Date());
        return newUser;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    @Override
    public Optional<String> getNameById(Long id) {
        return userRepository.findNameById(id);
    }
}
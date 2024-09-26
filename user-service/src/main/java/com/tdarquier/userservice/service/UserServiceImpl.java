package com.tdarquier.userservice.service;

import com.tdarquier.userservice.model.AuthProvider;
import com.tdarquier.userservice.model.User;
import com.tdarquier.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    //TODO manejar la validacion del usuario
    @Override
    public Optional<User> save(User user) {

        if(user.getPassword() != null) {
            user.setProvider(AuthProvider.local);
        }else {
            user.setProvider(AuthProvider.google);
        }

        User userUpdated = userRepository.save(user);
        return Optional.of(userUpdated);
    }
}
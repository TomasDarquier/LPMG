package com.example.user_service.services;

import com.example.user_service.entities.Activity;
import com.example.user_service.entities.User;
import com.example.user_service.entities.UserActivity;
import com.example.user_service.repositories.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.example.user_service.entities.Activity.*;

@RequiredArgsConstructor
@Service
public class UserActivityServiceImpl implements UserActivityService {

    private final UserActivityRepository userActivityRepository;

    @Override
    public void registerSignUpActivity(User user) {
        userActivityRepository.save(createUserActivity(user, REGISTER));
    }

    @Override
    public void registerLoginActivity(User user) {
        userActivityRepository.save(createUserActivity(user, LOGIN));
    }

    @Override
    public void registerCodeGenerationActivity(User user) {
        userActivityRepository.save(createUserActivity(user, CODE_GENERATION));
    }

    @Override
    public void registerDownloadActivity(User user) {
        userActivityRepository.save(createUserActivity(user, DOWNLOAD));
    }

    private UserActivity createUserActivity(User user, Activity activity) {
        return UserActivity.builder()
                .activityType(activity)
                .createdAt(new Date())
                .user(user)
                // at the moment, the IP is not being registered.
                // is mandatory the development of a privacy politic to save the IP
                .ipAddress("1111.1111.1111.1111")
                .build();
    }
}

package com.example.user_service.services;

import com.example.user_service.entities.Activity;
import com.example.user_service.entities.User;
import com.example.user_service.entities.UserActivity;
import com.example.user_service.repositories.UserActivityRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserActivityServiceImpl implements UserActivityService {

    final UserActivityRepository userActivityRepository;

    public UserActivityServiceImpl(UserActivityRepository userActivityRepository) {
        this.userActivityRepository = userActivityRepository;
    }

    @Override
    public void registerSignUpActivity(User user) {
        UserActivity userActivity = UserActivity.builder()
                .activityType(Activity.REGISTER)
                .createdAt(new Date())
                .user(user)
                // por el momento no se registra el ip
                .ipAddress("1111.1111.1111.1111")
                .build();

        userActivityRepository.save(userActivity);
    }

    @Override
    public void registerLoginActivity(User user) {
        UserActivity userActivity = UserActivity.builder()
                .activityType(Activity.LOGIN)
                .createdAt(new Date())
                .user(user)
                // por el momento no se registra el ip
                .ipAddress("1111.1111.1111.1111")
                .build();

        userActivityRepository.save(userActivity);
    }

    @Override
    public void registerCodeGenerationActivity(User user) {
        UserActivity userActivity = UserActivity.builder()
                .activityType(Activity.CODE_GENERATION)
                .createdAt((new Date()))
                .user(user)
                .ipAddress("1111.1111.1111.1111")
                .build();

        userActivityRepository.save(userActivity);
    }

    @Override
    public void registerDownloadActivity(User user) {

    }
}

package com.example.user_service.services;

import com.example.user_service.entities.User;

public interface UserActivityService {
    void registerSignUpActivity(User user);
    void registerLoginActivity(User user);
    void registerCodeGenerationActivity(User user);
    void registerDownloadActivity(User user);
}

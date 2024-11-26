package com.example.user_service.services;

import com.example.user_service.entities.Activity;
import com.example.user_service.entities.User;
import com.example.user_service.entities.UserActivity;
import com.example.user_service.repositories.UserActivityRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {UserActivityServiceTests.class})
public class UserActivityServiceTests {

    @Mock
    private UserActivityRepository userActivityRepository;

    @InjectMocks
    private UserActivityServiceImpl userActivityService;

    private User createTestUser() {
        return new User(
                1L,
                null,
                "12345oauth",
                "testemail@example.com",
                "John Doe",
                new Date(),
                null,
                null
        );
    }

    @Test
    public void test_registerSignUpActivity_shouldSaveRegisterActivity() {
        User user = createTestUser();
        Date now = new Date();

        userActivityService.registerSignUpActivity(user);

        ArgumentCaptor<UserActivity> captor = ArgumentCaptor.forClass(UserActivity.class);
        Mockito.verify(userActivityRepository).save(captor.capture());
        UserActivity savedActivity = captor.getValue();

        assertEquals(Activity.REGISTER, savedActivity.getActivityType());
        assertEquals(user, savedActivity.getUser());
        assertEquals("1111.1111.1111.1111", savedActivity.getIpAddress());
        assertTrue(Math.abs(savedActivity.getCreatedAt().getTime() - now.getTime()) < 1000);
    }

    @Test
    public void test_registerLoginActivity_shouldSaveLoginActivity() {
        User user = createTestUser();
        Date now = new Date();

        userActivityService.registerLoginActivity(user);

        // Capturar el UserActivity guardado
        ArgumentCaptor<UserActivity> captor = ArgumentCaptor.forClass(UserActivity.class);
        Mockito.verify(userActivityRepository).save(captor.capture());
        UserActivity savedActivity = captor.getValue();

        assertEquals(Activity.LOGIN, savedActivity.getActivityType());
        assertEquals(user, savedActivity.getUser());
        assertEquals("1111.1111.1111.1111", savedActivity.getIpAddress());
        assertTrue(Math.abs(savedActivity.getCreatedAt().getTime() - now.getTime()) < 1000);
    }

    @Test
    public void test_registerCodeGenerationActivity_shouldSaveCodeGenerationActivity() {
        User user = createTestUser();
        Date now = new Date();

        userActivityService.registerCodeGenerationActivity(user);

        ArgumentCaptor<UserActivity> captor = ArgumentCaptor.forClass(UserActivity.class);
        Mockito.verify(userActivityRepository).save(captor.capture());
        UserActivity savedActivity = captor.getValue();

        assertEquals(Activity.CODE_GENERATION, savedActivity.getActivityType());
        assertEquals(user, savedActivity.getUser());
        assertEquals("1111.1111.1111.1111", savedActivity.getIpAddress());
        assertTrue(Math.abs(savedActivity.getCreatedAt().getTime() - now.getTime()) < 1000);
    }

    @Test
    public void test_registerDownloadActivity_shouldSaveDownloadActivity() {
        User user = createTestUser();
        Date now = new Date();

        userActivityService.registerDownloadActivity(user);

        ArgumentCaptor<UserActivity> captor = ArgumentCaptor.forClass(UserActivity.class);
        Mockito.verify(userActivityRepository).save(captor.capture());
        UserActivity savedActivity = captor.getValue();

        assertEquals(Activity.DOWNLOAD, savedActivity.getActivityType());
        assertEquals(user, savedActivity.getUser());
        assertEquals("1111.1111.1111.1111", savedActivity.getIpAddress());
        assertTrue(Math.abs(savedActivity.getCreatedAt().getTime() - now.getTime()) < 1000);
    }
}


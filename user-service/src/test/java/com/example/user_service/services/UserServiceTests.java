package com.example.user_service.services;

import com.example.user_service.dtos.UserDto;
import com.example.user_service.entities.OauthProvider;
import com.example.user_service.entities.User;
import com.example.user_service.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Optional;

@SpringBootTest(classes = {UserServiceTests.class})
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto createTestUserDto() {
        return new UserDto(OauthProvider.GOOGLE, "123123qwerqwerqwer", "testemail@gmail.com", "Jhon");
    }

    private User createTestUser() {
        return new User(
                1L,
                OauthProvider.GOOGLE,
                "123123qwerqwerqwer",
                "testemail@gmail.com",
                "Jhon",
                new Date(),
                null,
                null
        );
    }

    @Test
    public void test_register_shouldSaveAndReturnUser() {
        UserDto dto = createTestUserDto();
        Date now = new Date();

        // Simula el repositorio devolviendo el objeto guardado
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Ejecuta el servicio
        User result = userService.register(dto);

        // Captura el usuario guardado
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(captor.capture());
        User savedUser = captor.getValue();

        // Valida los campos
        Assertions.assertEquals(dto.oauthProvider(), savedUser.getOauthProvider());
        Assertions.assertEquals(dto.oauthId(), savedUser.getOauthId());
        Assertions.assertEquals(dto.email(), savedUser.getEmail());
        Assertions.assertEquals(dto.name(), savedUser.getName());
        Assertions.assertTrue(Math.abs(savedUser.getCreatedAt().getTime() - now.getTime()) < 1000);

        // Valida el resultado final
        Assertions.assertEquals(result, savedUser);
    }

    @Test
    public void test_findById_shouldReturnUser() {
        User user = createTestUser();

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(1L);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(user, result.get());
    }

    @Test
    public void test_findById_shouldReturnEmptyWhenNotFound() {
        Mockito.when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(99L);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void test_existsByEmail_shouldReturnTrue() {
        Mockito.when(userRepository.existsByEmail("testemail@gmail.com")).thenReturn(true);

        boolean exists = userService.existsByEmail("testemail@gmail.com");

        Assertions.assertTrue(exists);
    }

    @Test
    public void test_findByEmail_shouldReturnUser() {
        User user = createTestUser();

        Mockito.when(userRepository.findByEmail("testemail@gmail.com")).thenReturn(user);

        Optional<User> result = userService.findByEmail("testemail@gmail.com");

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(user, result.get());
    }

    @Test
    public void test_findByEmail_shouldReturnEmptyWhenNotFound() {
        Mockito.when(userRepository.findByEmail("fakemail@gmail.com")).thenReturn(null);

        Optional<User> result = userService.findByEmail("fakemail@gmail.com");

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void test_getNameById_shouldReturnName() {
        Mockito.when(userRepository.findNameById(1L)).thenReturn(Optional.of("Jhon"));

        Optional<String> name = userService.getNameById(1L);

        Assertions.assertTrue(name.isPresent());
        Assertions.assertEquals("Jhon", name.get());
    }

    @Test
    public void test_getNameById_shouldReturnEmptyWhenNotFound() {
        Mockito.when(userRepository.findNameById(99L)).thenReturn(Optional.empty());

        Optional<String> name = userService.getNameById(99L);

        Assertions.assertFalse(name.isPresent());
    }
}
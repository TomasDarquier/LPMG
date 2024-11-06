package com.tdarquier.test.user_management_service.services;

import java.util.Date;
import java.util.Optional;

import com.tdarquier.test.user_management_service.exceptions.InvalidUserException;
import com.tdarquier.test.user_management_service.exceptions.UserAlreadyExistsException;
import com.tdarquier.test.user_management_service.exceptions.UserNotRegisteredException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.tdarquier.test.user_management_service.entities.User;
import com.tdarquier.test.user_management_service.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User getUserById(Long id) {
    Optional<User> user = userRepository.findById(id);
    return user.orElseThrow();
  }

  @Override
  public User createUser(User user) {
    if(userRepository.existsByEmail(user.getEmail())){
      throw new UserAlreadyExistsException("The email is already associated with an account");
    }else if(userRepository.existsByUsername(user.getUsername())){
      throw new UserAlreadyExistsException("The username already exists");
    }
    // at the register moment, the user doesn't have a hashed password
    if(user.getPasswordHash() == null){
      throw new InvalidUserException("Password is required");
    }
    user.setPasswordHash(hashPassword(user.getPasswordHash()));
    user.setCreatedAt(new Date());
    user.setUpdatedAt(new Date());
    return userRepository.save(user);
  }

  private static String hashPassword(String password) {
    String salt = BCrypt.gensalt(12);
    return BCrypt.hashpw(password, salt);
  }

  @Override
  public User updateUser(User modifiedUser) {
    validateUser(modifiedUser);
    // the password is not hashed, so we need to hash it to make the validation
    if(modifiedUser.getPasswordHash() == null){
      throw new InvalidUserException("Password is required to modify an account");
    }
    modifiedUser.setUpdatedAt(new Date());
    validatePassword(modifiedUser);

    modifiedUser.setPasswordHash(userRepository.findById(modifiedUser.getId()).get().getPasswordHash());
    return userRepository.save(modifiedUser);
  }

  private void validatePassword(User modifiedUser) {
    User user = getUserById(modifiedUser.getId());
    if(!BCrypt.checkpw(modifiedUser.getPasswordHash(), user.getPasswordHash())){
      throw new UserNotRegisteredException("The password does not match");
    }
  }

  @Override
  public void deleteUser(User userToDelete) {
    validatePassword(userToDelete);
    validateUser(userToDelete);
    userRepository.delete(userToDelete);
  }

  private void validateUser(User user) {
    if(user.getId() == null){
      throw new UserNotRegisteredException("The user doesn't specify an ID");
    }
    Optional<User> userFromDb = userRepository.findById(user.getId());

    if(userFromDb.isEmpty() || !userFromDb.get().equals(user)) {
      throw new UserNotRegisteredException("The user doesn't match with a registered one");
    }
  }

}

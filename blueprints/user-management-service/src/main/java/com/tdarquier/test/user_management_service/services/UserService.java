package com.tdarquier.test.user_management_service.services;

import com.tdarquier.test.user_management_service.entities.User;

public interface UserService{

  User getUserById(Long id);
  User createUser(User user);
  User updateUser(User userModified);
  void deleteUser(User user);

}

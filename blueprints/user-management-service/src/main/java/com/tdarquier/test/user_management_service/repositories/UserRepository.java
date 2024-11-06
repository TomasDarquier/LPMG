package com.tdarquier.test.user_management_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tdarquier.test.user_management_service.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

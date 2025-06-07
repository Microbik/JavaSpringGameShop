package com.example.gameplatform.service;

import com.example.gameplatform.dto.UserRegistrationDto;
import com.example.gameplatform.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    User createUser(User user);
    User updateUser(Long id, User updatedUser);
    void deleteUser(Long id);
    void register(UserRegistrationDto dto);
    Optional<User> findByEmail(String email);
    List<User> getAllUsersWithDetails();
}

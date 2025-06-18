package com.example.gameplatform.service;

import com.example.gameplatform.dto.UserRegistrationDto;
import com.example.gameplatform.model.Genre;
import com.example.gameplatform.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    User updateUser(Long id, User updatedUser);
    void deleteUser(Long id);
    void register(UserRegistrationDto dto);
    Optional<User> findByEmail(String email);
    List<User> getAllUsersWithDetails();
    Set<Genre> getUserFavoriteGenres(Long userId);
}

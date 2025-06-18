package com.example.gameplatform.service;

import com.example.gameplatform.dto.UserRegistrationDto;
import com.example.gameplatform.model.Genre;
import com.example.gameplatform.model.Role;
import com.example.gameplatform.model.User;
import com.example.gameplatform.model.UserGenre;
import com.example.gameplatform.repository.RoleRepository;
import com.example.gameplatform.repository.UserRepository;
import com.example.gameplatform.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public List<User> getAllUsersWithDetails() {
        return userRepository.findAllWithDetails();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Set<Genre> getUserFavoriteGenres(Long userId) {
        return userRepository.findFavoriteGenresByUserId(userId);
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        User user = getUserById(id);
        user.setRole(updatedUser.getRole());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void register(UserRegistrationDto dto) {

        Role role = roleRepository.findByName("PLAYER")
                .orElseThrow(() -> new RuntimeException("Role 'PLAYER' not found"));

        User user = new User();
        user.setName(dto.name);
        user.setEmail(dto.email);
        user.setPassword(passwordEncoder.encode(dto.password));
        user.setAge(dto.age);
        user.setBalance(BigDecimal.ZERO);
        user.setExperiencePoints(0);
        user.setRole(role);

        userRepository.save(user);

    }
}


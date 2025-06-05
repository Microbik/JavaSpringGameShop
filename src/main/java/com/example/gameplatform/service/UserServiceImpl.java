package com.example.gameplatform.service;

import com.example.gameplatform.dto.UserRegistrationDto;
import com.example.gameplatform.model.Role;
import com.example.gameplatform.model.User;
import com.example.gameplatform.repository.RoleRepository;
import com.example.gameplatform.repository.UserRepository;
import com.example.gameplatform.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
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
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        User user = getUserById(id);
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setAge(updatedUser.getAge());
        user.setBalance(updatedUser.getBalance());
        user.setExperiencePoints(updatedUser.getExperiencePoints());
        user.setRole(updatedUser.getRole());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void register(UserRegistrationDto dto) {

        Role role = roleRepository.findByName("Player")
                .orElseThrow(() -> new RuntimeException("Role 'Player' not found"));

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


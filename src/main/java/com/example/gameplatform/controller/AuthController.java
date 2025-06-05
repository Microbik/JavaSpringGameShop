package com.example.gameplatform.controller;

import com.example.gameplatform.dto.LoginRequest;
import com.example.gameplatform.dto.UserRegistrationDto;
import com.example.gameplatform.model.Role;
import com.example.gameplatform.model.User;
import com.example.gameplatform.repository.RoleRepository;
import com.example.gameplatform.repository.UserRepository;
import com.example.gameplatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationDto dto) {
        if (userService.findByEmail(dto.email).isPresent()) {
            return ResponseEntity.badRequest().body("Пользователь уже существует");
        }
        userService.register(dto);
        return ResponseEntity.ok("Регистрация успешна");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole().getName()
        ));
    }

}




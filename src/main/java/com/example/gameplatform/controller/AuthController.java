package com.example.gameplatform.controller;

import com.example.gameplatform.config.JwtUtils;
import com.example.gameplatform.dto.LoginRequest;
import com.example.gameplatform.dto.UserRegistrationDto;
import com.example.gameplatform.model.Role;
import com.example.gameplatform.model.User;
import com.example.gameplatform.repository.RoleRepository;
import com.example.gameplatform.repository.UserRepository;
import com.example.gameplatform.service.CustomUserDetailsService;
import com.example.gameplatform.service.SecurityUserDetails;
import com.example.gameplatform.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError firstError = bindingResult.getFieldErrors().get(0);
            return ResponseEntity.badRequest().body(firstError.getDefaultMessage());
        }

        if (userService.findByEmail(dto.email).isPresent()) {
            return ResponseEntity.badRequest().body("Пользователь уже существует");
        }

        userService.register(dto);
        return ResponseEntity.ok("Регистрация успешна");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());

            if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
            }

            SecurityUserDetails securityUserDetails = (SecurityUserDetails) userDetails;
            String token = jwtUtils.generateToken(userDetails);

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "name", securityUserDetails.getFullName(),
                    "id", securityUserDetails.getId(),
                    "email", securityUserDetails.getUsername(),
                    "role", securityUserDetails.getRole(),
                    "balance", securityUserDetails.getBalance()
            ));
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found", e);
        }
    }
}



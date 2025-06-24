package com.example.gameplatform.controller;

import com.example.gameplatform.config.JwtAuthenticationFilter;
import com.example.gameplatform.config.JwtUtils;
import com.example.gameplatform.dto.LoginRequest;
import com.example.gameplatform.dto.UserRegistrationDto;
import com.example.gameplatform.model.User;
import com.example.gameplatform.service.CustomUserDetailsService;
import com.example.gameplatform.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
class AuthControllerTest {

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private BindingResult bindingResult;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_Success() {
        // Create UserRegistrationDto with all fields
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.name = "Test User";
        dto.email = "test@example.com";
        dto.password = "password123";
        dto.age = 25;

        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());

        ResponseEntity<?> response = authController.register(dto, bindingResult);

        assertEquals("Регистрация успешна", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(userService, times(1)).register(any(UserRegistrationDto.class));
    }

    @Test
    void register_UserAlreadyExists() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.name = "Existing User";
        dto.email = "existing@example.com";
        dto.password = "password123";
        dto.age = 30;

        when(userService.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        ResponseEntity<?> response = authController.register(dto, bindingResult);

        assertEquals("Пользователь уже существует", response.getBody());
        assertEquals(400, response.getStatusCodeValue());
        verify(userService, never()).register(any(UserRegistrationDto.class));
    }

    @Test
    void register_MissingRequiredFields_ShouldFail() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.name = "Test User";
        dto.password = "password123";
        dto.age = 25;

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("userRegistrationDto", "email", "Email is required")));

        ResponseEntity<?> response = authController.register(dto, bindingResult);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Email is required", response.getBody());
    }

    @Test
    void register_InvalidEmailFormat_ShouldFail() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.name = "Test User";
        dto.email = "invalid-email";
        dto.password = "password123";
        dto.age = 25;

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("userRegistrationDto", "email", "Invalid email format")));

        ResponseEntity<?> response = authController.register(dto, bindingResult);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid email format", response.getBody());
    }

    @Test
    void login_UserNotFound() {
        LoginRequest request = new LoginRequest();
        request.setEmail("nonexistent@example.com");
        request.setPassword("password");

        when(customUserDetailsService.loadUserByUsername(anyString()))
                .thenThrow(new UsernameNotFoundException("User not found"));

        assertThrows(ResponseStatusException.class, () -> authController.login(request));
    }
}

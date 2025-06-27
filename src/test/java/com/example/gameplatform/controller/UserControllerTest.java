package com.example.gameplatform.controller;

import com.example.gameplatform.model.Genre;
import com.example.gameplatform.model.Role;
import com.example.gameplatform.model.User;
import com.example.gameplatform.repository.RoleRepository;
import com.example.gameplatform.service.CustomUserDetailsService;
import com.example.gameplatform.service.SecurityUserDetails;
import com.example.gameplatform.service.UserService;
import com.example.gameplatform.config.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtUtils jwtUtils;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(2L);
        user.setEmail("user@example.com");
        user.setPassword("pass");
        user.setName("User");
        Role role = new Role();
        role.setName("USER");
        user.setRole(role);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_AdminAccess_Success() throws Exception {
        when(userService.getAllUsersWithDetails()).thenReturn(Arrays.asList(new User(), new User()));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_AdminAccess_Success() throws Exception {
        mockMvc.perform(delete("/api/users/2")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(2L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteUser_NonAdmin_ShouldForbid() throws Exception {
        mockMvc.perform(delete("/api/users/2"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void getUserFavoriteGenres_Success() throws Exception {
        Set<Genre> genres = new HashSet<>();
        Genre genre = new Genre();
        genre.setName("Action");
        genres.add(genre);

        when(userService.getUserFavoriteGenres(anyLong())).thenReturn(genres);

        String json = "{\"userId\": 1}";

        mockMvc.perform(post("/api/users/get-genres")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void setUserRole_AdminAccess_Success() throws Exception {
        User mockUser = new User();
        mockUser.setId(2L);

        Role newRole = new Role();
        newRole.setName("USER");
        user.setRole(newRole);
        newRole.setId(2L);

        when(userService.getUserById(2L)).thenReturn(mockUser);
        when(roleRepository.findById(2L)).thenReturn(Optional.of(newRole));

        String json = "{\"userId\": 2, \"roleId\": 2}";

        mockMvc.perform(post("/api/users/set-role")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUser(eq(2L), any(User.class));
    }
}


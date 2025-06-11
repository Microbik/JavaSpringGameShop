package com.example.gameplatform.controller;

import com.example.gameplatform.model.Genre;
import com.example.gameplatform.model.Role;
import com.example.gameplatform.model.User;
import com.example.gameplatform.repository.RoleRepository;
import com.example.gameplatform.service.SecurityUserDetails;
import com.example.gameplatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsersWithDetails();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> getUserById(
            @PathVariable Long id,
            @AuthenticationPrincipal SecurityUserDetails currentUser
    ) {
        // Проверка: либо запрашиваем свой ID, либо пользователь - админ
        if (!currentUser.isAdmin() && !currentUser.getId().equals(id)) {
            throw new AccessDeniedException("You can only access your own profile");
        }

        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/get-genres")
    public ResponseEntity<Set<Genre>> getUserFavoriteGenres(@RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        Set<Genre> favoriteGenres = userService.getUserFavoriteGenres(userId);
        return ResponseEntity.ok(favoriteGenres);
    }

    @PostMapping("/set-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> setUserRole(@RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        Long roleId = request.get("roleId");

        User user = userService.getUserById(userId);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        user.setRole(role);
        userService.updateUser(userId, user);

        return ResponseEntity.ok().build();
    }
}

package com.example.gameplatform.controller;

import com.example.gameplatform.dto.GenreToggleRequest;
import com.example.gameplatform.dto.GenreToggleResponse;
import com.example.gameplatform.service.UserGenreService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favourite-genre")
public class UserGenreController {

    private final UserGenreService userGenreService;

    public UserGenreController(UserGenreService userGenreService) {
        this.userGenreService = userGenreService;
    }

    @PostMapping
    public ResponseEntity<GenreToggleResponse> toggleFavouriteGenre(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody GenreToggleRequest request) {
        GenreToggleResponse response = userGenreService.toggleUserGenre(
                userDetails.getUsername(),
                request.getGenreId()
        );
        return ResponseEntity.ok(response);
    }
}
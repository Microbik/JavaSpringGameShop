package com.example.gameplatform.service;

import com.example.gameplatform.dto.GenreToggleResponse;
import com.example.gameplatform.model.*;
import com.example.gameplatform.repository.UserGenreRepository;
import com.example.gameplatform.repository.UserRepository;
import com.example.gameplatform.repository.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserGenreService {

    private final UserGenreRepository userGenreRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;

    public UserGenreService(UserGenreRepository userGenreRepository,
                            UserRepository userRepository,
                            GenreRepository genreRepository) {
        this.userGenreRepository = userGenreRepository;
        this.userRepository = userRepository;
        this.genreRepository = genreRepository;
    }

    @Transactional
    public GenreToggleResponse toggleUserGenre(String username, Long genreId) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new RuntimeException("Genre not found"));

        Optional<UserGenre> existing = userGenreRepository.findByUserIdAndGenreId(user.getId(), genre.getId());

        if (existing.isPresent()) {
            userGenreRepository.delete(existing.get());
            return new GenreToggleResponse("removed", genre.getId(), genre.getName());
        } else {
            UserGenre userGenre = new UserGenre(user, genre);
            userGenreRepository.save(userGenre);
            return new GenreToggleResponse("added", genre.getId(), genre.getName());
        }
    }
}
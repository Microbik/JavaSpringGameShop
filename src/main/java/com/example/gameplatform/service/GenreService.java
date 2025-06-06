package com.example.gameplatform.service;

import com.example.gameplatform.model.Genre;
import java.util.List;

public interface GenreService {
    List<Genre> getAllGenres();
    Genre getGenreById(Long id);
    Genre createGenre(Genre genre);
    Genre updateGenre(Long id, Genre genreDetails);
    void deleteGenre(Long id);
}
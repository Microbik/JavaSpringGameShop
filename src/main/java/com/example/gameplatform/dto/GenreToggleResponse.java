package com.example.gameplatform.dto;

public class GenreToggleResponse {
    private String action; // "added" или "removed"
    private Long genreId;
    private String genreName;

    public GenreToggleResponse(String action, Long genreId, String genreName) {
        this.action = action;
        this.genreId = genreId;
        this.genreName = genreName;
    }

    public String getAction() {
        return action;
    }

    public Long getGenreId() {
        return genreId;
    }

    public String getGenreName() {
        return genreName;
    }
}
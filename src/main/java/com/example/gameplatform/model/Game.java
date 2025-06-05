package com.example.gameplatform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @Column(nullable = false)
    private String name;

    @Min(12) @Max(21)
    private Integer rating;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false, name = "releasedate")
    private LocalDate releaseDate;

    private String publisher;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Addon> addons = new HashSet<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Achievement> achievements = new HashSet<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserGame> users = new HashSet<>();

    // Конструкторы
    public Game() {}

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Set<Addon> getAddons() {
        return addons;
    }

    public void setAddons(Set<Addon> addons) {
        this.addons = addons;
    }

    public Set<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(Set<Achievement> achievements) {
        this.achievements = achievements;
    }

    public Set<UserGame> getUsers() {
        return users;
    }

    public void setUsers(Set<UserGame> users) {
        this.users = users;
    }

    // Методы для удобства
    public void addAddon(Addon addon) {
        this.addons.add(addon);
        addon.setGame(this);
    }

    public void removeAddon(Addon addon) {
        this.addons.remove(addon);
        addon.setGame(null);
    }

    public void addAchievement(Achievement achievement) {
        this.achievements.add(achievement);
        achievement.setGame(this);
    }

    public void removeAchievement(Achievement achievement) {
        this.achievements.remove(achievement);
        achievement.setGame(null);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", price=" + price +
                ", releaseDate=" + releaseDate +
                ", publisher='" + publisher + '\'' +
                '}';
    }
}
package com.example.gameplatform.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_games")
@Setter
@Getter
@NoArgsConstructor

public class UserGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usergame_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("user-games")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(nullable = false, name = "purchase_date")
    private LocalDate purchaseDate;

    public UserGame(User user, Game game) {
        this.user = user;
        this.game = game;
        this.purchaseDate = LocalDate.now();
    }

    @Override
    public String toString() {
        return "UserGame{" +
                "id=" + id +
                ", purchaseDate=" + purchaseDate +
                '}';
    }
}

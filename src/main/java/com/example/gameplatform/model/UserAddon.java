package com.example.gameplatform.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "user_addons")
public class UserAddon {
    // Геттеры и сеттеры
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "useraddon_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "addon_id", nullable = false)
    private Addon addon;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("user-addons")
    @JsonIgnore
    private User user;

    @Column(nullable = false, name = "purchase_date")
    private LocalDate purchaseDate;

    // Конструкторы
    public UserAddon() {
        this.purchaseDate = LocalDate.now();
    }

    public UserAddon(User user, Addon addon) {
        this();
        this.user = user;
        this.addon = addon;
    }

    @Override
    public String toString() {
        return "UserAddon{" +
                "id=" + id +
                ", purchaseDate=" + purchaseDate +
                '}';
    }
}

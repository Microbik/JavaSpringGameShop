package com.example.gameplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "addons")
@Setter
@Getter
@NoArgsConstructor
public class Addon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addon_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    @JsonIgnore
    private Game game;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AddonType type;

    @OneToMany(mappedBy = "addon", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<UserAddon> users = new HashSet<>();

    public enum AddonType {
        DLC, IN_GAME_PURCHASE
    }

    @Override
    public String toString() {
        return "Addon{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", type=" + type +
                '}';
    }
}

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

@Setter
@Getter
@Entity
@Table(name = "user_achievements")
public class UserAchievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userachievement_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("user-achievements")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "achievement_id", nullable = false)
    private Achievement achievement;

    @Column(nullable = false, name = "unlock_date")
    private LocalDate unlockDate;

    public UserAchievement() {
        this.unlockDate = LocalDate.now();
    }

    public UserAchievement(User user, Achievement achievement) {
        this();
        this.user = user;
        this.achievement = achievement;
    }

    @Override
    public String toString() {
        return "UserAchievement{" +
                "id=" + id +
                ", unlockDate=" + unlockDate +
                '}';
    }
}
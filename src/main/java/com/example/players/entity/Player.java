package com.example.players.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "player", indexes = @Index(name = "idx_ranking", columnList = "ranking"))
@Getter
@Setter
public class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String playerID;
    private int birthYear;
    private int birthMonth;
    private int birthDay;
    private String birthCountry;
    private String birthState;
    private String birthCity;
    private Integer deathYear; // Nullable
    private Integer deathMonth; // Nullable
    private Integer deathDay; // Nullable
    private String deathCountry; // Nullable
    private String deathState; // Nullable
    private String deathCity; // Nullable
    private String nameFirst;
    private String nameLast;
    private String nameGiven;
    private int weight;
    private int height;
    private String bats;
    private String throwSide;
    private LocalDate debut;
    private LocalDate finalGame;
    private String retroID;
    private String bbrefID;

    public LocalDate getDateOfBirth() {
        return LocalDate.of(birthYear, birthMonth, birthDay);
    }

    public LocalDate getDateOfDeath() {
        if (deathYear != null && deathMonth != null && deathDay != null) {
            return LocalDate.of(deathYear, deathMonth, deathDay);
        } else {
            return null; // Handle cases where the player is not deceased
        }
    }
}

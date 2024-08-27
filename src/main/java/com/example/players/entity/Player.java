package com.example.players.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "player", indexes = @Index(name = "idx_ranking", columnList = "ranking"))
@Getter
@Setter
public class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private String email;
    private String dateOfBirth;
    private String gamesPlayed;
    @Column(nullable = false, unique = true) //Unique constraint ranking
    private Integer ranking;
}

package com.example.players.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ApiUser {
    @Id
    private String username;
    private String password;
}

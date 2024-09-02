package com.example.players.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
public class FileProcessingTask {

    @Id
    private UUID taskId;
    private String status; // "PENDING", "IN_PROGRESS", "COMPLETED", "FAILED"
    private String resultUrl; // URL to retrieve the result if needed

    // Constructors, getters, setters
}


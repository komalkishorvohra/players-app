package com.example.players.repository;

import com.example.players.entity.FileProcessingTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileProcessingTaskRepository  extends JpaRepository<FileProcessingTask, UUID> {
}

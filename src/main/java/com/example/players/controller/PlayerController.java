package com.example.players.controller;

import com.example.players.entity.Player;
import com.example.players.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/players")
public class PlayerController {
    @Autowired
    PlayerService playerService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPlayers(@RequestParam("file") MultipartFile file) {
        try {
            playerService.processCSVFile(file);
            return ResponseEntity.status(HttpStatus.OK).body("CSV file is being processed");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process CSV File");
        }
    }

    @PostMapping("/upload-json")
    public ResponseEntity<String> uploadPlayersFromJson(@RequestBody List<Player> players) {
        playerService.saveAllPlayers(players);
        return new ResponseEntity<>("Players have been uploaded successfully", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Player>> getPlayers(@PageableDefault(size = 5) Pageable pageable) {
        Page<Player> players = playerService.getPlayers(pageable);
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable UUID id) {
        Player player = playerService.getPlayerById(id);
        if (player != null) {
            return ResponseEntity.ok(player);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found");
        }
    }

    @GetMapping("/top")
    public ResponseEntity<List<Player>> getTopRankingPlayers(@RequestParam(defaultValue = "5") int limit) {
        List<Player> topPlayers = playerService.getTopRankingPlayers(limit);
        return new ResponseEntity<>(topPlayers, HttpStatus.OK);
    }
}

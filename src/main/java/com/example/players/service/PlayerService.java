package com.example.players.service;

import com.example.players.entity.Player;
import com.example.players.repository.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    public Page<Player> getPlayers(Pageable pageable) {
        return playerRepository.findAll(pageable);
    }

    @Async
    public void processCSVFile(MultipartFile file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader() // Automatically uses the first record as header
                    .setSkipHeaderRecord(true) // skips the header record in parseing
                    .build();

            CSVParser csvParser = new CSVParser(reader, csvFormat);
            List<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                try {
                    //Parse and validate CSV Record
                    Player player = parseCSVRecord(csvRecord);
                    //Save player to database
                    playerRepository.save(player);
                } catch (DataIntegrityViolationException e) {
                    log.error("Ranking already taken for player: {}. Skipping record.", csvRecord);
                } catch (Exception e) {
                    log.error("Error processing record {}: {}", csvRecord, e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Error processing CSV file: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing CSV file.");
        }
    }

    private Player parseCSVRecord(CSVRecord csvRecord) {
        Player player = new Player();
        player.setName(csvRecord.get("Name"));
        player.setEmail(csvRecord.get("Email"));
        player.setDateOfBirth(csvRecord.get("Date of Birth"));
        player.setGamesPlayed(csvRecord.get("Games Played"));
        player.setRanking(Integer.valueOf(csvRecord.get("Ranking")));
        log.info(player.toString());
        return player;
    }

    public void saveAllPlayers(List<Player> players) {
        List<String> conflictMessages = new ArrayList<>();
        for (Player player : players) {
            try {
                playerRepository.save(player);
            } catch (DataIntegrityViolationException e) {
                conflictMessages.add("Ranking " + player.getRanking() + "for player " + player.getName() + "is already taken");
            }
        }
        if (!conflictMessages.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.join(";", conflictMessages));
        }
    }

    @Cacheable(value = "player", key="#id", unless = "#result == null")
    public Player getPlayerById(UUID id) {
        Optional<Player> playerOpt = playerRepository.findById(id);
        playerOpt.ifPresent(player -> log.info("returning player {}", player.getName()));
        return playerOpt.orElse(null);
    }

    @Cacheable(value = "player", key="'topPlayers'")
    public List<Player> getTopRankingPlayers(int limit) {
        return playerRepository.findTopPlayersByRankingDesc(limit);
    }
}

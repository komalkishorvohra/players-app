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
import java.time.LocalDate;
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
                    .setSkipHeaderRecord(true) // skips the header record in parsing
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

        player.setPlayerID(csvRecord.get("playerID"));
        player.setBirthYear(Integer.parseInt(csvRecord.get("birthYear")));
        player.setBirthMonth(Integer.parseInt(csvRecord.get("birthMonth")));
        player.setBirthDay(Integer.parseInt(csvRecord.get("birthDay")));
        player.setBirthCountry(csvRecord.get("birthCountry"));
        player.setBirthState(csvRecord.get("birthState"));
        player.setBirthCity(csvRecord.get("birthCity"));

        // Optional fields (death information)
        String deathYear = csvRecord.get("deathYear");
        String deathMonth = csvRecord.get("deathMonth");
        String deathDay = csvRecord.get("deathDay");

        player.setDeathYear(deathYear.isEmpty() ? null : Integer.parseInt(deathYear));
        player.setDeathMonth(deathMonth.isEmpty() ? null : Integer.parseInt(deathMonth));
        player.setDeathDay(deathDay.isEmpty() ? null : Integer.parseInt(deathDay));

        player.setDeathCountry(csvRecord.get("deathCountry"));
        player.setDeathState(csvRecord.get("deathState"));
        player.setDeathCity(csvRecord.get("deathCity"));
        player.setNameFirst(csvRecord.get("nameFirst"));
        player.setNameLast(csvRecord.get("nameLast"));
        player.setNameGiven(csvRecord.get("nameGiven"));
        player.setWeight(Integer.parseInt(csvRecord.get("weight")));
        player.setHeight(Integer.parseInt(csvRecord.get("height")));
        player.setBats(csvRecord.get("bats"));
        player.setThrowSide(csvRecord.get("throws"));

        // Parse debut and finalGame as LocalDate
        player.setDebut(LocalDate.parse(csvRecord.get("debut")));
        player.setFinalGame(LocalDate.parse(csvRecord.get("finalGame")));

        player.setRetroID(csvRecord.get("retroID"));
        player.setBbrefID(csvRecord.get("bbrefID"));

        // The Date of Birth is derived from birthYear, birthMonth, and birthDay
        LocalDate dateOfBirth = player.getDateOfBirth();

        log.info(player.toString());
        return player;
    }

    public void saveAllPlayers(List<Player> players) {
        List<String> conflictMessages = new ArrayList<>();
        for (Player player : players) {
            try {
                playerRepository.save(player);
            } catch (DataIntegrityViolationException e) {
                conflictMessages.add("Ranking " + player.getRetroID() + "for player " + player.getNameGiven() + "is already taken");
            }
        }
        if (!conflictMessages.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.join(";", conflictMessages));
        }
    }

    @Cacheable(value = "player", key="#id", unless = "#result == null")
    public Player getPlayerById(UUID id) {
        Optional<Player> playerOpt = playerRepository.findById(id);
        playerOpt.ifPresent(player -> log.info("returning player {}", player.getNameGiven()));
        return playerOpt.orElse(null);
    }

    @Cacheable(value = "player", key="'topPlayers'")
    public List<Player> getTopRankingPlayers(int limit) {
        return playerRepository.findTopPlayersByRankingDesc(limit);
    }
}

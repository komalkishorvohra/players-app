package com.example.players.repository;
import com.example.players.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID> {
    @Query(value = "SELECT * from player ORDER BY ranking LIMIT :limit", nativeQuery = true)
    List<Player> findTopPlayersByRankingDesc(int limit);
}

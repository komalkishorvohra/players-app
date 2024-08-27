package com.example.players.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.players.entity.Player;
import com.example.players.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PlayerServiceTest {

    @Mock
    PlayerRepository playerRepository;
    @InjectMocks
    private PlayerService playerService;
    private UUID playerId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        playerId = UUID.randomUUID();
    }

    @Test
    public void testGetAllPlayers() {
        Pageable pageable = PageRequest.of(0, 2);
        List<Player> players = Arrays.asList(new Player(), new Player());
        Page<Player> page = new PageImpl<>(players, pageable, players.size());

        when(playerRepository.findAll(pageable)).thenReturn(page);

        Page<Player> result = playerService.getPlayers(pageable);
        assertEquals(2, result.getNumberOfElements());
        verify(playerRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testGetPlayerId_Found() {
        Player player = new Player();
        player.setId(playerId);
        when(playerRepository.findById(playerId)).thenReturn((Optional.of(player)));

        Player result = playerService.getPlayerById(playerId);
        assertNotNull(result);
        verify(playerRepository, times(1)).findById(playerId);
    }

    @Test
    public void testGetPlayerId_NotFound() {
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        Player result = playerService.getPlayerById(playerId);
        assertNull(result);
        verify(playerRepository, times(1)).findById(playerId);
    }
}

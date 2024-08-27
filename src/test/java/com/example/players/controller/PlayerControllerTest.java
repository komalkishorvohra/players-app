package com.example.players.controller;

import com.example.players.entity.Player;
import com.example.players.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

@WebMvcTest(PlayerController.class)
public class PlayerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    private UUID playerId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        playerId = UUID.randomUUID();
    }

    @Test
    public void testGetAllPlayers() throws Exception {
        Pageable pageable = PageRequest.of(0,2);
        List<Player> players = Arrays.asList(new Player(), new Player());
        Page<Player> page = new PageImpl<>(players, pageable, players.size());

        when(playerService.getPlayers(pageable)).thenReturn(page);

        mockMvc.perform(get("/players?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    public void testGetPlayerId_Found() throws Exception {
        Player player = new Player();
        player.setId(playerId);
        player.setName("John Doe");

        System.out.println("UUID used in test: " + playerId);

        when(playerService.getPlayerById(playerId)).thenReturn(player);

        mockMvc.perform(get("/players/{id}", playerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(playerId.toString())));
    }

    @Test
    public void testGetPlayerById_NotFound() throws Exception  {
        when(playerService.getPlayerById(playerId)).thenReturn(null);

        mockMvc.perform(get("/players/{id}", playerId))
                .andExpect(status().isNotFound());
    }
}


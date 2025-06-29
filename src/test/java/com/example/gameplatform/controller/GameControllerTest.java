package com.example.gameplatform.controller;

import com.example.gameplatform.config.JwtAuthenticationFilter;
import com.example.gameplatform.model.Game;
import com.example.gameplatform.service.CustomUserDetailsService;
import com.example.gameplatform.service.GameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
@AutoConfigureMockMvc(addFilters = false)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    public void testGetAllGames() throws Exception {
        Game game = new Game();
        game.setId(1L);
        game.setName("Test Game");

        Mockito.when(gameService.getAll()).thenReturn(List.of(game));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/games")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is("Test Game")));
    }

    @Test
    public void testGetGameById_Exists() throws Exception {
        Game game = new Game();
        game.setId(1L);
        game.setName("Test Game");

        Mockito.when(gameService.getById(1L)).thenReturn(Optional.of(game));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/games/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Test Game")));
    }

    @Test
    public void testGetGameById_NotExists() throws Exception {
        Mockito.when(gameService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/games/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"GAMEMANAGER"})
    public void testCreateGame_WithAuthorization() throws Exception {
        Game game = new Game();
        game.setId(1L);
        game.setName("New Game");

        Mockito.when(gameService.create(any(Game.class))).thenReturn(game);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Game\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("New Game")));
    }

    @Test
    @WithMockUser(roles = {"GAMEMANAGER"})
    public void testUpdateGame_Exists() throws Exception {
        Game updatedGame = new Game();
        updatedGame.setId(1L);
        updatedGame.setName("Updated Game");

        Mockito.when(gameService.update(anyLong(), any(Game.class))).thenReturn(Optional.of(updatedGame));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/games/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Game\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Game")));
    }

    @Test
    @WithMockUser(roles = {"GAMEMANAGER"})
    public void testUpdateGame_NotExists() throws Exception {
        Mockito.when(gameService.update(anyLong(), any(Game.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/games/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Game\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testDeleteGame_WithAuthorization() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/games/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(gameService).delete(1L);
    }

}

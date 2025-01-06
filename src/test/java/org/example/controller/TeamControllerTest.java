package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.dto.PlayerCreateDTO;
import org.example.model.dto.TeamCreateDTO;
import org.example.model.dto.TeamDTO;
import org.example.model.entity.PlayerEntity;
import org.example.model.enumeration.PlayerPosition;
import org.example.model.mapper.PlayerMapper;
import org.example.model.repository.PlayerRepository;
import org.example.model.repository.TeamRepository;
import org.example.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayerMapper playerMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        teamRepository.deleteAll();
    }

    @Test
    void testCreationAndFindElements() throws Exception {
        // Given
        PlayerEntity playerA = new PlayerEntity(1L, "Player A", PlayerPosition.DEFENDER, null);
        PlayerEntity playerB = new PlayerEntity(2L, "Player B", PlayerPosition.GOALKEEPER, null);
        PlayerEntity playerC = new PlayerEntity(3L, "Player C", PlayerPosition.DEFENDER, null);
        playerRepository.saveAll(List.of(playerA, playerB, playerC));

        PlayerCreateDTO playerACreate = new PlayerCreateDTO(1L, "Player A", PlayerPosition.DEFENDER);
        PlayerCreateDTO playerBCreate = new PlayerCreateDTO(2L, "Player B", PlayerPosition.GOALKEEPER);
        PlayerCreateDTO playerCCreate = new PlayerCreateDTO(3L, "Player C", PlayerPosition.DEFENDER);
        TeamCreateDTO teamCreateDTO1 = new TeamCreateDTO("AJ Auxerre", "AJA", 1000L, List.of(playerACreate, playerBCreate));
        TeamCreateDTO teamCreateDTO2 = new TeamCreateDTO("Le Havre AC", "HAC", 1500L, List.of(playerCCreate));
        TeamCreateDTO teamCreateDTO3 = new TeamCreateDTO("ESTAC Troyes", "EST", 1250L, null);
        TeamCreateDTO teamCreateDTO4 = new TeamCreateDTO("FC Metz", "MET", 1750L, null);
        TeamCreateDTO teamCreateDTO5 = new TeamCreateDTO("SC Bastia", "BAS", 2000L, null);
        TeamCreateDTO teamCreateDTO6 = new TeamCreateDTO("Angers SCO", "ANG", 2500L, null);

        // Create teams
        TeamDTO createdTeam1 = teamService.create(teamCreateDTO1);
        TeamDTO createdTeam2 = teamService.create(teamCreateDTO2);
        TeamDTO createdTeam3 = teamService.create(teamCreateDTO3);
        TeamDTO createdTeam4 = teamService.create(teamCreateDTO4);
        TeamDTO createdTeam5 = teamService.create(teamCreateDTO5);
        TeamDTO createdTeam6 = teamService.create(teamCreateDTO6);

        // When & Then
        // Test for page 0

        mockMvc.perform(get("/team/list")
                        .param("nameSort", "NONE")
                        .param("acronymSort", "DESC")
                        .param("budgetSort", "NONE")
                        .param("page", "0")
                        .param("pageSize", "3"))

                // Verify teams for page 0
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(3))
                .andExpect(jsonPath("$.items[0].id").value(createdTeam4.getId()))
                .andExpect(jsonPath("$.items[1].id").value(createdTeam2.getId()))
                .andExpect(jsonPath("$.items[2].id").value(createdTeam3.getId()))

                // Verify pagination metadata for page 0
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.pageSize").value(3))
                .andExpect(jsonPath("$.pageCount").value(2))

                // Verify players for Team B
                .andExpect(jsonPath("$.items[1].players").isArray())
                .andExpect(jsonPath("$.items[1].players[0].name").value("Player C"));


        // Test for page 1
        mockMvc.perform(get("/team/list")
                        .param("nameSort", "NONE")
                        .param("acronymSort", "DESC")
                        .param("budgetSort", "NONE")
                        .param("page", "1")
                        .param("pageSize", "3"))

                // Verify teams for page 1
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(3))
                .andExpect(jsonPath("$.items[0].id").value(createdTeam5.getId()))
                .andExpect(jsonPath("$.items[1].id").value(createdTeam6.getId()))
                .andExpect(jsonPath("$.items[2].id").value(createdTeam1.getId()))

                // Verify pagination metadata for page 1
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.pageSize").value(3))
                .andExpect(jsonPath("$.pageCount").value(2));
    }

    @Test
    void testDelete() throws Exception {
        String uniqueTeamName = "Team A";
        TeamCreateDTO teamCreateDTO = new TeamCreateDTO(uniqueTeamName, "TA", 1000L, null);
        TeamDTO createdTeam = teamService.create(teamCreateDTO);

        mockMvc.perform(delete("/team/{id}", createdTeam.getId()))
                .andExpect(status().isOk());
    }
}
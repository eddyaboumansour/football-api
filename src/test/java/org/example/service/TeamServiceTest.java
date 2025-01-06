package org.example.service;

import org.example.exception.TeamNotFoundException;
import org.example.model.dto.*;
import org.example.model.entity.TeamEntity;
import org.example.model.enumeration.SortMode;
import org.example.model.mapper.PlayerMapper;
import org.example.model.mapper.TeamMapper;
import org.example.model.repository.PlayerRepository;
import org.example.model.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamMapper teamMapper;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerMapper playerMapper;

    @InjectMocks
    private TeamService teamService;

    @Test
    void testFindAll() {
        // Given
        TeamSortDTO teamSortDTO = new TeamSortDTO(SortMode.ASC, SortMode.DESC, SortMode.DESC);
        PageRequestDTO pageRequest = new PageRequestDTO(0, 2);

        TeamEntity teamEntityA = new TeamEntity(1L, "Team A", "TA", 1000L, null);
        TeamEntity teamEntityB = new TeamEntity(2L, "Team A", "TB", 2000L, null);

        when(teamMapper.toDTO(teamEntityA)).thenReturn(new TeamDTO(1L, "Team A", "TA", 1000L, null));
        when(teamMapper.toDTO(teamEntityB)).thenReturn(new TeamDTO(2L, "Team B", "TB", 2000L, null));

        when(teamRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(teamEntityA, teamEntityB)));

        // When
        PageDTO<TeamDTO> pages = teamService.findAll(teamSortDTO, pageRequest);

        // Then
        assertEquals(0, pages.getPage());
        assertEquals(2, pages.getPageSize());
        assertEquals(1, pages.getPageCount());
        assertEquals(2, pages.getItems().size());
    }

    @Test
    void testCreateTeam() {
        // Given
        TeamCreateDTO teamCreateDTO = new TeamCreateDTO("Team A", "TA", 1000L, null);
        TeamEntity teamEntity = new TeamEntity();
        teamEntity.setName("Team A");
        teamEntity.setAcronym("TA");
        teamEntity.setBudget(1000L);

        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName("Team A");
        teamDTO.setAcronym("TA");
        teamDTO.setBudget(1000L);

        when(teamMapper.toEntity(any(TeamCreateDTO.class))).thenReturn(teamEntity);
        when(teamRepository.save(any(TeamEntity.class))).thenReturn(teamEntity);
        when(teamMapper.toDTO(any(TeamEntity.class))).thenReturn(teamDTO);

        // When
        TeamDTO createdTeam = teamService.create(teamCreateDTO);

        // Then
        assertEquals("Team A", createdTeam.getName());
        assertEquals("TA", createdTeam.getAcronym());
        assertEquals(1000L, createdTeam.getBudget());
    }

    @Test
    void testDeleteTeam() {
        // Given
        Long teamId = 1L;
        TeamEntity teamEntity = new TeamEntity();
        teamEntity.setId(teamId);

        when(teamRepository.existsById(teamId)).thenReturn(true);
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(teamEntity));

        // When
        teamService.delete(teamId);

        // Then
        verify(teamRepository, times(1)).deleteById(teamId);
    }

    @Test
    void testDeleteTeamNotFound() {
        // Given
        Long teamId = 1L;

        when(teamRepository.existsById(teamId)).thenReturn(false);

        // When & Then
        assertThrows(TeamNotFoundException.class, () -> teamService.delete(teamId));
    }
}
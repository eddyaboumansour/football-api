package org.example.model.repository;

import org.example.model.entity.TeamEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// For these tests to run, we need to exclude LiquibaseAutoConfiguration from the auto-configuration.
@DataJpaTest
@ImportAutoConfiguration(exclude = LiquibaseAutoConfiguration.class)
class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Test
    void testFindAllTeamsSortedAndPaginated() {
        // Given
        TeamEntity teamEntityA = new TeamEntity(1L, "Team A", "TA", 1000L, null);
        TeamEntity teamEntityB = new TeamEntity(2L, "Team A", "TB", 2000L, null);
        TeamEntity teamEntityC = new TeamEntity(3L, "Team C", "TD", 3000L, null);
        TeamEntity teamEntityD = new TeamEntity(4L, "Team C", "TD", 4000L, null);
        TeamEntity teamEntityE = new TeamEntity(5L, "Team E", "TE", 5000L, null);

        teamRepository.saveAll(List.of(teamEntityA, teamEntityB, teamEntityC, teamEntityD, teamEntityE));
        Sort sortOrder = Sort.by(Sort.Order.asc("name"),
                Sort.Order.desc("acronym"),
                Sort.Order.desc("budget"));

        Pageable pageable0 = PageRequest.of(0, 4, sortOrder);
        Pageable pageable1 = PageRequest.of(1, 4, sortOrder);

        // When
        Page<TeamEntity> teamEntityPage0 = teamRepository.findAll(pageable0);
        Page<TeamEntity> teamEntityPage1 = teamRepository.findAll(pageable1);

        // Then
        assertEquals(5, teamEntityPage0.getTotalElements());

        assertEquals(4, teamEntityPage0.getNumberOfElements());
        assertEquals(2L, teamEntityPage0.getContent().get(0).getId());
        assertEquals(1L, teamEntityPage0.getContent().get(1).getId());
        assertEquals(4L, teamEntityPage0.getContent().get(2).getId());
        assertEquals(3L, teamEntityPage0.getContent().get(3).getId());

        assertEquals(1, teamEntityPage1.getNumberOfElements());
        assertEquals(5L, teamEntityPage1.getContent().get(0).getId());
    }

    @Test
    void testFindAllTeamsEmpty() {
        List<TeamEntity> teams = teamRepository.findAll();
        assertTrue(teams.isEmpty());
    }
}

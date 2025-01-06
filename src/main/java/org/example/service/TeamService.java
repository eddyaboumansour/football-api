package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.TeamNotFoundException;
import org.example.model.dto.*;
import org.example.model.entity.PlayerEntity;
import org.example.model.entity.TeamEntity;
import org.example.model.enumeration.SortMode;
import org.example.model.mapper.TeamMapper;
import org.example.model.repository.PlayerRepository;
import org.example.model.repository.TeamRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * A service that encapsulates all teams business-logic.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final TeamMapper teamMapper;

    /**
     * Query teams using sorting and pagination options. Converts entities to DTO
     *
     * @param sort        Sorting options
     * @param pageRequest Pagination options
     * @return One page of team DTOs
     */
    public PageDTO<TeamDTO> findAll(TeamSortDTO sort, PageRequestDTO pageRequest) {
        List<Sort.Order> orders = new ArrayList<>();

        applySorting(sort.getNameSort(), "name", orders);
        applySorting(sort.getAcronymSort(), "acronym", orders);
        applySorting(sort.getBudgetSort(), "budget", orders);
        Sort sortOrder = orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);

        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getPageSize(), sortOrder);
        Page<TeamEntity> teamEntityPage = teamRepository.findAll(pageable);
        List<TeamEntity> teamEntities = teamEntityPage.getContent();

        return new PageDTO<>(
                teamEntities.stream().map(teamMapper::toDTO).toList(),
                teamEntityPage.getNumberOfElements(),
                teamEntityPage.getNumber(),
                teamEntityPage.getTotalPages()
        );
    }

    private static void applySorting(SortMode sort, String name, List<Sort.Order> orders) {
        if (sort != SortMode.NONE) {
            Sort.Order nameOrder = new Sort.Order(getDirection(sort), name);
            orders.add(nameOrder);
        }
    }

    private static Sort.Direction getDirection(SortMode sort) {
        return sort == SortMode.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
    }

    /**
     * Create a new team
     *
     * @param team Team options
     * @return Created team
     */
    @Transactional
    public TeamDTO create(TeamCreateDTO team) {
        log.info("Creating a new team: name={}, acronym={}, budget={}", team.getName(), team.getAcronym(), team.getBudget());

        TeamEntity teamEntity = teamMapper.toEntity(team);

        teamRepository.save(teamEntity);

        // Assign players for the team
        if (teamEntity.getPlayers() != null) {
            for (PlayerEntity player : teamEntity.getPlayers()) {
                player.setTeam(teamEntity);
                playerRepository.saveAndFlush(player);
            }
        }

        TeamDTO result = teamMapper.toDTO(teamEntity);
        log.info("Successfully created team: {}", result);
        return result;
    }


    /**
     * Delete a team
     *
     * @param id Team id
     */
    @Transactional
    public void delete(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new TeamNotFoundException(id);
        }

        // Unassign players from the team
        teamRepository.findById(id).ifPresent(team -> {
            if (team.getPlayers() != null) {
                team.getPlayers().forEach(player -> player.setTeam(null));
            }
        });

        teamRepository.deleteById(id);
        log.info("Deleted team with id={}", id);
    }
}
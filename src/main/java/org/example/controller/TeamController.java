package org.example.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.*;
import org.example.service.TeamService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * Teams controller
 */
@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {
	private final TeamService teamService;
	
	/**
	 * An endpoint for query teams
	 * @param sort Sorting options
	 * @param pageRequest Pagination options
	 * @return One page with teams
	 */
	@GetMapping("/list")
	public PageDTO<TeamDTO> findAll(
			@ParameterObject TeamSortDTO sort,
			@ParameterObject @Valid PageRequestDTO pageRequest
	) {
		return teamService.findAll(sort, pageRequest);
	}
	
	/**
	 * An endpoint for create a new team
	 * @param team Team options
	 * @return Newly created team
	 */
	@PostMapping
	public TeamDTO create(@RequestBody @Valid TeamCreateDTO team) {
		return teamService.create(team);
	}
	
	/**
	 * An endpoint for delete a team
	 * @param id Team id
	 */
	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		teamService.delete(id);
	}
}

package org.example.model.mapper;

import org.example.model.dto.TeamCreateDTO;
import org.example.model.dto.TeamDTO;
import org.example.model.entity.TeamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Convert of team entities to DTOs and vise-versa
 */
@Mapper(componentModel = "spring")
public interface TeamMapper {
	TeamDTO toDTO(TeamEntity entity);

	@Mapping(target = "id", ignore = true)
	TeamEntity toEntity(TeamCreateDTO dto);
}

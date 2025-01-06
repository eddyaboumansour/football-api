package org.example.model.mapper;

import org.example.model.dto.PlayerCreateDTO;
import org.example.model.dto.PlayerDTO;
import org.example.model.entity.PlayerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


/**
 * Convert of player entities to DTOs and vise-versa
 */
@Mapper(componentModel = "spring")
public interface PlayerMapper {
	PlayerDTO toDTO(PlayerEntity entity);

	@Mapping(target = "team", ignore = true)
	PlayerEntity toEntity(PlayerCreateDTO dto);
}

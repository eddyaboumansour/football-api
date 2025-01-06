package org.example.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.enumeration.PlayerPosition;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerCreateDTO {
	private Long id;

	@NotEmpty
	private String name;
	
	@NotNull
	private PlayerPosition position;
}

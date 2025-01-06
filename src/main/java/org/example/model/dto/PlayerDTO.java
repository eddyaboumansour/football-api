package org.example.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.enumeration.PlayerPosition;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDTO {
	private Long id;

	private String name;
	
	private PlayerPosition position;

}

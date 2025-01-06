package org.example.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDTO {
	private Long id;
	
	private String name;
	
	private String acronym;
	
	private Long budget;
	
	private List<PlayerDTO> players;
}

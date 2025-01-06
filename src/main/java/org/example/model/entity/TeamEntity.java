package org.example.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "teams")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;

	private String acronym;
	
	private Long budget;

	@OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private List<PlayerEntity> players;
}

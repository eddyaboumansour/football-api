package org.example.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.enumeration.SortMode;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamSortDTO {
	private SortMode nameSort = SortMode.NONE;

	private SortMode acronymSort = SortMode.NONE;

	private SortMode budgetSort = SortMode.NONE;
}

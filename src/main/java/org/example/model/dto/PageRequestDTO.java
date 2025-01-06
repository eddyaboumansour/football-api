package org.example.model.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageRequestDTO {
	@Min(0)
	@Builder.Default
	private int page = 0;
	
	@Min(1)
	@Builder.Default
	private int pageSize = 10;
}

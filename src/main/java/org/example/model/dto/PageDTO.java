package org.example.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Generic type for paginated query result
 * @param <T> Page element type
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO<T> {
	private List<T> items;
	
	private int pageSize;
	
	private int page;
	
	private int pageCount;
	
	/**
	 * Apply transformation to page items
	 * @param transformation Transformation function
	 * @return PageDTO of new type
	 * @param <R> Type of transformed data
	 */
	public <R> PageDTO<R> transform(Function<T, R> transformation) {
		return PageDTO.<R>builder()
				.items(items.stream().map(transformation).collect(Collectors.toList()))
				.pageSize(pageSize)
				.page(page)
				.pageCount(pageCount)
				.build();
	}
}

package org.example.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@RequiredArgsConstructor
public class TeamNotFoundException extends RuntimeException {
	private final long id;
	
	@Override
	public String toString() {
		return String.format("Team with id=%d not found", id);
	}
}

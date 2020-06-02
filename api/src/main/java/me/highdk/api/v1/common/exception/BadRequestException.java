package me.highdk.api.v1.common.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.Errors;

import lombok.Getter;
import me.highdk.api.v1.error.ErrorDto;

public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 1570811344124646960L;
	
	@Getter
	private List<ErrorDto> errors;
	
	public BadRequestException(Errors errors) {
		super();
		this.errors = errors.getFieldErrors().stream().map(error -> new ErrorDto(error)).collect(Collectors.toList());
	}
	
}

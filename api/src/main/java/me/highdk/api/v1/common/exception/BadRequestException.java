package me.highdk.api.v1.common.exception;

import org.springframework.validation.Errors;

import lombok.Getter;

public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 1570811344124646960L;
	
	@Getter
	private Errors errors;
	
	public BadRequestException(Errors errors) {
		super();
		this.errors = errors;
	}
	
}

package me.highdk.api.v1.common.exception;

import java.util.Map;

import lombok.Getter;

public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 1570811344124646960L;
	
	@Getter
	private Map<String, String> error;
	
	public BadRequestException(Map<String, String> error) {
		super();
		this.error = error;
	}
	
}

package me.highdk.api.v1.common.exception;

import lombok.Getter;

public class NotFoundException extends RuntimeException{

	private static final long serialVersionUID = 909159879639764311L;
	
	@Getter
	private Long id;
	
	public NotFoundException(Long id) {
		super();
		this.id = id;
	}
	
}

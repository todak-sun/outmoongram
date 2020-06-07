package me.highdk.api.v1.post;

import org.springframework.validation.Errors;

import me.highdk.api.v1.common.exception.BadRequestException;

public class PostBadRequestException extends BadRequestException{

	private static final long serialVersionUID = -1161756050524778501L;

	public PostBadRequestException(Errors errors) {
		super(errors);
	}

}

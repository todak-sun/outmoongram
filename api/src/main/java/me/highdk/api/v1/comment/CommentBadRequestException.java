package me.highdk.api.v1.comment;

import org.springframework.validation.Errors;

import me.highdk.api.v1.common.exception.BadRequestException;

public class CommentBadRequestException extends BadRequestException {

	private static final long serialVersionUID = 5599606539419429388L;

	public CommentBadRequestException(Errors errors) {
		super(errors);
	}

}

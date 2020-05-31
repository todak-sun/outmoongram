package me.highdk.api.v1.comment;

import java.util.Map;

import me.highdk.api.v1.common.exception.BadRequestException;

public class CommentBadRequestException extends BadRequestException {

	private static final long serialVersionUID = 5599606539419429388L;

	public CommentBadRequestException(Map<String, String> error) {
		super(error);
	}

}

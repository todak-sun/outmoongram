package me.highdk.api.v1.comment;

import me.highdk.api.v1.common.exception.NotFoundException;

public class CommentNotFoundException extends NotFoundException {

	public CommentNotFoundException(Long id) {
		super(id);
	}

}

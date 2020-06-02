package me.highdk.api.v1.comment;

import lombok.Getter;
import me.highdk.api.v1.common.exception.NotFoundException;
import me.highdk.api.v1.error.ErrorDto;

public class CommentNotFoundException extends NotFoundException {
	
	private static final long serialVersionUID = 4252756064742917859L;
	
	@Getter
	private ErrorDto error;
	
	public CommentNotFoundException(Long id) {
		super(id);
		error = ErrorDto.builder()
						.name("Not exist")
						.message("해당 Comment는 존재하지 않습니다.")
						.rejectedValue(id)
						.build();
	}

}

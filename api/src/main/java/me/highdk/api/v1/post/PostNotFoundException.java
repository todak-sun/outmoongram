package me.highdk.api.v1.post;

import lombok.Getter;
import me.highdk.api.v1.common.exception.NotFoundException;
import me.highdk.api.v1.error.ErrorDto;

public class PostNotFoundException extends NotFoundException {

//	TODO: 이게 뭔지 질문..
	private static final long serialVersionUID = 4252756064742917859L;
	
	@Getter
	private ErrorDto error;
	
	public PostNotFoundException(Long id) {
		super(id);
		error = ErrorDto.builder()
				.name("Not Exist")
				.message("해당 Post는 존재하지 않습니다.")
				.rejectedValue(id)
				.build();
	}
	
}

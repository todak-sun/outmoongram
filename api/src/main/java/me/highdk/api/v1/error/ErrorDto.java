package me.highdk.api.v1.error;

import java.time.LocalDateTime;

import org.springframework.validation.FieldError;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.highdk.api.v1.comment.Comment;


@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {
	public ErrorDto(FieldError error) {
		this.name = error.getField();
		this.message = error.getDefaultMessage();
		this.rejectedValue = error.getRejectedValue();
	}
	
	private String name;
	
	private String message;
	
	private Object rejectedValue;
}

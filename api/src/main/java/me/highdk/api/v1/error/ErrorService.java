package me.highdk.api.v1.error;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import me.highdk.api.v1.index.IndexController;

@Service
public class ErrorService {
	
	public CollectionModel<ErrorResponse> toResponse(Errors errors) {
		List<ErrorDto> errorDtos = this.parse(errors.getFieldErrors());
		
		List<ErrorResponse> response = errors.getFieldErrors().stream().map(error -> {
			return ErrorResponse.builder().message(error.getDefaultMessage())
									 .name(error.getField())
									 .rejectedValue(error.getRejectedValue()).build();
		}).collect(Collectors.toList());
		Link indexLink = linkTo(IndexController.class).withRel("index");
		return CollectionModel.of(response, indexLink);
	}
	
	public EntityModel<ErrorResponse> toResponse(String name, String message, Object rejectedValue) {
		ErrorResponse response = ErrorResponse.builder()
										 .name(name)
										 .message(message)
										 .rejectedValue(rejectedValue)
										 .build();
		Link indexLink = linkTo(IndexController.class).withRel("index");
		return EntityModel.of(response, indexLink);
	}
	

	
	public ErrorResponse toResponse(ErrorDto errorDto) {
		return ErrorResponse.builder()
							.name(errorDto.getName())
							.message(errorDto.getMessage())
							.rejectedValue(errorDto.getRejectedValue())
							.build();
	}
	
	public List<ErrorResponse> toResponse(List<ErrorDto> errorDtos){
		return errorDtos.stream().map(this::toResponse)
								 .collect(Collectors.toList());
	}
	
	private ErrorDto parse(FieldError error) {
		return new ErrorDto(error);
	}
	
	private List<ErrorDto> parse(List<FieldError> errors){		
		return errors.stream().map(error -> new ErrorDto(error))
							  .collect(Collectors.toList());
	}

}

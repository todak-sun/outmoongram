package me.highdk.api.v1.post;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import me.highdk.api.v1.error.ErrorResponse;
import me.highdk.api.v1.error.ErrorService;

@RestControllerAdvice
public class PostControllerAdvice {
	
	private final ErrorService errorService;

	public PostControllerAdvice(ErrorService errorService) {
		this.errorService = errorService;
	}

	@ExceptionHandler(PostBadRequestException.class)
	public ResponseEntity<?> badRequest400(PostBadRequestException exception) {
		CollectionModel<ErrorResponse> resource = errorService.badReqeust400(exception.getErrors());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							 .body(resource);
	}
	
	@ExceptionHandler(PostNotFoundException.class)
	public ResponseEntity<?> notFound404(PostNotFoundException exception){
		EntityModel<ErrorResponse> resource = errorService.notFound404(exception.getError());
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
							  .body(resource);
	}
}

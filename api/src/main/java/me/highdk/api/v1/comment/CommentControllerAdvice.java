package me.highdk.api.v1.comment;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import me.highdk.api.v1.error.ErrorResponse;
import me.highdk.api.v1.error.ErrorService;

@RestControllerAdvice
public class CommentControllerAdvice {

	private final ErrorService errorService;

	public CommentControllerAdvice(ErrorService errorService) {
		this.errorService = errorService;
	}

	@ExceptionHandler(CommentBadRequestException.class)
	public ResponseEntity<?> badRequest400(CommentBadRequestException exception) {
		CollectionModel<ErrorResponse> resource = errorService.badReqeust400(exception.getErrors());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							 .body(resource);
	}
	
	@ExceptionHandler(CommentNotFoundException.class)
	public ResponseEntity<?> notFound404(CommentNotFoundException exception){
		EntityModel<ErrorResponse> resource = errorService.notFound404(exception.getError());
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
							  .body(resource);
	}
}

package me.highdk.api.v1.comment;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import me.highdk.api.v1.error.ErrorResponse;
import me.highdk.api.v1.error.ErrorService;
import me.highdk.api.v1.index.IndexController;

@RestControllerAdvice
public class CommentControllerAdvice {

	private final ErrorService errorService;

	public CommentControllerAdvice(ErrorService errorService) {
		this.errorService = errorService;
	}

	@ExceptionHandler(CommentBadRequestException.class)
	public ResponseEntity<?> badRequest(CommentBadRequestException exception) {
		List<ErrorResponse> errors =  errorService.toResponse(exception.getErrors());
		CollectionModel<ErrorResponse> resource = CollectionModel.of(errors);
		resource.add(linkTo(IndexController.class).withRel("index"));
		return ResponseEntity.badRequest().body(resource);
	}
}

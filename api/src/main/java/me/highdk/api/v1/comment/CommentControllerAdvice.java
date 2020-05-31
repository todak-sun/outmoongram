package me.highdk.api.v1.comment;

import java.util.Map;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommentControllerAdvice {
	
	@ExceptionHandler(CommentBadRequestException.class)
	public ResponseEntity<?> badRequest(CommentBadRequestException exception){
		EntityModel<Map<String,String>> resource = EntityModel.of(exception.getError());
//		resource.add(linkTo)
		return ResponseEntity
				.badRequest()
				.body(resource);
	}
}

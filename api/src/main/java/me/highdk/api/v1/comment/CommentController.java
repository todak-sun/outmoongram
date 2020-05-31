package me.highdk.api.v1.comment;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/comments")
public class CommentController {

	private final CommentService commentService;
	
	@Autowired
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<?> createComment(
			@Valid @RequestBody CommentRequest request,
			Errors errors
		) {
		
		if(errors.hasErrors()) {
			Map<String, String> error = commentService.doIfHavingErrors(errors);
			throw new CommentBadRequestException(error);
		}
		
		EntityModel<CommentResponse> resource = commentService.create(request);
		URI createdUri = linkTo(CommentController.class).slash(resource.getContent().getId()).toUri();
		return ResponseEntity
				.created(createdUri)
				.body(resource);
	}

}

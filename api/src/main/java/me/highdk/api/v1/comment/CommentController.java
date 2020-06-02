package me.highdk.api.v1.comment;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import me.highdk.api.v1.common.PageDto;

@Slf4j
@RestController
@RequestMapping(value = "/v1/api/comments", produces = MediaTypes.HAL_JSON_VALUE)
public class CommentController {

	private final CommentService commentService;
	
	@Autowired
	public CommentController(CommentService commentService) {
		this.commentService = commentService;		
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createComment(
			@Valid @RequestBody CommentRequest request,
			Errors errors ) {
		
		if(errors.hasErrors()) {
			throw new CommentBadRequestException(errors);
		}
		
		EntityModel<CommentResponse> resource = commentService.create(request);
		URI createdUri = linkTo(CommentController.class).slash(resource.getContent().getId()).toUri();
		return ResponseEntity
				.created(createdUri)
				.body(resource);
	}
	
	//TODO: SYJ, 페이징 다시 해야함 ㅠㅠㅠ...
	@GetMapping
	public ResponseEntity<?> readWithPaged(
								@RequestParam(required = true)  Long postId, 
								PageDto pageDto){
		log.info("/v1/api/comments");
		log.debug("Post Id is : {}, Page Information : {}",postId, pageDto);
		
		PagedModel<CommentResponse> resource = commentService.readPaged(postId, pageDto);
		
		return ResponseEntity.status(HttpStatus.OK)
							 .body(resource);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> readOne(@PathVariable Long id){
		log.info("id : {}", id);
		
		EntityModel<CommentResponse> resource = commentService.readOne(id);
		return ResponseEntity.status(HttpStatus.OK)
							 .body(resource);
	}
	
}

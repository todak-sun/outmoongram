package me.highdk.api.v1.post;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import me.highdk.api.v1.comment.CommentResponse;
import me.highdk.api.v1.comment.CommentService;
import me.highdk.api.v1.common.PageDto;

@RestController
@Slf4j
@RequestMapping(value = "/v1/api/posts")
public class PostController {
	
	private final PostService postService;
	
	private final CommentService commentService;
	
//	생성자 D.I
	@Autowired
	public PostController(PostService postService,
						  CommentService commentService) {
		this.postService = postService;
		this.commentService = commentService;
	}
	
	//TODO: HJH; validation & exception 처리할 것
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
				 produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<?> createPost(@RequestBody @Valid PostRequest postRequest,
										Errors errors) {
		
		log.info("/v1/api/post/");
		
		if(errors.hasErrors()) {
			throw new PostBadRequestException(errors);
		}
		
		EntityModel<PostResponse> resource = postService.create(postRequest);
		
		URI createdUri = linkTo(PostController.class).slash(resource.getContent().getId()).toUri();
		
		return ResponseEntity
				.created(createdUri)
				.body(resource);
	}
	
	@GetMapping
	public ResponseEntity<?> readWithPaged(PageDto pageDto){
		
		PagedModel<EntityModel<PostResponse>> resource = postService.readPaged(pageDto);
		
		return ResponseEntity.status(HttpStatus.OK)
							.body(resource);
	}
	
	@GetMapping(value = "/{postId}")
	public ResponseEntity<?> readOne(@PathVariable("postId") Long postId){
		
		log.info("/v1/api/post/" + postId);
		
		EntityModel<PostResponse> resource =  postService.readOne(postId);
		
		return ResponseEntity.status(HttpStatus.OK)
							.body(resource);
	}
	
	@GetMapping(value = "/{postId}/comments")
	public ResponseEntity<?> readCommentsByPostId( @PathVariable("postId") Long postId,
												   PageDto pageDto ){
		PagedModel<CommentResponse> resource = commentService.readPaged(postId, pageDto);
		return ResponseEntity.status(HttpStatus.OK)
							 .body(resource);
	}	
	
	
	
}

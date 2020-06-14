package me.highdk.api.v1.post;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import me.highdk.api.v1.comment.CommentResponse;
import me.highdk.api.v1.comment.CommentService;
import me.highdk.api.v1.common.PageDto;
import me.highdk.api.v1.hashtag.HashtagService;

@RestController
@Slf4j
@RequestMapping(value = "/v1/api/posts")
public class PostController {
	
	private final PostService postService;
	
	private final CommentService commentService;
	
	private final HashtagService hashtagService;
	
	@Autowired
	public PostController(PostService postService,
						  CommentService commentService,
						  HashtagService hashtagService) {
		this.postService = postService;
		this.commentService = commentService;
		this.hashtagService = hashtagService;
	}
	
	//TODO: HJH; validation & exception 처리할 것
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
				 produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<?> createPost(@RequestBody @Valid PostRequest postRequest,
										Errors errors) {
		if(errors.hasErrors()) {
			throw new PostBadRequestException(errors);
		}
		
		EntityModel<PostResponse> resource = postService.create(postRequest);
		hashtagService.taggingPost(resource.getContent().getContent(), resource.getContent().getId());
		
		URI createdUri = linkTo(PostController.class).slash(resource.getContent().getId()).toUri();
		
		return ResponseEntity
				.created(createdUri)
				.body(resource);
	}
	
	@GetMapping(value = "/{postId}",
				produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<?> readOne(@PathVariable("postId") Long postId ){
		EntityModel<PostResponse> resource =  postService.readOne(postId);
		
		return ResponseEntity.status(HttpStatus.OK)
							.body(resource);
	}
	
	@GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<?> readWithPaged( PageDto pageDto ){
		//TODO: HJH; comments 2개씩 담아주는 로직 필요
		PagedModel<PostResponse> resource = postService.readPaged(pageDto);
		
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
	
	@PutMapping(value = "/{postId}",
				consumes = MediaType.APPLICATION_JSON_VALUE,
				produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<?> updateOne(@PathVariable("postId") Long postId,
										@RequestBody @Valid PostRequest postRequest){
		EntityModel<PostResponse> resource = postService.updateOne(postId, postRequest);
		return ResponseEntity.status(HttpStatus.OK)
							.body(resource);
	}
	
	//HTTP 'DELETE' Method를 통해 Flag처리 삭제
	@DeleteMapping(value = "/{postId}")
	public ResponseEntity<?> deleteByFlag(@PathVariable("postId") Long postId){
		
		Map<String, String> messageMap = postService.deleteByFlag(postId);
		return ResponseEntity.status(HttpStatus.OK)
							.body(messageMap);
	}
	
	//HTTP 'PUT' Method를 통해 실제 데이터 삭제
	@PostMapping(value = "/{postId}")
	public ResponseEntity<?> deleteForReal(){
		
		return null;
	}
	
	
	
}

package me.highdk.api.v1.post;

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
@RequestMapping(value = "/v1/api/posts")
public class PostController {
	
	private final PostService postService;
	
	@Autowired
	public PostController(PostService postService) {
		this.postService = postService;
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<?> createPost(@Valid @RequestBody PostRequest postRequest, Errors errors) {
		
		if(errors.hasErrors()) {
			Map<String, String> errorMap = postService.doIfHavingErrors(errors);
//			throw new PostBadRequestException(errorMap);
		}
		
		EntityModel<PostResponse> resource = postService.createPost(postRequest);
		
		URI createdUri = linkTo(PostController.class).slash(resource.getContent().getId()).toUri();
		
		return ResponseEntity
				.created(createdUri)
				.body(resource);
	}
	
	
	
}

package me.highdk.api.v1.index;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.highdk.api.v1.comment.CommentController;
import me.highdk.api.v1.post.PostController;

@RestController
@RequestMapping("/v1/api")
public class IndexController {
	
	@GetMapping
	public ResponseEntity<?> index(){
		Link postsLink = linkTo(PostController.class).withRel("posts");
		Link commentsLink = linkTo(CommentController.class).withRel("comments");
		var resource = EntityModel.of("Welcome Outmoongram API. Please see the below links.",
						postsLink, commentsLink);
		return ResponseEntity.status(HttpStatus.OK)
							 .body(resource);
	}
	
}

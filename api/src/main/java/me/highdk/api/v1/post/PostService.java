package me.highdk.api.v1.post;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PostService {

	private final PostRepository postRepository;
	
	@Autowired
	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	public EntityModel<PostResponse> createPost(PostRequest postRequest) {
		
		Post newPost = parseTo(postRequest);
		Post savedPost = postRepository.save(newPost);
		
//		Long id = savedPost.getId();
//		String content = savedPost.getContent();
//		LocalDateTime updatedAt = savedPost.getUpdatedAt();
//		LocalDateTime writtenAt = savedPost.getWrittenAt();
//		Integer likeCnt = savedPost.getLikeCnt();
//		Integer commentCnt = savedPost.getCommentCnt();
//		Long writerId = savedPost.getWriterId();
//		
//		PostResponse response = PostResponse.builder()
//					.id(id)
//					.content(content)
//					.updatedAt(updatedAt)
//					.writtenAt(writtenAt)
//					.likeCnt(likeCnt)
//					.commentCnt(commentCnt)
//					.writerId(writerId)
//					.build();
		PostResponse postResponse = this.parseTo(savedPost);
		
		EntityModel<PostResponse> resource = EntityModel.of(postResponse);
		resource.add(linkTo(PostController.class).slash(postResponse.getId()).withSelfRel());
		return resource;
	}
	
	public Map<String, String> doIfHavingErrors(Errors errors){

		Map<String, String> errorMap = new HashMap<>();

		List<FieldError> fieldErrors = errors.getFieldErrors();
		
		log.info("===> fieldErros : " + fieldErrors);
		
		for(FieldError error : fieldErrors) {
			errorMap.put(error.getField(), error.getDefaultMessage());
		}
		
		log.info("===> Map<> errorMap : " + errorMap);
		
		return errorMap;
	}

	private Post parseTo(PostRequest postRequest) {
		return Post.builder()
					.content(postRequest.getContent())
					.writerId(postRequest.getWriterId())
					.build();
	}
	
	private PostResponse parseTo(Post post) {
		return PostResponse.builder()
				.id(post.getId())
				.content(post.getContent())
				.writtenAt(post.getWrittenAt())
				.updatedAt(post.getUpdatedAt())
				.likeCnt(post.getLikeCnt())
				.commentCnt(post.getCommentCnt())
				.writerId(post.getWriterId())
				.build();
	}

}

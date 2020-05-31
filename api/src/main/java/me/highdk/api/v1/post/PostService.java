package me.highdk.api.v1.post;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PostService {

	private final PostRepository postRepository;
	
	@Autowired
	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	public EntityModel<PostResponse> createPost(PostRequest request) {
		log.info("request : {}", request);
		Post newPost = parseTo(request);
		log.info("newPost : {}", newPost);
		Post savedPost = postRepository.save(newPost);
		
		Long id = savedPost.getId();
		String content = savedPost.getContent();
		LocalDateTime updatedAt = savedPost.getUpdatedAt();
		LocalDateTime writtenAt = savedPost.getWrittenAt();
		Integer likeCnt = savedPost.getLikeCnt();
		Integer commentCnt = savedPost.getCommentCnt();
		Long writerId = savedPost.getWriterId();
		
		PostResponse response = PostResponse.builder()
					.id(id)
					.content(content)
					.updatedAt(updatedAt)
					.writtenAt(writtenAt)
					.likeCnt(likeCnt)
					.commentCnt(commentCnt)
					.writerId(writerId)
					.build();
		
		EntityModel<PostResponse> resource = EntityModel.of(response);
		resource.add(linkTo(PostController.class).slash(response.getId()).withSelfRel());
		return resource;
	}

	private Post parseTo(PostRequest request) {
		return Post.builder()
					.content(request.getContent())
					.writerId(request.getWriterId())
					.build();
	}

}

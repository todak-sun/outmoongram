package me.highdk.api.v1.post;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import me.highdk.api.v1.common.OutmoonDetailService;
import me.highdk.api.v1.common.PageDto;

@Service
@Slf4j
public class PostService implements OutmoonDetailService<Post, PostRequest, PostResponse>{

	private final PostRepository postRepository;
	
	@Autowired
	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	@Override
	public EntityModel<PostResponse> create(PostRequest request) {
		
		Post newPost = this.toEntity(request);
		
		Post savedPost = postRepository.save(newPost);
		
		EntityModel<PostResponse> resource = this.toResource(savedPost);
//		var resource = this.toResource(savedPost);
		//TODO: HJH; var에 대해 다시 찾아볼 것

//		생성된 data 조회에 대한 link
		resource.add(linkTo(PostController.class).slash(savedPost.getId()).withSelfRel());
		
		return resource;
	}	

	public EntityModel<PostResponse> readOne(Long postId){
		return postRepository.findById(postId)
				.map(post ->{
					//TODO: HJH; methodOn API 확인
					var resource = this.toResource(this.toResponse(post));
					resource.add(linkTo(methodOn(PostController.class).readOne(postId)).withSelfRel());
					return resource;
				})
				.orElseThrow(() ->{
					log.info("NotFoundException: {}", postId);
					throw new PostNotFoundException(postId);
				});
		
	}
	
	public PagedModel<EntityModel<PostResponse>> readPaged(PageDto pageDto){
		List<Post> posts = postRepository.findAll(pageDto); 
		
//		List<PostResponse> postResponses = this.toResponse(posts);
		
		List<EntityModel<PostResponse>> list =  posts.stream()
				.map(postResponse -> {
					
					EntityModel<PostResponse> entityModel =  this.toResource(postResponse);
					Link selfLink = linkTo(methodOn(PostController.class).readOne(postResponse.getId())).withSelfRel();
					
					entityModel.add(selfLink);
					return entityModel;
				}).collect(Collectors.toList());
		
		log.info("===> list " + list);

		Long totalElements = postRepository.countTotal();
		
		PageMetadata pageMetadata = new PageMetadata(list.size(), pageDto.getPage(), totalElements);
		
		PagedModel<EntityModel<PostResponse>> resource =  PagedModel.of(list, pageMetadata);
		
		return resource;
	}
	
	
	
	
//	functional Methods..----------------------------------------------------------
	
	@Override
//	req DTO -> entity DTO
	public Post toEntity(PostRequest request) {
		return Post.builder()
				.writerId(request.getWriterId())
				.content(request.getContent())
				.build();
	}

	@Override
//	entity DTO -> res DTO	단일
	public PostResponse toResponse(Post post) {
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

	@Override
//	entity DTO -> res DTO	여러개
	public List<PostResponse> toResponse(List<Post> posts) {
		return posts.stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}

//	res DTO -> EntityModel wrapping
	public EntityModel<PostResponse> toResource(Post post){
		return this.toResource(this.toResponse(post));
	}

	

}

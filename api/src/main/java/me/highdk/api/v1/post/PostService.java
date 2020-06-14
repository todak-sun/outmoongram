package me.highdk.api.v1.post;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import me.highdk.api.v1.comment.CommentService;
import me.highdk.api.v1.common.OutmoonDetailService;
import me.highdk.api.v1.common.PageDto;
import me.highdk.api.v1.common.enums.FetchType;
import me.highdk.api.v1.image.ImageService;
import me.highdk.api.v1.user.User;
import me.highdk.api.v1.user.UserResponse;

@Service
@Slf4j
public class PostService implements OutmoonDetailService<Post, PostRequest, PostResponse>{

	private final PostRepository postRepository;
	
	private final CommentService commentService;
	
	private final ImageService imageService;
	
	@Autowired
	public PostService(PostRepository postRepository, 
					   CommentService commentService, 
					   ImageService imageService) {
		this.postRepository = postRepository;
		this.commentService = commentService;
		this.imageService = imageService;
	}

	@Override
	public EntityModel<PostResponse> create(PostRequest request) {
		
		Post newPost = this.toEntity(request);
		
		Post savedPost = postRepository.save(newPost);
		
		EntityModel<PostResponse> resource = this.toResource(savedPost);

		resource.add(linkTo(PostController.class).slash(savedPost.getId()).withSelfRel());
		
		return resource;
	}	

	public EntityModel<PostResponse> readOne(Long postId){
		return postRepository.findById(postId)
				.map(post ->{
					var resource = this.toResource(this.toResponse(post));
					resource.add(linkTo(methodOn(PostController.class).readOne(postId)).withSelfRel());
					return resource;
				})
				.orElseThrow(() ->{
					log.info("NotFoundException: {}", postId);
					throw new PostNotFoundException(postId);
				});
		
	}
	
	public PagedModel<PostResponse> readPaged(PageDto pageDto){
		
		
		log.info("pageDto : {}, fetchType : {}", pageDto, pageDto.getFetchType());
		List<Post> posts = null;
		
		if(pageDto.getFetchType().equals(FetchType.FULL)){
			posts = postRepository.findAllWithFullResource(pageDto);
		} else if (pageDto.getFetchType().equals(FetchType.ORIGIN)) {
			posts = postRepository.findAll(pageDto);
		}
		
		Long totalElements = postRepository.countTotal();

		List<PostResponse> list = this.toResponse(posts);
	
		PageMetadata pageMetadata = new PageMetadata(list.size(), pageDto.getPage(), totalElements);	
		PagedModel<PostResponse> resource =  PagedModel.of(list, pageMetadata);
		resource.add(linkTo(methodOn(PostController.class).readWithPaged(pageDto)).withSelfRel());
		
		return resource;
	}
	
	@Transactional
	public EntityModel<PostResponse> updateOne(Long postId, PostRequest postRequest){
//		postId가 있는지 먼저 확인,, 후에 있다면 map 실행, 없다면 PostNotFoundExeption 던지기
		return postRepository.findById(postId)
				.map(post -> {
					post.setContent(postRequest.getContent());
			
					EntityModel<PostResponse> resource = this.toResource(postRepository.updateOne(post));
					resource.add(linkTo(methodOn(PostController.class).readOne(postId)).withSelfRel());
			
					return resource;
			
				}).orElseThrow(() ->{
					throw new PostNotFoundException(postId);
				});
	}
	
	public Map<String, String> deleteByFlag(Long postId) {
		return postRepository.findById(postId)
					.map(post -> {
						int deletedResult = postRepository.deleteByFlag(postId);
						Map<String, String> messageMap = new HashMap<>();
						messageMap.put("message", postId + "번 게시물이 성공적으로 삭제되었습니다.");
						
						return messageMap;
					}).orElseThrow(() ->{
						throw new PostNotFoundException(postId);
					});
	}
	
	
	/**
	 * 임시로 만든 메서드
	 * 나중에 옮겨야 함.
	 * */
	public UserResponse toResponse(User user) {
		return Optional.ofNullable(user).map(u -> UserResponse.builder()
															  .id(u.getId())
															  .nickName(u.getNickName())
															  .userName(u.getUserName())
															  .registeredAt(u.getRegisteredAt())
															  .updatedAt(u.getUpdatedAt())
															  .build())
										.orElseGet(() -> null);
	}
	
	@Override
	public Post toEntity(PostRequest request) {
		return Post.builder()
				   .writerId(request.getWriterId())
				   .content(request.getContent())
				   .build();
	}

	@Override
	public PostResponse toResponse(Post post) {
		log.info("post : {}", post);
		return PostResponse.builder()
						    .id(post.getId())
							.content(post.getContent())
							.writtenAt(post.getWrittenAt())
							.updatedAt(post.getUpdatedAt())
							.likeCnt(post.getLikeCnt())
							.commentCnt(post.getCommentCnt())
							.writer(this.toResponse(post.getWriter()))
							.comments(commentService.toResponse(post.getComments()))
							.images(imageService.toResponse(post.getImages()))
							.build();
	}

	@Override
	public List<PostResponse> toResponse(List<Post> posts) {
		return posts.stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}

	
	public EntityModel<PostResponse> toResource(Post post){
		return this.toResource(this.toResponse(post));
	}

	

}

package me.highdk.api.v1.post;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	public PagedModel<PostResponse> readPaged(PageDto pageDto){
		List<Post> posts = postRepository.findAll(pageDto); 
		
//		List<PostResponse> postResponses = this.toResponse(posts);
		
		List<PostResponse> list = this.toResponse(posts);
		
		log.info("===> list " + list);

		Long totalElements = postRepository.countTotal();
		
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

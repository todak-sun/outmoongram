package me.highdk.api.v1.comment;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import me.highdk.api.v1.common.OutmoonDetailService;
import me.highdk.api.v1.common.PageDto;
import me.highdk.api.v1.index.IndexController;
import me.highdk.api.v1.post.PostController;
import me.highdk.api.v1.post.PostNotFoundException;
import me.highdk.api.v1.post.PostRepository;
import me.highdk.api.v1.user.UserResponse;
import me.highdk.api.v1.user.UserService;

@Slf4j
@Service
public class CommentService implements OutmoonDetailService<Comment, CommentRequest, CommentResponse>{
	
	private final PostRepository postRespository;
	
	private final CommentRepository commentRepository;
	
	private final UserService userService;
	
	@Autowired
	public CommentService(CommentRepository commentRepository,
						  PostRepository postRepository,
						  UserService userService) {
		this.commentRepository = commentRepository;
		this.postRespository = postRepository;
		this.userService = userService;
	}
	
	@Override
	@Transactional
	public EntityModel<CommentResponse> create(CommentRequest request) {
		Comment newComment = this.toEntity(request);
		Comment savedComment = commentRepository.save(newComment);
		
		var resource = this.toResource(savedComment);
		resource.add(linkTo(CommentController.class).slash(savedComment.getId()).withSelfRel());
		resource.add(linkTo(PostController.class).slash(savedComment.getPostId()).withRel("post"));
		
		return resource;
	}
	
	public EntityModel<CommentResponse> readOne(Long id) {
		return commentRepository.findById(id)
						 .map(comment -> {
							var resource = this.toResource(this.toResponse(comment));
							resource.add(linkTo(methodOn(CommentController.class).readOne(id)).withSelfRel());
							return resource;
						  })
						 .orElseThrow(() -> {
							 log.info("NotFoundException!!! : {}", id);
							 throw new CommentNotFoundException(id);
						  });
	}
	
	public PagedModel<CommentResponse> readPaged(Long postId, PageDto pageDto) {
		return postRespository.findById(postId)
							  .map(post -> {
									var comments = commentRepository.findByPostId(postId, pageDto);
									
									var responses = this.toResponse(comments);
									
									
									Long totalElements = commentRepository.countsByPostId(postId);
									
									PageMetadata metadata = new PageMetadata(comments.size(), pageDto.getPage(), totalElements);
									
									var resource = PagedModel.of(responses, metadata);
									
									//TODO: SYJ, @ReqeustParam 말고도 querystring 만드는 방법 찾아서 적용 할 것...
									Link indexLink = linkTo(methodOn(IndexController.class).index()).withRel("index");
									Link selfLink = linkTo(methodOn(PostController.class).readCommentsByPostId(postId, pageDto)).withSelfRel();
									Link nextLink = linkTo(methodOn(PostController.class).readCommentsByPostId(postId, new PageDto(pageDto.getStart() + pageDto.getSize(), pageDto.getSize()))).withRel("next").expand(pageDto);
									resource.add(indexLink);
									resource.add(selfLink);
									resource.add(nextLink);
									return resource;
								})
							    .orElseThrow(() -> {
							    	throw new PostNotFoundException(postId);
							    });
	}	
	
	@Transactional
	public EntityModel<CommentResponse> updateOne(Long id, CommentRequest request) {
		return commentRepository.findById(id)
									   .map(comment -> {
										   	comment.setContent(request.getContent());
											Comment updatedComment = commentRepository.save(comment);
											
											var resource = this.toResource(updatedComment);
											resource.add(linkTo(methodOn(CommentController.class).readOne(id)).withSelfRel());
										
											return resource;
									   })
									  .orElseThrow(() -> {
										  throw new CommentNotFoundException(id);
									   });
	}
	
	public EntityModel<CommentResponse> toResource(Comment comment){
		return this.toResource(this.toResponse(comment));
	}
	
	@Override
	public CommentResponse toResponse(Comment comment) {
		return CommentResponse.builder()
				.id(comment.getId())
				.parentId(comment.getParentId())
				.content(comment.getContent())
				.likeCnt(comment.getLikeCnt())
				.commentCnt(comment.getCommentCnt())
				.writtenAt(comment.getWrittenAt())
				.updatedAt(comment.getUpdatedAt())
				.postId(comment.getPostId())
				.writer(userService.toResponse(comment.getWriter()))
				.build();
	}

	@Override
	public Comment toEntity(CommentRequest request) {
		if(request.getParentId() == null) {
			// 자식 댓글로 생성된 댓글이 아닌 경우, parentId가 null이므로, 0값으로 교체해 줌.
			request.setParentId(0L);
		}
		
		return Comment.builder()
					  .parentId(request.getParentId())
					  .content(request.getContent())
					  .postId(request.getPostId())
					  .writerId(request.getWriterId())
					  .build();
	}

	@Override
	public List<CommentResponse> toResponse(List<Comment> comments) {
		return Optional.ofNullable(comments).map(cmts -> {
											/**
											 * 
											 *  postId에 해당하는 모든 댓글 중에서
											 *  자식 댓글이 아닌 모든 댓글을 담는다.
											 *  
											 * */
											log.info("cmts : {}", cmts);
											var parentComments = cmts.stream()
																	 .filter(cmt -> cmt.getParentId() == 0)
															   		 .collect(Collectors.toList());
											
											var responses = parentComments.stream()
																		  .map(this::toResponse)
																		  .collect(Collectors.toList());
											/**
											 * 
											 * parentComments만 담긴 응답용 객체를 순회하면서
											 * 자식 댓글을 응답용 객체로 변환하여 내부에 담는다. 
											 *  
											 * */
											responses.forEach(response -> {
												var childrenComments = cmts.stream()
																		   .filter(cmt -> response.getId() == cmt.getParentId())
														  				   .collect(Collectors.toList());
												response.setComments(this.toResponse(childrenComments));
											});
											
											return responses;
											})
											.orElseGet(() -> null);
		
	}

	public String deleteOne(Long id) {
		return commentRepository.findById(id).map(comment -> {
													/**
													 *  1. comment 내에 포함된 해시태그를 긁어와, 댓글과 관련한 해시태그 처리 => ?
													 *  2. parent 댓글인 경우, children 댓글 연쇄 삭제 => ?
													 *  3. post의 댓글 수 조정 => 트리거
													 *  4. 본인 삭제.
													 * */
													int affectedRowCount = commentRepository.deleteByParentId(id);
													affectedRowCount = commentRepository.deleteById(id);
													return "성공적으로 삭제되었습니다.";
											 })
											 .orElseThrow(() -> {
													throw new CommentNotFoundException(id);
											 });

	}
	
}

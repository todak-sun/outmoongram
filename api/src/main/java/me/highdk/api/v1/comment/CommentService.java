package me.highdk.api.v1.comment;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import me.highdk.api.v1.common.OutmoonDetailService;
import me.highdk.api.v1.common.PageDto;
import me.highdk.api.v1.post.PostController;

@Slf4j
@Service
public class CommentService implements OutmoonDetailService<Comment, CommentRequest, CommentResponse>{
	
	private final CommentRepository commentRepository;
	
	@Autowired
	public CommentService(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}
	
	@Override
	public EntityModel<CommentResponse> create(CommentRequest request) {
		Comment newComment = this.toEntity(request);
		Comment savedComment = commentRepository.save(newComment);
		CommentResponse response = this.toResponse(savedComment); 
		
		var resource = this.toResource(response);
		resource.add(linkTo(CommentController.class).slash(response.getId()).withSelfRel());
		resource.add(linkTo(PostController.class).slash(response.getPostId()).withRel("post"));
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
		List<Comment> comments = commentRepository.findByPostId(postId, pageDto);
		PageMetadata metadata = new PageMetadata(10L, 1L, 100L);
//		PagedModel.of(comments, metadata)
		return PagedModel.of(this.toResponse(comments), metadata);
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
				.writerId(comment.getWriterId())
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
		return comments.stream().map(this::toResponse)
								.collect(Collectors.toList());
	}

	
}

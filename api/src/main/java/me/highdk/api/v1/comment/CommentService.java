package me.highdk.api.v1.comment;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import me.highdk.api.v1.post.PostController;

@Service
public class CommentService {
	
	private final CommentRepository commentRepository;
	
	@Autowired
	public CommentService(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}
	
	
	public EntityModel<CommentResponse> create(CommentRequest request) {
		Comment newComment = this.parseTo(request);
		Comment savedComment = commentRepository.save(newComment);
		CommentResponse response = this.parseTo(savedComment); 
		
		EntityModel<CommentResponse> resource = EntityModel.of(response);
		resource.add(linkTo(CommentController.class).slash(response.getId()).withSelfRel());
		resource.add(linkTo(PostController.class).slash(response.getPostId()).withRel("post"));
		return resource;
	}
	
	private CommentResponse parseTo(Comment comment) {
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
	
	private Comment parseTo(CommentRequest request) {
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
	
	
}

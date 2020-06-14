package me.highdk.api.v1.comment;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.highdk.api.v1.user.UserResponse;

@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Relation(collectionRelation = "comments")
public class CommentResponse extends RepresentationModel<CommentResponse> {

	private Long id;

	private Long parentId;

	private String content;

	private LocalDateTime writtenAt;

	private LocalDateTime updatedAt;

	private Integer likeCnt;

	private Integer commentCnt;
	
	@JsonInclude(value = Include.NON_NULL)
	private Long postId;

	private UserResponse writer;
	
//	private Long writerId;
	
	
	@JsonInclude(Include.NON_EMPTY)
	private List<CommentResponse> comments;

}

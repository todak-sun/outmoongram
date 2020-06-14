package me.highdk.api.v1.post;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import me.highdk.api.v1.comment.CommentResponse;
import me.highdk.api.v1.image.ImageResponse;
import me.highdk.api.v1.user.UserResponse;

@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Relation(collectionRelation = "posts")
public class PostResponse extends RepresentationModel<PostResponse> {
	
	private Long id;

	private String content;

	private LocalDateTime writtenAt;

	private LocalDateTime updatedAt;

	private Integer likeCnt;

	private Integer commentCnt;
	
	@JsonInclude(value = Include.NON_NULL)
	private UserResponse writer;
	
	@JsonInclude(value = Include.NON_EMPTY)
	private List<CommentResponse> comments; 
	
	@JsonInclude(value = Include.NON_EMPTY)
	private List<ImageResponse> images;
	
	
}

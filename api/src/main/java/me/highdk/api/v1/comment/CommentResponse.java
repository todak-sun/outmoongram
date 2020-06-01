package me.highdk.api.v1.comment;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

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

	private Long postId;

	private Long writerId;

}

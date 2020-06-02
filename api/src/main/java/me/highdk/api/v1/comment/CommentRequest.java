package me.highdk.api.v1.comment;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.highdk.api.v1.common.RequestDto;

@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest extends RequestDto {
	
	private Long parentId;
	
	@NotNull(message = "내용이 필요합니다")
	private String content;
	
	@NotNull(message = "댓글이 게시될 게시물의 식별자가 필요합니다.")
	private Long postId;

	@NotNull(message = "작성자의 식별자가 필요합니다.")
	private Long writerId;
	
}

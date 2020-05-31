package me.highdk.api.v1.comment;

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

	private String content;

	private Long postId;

	private Long writerId;
	
}

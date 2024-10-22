package me.highdk.api.v1.post;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import me.highdk.api.v1.common.RequestDto;

@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostRequest extends RequestDto{
	
	@NotEmpty(message = "내용을 작성해주세요.")
	private String content;
	
	private Long writerId;
	
}

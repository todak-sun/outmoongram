package me.highdk.api.v1.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import me.highdk.api.vi.common.RequestDto;

@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostRequest extends RequestDto{
	
	private String content;
	
	private Long writerId;
	
	
}

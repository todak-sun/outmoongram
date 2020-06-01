package me.highdk.api.v1.post;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Post {

	private Long id;

	@NotEmpty
	private String content;

	private LocalDateTime writtenAt;

	private LocalDateTime updatedAt;

	private Integer likeCnt;

	private Integer commentCnt;

	@NotEmpty
	private Long writerId;

}

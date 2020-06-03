package me.highdk.api.v1.comment;

import java.time.LocalDateTime;

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
public class Comment {

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

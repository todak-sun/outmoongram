package me.highdk.api.v1.post;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import me.highdk.api.v1.comment.Comment;
import me.highdk.api.v1.image.Image;
import me.highdk.api.v1.user.User;

@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Post {

	private Long id;

	private String content;

	private LocalDateTime writtenAt;

	private LocalDateTime updatedAt;

	private Integer likeCnt;

	private Integer commentCnt;

	private Long writerId;
	
	private List<Comment> comments;
	
	private List<Image> images;
	
	private User writer;

}

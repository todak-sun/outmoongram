package me.highdk.api.boards;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@ToString
public class Board {

	private Long id;

	@Size(max=2000)
	@NotEmpty(message = "title을 입력하세요.")		//Type: CharSequence, Collection, Map :: null, null String 아니여야 한다.	
//	@NotNull									//Type: any Type 			:: null이 아닌 값이어야 한다.	
//	@NotBlank									// null이 아니여야 한다. 공백을 제외한 길이가 0보다 커야한다.	
	private String title;
	
	@NotEmpty(message = "content를 입력하세요.")
	private String content;

	private LocalDateTime writtenAt;

	private LocalDateTime updatedAt;

}

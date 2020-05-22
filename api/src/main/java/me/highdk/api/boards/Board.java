package me.highdk.api.boards;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@ToString
@Slf4j
public class Board {

	private Long id;
	
	private String title;

	private String content;

	private LocalDateTime writtenAt;

	private LocalDateTime updatedAt;

}

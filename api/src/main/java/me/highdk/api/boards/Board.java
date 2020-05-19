package me.highdk.api.boards;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.time.LocalDateTime;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Slf4j
public class Board {

	private Long id;

	private String title;

	private String content;

	private LocalDateTime writtenAt;

	private LocalDateTime updatedAt;

}

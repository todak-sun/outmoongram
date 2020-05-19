package me.highdk.api.boards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/api/boards")
@RestController
@Slf4j
public class BoardController {

	private final BoardService boardService;

	@Autowired
	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}

	@GetMapping(value = "", produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<?> readAll() {
		CollectionModel<EntityModel<Board>> resources = boardService.readAll();
		return ResponseEntity.status(HttpStatus.OK).body(resources);
	}

	@GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<?> read(@PathVariable Long id) {
		EntityModel<Board> resource = this.boardService.read(id);
		return ResponseEntity.status(HttpStatus.OK).body(resource);
	}

}

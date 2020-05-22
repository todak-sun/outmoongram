package me.highdk.api.boards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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


	@PostMapping(value = "", produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<?> create(@RequestBody Board board){
		
		EntityModel<Board> resource =  boardService.create(board);
		ResponseEntity<?> response =  ResponseEntity.status(HttpStatus.OK).body(resource);
		
		return response;
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
	
	@PutMapping(value="", produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody Board board){
		
		return ResponseEntity.status(HttpStatus.OK).body(boardService.update(board));
	}
	
	
//	무엇을 리턴할 지... 200 Ok 싸인으로 Delete되었다는 것을 알리고, 전이할 links를 담아서 보내줘야 이상
	@DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		
		
		EntityModel<Board> deleteResult = boardService.delete(id);
		
		return ResponseEntity.status(HttpStatus.OK).body(deleteResult);
	}

}

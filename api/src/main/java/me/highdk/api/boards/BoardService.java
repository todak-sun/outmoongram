package me.highdk.api.boards;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BoardService {

	private final BoardRepository boardRepository;

	@Autowired
	public BoardService(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}

	public Board create() {
		return null;
	}

	public void now() {
		Date date = this.boardRepository.now();
	}

	public CollectionModel<EntityModel<Board>> readAll() {
		
		List<EntityModel<Board>> boards = this.boardRepository.findAll()
															  .stream()
															  .map(this::toModel)
															  .collect(Collectors.toList());
		
		CollectionModel<EntityModel<Board>> collectionModel = CollectionModel.of(boards);
		collectionModel.add(linkTo(BoardController.class).withRel("list"));
		
		return collectionModel;
	}

	public EntityModel<Board> read(Long id) {
		return this.boardRepository.findById(id)
					.map(this::toModel)
					.orElseGet(() -> {
						return null;
					});
	}
	
	private EntityModel<Board> toModel(Board board){
		Link selfLink = linkTo(BoardController.class).slash(board.getId()).withSelfRel();
		return EntityModel.of(board, selfLink);
	}

}

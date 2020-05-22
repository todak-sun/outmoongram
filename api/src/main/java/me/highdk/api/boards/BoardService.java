package me.highdk.api.boards;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.Date;
import java.util.List;
import java.util.Optional;
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

	public EntityModel<Board> create(Board board) {
		
//		board에 id만 넣어주고, writtenAt, updatedAt을 넣어주지 않는다
//		따라서 id만 추출하고 그 id로 read()로 다시 가져온다..
		boardRepository.create(board);
		
		return boardRepository.findById(board.getId()).map(this::toModel)
												.orElseGet(() -> {
													return null;
													});
		
//		EntityModel<Board> returnBoard = this.read(board.getId());
		
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
		
//		Optional<Board> testBoard = boardRepository.findById(2L);
		
//		testBoard.map(board -> {
//			board.setContent("");
//			board.setId(0L);
//			return board;
//		}).orElseThrow(() -> {throw new NotFoundException()});
		
//		testBoard.get();
//		
//		if(testBoard.isEmpty()) {
//			throw new Error("Not found Exception");
//		}
		
		return this.boardRepository.findById(id)
					.map(this::toModel)	//각 인자들에게 this.toModel() 실행
					.orElseGet(() -> {
						return null;
					});
	}
	
	public EntityModel<Board> update(Board board){
		
		int updatedCnt = boardRepository.update(board);
		
		if(updatedCnt < 1) {
//			어떻게 처리할지 잘 모르겠어서 일단 Error 던진다
			throw new Error("update failed");
		};
		
		return boardRepository.findById(board.getId()).map(b->
												this.toModel(b)
												).orElseGet(null);
	}
	
	public EntityModel<Board> delete(Long id) {
		
//		문제가 있따 .....null을 넣으면 작동을 안한다.
		Board board = new Board();
		
//		delete 실패시 자기 자신 링크 다시 보냄..
		if(boardRepository.deleteById(id) < 1) {
			return EntityModel.of(board, linkTo(BoardController.class).slash(id).withSelfRel());
		}
		
//		delete 성공시 list 링크 담아서 보냄 
		return EntityModel.of(board, linkTo(BoardController.class).withRel("list"));
	}
	
	
//	fuctional Methods ......
	
//	content(Board)에 self link를 달아주는 method
	private EntityModel<Board> toModel(Board board){
		Link selfLink = linkTo(BoardController.class).slash(board.getId()).withSelfRel();
		return EntityModel.of(board, selfLink);
	}

	
	public void now() {
		Date date = this.boardRepository.now();
	}
}

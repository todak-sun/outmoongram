package me.highdk.api.boards;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class BoardServiceTest {

	@Autowired
	private BoardService boardService;

//	@Autowired
//	private BoardRepository boardRepository;

//	@Test
	public void createTest() {
		
		Board board = Board.builder()
					.title("title")
					.content("content")
					.build();
		
		EntityModel<Board> returnBoard =  boardService.create(board);
		
		log.info("returnBoard: "+ returnBoard);
		
		assertEquals(3, returnBoard.getContent().getId());
	}
	
	
//	@Test
	public void readTest() {
		EntityModel<Board> readResult = boardService.read(1L);
		
		log.info("result: "+ readResult );
		log.info("links: " + readResult.getLinks());
		
		assertEquals(1L, readResult.getContent().getId());
		
	}
	
//	@Test
	public void readAllTest() {
		CollectionModel<EntityModel<Board>> readAllresult =  boardService.readAll();
		
		log.info("readAllresult: "+ readAllresult);
		log.info(" links of  readAllresult: "+ readAllresult.getLinks("list"));
		readAllresult.getContent().stream()
						.forEach(con -> System.out.println(con));
	}
	
	
//	@Test
	public void updateTest() {
		Board board = Board.builder()
						.id(1L)
						.title("update Title")
						.content("updated Content")
						.build();
		log.info("updated Board : " + boardService.update(board));
	}
	
	@Test
	public void deleteTest() {
		
		log.info("delete Result :  " + boardService.delete(1L));
		log.info("delete Result :  " + boardService.delete(0L));
		
	}

}

package me.highdk.api.boards;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class BoardRepositoryTest {
	
	@Autowired
	private BoardRepository boardRepository;
	
//	@Test
	public void now() {
		Date date = boardRepository.now();
		assertNotNull(date);
	}
	
	@Test
	public void createTest() {
		
		
		Board board = new Board();
		board.setContent("test Content")
//			 .setId(1L)
			 .setTitle("title")
			 .setWrittenAt(LocalDateTime.now());
		
		
		log.info("boardRepository : "+ boardRepository.create(board));
		System.out.println(board.toString());
		
		
		Board board2 = Board.builder()
//				.id(1L)
				.content("content2")
				.title("title2")
				.writtenAt(LocalDateTime.now())
				.build();
		
		boardRepository.create(board2);
		System.out.println("==> "+board2.toString());
	}
	
//	@Test
	public void updateByIdTest() {
		
		Board board = Board.builder()
						.id(1L)
//						.id(4L)
						.title("updated title")
						.content("updated Content")
						.build();
		
		int updatedCnt =  boardRepository.update(board);
		
		log.info("udatedCnt : " + updatedCnt);
		log.info("udatedBoard : " + boardRepository.findById(1L));
		
	}
	
//	@Test
	public void deleteByIdTest() {
		
		int deletedCnt = boardRepository.deleteById(1L);
		
		log.info("deletedCnt: " + deletedCnt);
	}
	
}

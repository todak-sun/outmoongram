package me.highdk.api.boards;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BoardServiceTest {

	private BoardService boardService;

	@Autowired
	private BoardRepository boardRepository;

	

}

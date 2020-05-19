package me.highdk.api.boards;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BoardTest {

	@Test
	@DisplayName(value = "객체 생성")
	public void instance() {
		Board board = Board.builder().build();
		assertNotNull(board);
	}

}

package me.highdk.api.v1.common;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PageDtoTest {

	@Test
	@DisplayName(value = "기본 생성시 초기값을 들고 있는지 테스트.")
	public void init() {
		PageDto page = new PageDto();
		
		assertSame(page.getStart(), 0L);
		assertSame(page.getSize(), 10L);

		assertNull(page.getPrevious());
		assertSame(page.getNext(), 2L);
		assertSame(page.getPage(), 1L);
	}

}

package me.highdk.api.v1.common;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PageDtoTest {

	@Test
	@DisplayName(value = "기본 생성시 초기값을 들고 있는지 테스트.")
	public void test() {
		PageDto page = new PageDto();
		assertSame(page.getStart(), 0);
		assertSame(page.getSize(), 10);
	}

}

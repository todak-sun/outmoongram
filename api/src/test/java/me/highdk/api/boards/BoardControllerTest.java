package me.highdk.api.boards;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootTest
@ActiveProfiles("Test")
public class BoardControllerTest {

	@Autowired
	private WebApplicationContext ctx;

	private MockMvc mvc;

	@BeforeEach
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(ctx) // MockMvc의 컨텍스트 설정.
				.addFilters(new CharacterEncodingFilter("UTF-8", true)) // 한글 안깨지게.
				.alwaysDo(print()) // 실행 결과를 Console창에 출력.
				.build();
	}
	
	@Test
	@DisplayName(value = "생성")
	public void create() throws Exception{
		
	}
	
	@Test
	@DisplayName(value = "모두 가져오는 테스트")
	public void readAll() throws Exception {
		this.mvc.perform(get("/api/boards").contentType(MediaType.APPLICATION_JSON).accept(MediaTypes.HAL_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName(value = "아이디로 조회")
	public void readById() throws Exception {
		this.mvc.perform(get("/api/boards/1").contentType(MediaType.APPLICATION_JSON).accept(MediaTypes.HAL_JSON))
				.andExpect(status().isOk());
	}

}

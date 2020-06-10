package me.highdk.api.v1.hashtag;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class HashtagControllerTest {

	@Autowired
	private WebApplicationContext ctx;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private MockMvc mvc;
	
	@BeforeEach
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(ctx) // MockMvc의 컨텍스트 설정.
				.addFilters(new CharacterEncodingFilter("UTF-8", true)) // 한글 안깨지게.
				.alwaysDo(print()) // 실행 결과를 Console창에 출력.
				.build();
	}
	
	
	@Test
	@DisplayName(value = "키워드로 받아오는 태그들 테스트")
	public void findAllTagsBykeywordTest()throws Exception{
		this.mvc.perform(get("/v1/api/hashtags")
				.param("keyword", "su")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaTypes.HAL_JSON))
				.andExpect(status().isOk());
	}
}

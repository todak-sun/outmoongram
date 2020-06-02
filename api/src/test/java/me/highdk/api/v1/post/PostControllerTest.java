package me.highdk.api.v1.post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class PostControllerTest {

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
	@DisplayName(value="Create 테스트")
	public void createTest() throws Exception {
		
		PostRequest request = PostRequest.builder()
				.content("안녕하세요")
				.writerId(1L)
				.build();
		
		String str = objectMapper.writeValueAsString(request);
		
		log.info("str : {}", str);
		
		this.mvc.perform(post("/v1/api/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(str))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"));
		
		PostRequest request2 = PostRequest.builder()
				.content("내용입니다")
				.build();
		
		String str2 = objectMapper.writeValueAsString(request2);
		
		this.mvc.perform(post("/v1/api/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(str2))
				.andExpect(status().isBadRequest());
	}

}

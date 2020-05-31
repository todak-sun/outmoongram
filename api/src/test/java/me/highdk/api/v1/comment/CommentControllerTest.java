package me.highdk.api.v1.comment;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class CommentControllerTest {

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
	@DisplayName(value="성공적인 요청을 했을 때의 테스트.")
	public void create_success() throws Exception {
		
		CommentRequest request = CommentRequest.builder()
				.parentId(0L)
				.content("댓글 테스트입니다...")
				.postId(1L)
				.writerId(1L)
				.build();
		
		this.mvc.perform(post("/v1/api/comments")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, containsString(MediaTypes.HAL_JSON_VALUE)))
				.andExpect(jsonPath("id").exists())
				.andExpect(jsonPath("parentId").exists())
				.andExpect(jsonPath("content").exists())
				.andExpect(jsonPath("writtenAt").exists())
				.andExpect(jsonPath("updatedAt").exists())
				.andExpect(jsonPath("likeCnt").exists())
				.andExpect(jsonPath("commentCnt").exists())
				.andExpect(jsonPath("postId").exists())
				.andExpect(jsonPath("postId", is(request.getPostId())))
				.andExpect(jsonPath("writerId").exists())
				.andExpect(jsonPath("writerId", is(request.getWriterId())))
				.andExpect(jsonPath("_links").exists())
				.andExpect(jsonPath("_links.self").exists())
				.andExpect(jsonPath("_links.self.href").exists())
				.andExpect(jsonPath("_links.post").exists())
				.andExpect(jsonPath("_links.post.href").exists())
		;
		
	}

}

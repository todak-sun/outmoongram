package me.highdk.api.boards;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@ActiveProfiles("Test")
@Slf4j
public class BoardControllerTest {

	@Autowired
	private WebApplicationContext ctx;

	private MockMvc mvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(ctx) // MockMvc의 컨텍스트 설정.
				.addFilters(new CharacterEncodingFilter("UTF-8", true)) // 한글 안깨지게.
				.alwaysDo(print()) // 실행 결과를 Console창에 출력.
				.build();
	}
	
//	@Test
	@DisplayName(value = "생성")
	public void create() throws Exception{
		
		Board board = Board.builder()
				.title("test Title")
				.content("Controller Test content")
				.build();
		
		String content = objectMapper.writeValueAsString(board);
		
		this.mvc.perform(MockMvcRequestBuilders.post("/api/boards").contentType(MediaType.APPLICATION_JSON).accept(MediaTypes.HAL_JSON)
//				.param("title", "test Controller Title")
//				.param("content", "test Controller Content")
				.content(content)
				).andExpect(status().isOk())
				.andExpect(jsonPath("id").exists())
				.andExpect(jsonPath("_links.self").exists())
				.andExpect(jsonPath("content").value(board.getContent()));
	}
	
//	@Test
	@DisplayName(value = "모두 가져오는 테스트")
	public void readAll() throws Exception {
		this.mvc.perform(get("/api/boards").contentType(MediaType.APPLICATION_JSON).accept(MediaTypes.HAL_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("_embedded").exists())
				.andExpect(jsonPath("_embedded.boardList").exists())
				.andExpect(jsonPath("_links").exists())
				;
	}

//	@Test
	@DisplayName(value = "아이디로 조회")
	public void readById() throws Exception {
		this.mvc.perform(get("/api/boards/1").contentType(MediaType.APPLICATION_JSON).accept(MediaTypes.HAL_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("id").exists())
				.andExpect(jsonPath("_links.self").exists())
				;
	}
	
//	@Test
	@DisplayName("업데이트 테스트")
	public void update()throws Exception{
		
		Board board = Board.builder()
				.id(1L)
				.title("Controller Test update  Title")
				.content("Controller Test update Content")
				.build();
		
		
		
		this.mvc.perform(MockMvcRequestBuilders.put("/api/boards").contentType(MediaType.APPLICATION_JSON).accept(MediaTypes.HAL_JSON)
				.content(objectMapper.writeValueAsString(board)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("id").value(1L))
				.andExpect(jsonPath("_links").exists())
				.andExpect(jsonPath("title").value(board.getTitle()));
				
		
	}
	
	
//	@Test
	@DisplayName("DELETE METHOD 테스트")
	public void delete() throws Exception {
		this.mvc.perform(MockMvcRequestBuilders.delete("/api/boards/4").contentType(MediaType.APPLICATION_JSON).accept(MediaTypes.HAL_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName(value = "생성_ 유효성 검사 ")
	public void validCheckCreate() throws Exception{
		
		Board board = Board.builder()
				.title("")
				.build();
		
		log.info("board: "+ board);
		
		String content = objectMapper.writeValueAsString(board);
		
		this.mvc.perform(MockMvcRequestBuilders.post("/api/boards").contentType(MediaType.APPLICATION_JSON).accept(MediaTypes.HAL_JSON)
				.content(content)
				).andExpect(status().isBadRequest());
	}

}

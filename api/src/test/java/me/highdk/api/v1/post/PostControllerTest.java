package me.highdk.api.v1.post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import me.highdk.api.v1.user.User;
import me.highdk.api.v1.user.UserRepository;

@SpringBootTest
@Slf4j
class PostControllerTest {

	@Autowired
	private WebApplicationContext ctx;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	private MockMvc mvc;

	@BeforeEach
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(ctx) // MockMvc의 컨텍스트 설정.
				.addFilters(new CharacterEncodingFilter("UTF-8", true)) // 한글 안깨지게.
				.alwaysDo(print()) // 실행 결과를 Console창에 출력.
				.build();
	}

	@Test
	@DisplayName("생성 테스트 & 해쉬태그")
	public void createTest() throws Exception {
		
		User user = userRepository.create(User.builder()
								  .userName("test@test.com")
								  .nickName("testAccount")
								  .password("1234")
								  .build());
		
		PostRequest request = PostRequest.builder()
				.content("오늘은 #날씨 가 #너무 덥죠.. #행복 #소!통 #%$맛집 #날씨 #맛집")
				.writerId(user.getId())
				.build();
		
		MvcResult result = this.mvc.perform(post("/v1/api/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andExpect(jsonPath("id").exists())
				.andReturn()
				;
		
		String str = result.getResponse()
						   .getContentAsString(Charset.forName("UTF-8"));
		Post newPost = objectMapper.convertValue(str, Post.class);
		
		userRepository.deleteById(user.getId());
		postRepository.deleteById(newPost.getId());
	}
	
	@Test
	@DisplayName("하나 읽기 테스트")
	public void readOneTest() throws Exception{
		
		this.mvc.perform(get("/v1/api/posts/2")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("id").exists())
				.andExpect(jsonPath("_links.self").exists());
	}
	
	@Test
	@DisplayName("없는 포스트 읽어오는 테스트")
	public void readOneWithNotExistedPostId() throws Exception {
		
		this.mvc.perform(get("/v1/api/posts/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("rejectedValue").exists());
	}
	
	@Test
	@DisplayName("페이징 여러개 읽기 테스트")
	public void readWithPagedTest() throws Exception{
		
		this.mvc.perform(get("/v1/api/posts")
				.accept(MediaTypes.HAL_JSON)
				.param("start", "0")
				.param("size", "5"))
				.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("업데이트 확인")
	public void updateOneTest()throws Exception{
		String content = "업데이트되는 콘텐트 ";
		PostRequest postRequest = PostRequest.builder()
									.content(content)	
									.build();
		
		String str = objectMapper.writeValueAsString(postRequest);
	
		this.mvc.perform(put("/v1/api/posts/3")
				.accept(MediaTypes.HAL_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(str))
				.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("컨텐트 안넣고 업데이트 유효성 확인")
	public void updateOneValidTest()throws Exception{
//		String content = "업데이트되는 콘텐트 ";
		PostRequest postRequest = PostRequest.builder()
									.content(null)	
									.build();
		
		String str = objectMapper.writeValueAsString(postRequest);
	
		this.mvc.perform(put("/v1/api/posts/4")
				.accept(MediaTypes.HAL_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(str))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("code").exists());
	}

	@Test
	@DisplayName("없는 postId 업데이트 유효성 확인")
	public void updateOneParamValidTest()throws Exception{
		String content = "업데이트되는 콘텐트 ";
		PostRequest postRequest = PostRequest.builder()
									.content(content)	
									.build();
		
		String str = objectMapper.writeValueAsString(postRequest);
	
		this.mvc.perform(put("/v1/api/posts/400")
				.accept(MediaTypes.HAL_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(str))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("name").exists());
	}
	
	@Test
	@DisplayName("flag로 삭제 테스트")
	public void deleteByFlag()throws Exception{
		this.mvc.perform(delete("/v1/api/posts/1")
				.accept(MediaTypes.HAL_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("message").exists());
	}

}

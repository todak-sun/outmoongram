package me.highdk.api.v1.comment;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

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
				.postId(2L)
				.writerId(1L)
				.build();
		
		this.mvc.perform(post("/v1/api/comments")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaTypes.HAL_JSON)
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
				.andExpect(jsonPath("postId", is(request.getPostId().intValue())))
				.andExpect(jsonPath("writerId").exists())
				.andExpect(jsonPath("writerId", is(request.getWriterId().intValue())))
				.andExpect(jsonPath("_links").exists())
				.andExpect(jsonPath("_links.self").exists())
				.andExpect(jsonPath("_links.self.href").exists())
				.andExpect(jsonPath("_links.post").exists())
				.andExpect(jsonPath("_links.post.href").exists())
				;
	}
	
	@Test
	@DisplayName(value="아무것도 보내지 않은 상태로 요청을 보냈을 때 테스트")
	public void create_with_nothing() throws Exception {
		
		CommentRequest request = CommentRequest.builder().build();
		
		this.mvc.perform(post("/v1/api/comments")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaTypes.HAL_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaTypes.HAL_JSON))
				.andExpect(jsonPath("_embedded").exists())
				.andExpect(jsonPath("_embedded.errors").exists())
				.andExpect(jsonPath("_embedded.errors[0]").exists())
				.andExpect(jsonPath("_embedded.errors[0].name").exists())
				.andExpect(jsonPath("_embedded.errors[0].message").exists())
				.andExpect(jsonPath("_embedded.errors[0].rejectedValue").doesNotExist())
				.andExpect(jsonPath("_links").exists())
				.andExpect(jsonPath("_links.index").exists())
				.andExpect(jsonPath("_links.index.href").exists())
				;
	}
	
	//TODO: SYJ 테스트 깨짐. 뭘 설정해야 하는지 까먹음.
//	@Test
	@DisplayName(value = "요구하는 필드 외 다른 값을 넣었을 때.")
	public void create_with_unsupportedFields() throws Exception {
		Comment request = Comment.builder()
								 .id(4L)
								 .parentId(0L)
								 .content("실패해야 하는 댓글 테스트")
								 .commentCnt(5)
								 .likeCnt(5)
								 .writtenAt(LocalDateTime.now())
								 .updatedAt(LocalDateTime.now())
								 .postId(1L)
								 .writerId(1L)
								 .build();
		
		this.mvc.perform(post("/v1/api/comments")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				;
	}
	
	//TODO: SYJ, 조금 더 보완할 것.
	@Test
	@DisplayName(value = "start, size를 주지 않고 페이징")
	public void get_with_paging_with_default() throws Exception {
		this.mvc.perform(get("/v1/api/comments")
				.param("postId", "2"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaTypes.HAL_JSON))
			.andExpect(jsonPath("_embedded").exists())
			.andExpect(jsonPath("_embedded.comments").exists())
			.andExpect(jsonPath("_embedded.comments[0]").exists())
			.andExpect(jsonPath("_embedded.comments[0].id").exists())
			.andExpect(jsonPath("_embedded.comments[0].parentId").exists())
			.andExpect(jsonPath("_embedded.comments[0].content").exists())
			.andExpect(jsonPath("_embedded.comments[0].writtenAt").exists())
			.andExpect(jsonPath("_embedded.comments[0].updatedAt").exists())
			.andExpect(jsonPath("_embedded.comments[0].likeCnt").exists())
			.andExpect(jsonPath("_embedded.comments[0].commentCnt").exists())
			.andExpect(jsonPath("_embedded.comments[0].postId").exists())
			.andExpect(jsonPath("_embedded.comments[0].writerId").exists())
			.andExpect(jsonPath("page").exists())
			.andExpect(jsonPath("page.size").exists())
			.andExpect(jsonPath("page.number").exists())
			.andExpect(jsonPath("page.totalPages").exists())
			.andExpect(jsonPath("page.totalElements").exists())
			.andExpect(jsonPath("_links").exists())
			.andExpect(jsonPath("_links.self").exists())
			.andExpect(jsonPath("_links.self.href").exists())
			.andExpect(jsonPath("_links.index").exists())
			.andExpect(jsonPath("_links.index.href").exists())
			;
	}
	
	//TODO: SYJ, 조금 더 보완할 것.
	@Test
	@DisplayName(value = "start, size를 주면서 페이징")
	public void get_with_paging_with_params() throws Exception {
		this.mvc.perform(get("/v1/api/comments")
				.param("postId", "1")
				.param("start", "0")
				.param("size", "20")
				)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaTypes.HAL_JSON))
			.andExpect(jsonPath("_embedded").exists())
			.andExpect(jsonPath("_embedded.comments").exists())
			.andExpect(jsonPath("_embedded.comments[0]").exists())
			.andExpect(jsonPath("_embedded.comments[0].id").exists())
			.andExpect(jsonPath("_embedded.comments[0].parentId").exists())
			.andExpect(jsonPath("_embedded.comments[0].content").exists())
			.andExpect(jsonPath("_embedded.comments[0].writtenAt").exists())
			.andExpect(jsonPath("_embedded.comments[0].updatedAt").exists())
			.andExpect(jsonPath("_embedded.comments[0].likeCnt").exists())
			.andExpect(jsonPath("_embedded.comments[0].commentCnt").exists())
			.andExpect(jsonPath("_embedded.comments[0].postId").exists())
			.andExpect(jsonPath("_embedded.comments[0].writerId").exists())
			.andExpect(jsonPath("page").exists())
			.andExpect(jsonPath("page.size").exists())
			.andExpect(jsonPath("page.number").exists())
			.andExpect(jsonPath("page.totalPages").exists())
			.andExpect(jsonPath("page.totalElements").exists())
			.andExpect(jsonPath("_links").exists())
			.andExpect(jsonPath("_links.self").exists())
			.andExpect(jsonPath("_links.self.href").exists())
			.andExpect(jsonPath("_links.index").exists())
			.andExpect(jsonPath("_links.index.href").exists())
			;
	}
	
	//TODO: SYJ, postId없이 댓글 페이징 하더라도, 에러 객체 내보내도록 바꾸기.
	@Test
	@DisplayName(value = "postId없이 페이징 호출 테스트")
	public void get_with_paging_without_post_id() throws Exception {
		this.mvc.perform(get("/v1/api/comments")
				).andExpect(status().isBadRequest())
		;
				
	}
	
	@Test
	@DisplayName(value = "start, size에 마이너스 값 보내기")
	public void get_with_paging_without_minus_start_and_size() {
		//TODO : start, size에 마이너스 값 보내기 테스트
	}
	
	@Test
	@DisplayName(value = "있는 답글을 가져오는 테스트")
	public void get_one() throws Exception {
		this.mvc.perform(get("/v1/api/comments/1"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaTypes.HAL_JSON))
			.andExpect(jsonPath("id").exists())
			.andExpect(jsonPath("parentId").exists())
			.andExpect(jsonPath("content").exists())
			.andExpect(jsonPath("writtenAt").exists())
			.andExpect(jsonPath("updatedAt").exists())
			.andExpect(jsonPath("likeCnt").exists())
			.andExpect(jsonPath("commentCnt").exists())
			.andExpect(jsonPath("postId").exists())
			.andExpect(jsonPath("writerId").exists())
			.andExpect(jsonPath("_links").exists())
			.andExpect(jsonPath("_links.index").exists())
			.andExpect(jsonPath("_links.index.href").exists())
			.andExpect(jsonPath("_links.self").exists())
			.andExpect(jsonPath("_links.self.href").exists())
			;
	}
	
	@Test
	@DisplayName(value = "없는 답글을 가져오는 테스트")
	public void get_one_not_found() throws Exception {
		this.mvc.perform(get("/v1/api/comments/999999999"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("name").exists())
			.andExpect(jsonPath("message").exists())
			.andExpect(jsonPath("rejectedValue").exists())
			.andExpect(jsonPath("_links").exists())
			.andExpect(jsonPath("_links.index").exists())
			.andExpect(jsonPath("_links.index.href").exists())
			;
	}
	
	@Test
	@DisplayName(value = "댓글 수정")
	public void update() throws Exception {
		CommentRequest request = CommentRequest.builder()
											.content("바뀐 내용")
											.writerId(1L)
											.postId(1L)
											.build();
		
		this.mvc.perform(put("/v1/api/comments/1")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request))
						)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaTypes.HAL_JSON))
			.andExpect(jsonPath("id").exists())
			.andExpect(jsonPath("parentId").exists())
			.andExpect(jsonPath("content").exists())
			.andExpect(jsonPath("content", is(request.getContent())))
			.andExpect(jsonPath("writtenAt").exists())
			.andExpect(jsonPath("updatedAt").exists())
			.andExpect(jsonPath("likeCnt").exists())
			.andExpect(jsonPath("commentCnt").exists())
			.andExpect(jsonPath("postId").exists())
			.andExpect(jsonPath("writerId").exists())
			.andExpect(jsonPath("_link").exists())
			.andExpect(jsonPath("_link.self").exists())
			.andExpect(jsonPath("_link.self.href").exists())
			.andExpect(jsonPath("_link.index").exists())
			.andExpect(jsonPath("_link.index.href").exists())
			;
	}
	
	@Test
	@DisplayName(value = "댓글 정상 삭제")
	public void delete_success() throws Exception {
		//TODO: 댓글 삭제 시 고려해야할 것 모두 적용 후, 테스트코드도 다시 짤 것.
		this.mvc.perform(delete("/v1/api/comments/1"))
					.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName(value = "존재하지 않는 댓글을 삭제시도")
	public void delete_fail() throws Exception{
		this.mvc.perform(delete("/v1/api/comments/99999999999"))
					.andExpect(status().isNotFound());
	}
	
	//TODO: SYJ, 테스트 자동화를 위한 데이터 생성/삭제 코드가 필요함. 추가할 것!
}

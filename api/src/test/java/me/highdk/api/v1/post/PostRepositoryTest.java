package me.highdk.api.v1.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.highdk.api.v1.common.PageDto;

@SpringBootTest
public class PostRepositoryTest {
	
	@Autowired
	PostRepository postRepository;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Test
	public void test() throws JsonProcessingException {
		var result = postRepository.findAllWithFullResource(new PageDto());
		String json = objectMapper.writeValueAsString(result);
		System.out.println(json);
	}

}

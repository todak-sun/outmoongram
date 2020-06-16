package me.highdk.api.v1.hashtag;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class HashtagServiceTest {
	
	@Autowired
	private HashtagService hashtagService;

	@Test
	@DisplayName(value = "포스트 태그 파싱 테스트")
	public void taggingPostTest() {
//		String postContent = "#불금 하루였습니다. 뭔가 더워서 짜증이 날 때도 있고, #취업 이 안돼서 속상할 때도 있죠... 그치만 #지금 열심히 공부를 하고 있으니 언젠가는 #꿈 이 이뤄질꺼라 #믿어요 #행복 #불금";
		String postContent = "#HOT#coLd #!#   ##SUMMER  #$행복      #^불금 #!맛집";
		Long postId = 3L;
		
		int result = hashtagService.taggingPost(postContent, postId);
		
		log.info("result : {}", result);
		
	}
	
	@Test
	@DisplayName(value = "댓글 태그 파싱 테스트")
	public void taggingCmtTest() {
//		String postContent = "#불금 하루였습니다. 뭔가 더워서 짜증이 날 때도 있고, #취업 이 안돼서 속상할 때도 있죠... 그치만 #지금 열심히 공부를 하고 있으니 언젠가는 #꿈 이 이뤄질꺼라 #믿어요 #행복 #불금";
		String cmtContent = "#Happy#coLd #해이!해!#   ##SUM#MER  #행복      #^불금 #!맛집";
		Long cmtId = 3L;
		
		int result = hashtagService.taggingCmt(cmtContent, cmtId);
		
		log.info("result : {}", result);
		
	}
	
	@Test
	public void findAllTagsBykeywordTest() throws Exception {
		log.info("===> {}", hashtagService.findAllTagBykeyword("sum"));
	}

}

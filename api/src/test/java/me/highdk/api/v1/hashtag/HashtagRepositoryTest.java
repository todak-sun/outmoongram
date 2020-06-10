package me.highdk.api.v1.hashtag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class HashtagRepositoryTest {
	
	@Autowired
	private HashtagRepository hashtagRepository;

	@Test
	public void findByTag() {
		
		
		Long result =  hashtagRepository.findByTag("얼음");
		
		log.info("=======> result  : "+  result);
	}
	
	@Test
	public void taggingPostTest() {
	
		String content = "행복한 #시간 #불금 행복한 시간들이잖아요? 일단은요? #맛집 이라는데요?";
		
		String[] parsedContent = content.split(" ");
		List<String> tagsList = new ArrayList<String>();
		
		log.info("tags : {}", Arrays.toString(parsedContent));
		
		for(String tag : parsedContent) {
			if(tag.charAt(0) == ('#')) {
				log.info(tag);
				tagsList.add(tag);
			}
		}
		String[] tags = tagsList.toArray(new String[tagsList.size()]);
		
		log.info("tagsList : {}", tagsList);
		log.info("tags : {}", Arrays.toString(tags));
		
		Long postId = 1L;
		
		int result = hashtagRepository.taggingPost(tags, postId);
		
		log.info("result: {}", result);
	}

}

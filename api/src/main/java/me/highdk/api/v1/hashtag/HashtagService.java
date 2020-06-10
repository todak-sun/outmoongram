package me.highdk.api.v1.hashtag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import me.highdk.api.v1.common.OutmoonService;

@Service
@Slf4j
public class HashtagService implements OutmoonService<Hashtag, HashtagResponse> {

	private final HashtagRepository hashtagRepository;
	
	@Autowired
	public HashtagService(HashtagRepository hashtagRepository) {
		this.hashtagRepository = hashtagRepository;
	}
	
	public CollectionModel<HashtagResponse> findAllTagBykeyword(String keyword){
		List<HashtagResponse> hashtags = this.toResponse(hashtagRepository.findAllTagsByKeyword(keyword));
		
		CollectionModel<HashtagResponse> resource = CollectionModel.of(hashtags);
		
		Map<String, String> parameters = new HashMap<>();
		parameters.put("keyword", keyword);

//		methodOn을 통해 Controller의 method의 Parameters를 queryString으로 넣어준다.
		Link selfLink = linkTo(methodOn(HashtagController.class).findAllTagsByKeyword(keyword)).withSelfRel();
		resource.add(selfLink);
		
		return resource;
								
	}
	
	public int taggingPost(String postContent, Long postId) {
		String[] tags = this.parseTag(postContent); 
		return hashtagRepository.taggingPost(tags, postId);
	}
	
	public int taggingCmt(String cmtContent, Long cmtId) {
		String[] tags = this.parseTag(cmtContent);
		return hashtagRepository.taggingCmt(tags, cmtId);
	}
	
	public String[] parseTag(String content) {
//		#으로 시작하고 문자열인 정규식
//		java.util.regex package에 있는 Pattern 과 Matcher class 를 사용한다.	
		Pattern pattern = Pattern.compile("\\#([0-9a-zA-Z가-힣]*)");
		Matcher matcher = pattern.matcher(content);

		List<String> parsedTags = new ArrayList<String>();
		
		while(matcher.find()) {
			boolean isDuplicate = false;
//			정규식으로 한글, 숫자, 영어 아닌(^) 것들을 nullString으로 대체 그리고 소문자로 변환
			String tag = matcher.group().replaceAll("[^가-힣0-9a-zA-Z]", "").toLowerCase();
			
			log.info("tag: {}", tag);
			if(!tag.isEmpty()) {
				for(String tagInList : parsedTags) {
					if(tag.equals(tagInList)) {
						isDuplicate = true;
						break;
					}
				}
				if(isDuplicate == false) {
					parsedTags.add(tag);
				}
			}
		}
		return parsedTags.toArray(new String[parsedTags.size()]);
	}

	@Override
	public HashtagResponse toResponse(Hashtag hashtag) {
		return HashtagResponse.builder()
				.id(hashtag.getId())
				.tag(hashtag.getTag())
				.usedCnt(hashtag.getUsedCnt())
				.build();
	}

	@Override
	public List<HashtagResponse> toResponse(List<Hashtag> hashtags) {
		return hashtags.stream().map(this::toResponse)
								.collect(Collectors.toList());
	}
	

}

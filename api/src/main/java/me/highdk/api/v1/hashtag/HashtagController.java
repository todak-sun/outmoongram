package me.highdk.api.v1.hashtag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/v1/api/hashtags")
public class HashtagController {
	
	private final HashtagService hashtagService;

	@Autowired
	public HashtagController(HashtagService hashtagService) {
		this.hashtagService = hashtagService;
	}
	
	@GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<?> findAllTagsByKeyword(@RequestParam(value = "keyword", required = false) String keyword){
		
		CollectionModel<HashtagResponse> resource =  hashtagService.findAllTagBykeyword(keyword);
		
		return ResponseEntity.status(HttpStatus.OK)
							.body(resource);
	}
	

}

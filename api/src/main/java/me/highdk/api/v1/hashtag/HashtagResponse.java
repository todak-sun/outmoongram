package me.highdk.api.v1.hashtag;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Relation(collectionRelation = "hashtags")
public class HashtagResponse extends RepresentationModel<HashtagResponse>{
	
	private Long id;
	
	private String tag;
	
	private Long usedCnt;

}

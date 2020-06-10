package me.highdk.api.v1.hashtag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Hashtag {
	
	private Long id;
	
	private String tag;
	
	private Long usedCnt;

}

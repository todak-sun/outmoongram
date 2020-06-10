package me.highdk.api.v1.hashtag;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HashtagDao {
	
	public Long findByTag(String tag);
	
	public List<Hashtag> findAllTagsByKeyword(String keyword);
	
	public int taggingPost(@Param("tags") String[] array, @Param("postId") Long postId);
	
	public int taggingCmt(@Param("tags") String[] array, @Param("cmtId") Long cmtId);

}

package me.highdk.api.v1.hashtag;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me.highdk.api.v1.common.OutmoonRepository;

@Repository
public class HashtagRepository implements OutmoonRepository<Long, Hashtag> {
	
	private final HashtagDao hashtagDao;

	@Autowired
	public HashtagRepository(HashtagDao hashtagDao) {
		this.hashtagDao = hashtagDao;
	}

	@Override
	public Hashtag save(Hashtag resource) {
		return null;
	}

	@Override
	public Optional<Hashtag> findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Long findByTag(String tag){
		return hashtagDao.findByTag(tag);
	}
	
	public List<Hashtag> findAllTagsByKeyword(String keyword){
		return hashtagDao.findAllTagsByKeyword(keyword);
	}
	
	public int taggingPost(String[] array, Long postId) {
		return hashtagDao.taggingPost(array, postId);
	}
	
	public int taggingCmt(String[] array, Long cmtId) {
		return hashtagDao.taggingCmt(array, cmtId);
	}

}

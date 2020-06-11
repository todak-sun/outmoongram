package me.highdk.api.v1.post;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me.highdk.api.v1.common.OutmoonRepository;
import me.highdk.api.v1.common.PageDto;

@Repository
public class PostRepository implements OutmoonRepository<Long, Post>{

	private final PostDao postDao;
	
	@Autowired
	public PostRepository(PostDao postDao) {
		this.postDao = postDao;
	}
	
	@Override
	public Post save(Post post) {
		Long id = postDao.create(post);
		
		return postDao.getById(post.getId());
	}
	
	@Override
	public Optional<Post> findById(Long postId) {
		return postDao.findById(postId);
	}

	public List<Post> findAll(PageDto pageDto){
		
		return postDao.findAll(pageDto);
	}
	
	public Long countTotal() {
		return postDao.countTotal();
	}
	
	public Post updateOne(Post post) {
		int updateResult =  postDao.update(post);
		if(updateResult != 1) {
			throw new RuntimeException();
		}
		return postDao.getById(post.getId());
	}
	
	public int deleteByFlag(Long postId) {
		int deletedResult = postDao.deleteByFlag(postId);
		if(deletedResult != 1) {
			throw new RuntimeException();
		}
		
		return deletedResult;
	}
	

}

package me.highdk.api.v1.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PostRepository {

	private final PostDao postDao;
	
	@Autowired
	public PostRepository(PostDao postDao) {
		this.postDao = postDao;
	}
	
	@Transactional
	public Post save(Post newPost) {
		Long id = postDao.create(newPost);
		return postDao.getById(newPost.getId());
	}

}

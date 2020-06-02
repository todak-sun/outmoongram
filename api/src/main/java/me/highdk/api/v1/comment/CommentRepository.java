package me.highdk.api.v1.comment;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import me.highdk.api.v1.common.OutmoonRepository;

@Repository
public class CommentRepository implements OutmoonRepository<Long, Comment>{

	private final CommentDao commentDao;
	
	@Autowired
	public CommentRepository(CommentDao commentDao) {
		this.commentDao = commentDao;
	}
	
	@Transactional
	@Override
	public Comment save(Comment comment) {
		Integer affectedRowCount = commentDao.create(comment);
		return commentDao.getById(comment.getId());
	}

	@Override
	public Optional<Comment> findById(Long id) {
		return commentDao.findById(id);
	}
	
	
	
}

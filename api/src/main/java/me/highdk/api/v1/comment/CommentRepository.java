package me.highdk.api.v1.comment;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import me.highdk.api.v1.common.OutmoonRepository;
import me.highdk.api.v1.common.PageDto;

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

	public List<Comment> findByPostId(Long postId, PageDto pageDto) {
		return commentDao.getByPostId(postId, pageDto);
	}
	
	
	
}

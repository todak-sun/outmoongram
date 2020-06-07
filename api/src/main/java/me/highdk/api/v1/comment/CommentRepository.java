package me.highdk.api.v1.comment;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me.highdk.api.v1.common.OutmoonRepository;
import me.highdk.api.v1.common.PageDto;

@Repository
public class CommentRepository implements OutmoonRepository<Long, Comment>{

	private final CommentDao commentDao;
	
	@Autowired
	public CommentRepository(CommentDao commentDao) {
		this.commentDao = commentDao;
	}
	
	
	@Override
	public Comment save(Comment comment) {
		var optionalId = Optional.ofNullable(comment.getId());
		if(optionalId.isEmpty()) {
			int affectedRowCount = commentDao.create(comment);
			if(affectedRowCount != 1) {
				throw new RuntimeException();
			}
			return commentDao.getById(comment.getId());
		} else {
			int affectedRowCount = commentDao.update(comment);
			if(affectedRowCount != 1) {
				throw new RuntimeException();
			}
			return commentDao.getById(comment.getId());
		}		
	}

	@Override
	public Optional<Comment> findById(Long id) {
		return commentDao.findById(id);
	}

	public List<Comment> findByPostId(Long postId, PageDto pageDto) {
		return commentDao.getByPostId(postId, pageDto);
	}

	public Long countsByPostId(Long postId) {
		return commentDao.countsByPostId(postId);
	}


	public int deleteById(Long id) {
		return commentDao.deleteById(id);
	}


	public int deleteByParentId(Long id) {
		return commentDao.deleteByParentId(id);
	}
	
	
	
}

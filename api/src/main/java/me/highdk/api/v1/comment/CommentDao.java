package me.highdk.api.v1.comment;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentDao {

	public Integer create(Comment comment);

	public Comment getById(Long id);

	public Optional<Comment> findById(Long id);

}

package me.highdk.api.v1.comment;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me.highdk.api.v1.common.PageDto;

@Mapper
public interface CommentDao {

	public int create(Comment comment);

	public Comment getById(Long id);

	public Optional<Comment> findById(Long id);

	public List<Comment> getByPostId(@Param("postId") Long postId, @Param("pageDto") PageDto pageDto);

	public Long countsByPostId(Long postId);

	public int update(Comment comment);

	public int deleteById(Long id);

	public int deleteByParentId(Long id);

}

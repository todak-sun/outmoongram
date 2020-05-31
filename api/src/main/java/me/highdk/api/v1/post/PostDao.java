package me.highdk.api.v1.post;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostDao {
	
	public Long create(Post post);

	public Post getById(Long id);
	
	public Optional<Post> findById(Long id);
	
}

package me.highdk.api.v1.post;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me.highdk.api.v1.common.PageDto;

@Mapper
public interface PostDao {
	
	public Long create(Post post);

//	무조건 id가 있을 경우.. ex)게시물 작성 후 해당 아이디로 찾아서 return(server에서 내부적으로 로직 돌리고 return하는 상황)
	public Post getById(Long id);
	
//	id가 없을 수도 있는 경우.. NotFoundException을 날릴 수도 있는 상황(client가 요청보내는 상황)
	public Optional<Post> findById(Long id);
	
	public List<Post> findAll(@Param("pageDto") PageDto pageDto);
	
	public Long countTotal();
	
}

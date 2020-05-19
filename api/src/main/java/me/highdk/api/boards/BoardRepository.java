package me.highdk.api.boards;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BoardRepository {
	
	public Long create(Board board);
	
	public Optional<Board> findById(Long id);
	
	public List<Board> findAll();
	
	public Date now();

	
}

package me.highdk.api.v1.user;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
	
	public int create(User user);
	
	public User getById(Long id);
	
	public Optional<User> findById(Long id);

	public int deleteById(Long id);
	
}

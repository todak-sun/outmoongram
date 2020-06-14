package me.highdk.api.v1.user;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
	
	private final UserDao userDao;
	
	public UserRepository(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public User create(User user) {
		int affectedRowCount = userDao.create(user);		
		return userDao.getById(user.getId());
	}
	
	public int deleteById(Long id) {
		return userDao.deleteById(id);
	}
}

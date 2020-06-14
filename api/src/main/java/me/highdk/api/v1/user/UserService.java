package me.highdk.api.v1.user;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	public UserResponse toResponse(User user) {
		return Optional.ofNullable(user).map(u -> UserResponse.builder()
															  .id(u.getId())
															  .nickName(u.getNickName())
															  .userName(u.getUserName())
															  .registeredAt(u.getRegisteredAt())
															  .updatedAt(u.getUpdatedAt())
															  .build())
										.orElseGet(() -> null);
	}
	
}

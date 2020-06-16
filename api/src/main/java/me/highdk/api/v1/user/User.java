package me.highdk.api.v1.user;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
@ToString
public class User {
	
	private Long id;
	
	private String userName;
	
	private String password;
	
	private String nickName;
	
	private LocalDateTime registeredAt;
	
	private LocalDateTime updatedAt;
	
}

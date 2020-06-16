package me.highdk.api.v1.user;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
public class UserResponse {

	private Long id;

	@JsonInclude(value = Include.NON_NULL)
	private String userName;
	
	@JsonInclude(value = Include.NON_NULL)
	private String nickName;
	
	@JsonInclude(value = Include.NON_NULL)
	private LocalDateTime registeredAt;

	@JsonInclude(value = Include.NON_NULL)
	private LocalDateTime updatedAt;
}

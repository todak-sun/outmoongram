package me.highdk.api.boards.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
//Exception 처리를 할 때 리턴할 Object
public class ErrorResponse {
	
	
	//Field
	private String code;
	
	private String description;
	
	private String detail;

	
	//Constructor
	public ErrorResponse(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
}

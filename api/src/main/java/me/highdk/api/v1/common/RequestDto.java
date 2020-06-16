package me.highdk.api.v1.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.highdk.api.v1.common.enums.FetchType;

@Getter
@Setter
@ToString
public class RequestDto {
	
	private String back;
	
	private FetchType fetchType;
	
	public RequestDto() {
		this.fetchType = FetchType.ORIGIN;
	}
	
}

package me.highdk.api.v1.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PageDto {
	
	private int start;

	private int size;

	public PageDto(int start, int size) {
		this.start = start;
		this.size = size;
	}
	
	public PageDto() {
		this(0, 10);
	}

}

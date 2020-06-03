package me.highdk.api.v1.common;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class PageDto {
	
	@Min(0)
	@Setter
	private long start;
	
	@Min(1)
	@Setter
	private long size;
	
	private long page;
	
	private Long previous;
	
	private Long next;
	
	public PageDto(long start, long size) {
		this.start = start;
		this.size = size;
		
		this.page = (long) Math.ceil((start + 1) / (double) size);
		this.previous = this.page - 1 == 0 ? null : this.page - 1;
		this.next = page + 1;
	}
	
	public PageDto() {
		this(0L, 10L);
	}

}

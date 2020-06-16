package me.highdk.api.v1.common;

import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class PageDto extends RequestDto {
	
	@Min(0)
	@Setter
	private long start;
	
	@JsonIgnore
	private long end;
	
	@Min(1)
	@Setter
	private long size;
	
	@JsonIgnore
	private long page;
	
	@JsonIgnore
	private Long previous;
	
	@JsonIgnore
	private Long next;
	
	public PageDto(long start, long size) {
		super();
		this.start = start;
		this.size = size;
		
		this.end = start + size;
		
		this.page = (long) Math.ceil((start + 1) / (double) size);
		this.previous = this.page - 1 == 0 ? null : this.page - 1;
		this.next = page + 1;
	}
	
	public PageDto() {
		this(0L, 10L);
	}

}

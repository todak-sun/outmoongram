package me.highdk.api.v1.common;

import org.springframework.hateoas.EntityModel;

public interface OutmoonService<Content, Req, Res> {
	
	public EntityModel<Res> create(Req request);
	

	
}

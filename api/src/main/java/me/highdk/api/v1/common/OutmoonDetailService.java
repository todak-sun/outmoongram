package me.highdk.api.v1.common;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

public interface OutmoonDetailService<Entity, Request, Response extends RepresentationModel<Response>> extends OutmoonService<Entity, Response> {
	
	public EntityModel<Response> create(Request request);
	
	public Entity toEntity(Request request);
		
}

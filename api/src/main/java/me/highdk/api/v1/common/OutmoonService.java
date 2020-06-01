package me.highdk.api.v1.common;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

public interface OutmoonService<Entity, Request, Response extends RepresentationModel<Response>> {
	
	public EntityModel<Response> create(Request request);
	
	Response toResponse(Entity entity);
	
	Entity toEntity(Request request);
	
}

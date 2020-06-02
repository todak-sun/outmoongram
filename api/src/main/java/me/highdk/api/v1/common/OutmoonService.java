package me.highdk.api.v1.common;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import me.highdk.api.v1.index.IndexController;

public interface OutmoonService<Entity, Response extends RepresentationModel<Response>> {

	
	public Response toResponse(Entity entity);
	
	public List<Response> toResponse(List<Entity> entities);
	
	public default CollectionModel<Response> toResource(List<Response> responses){
		Link indexLink = linkTo(IndexController.class).withRel("index");
		return CollectionModel.of(responses, indexLink);
	}
	
	public default CollectionModel<Response> toResource(List<Response> responses, Link... links){
		var resource =  this.toResource(responses);
		resource.add(links);
		return resource;
	}
	
	public default EntityModel<Response> toResource(Response response){
		Link indexLink = linkTo(IndexController.class).withRel("index");
		return EntityModel.of(response, indexLink);
	}
	
	public default EntityModel<Response> toResource(Response response, Link... links){
		var resource =  this.toResource(response);
		resource.add(links);
		return resource;
	}
	
}

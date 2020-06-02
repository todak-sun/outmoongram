package me.highdk.api.v1.error;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(collectionRelation = "errors")
public class ErrorResponse extends RepresentationModel<ErrorResponse>{
	
	private String name;

	private String message;

	private Object rejectedValue;
	
}

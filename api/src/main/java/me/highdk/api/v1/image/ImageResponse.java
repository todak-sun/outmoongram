package me.highdk.api.v1.image;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Relation(collectionRelation = "images")
public class ImageResponse extends RepresentationModel<ImageResponse> {
	
	private String name;
	
	@JsonInclude(value = Include.NON_EMPTY)
	private String src;
	
	@JsonInclude(value = Include.NON_EMPTY)
	private String message;
	
	/**
	 * @JsonIgnore 추가시, 응답할 때 추가되지 않음.
	 * */
	@JsonIgnore
	private byte[] serializedImage;
	
	@JsonIgnore
	private String mimeType;
	
	
}

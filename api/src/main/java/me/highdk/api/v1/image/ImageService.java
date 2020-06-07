package me.highdk.api.v1.image;

import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

import me.highdk.api.v1.common.OutmoonDetailService;

public class ImageService implements OutmoonDetailService<Image, ImageRequest, ImageResponse> {

	@Override
	public ImageResponse toResponse(Image entity) {
		
		return null;
	}

	@Override
	public List<ImageResponse> toResponse(List<Image> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityModel<ImageResponse> create(ImageRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image toEntity(ImageRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}

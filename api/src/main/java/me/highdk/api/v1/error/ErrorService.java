package me.highdk.api.v1.error;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import me.highdk.api.v1.common.OutmoonService;

@Service
public class ErrorService implements OutmoonService<ErrorDto, ErrorResponse>{
		
	public CollectionModel<ErrorResponse> badReqeust400(List<ErrorDto> errors){
		List<ErrorResponse> response = this.toResponse(errors); 
		return this.toResource(response);
	}
	
	public EntityModel<ErrorResponse> notFound404(ErrorDto error){
		ErrorResponse response = this.toResponse(error);
		return this.toResource(response);
	}

	@Override
	public ErrorResponse toResponse(ErrorDto errorDto) {
		return ErrorResponse.builder()
		.name(errorDto.getName())
		.message(errorDto.getMessage())
		.rejectedValue(errorDto.getRejectedValue())
		.build();
	}

	@Override
	public List<ErrorResponse> toResponse(List<ErrorDto> errorDtos) {
		return errorDtos.stream().map(this::toResponse)
				 .collect(Collectors.toList());
	}
	
}

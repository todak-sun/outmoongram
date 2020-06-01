package me.highdk.api.v1.error;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class ErrorService {

	public List<ErrorResponse> toResponse(Errors errors) {
		List<ErrorResponse> errorList = errors.getFieldErrors().stream().map(error -> {
			return ErrorResponse.builder().message(error.getDefaultMessage())
									 .name(error.getField())
									 .rejectedValue(error.getRejectedValue()).build();
		}).collect(Collectors.toList());
		return errorList;
	}

}

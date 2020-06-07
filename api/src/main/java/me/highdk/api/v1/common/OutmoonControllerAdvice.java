package me.highdk.api.v1.common;

import java.net.ConnectException;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import me.highdk.api.v1.common.exception.BadRequestException;
import me.highdk.api.v1.error.ErrorResponse;
import me.highdk.api.v1.error.ErrorService;

@RestControllerAdvice
public class OutmoonControllerAdvice {
	
	private final ErrorService errorService;
	
	public OutmoonControllerAdvice(ErrorService errorService) {
		this.errorService = errorService;
	}
	
	@ExceptionHandler(ConnectException.class)
	public ResponseEntity<?> couldNotConnectDatabase(){
		return ResponseEntity
					.status(HttpStatus.SERVICE_UNAVAILABLE)
					.body("데이터베이스 서버가 정상적이지 않습니다.");
	}
	
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<?> badRequest(BadRequestException exception){
		CollectionModel<ErrorResponse> resource = errorService.badReqeust400(exception.getErrors());
		return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(resource);
	}
	
}

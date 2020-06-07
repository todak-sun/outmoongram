package me.highdk.api.v1.common;

import java.net.ConnectException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import me.highdk.api.v1.common.exception.NotFoundException;
import me.highdk.api.v1.error.ErrorService;

@RestControllerAdvice
public class OutmoonControllerAdvice {
	
	private final ErrorService errorService;
	
	public OutmoonControllerAdvice(ErrorService errorService) {
		this.errorService = errorService;
	}
	
//	@ExceptionHandler(NotFoundException.class)
//	public ResponseEntity<?> notFound(NotFoundException exception) {
//		return ResponseEntity
//					.status(HttpStatus.NOT_FOUND)
//					.body(exception.getId());
//	}
	
	@ExceptionHandler(ConnectException.class)
	public ResponseEntity<?> couldNotConnectDatabase(){
		return ResponseEntity
					.status(HttpStatus.SERVICE_UNAVAILABLE)
					.body("데이터베이스 서버가 정상적이지 않습니다.");
	}
	
}

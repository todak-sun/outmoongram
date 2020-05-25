package me.highdk.api.boards.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

//전역 에러 Handler..
@RestControllerAdvice
@Slf4j
public class BoardControllerAdvice {

	
//	@Valid 를 통과하지 못하면, MethodAgumentNotValidException 발생
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException validEx){
		
		
		Map<String, String> errors = new HashMap<>();
		
		validEx.getBindingResult().getAllErrors()
				.forEach(err -> {
					log.error("==> error: " + err);
					
//					FieldError로 Casting 후에 메세지랑 Map에 담는다.. 나중엔 Map이 아니라 error를 뿌려줄 response나 Vo를 만들어서 리턴하는 듯..
					errors.put( ((FieldError) err).getField(), err.getDefaultMessage());
		});
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		
	}
}

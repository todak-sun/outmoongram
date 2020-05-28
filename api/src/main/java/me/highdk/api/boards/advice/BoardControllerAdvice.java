package me.highdk.api.boards.advice;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import me.highdk.api.boards.exception.NotExistExeption;

//RestController 내부에서 일어나는 Exception을 처리하겠다는 의미
//내부적으로 @ResponseBody를 갖고 있어 필요에 따라 원하는 Object로 return이 가능
@RestControllerAdvice
@Slf4j
public class BoardControllerAdvice {

	
//	@Valid 를 통과하지 못하면, MethodAgumentNotValidException 발생
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException arguValidEx
																			){
		
		Map<String, String> errors = new HashMap<>();
		
		arguValidEx.getBindingResult().getAllErrors()
				.forEach(err -> {
					log.error("==> err Data Type: " + err.getClass().getName());
					log.error("==> error: " + err);
					
//					FieldNamed을 얻기 위해 FieldError로 Casting
//					후에 메세지랑 Map에 담는다.. 나중엔 Map이 아니라 error를 뿌려줄 response나 Vo를 만들어서 리턴하는 듯..
					errors.put( ((FieldError) err).getField(), err.getDefaultMessage());
		});
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, String>> handleConstValidationException(ConstraintViolationException constValidEx){
		
		Map<String, String> errors = new HashMap<>();
		
		errors.put("message", constValidEx.getMessage());
		
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}
	
	@ExceptionHandler(NotExistExeption.class)
	public ResponseEntity<?> handleNotExistException(NotExistExeption notExistEx){
		
		Map<String, String> errors = new HashMap<>();
		
		errors.put("message", notExistEx.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}
	
}

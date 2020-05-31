package me.highdk.api.boards.exception;

import lombok.Getter;

//ErrorResponse에 사용할 코드들
public enum ErrorCode {

	NOT_NULL("ERROR_CODE_001", "필수 값이 누락되었습니다."),
	NOT_EMPTY("ERROR_CODE_002", "필수 값이 누락되었습니다."),
	POSITIVE("ERROR_CODE_003", "1 이상의 값을 입력하십시오. "),
	NOT_EXIST("ERROR_CODE_004", "해당 번호는 존재하지 않습니다.")
	;
	
	@Getter
	private String code;
	
	@Getter
	private String description;
	
	ErrorCode(String code, String description){
		this.code = code;
		this.description = description;
	}
}

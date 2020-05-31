package me.highdk.api.boards.exception;


//argument로 조회했을 때 해당 id가 존재하지 않는 경우 처리할 사용자 예외
public class NotExistExeption extends RuntimeException {
	
	private Long id;

	public NotExistExeption() {}
	
	public NotExistExeption(Long id) {
		super("해당 \'" + id+ "\'는 존재하지 않습니다.");
	}
}

--mybatis가 로드된 직후, 실행되는 SQL을 정의하는 문서.

DROP TABLE IF EXISTS BOARD;

CREATE TABLE BOARD
(
	ID INT PRIMARY KEY AUTO_INCREMENT,
	TITLE VARCHAR,
	CONTENT VARCHAR,
	WRITTEN_AT TIMESTAMP,
	UPDATED_AT TIMESTAMP
);

INSERT INTO BOARD(TITLE, CONTENT, WRITTEN_AT, UPDATED_AT)
VALUES('테스트 제목1', '테스트 내용1', now(), now());

INSERT INTO BOARD(TITLE, CONTENT, WRITTEN_AT, UPDATED_AT)
VALUES('테스트 제목2', '테스트 내용2', now(), now());
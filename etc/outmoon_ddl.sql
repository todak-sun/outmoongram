/*
 * DDL 정의서 
 * 
 * 작성일 : 2020.05.26
 * 수정이력 : 
 * 2020.05.31 허지행 POST에 IS_USED 컬럼추가.
 * 2020.05.31 선용주 OUTMOON_CMT에 SORT, DEPTH 삭제.
 * 
 * */

DROP TABLE IF EXISTS OUTMOON_USER CASCADE;
DROP TABLE IF EXISTS OUTMOON_HASHTAG CASCADE;
DROP TABLE IF EXISTS OUTMOON_IMAGE CASCADE; 
DROP TABLE IF EXISTS OUTMOON_POST CASCADE;
DROP TABLE IF EXISTS OUTMOON_POST_LIKE CASCADE;
DROP TABLE IF EXISTS OUTMOON_LINK_POST_HASH CASCADE;
DROP TABLE IF EXISTS OUTMOON_LINK_POST_IMAGE CASCADE;
DROP TABLE IF EXISTS OUTMOON_CMT CASCADE;
DROP TABLE IF EXISTS OUTMOON_LINK_CMT_HASH CASCADE;
DROP TABLE IF EXISTS OUTMOON_CMT_LIKE CASCADE;
DROP TABLE IF EXISTS OUTMOON_LINK_USER_HASH CASCADE;
DROP TABLE IF EXISTS OUTMOON_LINK_USER_IMAGE CASCADE;

--해시태그 테이블
CREATE TABLE OUTMOON_HASHTAG
(
   ID BIGSERIAL PRIMARY KEY,
   TAG VARCHAR(60) NOT NULL, --한글 기준 최대 15글자
   USED_CNT INT NOT NULL DEFAULT 0
);

--이미지 테이블
CREATE TABLE OUTMOON_IMAGE 
(
   ID BIGSERIAL PRIMARY KEY,
   NAME VARCHAR(150) NOT NULL, --한글 기준 최대 50글자
   FILE_PATH VARCHAR(200) NOT NULL--일단 길게 설정
);

--회원 테이블
CREATE TABLE OUTMOON_USER
(
   ID BIGSERIAL PRIMARY KEY,
   USER_NAME VARCHAR(100) NOT NULL UNIQUE, -- 메일 계정아이디
   NICK_NAME VARCHAR(30) NOT NULL UNIQUE, --한글 기준 최대 10자, 영어 또는 숫자는 30자.
   PASSWORD VARCHAR(100) NOT NULL,
   REGISTERED_AT TIMESTAMP NOT NULL,
   UPDATED_AT TIMESTAMP NOT NULL,
   POST_CNT INT NOT NULL DEFAULT 0,
   IS_USED BOOLEAN NOT NULL DEFAULT TRUE
);

--포스트 테이블
CREATE TABLE OUTMOON_POST
(
   ID BIGSERIAL PRIMARY KEY,
   CONTENT VARCHAR(3000) NOT NULL, --한글 기준 최대 1000자 까지만.
   WRITTEN_AT TIMESTAMP NOT NULL,
   UPDATED_AT TIMESTAMP NOT NULL,
   LIKE_CNT INT NOT NULL DEFAULT 0,
   COMMENT_CNT INT NOT NULL DEFAULT 0,
   IS_USED BOOLEAN NOT NULL DEFAULT true,
   WRITER_ID BIGINT
);
ALTER TABLE OUTMOON_POST ADD CONSTRAINT FK_USER_ID FOREIGN KEY (WRITER_ID) REFERENCES OUTMOON_USER (ID);

--포스트 - 해시태그 연결테이블
CREATE TABLE OUTMOON_LINK_POST_HASH
(
   POST_ID BIGINT,
   TAG_ID BIGINT
);
ALTER TABLE OUTMOON_LINK_POST_HASH ADD CONSTRAINT FK_POST_ID FOREIGN KEY (POST_ID) REFERENCES OUTMOON_POST (ID);
ALTER TABLE OUTMOON_LINK_POST_HASH ADD CONSTRAINT FK_TAG_ID FOREIGN KEY (TAG_ID) REFERENCES OUTMOON_HASHTAG (ID);

--포스트 - 이미지 연결테이블
CREATE TABLE OUTMOON_LINK_POST_IMAGE
(
   POST_ID BIGINT,
   IMAGE_ID BIGINT
);
ALTER TABLE OUTMOON_LINK_POST_IMAGE ADD CONSTRAINT FK_POST_ID FOREIGN KEY (POST_ID) REFERENCES OUTMOON_POST (ID);
ALTER TABLE OUTMOON_LINK_POST_IMAGE ADD CONSTRAINT FK_IMAGE_ID FOREIGN KEY (IMAGE_ID) REFERENCES OUTMOON_IMAGE (ID);

--포스트 좋아요 테이블
CREATE TABLE OUTMOON_POST_LIKE
(
   ID BIGSERIAL PRIMARY KEY,
   POST_ID BIGINT,
   USER_ID BIGINT
);
ALTER TABLE OUTMOON_POST_LIKE ADD CONSTRAINT FK_POST_ID FOREIGN KEY (POST_ID) REFERENCES OUTMOON_POST (ID);
ALTER TABLE OUTMOON_POST_LIKE ADD CONSTRAINT FK_USER_ID FOREIGN KEY (USER_ID) REFERENCES OUTMOON_USER (ID);


--댓글 테이블
CREATE TABLE OUTMOON_CMT
(
   ID BIGSERIAL PRIMARY KEY,
   PARENT_ID BIGINT NOT NULL DEFAULT 0,
   CONTENT VARCHAR(1500) NOT NULL, --한글 기준 최대 500자 까지만.
   WRITTEN_AT TIMESTAMP NOT NULL,
   UPDATED_AT TIMESTAMP NOT NULL,
   LIKE_CNT INT NOT NULL DEFAULT 0,
   COMMENT_CNT INT NOT NULL DEFAULT 0,
   POST_ID BIGINT,
   WRITER_ID BIGINT
);
ALTER TABLE OUTMOON_CMT ADD CONSTRAINT FK_POST_ID FOREIGN KEY (POST_ID) REFERENCES OUTMOON_POST (ID);
ALTER TABLE OUTMOON_CMT ADD CONSTRAINT FK_WRITER_ID FOREIGN KEY (WRITER_ID) REFERENCES OUTMOON_USER (ID);

--댓글 - 해시태그 연결 테이블
CREATE TABLE OUTMOON_LINK_CMT_HASH
(
   CMT_ID BIGINT,
   TAG_ID BIGINT
);
ALTER TABLE OUTMOON_LINK_CMT_HASH ADD CONSTRAINT FK_CMT_ID FOREIGN KEY (CMT_ID) REFERENCES OUTMOON_CMT (ID);
ALTER TABLE OUTMOON_LINK_CMT_HASH ADD CONSTRAINT FK_TAG_ID FOREIGN KEY (TAG_ID) REFERENCES OUTMOON_HASHTAG (ID);

--댓글 좋아요 테이블
CREATE TABLE OUTMOON_CMT_LIKE
(
   ID BIGSERIAL PRIMARY KEY,
   CMT_ID BIGINT,
   USER_ID BIGINT
);
ALTER TABLE OUTMOON_CMT_LIKE ADD CONSTRAINT FK_CMT_ID FOREIGN KEY (CMT_ID) REFERENCES OUTMOON_CMT (ID);
ALTER TABLE OUTMOON_CMT_LIKE ADD CONSTRAINT FK_USER_ID FOREIGN KEY (USER_ID) REFERENCES OUTMOON_USER (ID);


--회원 - 해쉬 연결
CREATE TABLE OUTMOON_LINK_USER_HASH
(
   USER_ID BIGINT,
   TAG_ID BIGINT
);
ALTER TABLE OUTMOON_LINK_USER_HASH ADD CONSTRAINT FK_USER_ID FOREIGN KEY (USER_ID) REFERENCES OUTMOON_USER (ID);
ALTER TABLE OUTMOON_LINK_USER_HASH ADD CONSTRAINT FK_TAG_ID FOREIGN KEY (TAG_ID) REFERENCES OUTMOON_HASHTAG (ID);

--회원 - 이미지 연결
CREATE TABLE OUTMOON_LINK_USER_IMAGE
(
   USER_ID BIGINT,
   IMAGE_ID BIGINT
);
ALTER TABLE OUTMOON_LINK_USER_IMAGE ADD CONSTRAINT FK_USER_ID FOREIGN KEY (USER_ID) REFERENCES OUTMOON_USER (ID);
ALTER TABLE OUTMOON_LINK_USER_IMAGE ADD CONSTRAINT FK_IMAGE_ID FOREIGN KEY (IMAGE_ID) REFERENCES OUTMOON_IMAGE (ID);
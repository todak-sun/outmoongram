DROP TRIGGER IF EXISTS TRIGGER_POST_LIKE_CNT ON PUBLIC.OUTMOON_POST_LIKE;
DROP FUNCTION IF EXISTS PUBLIC.POST_LIKE_CNT();
DROP TRIGGER IF EXISTS TRIGGER_CMT_LIKE_CNT ON PUBLIC.OUTMOON_CMT_LIKE;
DROP FUNCTION IF EXISTS PUBLIC.CMT_LIKE_CNT();
DROP TRIGGER IF EXISTS TRIGGER_POST_COMMENT_CNT ON PUBLIC.OUTMOON_CMT;
DROP FUNCTION IF EXISTS PUBLIC.POST_COMMENT_CNT();
DROP TRIGGER IF EXISTS TRIGGER_USER_POST_CNT ON PUBLIC.OUTMOON_POST;
DROP FUNCTION IF EXISTS PUBLIC.USER_POST_CNT();
DROP TRIGGER IF EXISTS TRIGGER_POST_HASHTAG_USED_CNT ON PUBLIC.OUTMOON_LINK_POST_HASH;
DROP FUNCTION IF EXISTS POST_HASHTAG_USED_CNT();
DROP TRIGGER IF EXISTS TRIGGER_CMT_HASHTAG_USED_CNT ON PUBLIC.OUTMOON_LINK_CMT_HASH ;
DROP FUNCTION IF EXISTS CMT_HASHTAG_USED_CNT();

DROP FUNCTION IF EXISTS PUBLIC.TAGGING_POST(_VARCHAR, INT8);
DROP FUNCTION IF EXISTS PUBLIC.TAGGING_CMT(_VARCHAR, INT8);

--해쉬태그_POST 처리하는 PROCEDURE
CREATE OR REPLACE FUNCTION TAGGING_POST(VARCHAR[], BIGINT) RETURNS INTEGER AS $TAGGING_POST$
DECLARE 
	A_TAGS 	ALIAS FOR $1;
	A_POST_ID ALIAS FOR $2;
	V_VAL	VARCHAR;
	V_ID	BIGINT;
	RESULT INT := 0;
	
BEGIN 
	FOREACH V_VAL IN ARRAY A_TAGS
	LOOP
		SELECT ID INTO V_ID
			FROM OUTMOON_HASHTAG
			WHERE TAG = V_VAL;
		IF(V_ID IS NOT NULL) THEN 
			RAISE NOTICE 'V_VAL: %', V_VAL;
			RAISE NOTICE 'V_ID: %', V_ID;
		ELSE
			INSERT INTO OUTMOON_HASHTAG (
				TAG
			)VALUES(
				V_VAL
			);
		
			SELECT CURRVAL(PG_GET_SERIAL_SEQUENCE('OUTMOON_HASHTAG','id')) INTO V_ID;	
		
			RAISE NOTICE 'V_VAL: %', V_VAL;
			RAISE NOTICE 'V_ID: %', V_ID;
			
		END IF;
	
		INSERT INTO OUTMOON_LINK_POST_HASH (
				POST_ID,
				TAG_ID
			)VALUES(
				A_POST_ID,
				V_ID
			);
		RESULT := RESULT + 1;
	
	END LOOP;
	RETURN RESULT;
END;
$TAGGING_POST$ LANGUAGE PLPGSQL;

--해쉬태그_POST 처리하는 PROCEDURE
CREATE OR REPLACE FUNCTION TAGGING_CMT(VARCHAR[], BIGINT) RETURNS INTEGER AS $TAGGING_CMT$
DECLARE 
	A_TAGS 	ALIAS FOR $1;
	A_CMT_ID ALIAS FOR $2;
	V_VAL	VARCHAR;
	V_ID	BIGINT;
	RESULT INT := 0;
	
BEGIN 
	FOREACH V_VAL IN ARRAY A_TAGS
	LOOP
		SELECT ID INTO V_ID
			FROM OUTMOON_HASHTAG
			WHERE TAG = V_VAL;
		IF(V_ID IS NOT NULL) THEN 
			RAISE NOTICE 'V_VAL: %', V_VAL;
			RAISE NOTICE 'V_ID: %', V_ID;
		ELSE
			INSERT INTO OUTMOON_HASHTAG (
				TAG
			)VALUES(
				V_VAL
			);
		
			SELECT CURRVAL(PG_GET_SERIAL_SEQUENCE('OUTMOON_HASHTAG','id')) INTO V_ID;	
		
			RAISE NOTICE 'V_VAL: %', V_VAL;
			RAISE NOTICE 'V_ID: %', V_ID;
			
		END IF;
	
		INSERT INTO OUTMOON_LINK_CMT_HASH (
				CMT_ID,
				TAG_ID
			)VALUES(
				A_CMT_ID,
				V_ID
			);
		RESULT := RESULT + 1;
	
	END LOOP;
	RETURN RESULT;
END;
$TAGGING_CMT$ LANGUAGE PLPGSQL;


--POST에 대한 좋아요 트리거 함수 
CREATE OR REPLACE FUNCTION POST_LIKE_CNT() RETURNS TRIGGER AS $TRIGGER_POST_LIKE_CNT$
DECLARE COUNT INT;

BEGIN
	IF(TG_OP = 'INSERT') THEN
		SELECT COUNT(*) INTO COUNT 
			FROM OUTMOON_POST_LIKE 
			WHERE POST_ID = NEW.POST_ID;

		UPDATE OUTMOON_POST
			SET LIKE_CNT = COUNT
			WHERE ID = NEW.POST_ID;
		RETURN NEW;
	
	ELSEIF(TG_OP = 'DELETE') THEN
		SELECT COUNT(*) INTO COUNT
			FROM OUTMOON_POST_LIKE 
			WHERE POST_ID = OLD.POST_ID;
		
		UPDATE OUTMOON_POST 
			SET LIKE_CNT = COUNT
			WHERE ID = OLD.POST_ID;
		RETURN OLD;
	END IF;
	
	RETURN NULL;
		
END;

$TRIGGER_POST_LIKE_CNT$ LANGUAGE PLPGSQL;

--COMMENT 에 대한 좋아요 트리거 함수
CREATE OR REPLACE FUNCTION CMT_LIKE_CNT() RETURNS TRIGGER AS $TRIGGER_CMT_LIKE_CNT$
DECLARE COUNT INT;

BEGIN
	IF(TG_OP = 'INSERT') THEN
		SELECT COUNT(*) INTO COUNT
			FROM OUTMOON_CMT_LIKE
			WHERE CMT_ID =  NEW.CMT_ID;
		
		UPDATE OUTMOON_CMT 
			SET LIKE_CNT = COUNT
			WHERE ID = NEW.CMT_ID;
		
		RETURN NEW;
	ELSEIF(TG_OP = 'DELETE') THEN
		SELECT COUNT(*) INTO COUNT
			FROM OUTMOON_CMT_LIKE
			WHERE CMT_ID  = OLD.CMT_ID;
		
		UPDATE OUTMOON_CMT 
			SET LIKE_CNT = COUNT
			WHERE ID = OLD.CMT_ID;
		
		RETURN OLD;
	END IF;

	RETURN NULL;
END;
$TRIGGER_CMT_LIKE_CNT$ LANGUAGE PLPGSQL;


--게시물에 대한 댓글 갯수 트리거 함수
--댓글에 대한 대댓글 갯수 트리거 함수
CREATE OR REPLACE FUNCTION POST_COMMENT_CNT() RETURNS TRIGGER AS $TRIGGER_POST_COMMENT_CNT$
DECLARE COUNT INT;

BEGIN
	IF(TG_OP = 'INSERT') THEN
	
--		대댓글인 경우, PARENT CMT의 댓글 갯수 업데이트
		IF(NEW.PARENT_ID != 0) THEN
			SELECT COUNT(*) INTO COUNT
				FROM OUTMOON_CMT
				WHERE PARENT_ID =  NEW.PARENT_ID;
			
			UPDATE OUTMOON_CMT 
				SET COMMENT_CNT = COUNT
				WHERE ID = NEW.PARENT_ID;
			
			RAISE NOTICE 'POST_ID IS %', NEW.POST_ID;
			RAISE NOTICE 'PARENT_ID IS %', NEW.PARENT_ID;
		END IF;
	
		SELECT COUNT(*) INTO COUNT
			FROM OUTMOON_CMT
			WHERE POST_ID =  NEW.POST_ID;
		
		UPDATE OUTMOON_POST 
			SET COMMENT_CNT = COUNT
			WHERE ID = NEW.POST_ID;
		
		RAISE NOTICE 'POST_ID IS %', NEW.POST_ID;
		RAISE NOTICE 'PARENT_ID IS %', NEW.PARENT_ID;
		
		RETURN NEW;
	
	ELSEIF(TG_OP = 'DELETE') THEN
	
--		대댓글인 경우 PARENT CMT의 댓글 갯수 업데이트
		IF(OLD.PARENT_ID != 0) THEN
			SELECT COUNT(*) INTO COUNT
				FROM OUTMOON_CMT
				WHERE PARENT_ID  = OLD.PARENT_ID;
			
			UPDATE OUTMOON_CMT 
				SET COMMENT_CNT = COUNT
				WHERE ID = OLD.PARENT_ID;
		END IF;
	
	
--		PARENT CMT가 삭제될 때, CHILREN CMT도 같이 삭제되도록.. 제약조건 때문에..BEFOR로 트리거 설정해야하나...
		DELETE 
			FROM OUTMOON_CMT 
			WHERE PARENT_ID = OLD.ID;
	
		SELECT COUNT(*) INTO COUNT
			FROM OUTMOON_CMT
			WHERE POST_ID  = OLD.POST_ID;
		
		UPDATE OUTMOON_POST 
			SET COMMENT_CNT = COUNT
			WHERE ID = OLD.POST_ID;
		
		RAISE NOTICE 'POST_ID IS %', OLD.POST_ID;
		RAISE NOTICE 'PARENT_ID IS %', OLD.PARENT_ID;
		
		RETURN OLD;
	END IF;
	RETURN NULL;
END;
$TRIGGER_POST_COMMENT_CNT$ LANGUAGE PLPGSQL;


--COMMENT 에 대한 좋아요 트리거 함수
CREATE OR REPLACE FUNCTION USER_POST_CNT() RETURNS TRIGGER AS $TRIGGER_USER_POST_CNT$
DECLARE COUNT INT;

BEGIN
	IF(TG_OP = 'INSERT') THEN
		SELECT COUNT(*) INTO COUNT
			FROM OUTMOON_POST
			WHERE WRITER_ID =  NEW.WRITER_ID;
		
		UPDATE OUTMOON_USER 
			SET POST_CNT = COUNT
			WHERE ID = NEW.WRITER_ID;
		
		RETURN NEW;
--	POST는 FLAG로 DELETE여부를 처리하기 때문에.. TRIGGER가 일어날 OPERATION를 UPDATE로 설정하겠다.	
	ELSEIF(TG_OP = 'DELETE') THEN
		SELECT COUNT(*) INTO COUNT
			FROM OUTMOON_POST
			WHERE WRITER_ID  = OLD.WRITER_ID;
		
		UPDATE OUTMOON_USER 
			SET POST_CNT = COUNT
			WHERE ID = OLD.WRITER_ID;
		
		RETURN OLD;
	END IF;

	RETURN NULL;
END;
$TRIGGER_USER_POST_CNT$ LANGUAGE PLPGSQL;

--LINK_POST_HASH에 대한 TAG_CNT 트리거 함수
CREATE OR REPLACE FUNCTION POST_HASHTAG_USED_CNT() RETURNS TRIGGER AS $TRIGGER_POST_HASHTAG_USED_CNT$
DECLARE COUNT INT;

BEGIN
	IF(TG_OP = 'INSERT') THEN
		SELECT COUNT(*) INTO COUNT
			FROM OUTMOON_LINK_POST_HASH
			WHERE TAG_ID = NEW.TAG_ID;
		
		UPDATE OUTMOON_HASHTAG 
			SET USED_CNT = COUNT
			WHERE ID = NEW.TAG_ID;
		RETURN NEW;
	
	ELSEIF(TG_OP = 'DELETE') THEN
		SELECT COUNT(*) INTO COUNT
			FROM OUTMOON_LINK_POST_HASH
			WHERE TAG_ID = OLD.TAG_ID;
		
		UPDATE OUTMOON_HASHTAG 
			SET USED_CNT = COUNT
			WHERE ID = OLD.TAG_ID;
		RETURN OLD;
	END IF;
	RETURN NULL;
END;
$TRIGGER_POST_HASHTAG_USED_CNT$ LANGUAGE PLPGSQL;

--LINK_CMT_HASH에 대한 TAG_CNT 트리거 함수
CREATE OR REPLACE FUNCTION CMT_HASHTAG_USED_CNT() RETURNS TRIGGER AS $TRIGGER_CMT_HASHTAG_USED_CNT$
DECLARE COUNT INT;

BEGIN
	IF(TG_OP = 'INSERT') THEN
		SELECT COUNT(*) INTO COUNT
			FROM OUTMOON_LINK_CMT_HASH 
			WHERE TAG_ID = NEW.TAG_ID;
		
		UPDATE OUTMOON_HASHTAG 
			SET USED_CNT = COUNT
			WHERE ID = NEW.TAG_ID;
		RETURN NEW;
	
	ELSEIF(TG_OP = 'DELETE') THEN
		SELECT COUNT(*) INTO COUNT
			FROM OUTMOON_LINK_CMT_HASH 
			WHERE TAG_ID = OLD.TAG_ID;
		
		UPDATE OUTMOON_HASHTAG 
			SET USED_CNT = COUNT
			WHERE ID = OLD.TAG_ID;
		RETURN OLD;
	END IF;
	RETURN NULL;
END;
$TRIGGER_CMT_HASHTAG_USED_CNT$ LANGUAGE PLPGSQL;

--트리거 생성
CREATE TRIGGER TRIGGER_POST_LIKE_CNT
	AFTER INSERT OR UPDATE OR DELETE 
	ON OUTMOON_POST_LIKE
	FOR EACH ROW EXECUTE PROCEDURE POST_LIKE_CNT();
	
CREATE TRIGGER TRIGGER_CMT_LIKE_CNT
	AFTER INSERT OR UPDATE OR DELETE 
	ON OUTMOON_CMT_LIKE
	FOR EACH  ROW EXECUTE PROCEDURE CMT_LIKE_CNT();

CREATE TRIGGER TRIGGER_POST_COMMENT_CNT
	AFTER INSERT OR UPDATE OR DELETE
	ON OUTMOON_CMT 
	FOR EACH ROW EXECUTE PROCEDURE POST_COMMENT_CNT();

CREATE TRIGGER TRIGGER_USER_POST_CNT
	AFTER INSERT OR UPDATE OR DELETE 
	ON OUTMOON_POST
	FOR EACH  ROW EXECUTE PROCEDURE USER_POST_CNT();
	
CREATE TRIGGER TRIGGER_POST_HASHTAG_USED_CNT
	AFTER INSERT OR UPDATE OR DELETE 
	ON OUTMOON_LINK_POST_HASH
	FOR EACH  ROW EXECUTE PROCEDURE POST_HASHTAG_USED_CNT();

CREATE TRIGGER TRIGGER_CMT_HASHTAG_USED_CNT
	AFTER INSERT OR UPDATE OR DELETE 
	ON OUTMOON_LINK_CMT_HASH
	FOR EACH  ROW EXECUTE PROCEDURE CMT_HASHTAG_USED_CNT();

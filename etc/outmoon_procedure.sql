DROP TRIGGER IF EXISTS trigger_post_like_cnt ON public.outmoon_post_like;
DROP FUNCTION IF EXISTS public.post_like_cnt();
DROP TRIGGER IF EXISTS trigger_cmt_like_cnt ON public.outmoon_cmt_like;
DROP FUNCTION IF EXISTS public.cmt_like_cnt();
DROP TRIGGER IF EXISTS trigger_post_comment_cnt ON public.outmoon_cmt;
DROP FUNCTION IF EXISTS public.post_comment_cnt();
DROP TRIGGER IF EXISTS trigger_user_post_cnt ON public.outmoon_post;
DROP FUNCTION IF EXISTS public.user_post_cnt();


--post에 대한 좋아요 트리거 함수 
CREATE OR REPLACE FUNCTION post_like_cnt() RETURNS TRIGGER AS $trigger_post_like_cnt$
DECLARE count INT;

BEGIN
	IF(TG_OP = 'INSERT') THEN
		SELECT count(*) INTO count 
			FROM outmoon_post_like 
			WHERE post_id = NEW.post_id;

		UPDATE outmoon_post
			SET like_cnt = count
			WHERE id = NEW.post_id;
		
		RAISE NOTICE 'THIS IS %', count;
		RAISE NOTICE 'THIS IS %', NEW.post_id;
		
		RETURN NEW;
	
	ELSEIF(TG_OP = 'DELETE') THEN
		SELECT COUNT(*) INTO count
			FROM outmoon_post_like 
			WHERE post_id = OLD.post_id;
		
		UPDATE outmoon_post 
			SET like_cnt = count
			WHERE id = OLD.post_id;
		
		RAISE NOTICE 'THIS IS %', count;
		RAISE NOTICE 'THIS IS %', OLD.post_id;
	
		RETURN OLD;
	END IF;
	
	RETURN NULL;
		
END;

$trigger_post_like_cnt$ LANGUAGE plpgsql;

--COMMENT 에 대한 좋아요 트리거 함수
CREATE OR REPLACE FUNCTION cmt_like_cnt() RETURNS TRIGGER AS $trigger_cmt_like_cnt$
DECLARE count INT;

BEGIN
	IF(TG_OP = 'INSERT') THEN
		SELECT COUNT(*) INTO count
			FROM outmoon_cmt_like
			WHERE cmt_id =  NEW.cmt_id;
		
		UPDATE outmoon_cmt 
			SET like_cnt = count
			WHERE id = NEW.cmt_id;
		
		RETURN NEW;
	ELSEIF(TG_OP = 'DELETE') THEN
		SELECT COUNT(*) INTO count
			FROM outmoon_cmt_like
			WHERE cmt_id  = OLD.cmt_id;
		
		UPDATE outmoon_cmt 
			SET like_cnt = count
			WHERE id = OLD.cmt_id;
		
		RETURN OLD;
	END IF;

	RETURN NULL;
END;


$trigger_cmt_like_cnt$ LANGUAGE plpgsql;


--게시물에 대한 댓글 갯수 트리거 함수
--댓글에 대한 대댓글 갯수 트리거 함수
CREATE OR REPLACE FUNCTION post_comment_cnt() RETURNS TRIGGER AS $trigger_post_comment_cnt$
DECLARE count INT;

BEGIN
	IF(TG_OP = 'INSERT') THEN
	
--		대댓글인 경우, parent cmt의 댓글 갯수 업데이트
		IF(NEW.parent_id != 0) THEN
			SELECT COUNT(*) INTO count
				FROM outmoon_cmt
				WHERE parent_id =  NEW.parent_id;
			
			UPDATE outmoon_cmt 
				SET comment_cnt = count
				WHERE id = NEW.parent_id;
			
			RAISE NOTICE 'post_id IS %', NEW.post_id;
			RAISE NOTICE 'parent_id IS %', NEW.parent_id;
		END IF;
	
		SELECT COUNT(*) INTO count
			FROM outmoon_cmt
			WHERE post_id =  NEW.post_id;
		
		UPDATE outmoon_post 
			SET comment_cnt = count
			WHERE id = NEW.post_id;
		
		RAISE NOTICE 'post_id IS %', NEW.post_id;
		RAISE NOTICE 'parent_id IS %', NEW.parent_id;
		
		RETURN NEW;
	
	ELSEIF(TG_OP = 'DELETE') THEN
	
--		대댓글인 경우 parent cmt의 댓글 갯수 업데이트
		IF(OLD.parent_id != 0) THEN
			SELECT COUNT(*) INTO count
				FROM outmoon_cmt
				WHERE parent_id  = OLD.parent_id;
			
			UPDATE outmoon_cmt 
				SET comment_cnt = count
				WHERE id = OLD.parent_id;
		END IF;
	
	
--		parent cmt가 삭제될 때, chilren cmt도 같이 삭제되도록.. 제약조건 때문에..Befor로 트리거 설정해야하나...
		DELETE 
			FROM outmoon_cmt 
			WHERE parent_id = OLD.id;
	
		SELECT COUNT(*) INTO count
			FROM outmoon_cmt
			WHERE post_id  = OLD.post_id;
		
		UPDATE outmoon_post 
			SET comment_cnt = count
			WHERE id = OLD.post_id;
		
		RAISE NOTICE 'post_id IS %', OLD.post_id;
		RAISE NOTICE 'parent_id IS %', OLD.parent_id;
		
		RETURN OLD;
	END IF;
	RETURN NULL;
END;
$trigger_post_comment_cnt$ LANGUAGE plpgsql;


--COMMENT 에 대한 좋아요 트리거 함수
CREATE OR REPLACE FUNCTION user_post_cnt() RETURNS TRIGGER AS $trigger_user_post_cnt$
DECLARE count INT;

BEGIN
	IF(TG_OP = 'INSERT') THEN
		SELECT COUNT(*) INTO count
			FROM outmoon_post
			WHERE writer_id =  NEW.writer_id;
		
		UPDATE outmoon_user 
			SET post_cnt = count
			WHERE id = NEW.writer_id;
		
		RETURN NEW;
--	post는 flag로 delete여부를 처리하기 때문에.. trigger가 일어날 Operation를 update로 설정하겠다.	
	ELSEIF(TG_OP = 'DELETE') THEN
		SELECT COUNT(*) INTO count
			FROM outmoon_post
			WHERE writer_id  = OLD.writer_id;
		
		UPDATE outmoon_user 
			SET post_cnt = count
			WHERE id = OLD.writer_id;
		
		RETURN OLD;
	END IF;

	RETURN NULL;
END;
$trigger_user_post_cnt$ LANGUAGE plpgsql;

--트리거 생성
CREATE TRIGGER trigger_post_like_cnt
	AFTER INSERT OR UPDATE OR DELETE 
	ON outmoon_post_like
	FOR EACH ROW EXECUTE PROCEDURE post_like_cnt();
	
CREATE TRIGGER trigger_cmt_like_cnt
	AFTER INSERT OR UPDATE OR DELETE 
	ON outmoon_cmt_like
	FOR EACH  ROW EXECUTE PROCEDURE cmt_like_cnt();

CREATE TRIGGER trigger_post_comment_cnt
	AFTER INSERT OR UPDATE OR DELETE 
	ON outmoon_cmt
	FOR EACH  ROW EXECUTE PROCEDURE post_comment_cnt();

CREATE TRIGGER trigger_user_post_cnt
	AFTER INSERT OR UPDATE OR DELETE 
	ON outmoon_post
	FOR EACH  ROW EXECUTE PROCEDURE user_post_cnt();


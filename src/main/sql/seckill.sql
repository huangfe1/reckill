-- 秒杀执行存储过程
DElIMITER $$  -- console ; 转换为 $$
--定义存储过程
--参数：in表示输入参数; out 输出参数
CREATE PROCEDURE excuteSeckill(IN v_seckill_id bigint,IN v_phone bigint,IN v_kill_time timestamp,OUT r_result INT)
  BEGIN
    DECLARE insert_count INT DEFAULT  0;
    START TRANSACTION ;
    INSERT  ignore INTO success_killed
    (seckill_id,user_phone,create_time)
    VALUES  (v_seckill_id,v_phone,v_kill_time);
    SELECT ROW_COUNT() INTO insert_count;
    IF (insert_count=0) THEN
      ROLLBACK ;
      SET r_result = -1;
      ELSEIF(insert_count<0) THEN
       ROLLBACK ;
      SET r_result = -2;
      ELSE
      UPDATE seckill
      SET number = number -1
      WHERE seckill_id =  v_seckill_id
      AND end_time > v_kill_time
      AND start_time < v_kill_time
      AND number > 0;
      SELECT ROW_COUNT() INTO insert_count;
      IF (insert_count=0) THEN
        ROLLBACK ;
        SET r_result = 0;
      ELSEIF(insert_count<0) THEN
        ROLLBACK ;
        SET r_result = -2;
      ELSE
        COMMIT;
        SET r_result = 1;
      END IF;
    END IF;
  END;
$$
--存储过程定义结束
--查找存储过程  show create procedure name
--删除存储过程 drop procedure name
--调用存储过程 call name

DELIMITER ;
set @r_result = -3;
call excuteSeckill(1004,15673188984,now(),@r_result);
select @r_result;
-- 存储过程
-- 1：存储过程优化：事物行级锁持有时间
-- 2：不要过度依赖存储过程
-- 3：简单的逻辑，可以应用存储过程



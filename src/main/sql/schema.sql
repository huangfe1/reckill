-- 创建数据库
CREATE DATABASE seckill;

--使用数据库
use seckill;

--创建秒杀库存表
CREATE TABLE seckill(
  seckill_id bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
  name varchar(120) NOT NULL COMMENT '商品名称',
  number int NOT NULL COMMENT '产品数量',
  start_time timestamp NOT NULL COMMENT '秒杀开始时间',
  end_time timestamp NOT NULL DEFAULT  CURRENT_TIMESTAMP  COMMENT '秒杀结束时间',
  create_time timestamp NOT NULL DEFAULT  CURRENT_TIMESTAMP  COMMENT '创建时间',
  PRIMARY KEY (seckill_id),
  key idx_start_time(start_time),
  key idx_end_time(end_time),
  key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

INSERT INTO
  seckill(name,number,start_time,end_time)
VALUES
  ('10元秒杀iphone1',100,"2016-05-25 00:00:00","2016-05-26 00:00:00"),
  ('10元秒杀iphone2',200,"2016-05-25 00:00:00","2016-05-26 00:00:00"),
  ('10元秒杀iphone3',300,"2016-05-25 00:00:00","2016-05-26 00:00:00"),
  ('10元秒杀iphone4',400,"2016-05-25 00:00:00","2016-05-26 00:00:00"),
  ('10元秒杀iphone5',500,"2016-05-25 00:00:00","2016-05-26 00:00:00"),
  ('10元秒杀iphone6',600,"2016-05-25 00:00:00","2016-05-26 00:00:00");

--创建秒杀成功明细表
--用户登陆认证相关信息
CREATE TABLE success_killed(
  seckill_id bigint NOT NULL COMMENT '秒杀商品id',
  user_phone bigint NOT NULL COMMENT '用户手机',
  state tinyint NOT NULL DEFAULT -1 COMMENT '状态-1无效,0成功,已付款',
  create_time timestamp NOT NULL COMMENT '创建时间',
  PRIMARY KEY (seckill_id,user_phone),
  key idx_create_time(create_time)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';

--连接数据库的连接台
mysql -uroot -p123456

--为什么要手写DDL  Data Definition language

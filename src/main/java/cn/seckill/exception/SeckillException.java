package cn.seckill.exception;

/**
 * Created by huangfei on 2016/12/30.
 * 秒杀相关业务异常
 */
public class SeckillException extends RuntimeException{
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}

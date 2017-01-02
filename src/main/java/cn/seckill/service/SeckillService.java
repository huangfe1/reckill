package cn.seckill.service;

import cn.seckill.dto.Exposer;
import cn.seckill.dto.SeckillExecutiton;
import cn.seckill.entity.Seckill;
import cn.seckill.exception.SeckillException;

import java.util.List;

/**
 * Created by huangfei on 2016/12/30.
 * 业务接口：站在"使用者"的角度去设计接口
 * 方法定义粒度，参数，返回类型（return 类型／异常）
 *
 */
public interface SeckillService {
    /**
     * 查询所有秒杀记录
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 获取单个Seckill
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 输出秒杀接口地址
     * 秒杀开始输出地址
     * 否则输出系统时间与秒杀时间
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecutiton executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException;


    /**
     * 执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecutiton executeSeckillProcedure(long seckillId, long userPhone, String md5);

}
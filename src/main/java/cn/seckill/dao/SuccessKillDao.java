package cn.seckill.dao;

import cn.seckill.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

/**
 * Created by huangfei on 16/12/16.
 */
public interface SuccessKillDao {

    /**
     * 插入购买明细，可过滤重复
     * @param id
     * @param userPhone
     * @return 插入的行数
     */
    int insertSuccessKill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone,@Param("state")short state);

    /**
     * 通过Id查询,并携带产品秒杀
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWhithSecKill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}

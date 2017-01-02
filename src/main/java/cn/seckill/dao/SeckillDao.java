package cn.seckill.dao;

import cn.seckill.entity.Seckill;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by huangfei on 16/12/16.
 */
@Repository
public interface SeckillDao {

    /**
     * 减去库存
     * @param seckillId
     * @param killTime
     * @return 如果行数表示>1 表示更新行数
     */
    int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);


    /**
     * 根据ID查询秒杀对象
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询商品列表
     * @param offet
     * @return
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 存储过程执行秒杀
     */
    void killByProcedure(Map<String,Object> paramMap);
}

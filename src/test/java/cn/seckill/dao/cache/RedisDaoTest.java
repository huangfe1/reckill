package cn.seckill.dao.cache;

import cn.seckill.dao.SeckillDao;
import cn.seckill.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by huangfei on 2017/1/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {

    private long id =1001;

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void getSeckill() throws Exception {
       Seckill seckill = redisDao.getSeckill(id);
       if(seckill == null){
           seckill = seckillDao.queryById(id);
           if(seckill != null){
             String result = redisDao.putSeckill(seckill);
               System.out.println(result);
               seckill = redisDao.getSeckill(seckill.getSeckillId());
               System.out.println(seckill);
           }
       }
    }

    @Test
    public void putSeckill() throws Exception {

    }

}
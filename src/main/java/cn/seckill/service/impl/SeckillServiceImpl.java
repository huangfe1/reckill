package cn.seckill.service.impl;

import cn.seckill.dao.SeckillDao;
import cn.seckill.dao.SuccessKillDao;
import cn.seckill.dao.cache.RedisDao;
import cn.seckill.dto.Exposer;
import cn.seckill.dto.SeckillExecutiton;
import cn.seckill.entity.Seckill;
import cn.seckill.entity.SuccessKilled;
import cn.seckill.enums.SeckillStatEnum;
import cn.seckill.exception.RepeatKillException;
import cn.seckill.exception.SeckillCloseException;
import cn.seckill.exception.SeckillException;
import cn.seckill.service.SeckillService;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by huangfei on 2016/12/30.
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SuccessKillDao successKillDao;

    //md5盐值字符串，用于混淆md5
    private final String slat = "asdasdffasdeweqasdf221**&&6^%";

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        //优化点：缓存优化，超时的基础上维护一致性
        //1:访问redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            //2：访问数据库
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            } else {
                //3:存入redis
                redisDao.putSeckill(seckill);
            }
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //系统当前时间
        Date now = new Date();
        if (now.getTime() < startTime.getTime() || now.getTime() > endTime.getTime()) {
            return new Exposer(false, now.getTime(), startTime.getTime(), endTime.getTime(), seckillId);
        }
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }


    @Transactional
    public SeckillExecutiton executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }


        try {

            short state = 1;
            //记录购买行为
            int insertCount = successKillDao.insertSuccessKill(seckillId, userPhone, state);
            //唯一：seckillId，userPhone
            if (insertCount <= 0) {
                throw new RepeatKillException("seckill repeated");
            } else {
                //执行秒杀逻辑：减库存 + 记录购买行为
                Date nowTime = new Date();
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                if (updateCount <= 0) {
                    //没有更新记录  秒杀结束
                    throw new SeckillCloseException("seckill is close");
                } else {
                    SuccessKilled successKilled = successKillDao.queryByIdWhithSecKill(seckillId, userPhone);
                    return new SeckillExecutiton(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }


        } catch (RepeatKillException e) {
            throw e;
//            throw new SeckillException("seckill inner error"+e.getMessage());
//            return new SeckillResult<Ex>();
        } catch (SeckillCloseException e) {
            //所以编译异常转换为运行异常,为了让所有异常回滚
            throw e;
        } catch (Exception e) {
            throw new SeckillException("seckill inner error" + e.getMessage());
        }
    }

    public SeckillExecutiton executeSeckillProcedure(long seckillId, long userPhone, String md5){
        if(md5==null||!md5.equals(getMD5(seckillId))){
            return new SeckillExecutiton(seckillId,SeckillStatEnum.DATA_REWRITE);
        }
        Date time = new Date();
        Map<String,Object> map = new HashMap();
        map.put("seckillId",seckillId);
        map.put("phone",userPhone);
        map.put("killTime",time);
        map.put("result",null);
        //执行存储过程 result被赋值
        try {
            seckillDao.killByProcedure(map);
           int result = MapUtils.getInteger(map,"result",-2);
            if(result == 1){
                SuccessKilled sk = successKillDao.queryByIdWhithSecKill(seckillId,userPhone);
                return  new SeckillExecutiton(seckillId,SeckillStatEnum.SUCCESS);
            }else {
                return  new SeckillExecutiton(seckillId,SeckillStatEnum.stateOf(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  new SeckillExecutiton(seckillId,SeckillStatEnum.INSERT_ERROR);
        }

    }
}

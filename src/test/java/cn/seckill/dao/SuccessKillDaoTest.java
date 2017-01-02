package cn.seckill.dao;

import cn.seckill.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by huangfei on 16/12/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKillDaoTest {
    @Resource
    private SuccessKillDao successKillDao;
    @Test
    public void insertSuccessKill() throws Exception {
        long sid=1000L;
        long phone=15673188984L;
        short state=1;
        int insertCount=successKillDao.insertSuccessKill(sid,phone,state);
        System.out.println(insertCount);
    }

    @Test
    public void queryByIdWhithSecKill() throws Exception {
        long sid=1000L;
        long phone=15673188984L;
        SuccessKilled successKilled=successKillDao.queryByIdWhithSecKill(sid,phone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }

}
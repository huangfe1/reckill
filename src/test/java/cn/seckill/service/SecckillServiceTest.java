package cn.seckill.service;

import cn.seckill.dto.Exposer;
import cn.seckill.dto.SeckillExecutiton;
import cn.seckill.entity.Seckill;
import cn.seckill.exception.RepeatKillException;
import cn.seckill.exception.SeckillCloseException;
import cn.seckill.exception.SeckillException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


/**
 * Created by huangfei on 2016/12/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
"classpath:spring/spring-service.xml"})
public class SecckillServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;
    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> seckills = seckillService.getSeckillList();
        logger.info("list={}",seckills);
    }

    @Test
    public void getById() throws Exception {
        long seckillId=1000;
        Seckill seckill = seckillService.getById(seckillId);
        logger.info("seckill={}",seckill);
        //f96a0d5b35dc083d50b1b172550fb711
    }

    @Test
    public void exportSeckillUrl() throws Exception {
        long id =1000;
        Exposer exposer =seckillService.exportSeckillUrl(id);
        logger.info("exposer={}",exposer.toString());
    }

    @Test
    public void executeSeckill() throws Exception {
        long id = 1000;
        long phone = 12233;
        String md5="f96a0d5b35dc083d50b1b172550fb711";
        try {
            SeckillExecutiton seckillExecutiton = seckillService.executeSeckill(id,phone,md5);
        }catch (SeckillCloseException e){
            logger.error(e.getMessage());
        }catch (RepeatKillException e){
            logger.error(e.getMessage());
        }
        catch (SeckillException e) {
            logger.error(e.getMessage());
        }
//        logger.info("result={}",seckillExecutiton.toString());
    }

    @Test
    public void executeSeckillLogic() throws Exception {
        long id = 1001;
        Exposer exposer =seckillService.exportSeckillUrl(id);
        if(exposer.isExposed()){
            long phone = 12233;
            String md5=exposer.getMd5();
            try {
                SeckillExecutiton seckillExecutiton = seckillService.executeSeckill(id,phone,md5);
            }catch (SeckillCloseException e){
                logger.error(e.getMessage());
            }catch (RepeatKillException e){
                logger.error(e.getMessage());
            }
            catch (SeckillException e) {
                logger.error(e.getMessage());
            }
        }else {
            //秒杀未开启
            logger.warn("exposer={}",exposer);
        }
    }

    @Test
    public void executeSeckillByProcedureLogic() throws Exception {
        long id = 1004;
        Exposer exposer =seckillService.exportSeckillUrl(id);
        if(exposer.isExposed()){
            long phone = 12121231;
            String md5=exposer.getMd5();
            try {
                SeckillExecutiton seckillExecutiton = seckillService.executeSeckillProcedure(id,phone,md5);
                logger.info(seckillExecutiton.getStateInfo());
            }catch (SeckillCloseException e){
                logger.error(e.getMessage());
            }catch (RepeatKillException e){
                logger.error(e.getMessage());
            }
            catch (SeckillException e) {
                logger.error(e.getMessage());
            }
        }else {
            //秒杀未开启
            logger.warn("exposer={}",exposer);
        }
    }

}
package cn.seckill.web;

import cn.seckill.dto.Exposer;
import cn.seckill.dto.SeckillExecutiton;
import cn.seckill.dto.SeckillResult;
import cn.seckill.entity.Seckill;
import cn.seckill.enums.SeckillStatEnum;
import cn.seckill.exception.RepeatKillException;
import cn.seckill.exception.SeckillCloseException;
import cn.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by huangfei on 2016/12/31.
 */
@Controller
@RequestMapping("/seckill")//url:/模块/资源/{id}/细分 /seckill/list
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        List<Seckill> seckills = seckillService.getSeckillList();
        model.addAttribute("list", seckills);
        return "list";
    }

    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(Model model, @PathVariable("seckillId") Long seckillId) {
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    @RequestMapping(value = "/{seckillId}/exposer",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable(value = "seckillId") Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            result = new SeckillResult<Exposer>(false,e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution",method = RequestMethod.POST)
    @ResponseBody
    public SeckillResult<SeckillExecutiton> execute(@PathVariable("seckillId")Long seckillId,@PathVariable("md5")String md5,@CookieValue(value ="killPhone",required = false)Long phone){

        if(phone==null){
            return new  SeckillResult<SeckillExecutiton>(false,"未注册");
        }
        SeckillExecutiton seckillExecutiton;
        try {
//             seckillExecutiton = seckillService.executeSeckill(seckillId,phone,md5);
//通过存储调用
             seckillExecutiton = seckillService.executeSeckillProcedure(seckillId,phone,md5);
            return new SeckillResult<SeckillExecutiton>(true,seckillExecutiton);
        }catch (RepeatKillException e){
             seckillExecutiton = new SeckillExecutiton(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecutiton>(false,seckillExecutiton);

        }catch (SeckillCloseException e){
             seckillExecutiton = new SeckillExecutiton(seckillId, SeckillStatEnum.END);
            return new SeckillResult<SeckillExecutiton>(false,seckillExecutiton);
        }
        catch (Exception e) {
            logger.error(e.getMessage(),e);
            seckillExecutiton = new SeckillExecutiton(seckillId, SeckillStatEnum.INSERT_ERROR);
            return new SeckillResult<SeckillExecutiton>(false,seckillExecutiton);
        }
    }

    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date now = new Date();
        return new SeckillResult<Long>(true,now.getTime());
    }
}

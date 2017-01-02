package cn.seckill.dao.cache;

import cn.seckill.entity.Seckill;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/**
 * Created by huangfei on 2017/1/2.
 */
public class RedisDao {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private JedisPool jedisPool;

    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public RedisDao(String ip,int port){
        jedisPool = new JedisPool(ip,port);
    }

    public Seckill getSeckill(long seckillId){
        //redis操作的逻辑
        Jedis jedis = jedisPool.getResource();
        try {
            String key  = "seckillId :"+seckillId;
            //并没有实现内部序列化操作
            //get -> byte[]->反序列化->Object(Seckill)
            //采用自定义的序列化Api protostuff ： pojo
            byte[] bytes = jedis.get(key.getBytes());
            //获取到了
            if(bytes!=null){
                Seckill seckill = schema.newMessage();
                ProtobufIOUtil.mergeFrom(bytes,seckill,schema);
                //seckill 被反序列化
                return seckill;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }finally {
            jedis.close();
        }
        return null;
    }

    public String putSeckill(Seckill seckill){
        //set Object(seckill) -> 序列化 -> byte[]
        Jedis jedis = jedisPool.getResource();
        String key = "seckillId :"+seckill.getSeckillId();
        byte[] bytes = ProtobufIOUtil.toByteArray(seckill,schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        int timeout = 60*60;//1小时
        String result = jedis.setex(key.getBytes(),timeout,bytes);
        return result;
    }
}

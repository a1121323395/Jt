package com.jt.aspect;

import com.jt.annotation.AddJedis;
import com.jt.util.ObjectMapperUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisCluster;

@Aspect
@Component
public class JedisAspect {
    @Autowired(required = false)//required表示是否必须的
    private JedisCluster jedis;//redis集群
    //private Jedis jedis;    //哨兵机制
    //private ShardedJedis jedis; //分片机制
    //private Jedis jedis;//单台redis

    @Pointcut("@annotation(com.jt.annotation.AddJedis)")
    public void jedisPonitCut() {
    }

    @Around("jedisPonitCut()")
    public Object aroundAdvice(ProceedingJoinPoint jp) {
        String key = getKey(jp);//获取key
        int seconds = getSeconds(jp);//获取seconds
        String json = jedis.get(key);//通过key从redis中获取数据
        try {
            if ((StringUtils.isEmpty(json))) {//如果redis中没有数据
                Object value = jp.proceed();//执行目标方法,从数据库查询数据
                if (seconds == 0)//如果seconds为0
                    jedis.set(key,ObjectMapperUtil.toJSON(value));//直接存入redis
                else
                    jedis.setex(key,seconds,ObjectMapperUtil.toJSON(value));//存入redis并设置超时时间
                return value;//返回查询到的数据
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException(throwable);
        }
        System.out.println("从redis获取数据");
        return ObjectMapperUtil.toObject(json,getReturnType(jp));//返回redis中的数据
    }

    //获取key
    private String getKey(ProceedingJoinPoint jp) {
        String key = ((MethodSignature) jp.getSignature()).getMethod().getAnnotation(AddJedis.class).key();//获取注解中的key值作为key
        if (!StringUtils.isEmpty(key))//如果key不为空
            return key;//返回用户自定义的key
        String className = jp.getSignature().getDeclaringTypeName();//获取类名
        String methodName = jp.getSignature().getName();//获取方法名
        Object[] args = jp.getArgs();//获取方法参数//获取参数列表
        key = className + "." + methodName + "::" + args[0];//动态生成key
        return key;
    }

    //获取seconds
    private int getSeconds(ProceedingJoinPoint jp) {
        return ((MethodSignature) jp.getSignature()).getMethod().getAnnotation(AddJedis.class).seconds();
    }

    //获取返回值类型
    private Class<?> getReturnType(ProceedingJoinPoint jp) {
        return ((MethodSignature) jp.getSignature()).getReturnType();
    }
}

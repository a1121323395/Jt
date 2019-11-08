package com.jt.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.util.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import redis.clients.jedis.JedisCluster;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service(timeout = 300)
public class DubboUserServiceImpl implements DubboUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JedisCluster jedisCluster;

    @Override
    public void saveUser(User user) {
        //String solt=user.getPassword()+"www.jt.com";//加盐值
        //防止email为null时报错，使用电话号码报错
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());//将密码进行md5+哈希加密
        user.setPassword(md5Pass).setEmail(user.getPhone()).setCreated(new Date()).setUpdated(user.getCreated());
        userMapper.insert(user);
    }

    /*@Override//实现登录操作
    public String doLogin(User user) {
        User result = findUserByUP(user);//查询数据
        if (result != null) {//有数据
            String uuid= UUID.randomUUID().toString();//生成唯一的UUID
            String ticket = DigestUtils.md5DigestAsHex(uuid.getBytes());//生成加密密钥
            System.out.println(ticket);
            //将某些敏感数据进行脱敏操作
            result.setPassword("********");//密码脱敏
            String json = ObjectMapperUtil.toJSON(result);//将user对象转换为json串
            jedisCluster.setex(ticket,7*24*3600,json);//将信息存入redis,保存7天
            return ticket;//返回密钥
        }
        return null;
    }*/

    @Override//实现登录操作
    public String doLogin(User user,String ip) {
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
        User userDB = userMapper.selectOne(queryWrapper);
        if (userDB == null) //说明 用户名和密码不正确
            return null;
        //表示用户信息正确. 保存ticket/ip/userjson
        String uuid = UUID.randomUUID().toString();
        String ticket = DigestUtils.md5DigestAsHex(uuid.getBytes());
        userDB.setPassword("你猜猜!!!!!!");
        String userJSON = ObjectMapperUtil.toJSON(userDB);
        Map<String, String> hash = new HashMap<String, String>();
        hash.put("JT_TICKET",ticket);
        hash.put("JT_USERJSON",userJSON);
        hash.put("JT_IP",ip);
        jedisCluster.hmset(user.getUsername(),hash);
        jedisCluster.expire(user.getUsername(),7 * 24 * 3600);
        return ticket;
    }

    //对密码进行加密并根据用户名和加密后的密码查询数据
    public User findUserByUP(User user) {
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());//密码加密
        user.setPassword(md5Pass);//设置加密后的密码
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);//设置查询条件
        return userMapper.selectOne(queryWrapper);//返回查询的数据
    }
}

package com.jt.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.service.UserService;
import com.jt.util.IPUtil;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 根据用户信息实现数据的校验
     * 返回值结果：
     * true：已经存在     false：用户可以使用该数据
     * @return 校验信息
     */
    @RequestMapping("/check/{param}/{type}")
    public JSONPObject checkUser(@PathVariable String param,@PathVariable Integer type,String callback) {
        Boolean data = userService.checkUser(param,type);
        return new JSONPObject(callback,SysResult.success(data));
    }

    /**
     * 根据ticket查询用户信息
     * @param ticket 加密密钥
     * @return 查询到的用户信息
     */
    @RequestMapping("/query/{ticket}/{username}")
    public JSONPObject findUserByTicket(
            @PathVariable String ticket,
            @PathVariable String username,
            String callback,
            HttpServletRequest request) {
        //校验用户的IP地址
        String ip = IPUtil.getIpAddr(request);
        String localIP = jedisCluster.hget(username, "JT_IP");
        if(!ip.equalsIgnoreCase(localIP))
            return new JSONPObject(callback, SysResult.fail());
        //校验ticket信息
        String localTicket = jedisCluster.hget(username, "JT_TICKET");
        if(!ticket.equalsIgnoreCase(localTicket))
            return new JSONPObject(callback, SysResult.fail());
        //短信验证 200
        //人脸识别/指纹 移动端
        //说明用户信息正确
        String userJSON = jedisCluster.hget(username, "JT_USERJSON");
        System.out.println(userJSON);
        return new JSONPObject(callback,SysResult.success(userJSON));
    }
}

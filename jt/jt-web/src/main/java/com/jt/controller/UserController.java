package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.util.CookieUtil;
import com.jt.util.IPUtil;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController {
    @Reference(check = false)	//不需要校验
    private DubboUserService userService;
    @Autowired
    private JedisCluster jedisCluster;

    //注册：http://www.jt.com/user/register.html
    //登录：http://www.jt.com/user/login.html
    @RequestMapping("/{moduleName}")
    public String module(@PathVariable String moduleName) {
        return moduleName;
    }

    @ResponseBody
    @RequestMapping("/doRegister")
    public SysResult saveUser(User user) {
        userService.saveUser(user);
        return SysResult.success();
    }

    /**
     * setMaxAge（>0）； 存活的声明周期 单位秒
     * setMaxAge（0）；立即删除cookie
     * setMaxAge（-1）；会话关闭，删除cookie
     *
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping("/doLogin")
    public SysResult doLogin(User user,HttpServletResponse response,HttpServletRequest request) {
        //获取用户ip信息
        String ip = IPUtil.getIpAddr(request);
        //完成用户信息的校验
        String ticket = userService.doLogin(user,ip);
        if (StringUtils.isEmpty(ticket))
            return SysResult.fail();
        //将用户信息写入cookie
        CookieUtil.addCookie(request,response,"JT_TICKET",ticket,7 * 24 * 3600,"jt.com");
        CookieUtil.addCookie(request,response,"JT_USERNAME",user.getUsername(),7 * 24 * 3600,"jt.com");
        return SysResult.success();
    }
   /* public SysResult doLogin(User user,HttpServletResponse response) {
        String ticket = userService.doLogin(user);
        if (StringUtils.isEmpty(ticket))
            return SysResult.fail("用户名或密码错误");
        //将ticket保存到客户端的cookie中
        Cookie cookie = new Cookie(TICKET,ticket);
        cookie.setMaxAge(7 * 24 * 3600);//7天有效
        cookie.setPath("/");//cookie权限设定（路径）
        cookie.setDomain("jt.com");
        response.addCookie(cookie);//设置cookie
        return SysResult.success();
    }*/

    /**
     * 退出登录（删除对应的cookie及redis中对应的数据）
     *
     * @param request  请求对象
     * @param response 响应对象
     * @return 重定向到首页
     */
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String ticket = null;
        if (cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JT_TICKET")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        if (!StringUtils.isEmpty(ticket)) {
            jedisCluster.del(ticket);
            Cookie cookie = new Cookie("JT_TICKET","");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            cookie.setDomain("jt.com");
            response.addCookie(cookie);
        }
        return "redirect:/";
    }
}

package com.jt.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jt.pojo.User;
import com.jt.util.CookieUtil;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.ThreadLocalUtil;

import redis.clients.jedis.JedisCluster;

import java.util.Map;

@Component
public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 方法执行之前执行
     *
     * @param request
     * @param response
     * @param handler
     * @return 是否放行（true | false）
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception {
        String ticket = CookieUtil.getCookieValue("JT_TICKET",request);
        String username = CookieUtil.getCookieValue("JT_USERNAME",request);
        if (!StringUtils.isEmpty(ticket)&&!StringUtils.isEmpty(username)) {
            String json = jedisCluster.hget(username,"JT_USERJSON");
            if (!StringUtils.isEmpty(json)) {
                User user = ObjectMapperUtil.toObject(json,User.class);
                System.out.println(user);
                ThreadLocalUtil.set(user);
                System.out.println("用户信息保存到域中!!!");
                return true;    //表示程序放行
            }
        }
        //一般拦截器中的false和重定向联用
        //应该重定向到登录页面
        response.sendRedirect("/user/login.html");
        return false; //表示拦截
    }

    /**
     * 方法执行之后执行
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request,HttpServletResponse response,Object handler,ModelAndView modelAndView) throws Exception {
        //System.out.println("方法执行之后：post");
    }

    /**
     * 方法完成的最后阶段
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request,HttpServletResponse response,Object handler,Exception ex) throws Exception {
        ThreadLocalUtil.remove();
        //System.out.println("拦截器最后的管理范围");
    }
}

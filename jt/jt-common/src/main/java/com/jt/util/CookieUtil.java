package com.jt.util;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CookieUtil {
    /**
     * 通过cookie名称获取cookie的值
     */
    public static String getCookieValue(String cookieName,HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies.length == 0 || cookies == null)
            return null;//如果没有cookie，则直接返回null
        String value = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                value = cookie.getValue();
                break;
            }
        }
        return value;
    }

    /**
     * 添加cookie
     * @param request 请求对象
     * @param response 响应对象
     * @param cookieName cookie名字
     * @param cookieVlaue cookie内容
     * @param seconds cookie存活时间
     * @param domain cookie权限
     */
    public static void addCookie(
            HttpServletRequest request,
            HttpServletResponse response,
            String cookieName,
            String cookieVlaue,
            int seconds,
            String domain) {
        Cookie cookie = new Cookie(cookieName,cookieVlaue);
        cookie.setMaxAge(seconds);
        cookie.setPath("/");
        cookie.setDomain(domain);
        response.addCookie(cookie);
    }
}

package com.jt.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HttpClientService {
    @Autowired
    private CloseableHttpClient httpClient;
    @Autowired
    private RequestConfig requestConfig;

    /**
     * 目的：用户传递参数，工具API发送请求，将返回值结果返回给用户
     *
     * @param url     请求url
     * @param params  传递的参数
     * @param charset 字符集编码格式
     * @return
     */
    public String doGet(String url,Map<String, String> params,String charset) {
        //1.判断字符集编码是否为空 如果为空则给定默认值utf-8
        if (StringUtils.isEmpty(charset))
            charset = "utf-8";
        //2.判断用户是否需要传递参数
        if (params!=null) {//有参数
            url += "?";//拼接？号
            for (Map.Entry<String, String> entry : params.entrySet())//遍历参数集合
                url += entry.getKey() + "=" + entry.getValue() + "&";//拼接参数
            url = url.substring(0,url.length() - 1);//去除多余的&
        }

        //3.定义get请求对象
        HttpGet get = new HttpGet(url);
        get.setConfig(requestConfig);//定义请求超时时间

        String result = null;//返回值
        //4.发起http请求
        try {
            CloseableHttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == 200)
                result = EntityUtils.toString(response.getEntity(),charset);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

    public String doGet(String url) {
        return doGet(url,null,null);
    }

    public String doGet(String url,Map<String, String> params) {
        return doGet(url,params,null);
    }

    public String doGet(String url,String charset) {
        return doGet(url,null,charset);
    }

    //实现httpClient POST提交
    public String doPost(String url,Map<String, String> params,String charset) {
        String result = null;

        //1.定义请求类型
        HttpPost post = new HttpPost(url);
        post.setConfig(requestConfig);    //定义超时时间

        //2.判断字符集是否为null
        if (StringUtils.isEmpty(charset)) {
            charset = "UTF-8";
        }

        //3.判断用户是否传递参数
        if (params != null) {
            //3.2准备List集合信息
            List<NameValuePair> parameters = new ArrayList<>();
            //3.3将数据封装到List集合中
            for (Map.Entry<String, String> entry : params.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
            }

            //3.1模拟表单提交
            try {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters,charset); //采用u8编码
                //3.4将实体对象封装到请求对象中
                post.setEntity(formEntity);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        //4.发送请求
        try {
            CloseableHttpResponse response = httpClient.execute(post);
            //4.1判断返回值状态
            if (response.getStatusLine().getStatusCode() == 200) {
                //4.2表示请求成功
                result = EntityUtils.toString(response.getEntity(),charset);
            } else {
                System.out.println("获取状态码信息:" + response.getStatusLine().getStatusCode());
                throw new RuntimeException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public String doPost(String url) {
        return doPost(url,null,null);
    }

    public String doPost(String url,Map<String, String> params) {
        return doPost(url,params,null);
    }

    public String doPost(String url,String charset) {
        return doPost(url,null,charset);
    }
}

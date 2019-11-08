package com.jt;

import com.jt.util.HttpClientService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)//启动Spring容器
public class TestHttpClient {
    @Autowired
    private HttpClientService httpClientService;

    /**
     * 1.实例化工具API对象
     * 2.确定请求url地址
     * 3.定义请求方式 get/post
     * 4.发起http请求，并且获取响应数据
     * 5.判断状态码status是否为200
     * 6.获取服务器的返回值数据
     * @throws IOException
     */
    @Test
    public void testGet() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();//实例化工具API对象
        String url="https://www.baidu.com";//定义请求url地址
        HttpGet get=new HttpGet(url);//定义请求方式 get
        CloseableHttpResponse response = client.execute(get);//发起http请求，并且获取响应数据
        if (response.getStatusLine().getStatusCode()==200){//判断状态嘛status是否为200
            String data = EntityUtils.toString(response.getEntity(),"utf-8");//获取服务器的返回值数据
            System.out.println(data);//显示html代码
        }
    }

    @Test
    public void tesHttpClient(){
        Map<String,String> params=new HashMap<>();
        params.put("id","10086" );
        params.put("name","艹" );
        String result = httpClientService.doGet("http://www.baidu.com",params,"utf-8");
        System.out.println(result);
    }
}

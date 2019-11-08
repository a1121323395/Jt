package com.jt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.Set;

@Configuration
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {

    @Value("${redis.nodes}")
    private String nodes;

    @Bean//配置redis集群
    public JedisCluster jedisCluster(){
        Set<HostAndPort> list = getSet();
        JedisCluster jedis = new JedisCluster(list);
        return jedis;
    }

    private Set<HostAndPort> getSet(){
        Set<HostAndPort> list = new HashSet<>();
        String[] arrayNodes=nodes.split(",");
        for(String node:arrayNodes){
            String host=node.split(":")[0];
            int port=Integer.parseInt(node.split(":")[1]);
            list.add(new HostAndPort(host,port ));
        }
        return list;
    }

   /* //@Bean//配置哨兵
    @Scope("prototype") //多例对象
    public Jedis jedis(JedisSentinelPool sentinelPool){
        return sentinelPool.getResource();
    }

    //@Bean //单例
    public JedisSentinelPool jedisSentinelPool() {
        Set<String> sentinels = new HashSet<>();
        sentinels.add(nodes);
        JedisSentinelPool sentinelPool = new JedisSentinelPool("mymaster", sentinels);
        return sentinelPool;
    }

    //@Value("${redis.host}")
    private String host;
    //@Value("${redis.port}")
    private Integer port;

   // @Bean//单台redis
    public Jedis jedis(){
        return new Jedis(host,port);
    }

    //@Bean//redis分片
    public ShardedJedis shardedJedis(){
        List<JedisShardInfo> shards=getList();
        return new ShardedJedis(shards);
    }

   //获取分片信息
    private List<JedisShardInfo> getList(){
        List<JedisShardInfo> shards=new ArrayList<>();
        String[] arrayNodes=nodes.split(",");
        for (String node:arrayNodes){
            String host = node.split(":")[0];
            int port = Integer.parseInt(node.split(":")[1]);
            shards.add(new JedisShardInfo(host,port ));
        }
        return shards;
    }*/
}

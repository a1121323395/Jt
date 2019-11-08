package com.jt;

import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestRedis {
    private Jedis jedis = new Jedis("192.168.88.131",6379);

    /**
     * 测试String类型
     * 参数说明:
     * host:redisIP地址
     * port:redis端口号
     */
    @Test
    public void testString() throws InterruptedException {
        jedis.set("1906","redis入门案例");
        String value = jedis.get("1906");
        System.out.println(value);

        //2.测试key相同时value是否覆盖(值被覆盖)
        jedis.set("1906","新的value");
        System.out.println(jedis.get("1906"));

        //3.如果值已经存在则不允许覆盖
        jedis.setnx("1906","NBA不转播了");
        System.out.println(jedis.get("1906"));

        //4.为数据添加超时时间
        jedis.set("time","超时测试");
        jedis.expire("time",60);
        //保证数据操作有效性(原子性)
        jedis.setex("time",10,"超时测试");
        for (int i = 0;i < 9;i++) {
            Thread.sleep(1000);
            System.out.println("当前数据还能活" + jedis.ttl("time") + "秒");
        }

        //5.要求key存在时不允许操作,并设置超时时间
        //nx:不允许覆盖   nx:允许覆盖
        //ex:单位秒          px:单位毫秒
        jedis.set("时间","测试是否有效","NX","EX",10);
        System.out.println(jedis.get("时间"));
        for (int i = 0;i < 9;i++) {
            Thread.sleep(1000);
            System.out.println("当前数据还能活" + jedis.ttl("时间") + "秒");
        }
    }

    /**
     * 2.测试hash散列
     */
    @Test
    public void tetHash() {
        jedis.hset("person","id","100");
        jedis.hset("person","name","琦玉");
        System.out.println(jedis.hgetAll("person"));
    }

    /**
     * 3.测试list集合
     */
    @Test
    public void testList() {
        jedis.rpush("list","1,2,3,4");
        System.out.println(jedis.lpop("list"));
        jedis.rpush("list","1","2","3","4");
        for (int i = 0;i < 4;i++)
            System.out.println(jedis.lpop("list"));
        jedis.rpush("list","1","2","3","4");
        for (int i = 0;i < 4;i++)
            System.out.println(jedis.lpop("list"));
    }

    /**
     * 4.测试事务
     */
    @SuppressWarnings("unused")
	@Test
    public void testTx() {
        Transaction transaction = jedis.multi();
        try {
            transaction.set("a","aa");
            transaction.set("b","bb");
            transaction.set("c","cc");
            int a = 1 / 0;
            transaction.exec();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.discard();
        }
    }

    /**
     * 测试redis分片
     */
    @SuppressWarnings("resource")
	@Test
    public void testShards() {
        List<JedisShardInfo> shards = new ArrayList<>();
        shards.add(new JedisShardInfo("192.168.88.131",6379));
        shards.add(new JedisShardInfo("192.168.88.131",6380));
        shards.add(new JedisShardInfo("192.168.88.131",6381));
        ShardedJedis jedis = new ShardedJedis(shards);
        for (int i = 1;i < 100;i++)
            jedis.set(String.valueOf(1900 + i),String.valueOf(1900 + i));
        for (int i = 1;i < 100;i++)
            System.out.println(jedis.getShardInfo(String.valueOf(1900 + i)) + ":" + jedis.get(String.valueOf(1900 + i)));
    }

    /**
     * 测试哨兵
     * 调用原理:用户通过哨兵连接主机,进行操作
     * 参数说明:
     * mastername:主机的变量名称
     * sentinels:redis节点信息 (set<String>)
     */
    @SuppressWarnings("resource")
	@Test
    public void testSentinel() {
        Set<String> sentinels = new HashSet<>();
        sentinels.add("192.168.88.131:26379");
        JedisSentinelPool jedisSentinelPool = new JedisSentinelPool("mymaster",sentinels);
        Jedis jedis = jedisSentinelPool.getResource();
        jedis.set("1906","哨兵测试成功");
        System.out.println(jedis.get("1906"));
    }

    @SuppressWarnings("unused")
	@Autowired
    private JedisCluster jedisCluster;

    /**
     * 测试redis集群
     */
    @SuppressWarnings("resource")
	@Test
    public void testCluster() {
        Set<HostAndPort> node = new HashSet<>();
        node.add(new HostAndPort("192.168.88.131",7000));
        node.add(new HostAndPort("192.168.88.131",7001));
        node.add(new HostAndPort("192.168.88.131",7002));
        node.add(new HostAndPort("192.168.88.131",7003));
        node.add(new HostAndPort("192.168.88.131",7004));
        node.add(new HostAndPort("192.168.88.131",7005));
        JedisCluster cluster = new JedisCluster(node);
        cluster.set("1906","redis集群测试成功" );
        System.out.println(cluster.get("1906"));
       /* jedisCluster.set("1907","redis集群注入测试成功");
        System.out.println(jedisCluster.get("1907"));*/
    }
}

package com.cjwei;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.JedisCluster;

public class ClusterSingleton {
    
	private volatile static JedisCluster jedisCluster = null;
    
    public static JedisCluster getJedisCluster(){
    	if (jedisCluster == null) {
			synchronized (ClusterSingleton.class) {
				if (jedisCluster == null) {
					//读取集群配置文件
					ApplicationContext app = new ClassPathXmlApplicationContext("jedisCluster.xml");
					//获取集群代理对象
					jedisCluster = (JedisCluster) app.getBean("jedisCluster");
				}
			} 
		}
    	return jedisCluster;
    }
}

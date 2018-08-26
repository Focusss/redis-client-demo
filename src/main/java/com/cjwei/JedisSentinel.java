package com.cjwei;


import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

/**
 * 
 * @Title:  JedisSentinel.java   
 * @Package com.cjwei   
 * @Description:    TODO redis哨兵架构+Jedis客户端调用方式    
 * @author: cjwei    
 * @date:   2018年8月26日 下午6:25:18   
 *
 */
public class JedisSentinel {
	
    private Jedis jedis;
	
	@Before
	public void springAndJedisLinkRedis(){
		try {
			//加载jedis配置文件
			ApplicationContext app = new ClassPathXmlApplicationContext("jedisSentinel.xml");
			//获取jedisPool代理对象
			JedisSentinelPool jedisSentinelPool = (JedisSentinelPool) app.getBean("jedisSentinelPool");
			
			//获取资源对象
			jedis = jedisSentinelPool.getResource();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSentinel() {
		try {
			jedis.set("jedisSenTest", "jedisSenTest");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}

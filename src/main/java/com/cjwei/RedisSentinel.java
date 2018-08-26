package com.cjwei;

import java.util.Collection;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.RedisServer;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 
 * @Title:  RedisSentinel.java   
 * @Package com.cjwei   
 * @Description:    TODO redis哨兵架构+spring-redis客户端调用方式   
 * @author: cjwei    
 * @date:   2018年8月26日 下午6:23:43   
 *
 */
public class RedisSentinel {
	@Test
	public void testSentinelBySpring() {
		try {
			ApplicationContext ctx = new ClassPathXmlApplicationContext("springRedisSentinel.xml");
		    StringRedisTemplate redisTemplate = ctx.getBean(StringRedisTemplate.class);
		    Collection<RedisServer> redisServers = redisTemplate.getConnectionFactory().getSentinelConnection().masters();
		    System.out.println(redisServers);
		    String key = "test";
		    String value = redisTemplate.opsForValue().get(key);
		    System.out.println(value);
		    redisServers = redisTemplate.getConnectionFactory().getSentinelConnection().masters();
		    System.out.println(redisServers);
		    redisTemplate.opsForValue().set(key,"New Master...");
		    value = redisTemplate.opsForValue().get(key);
		    System.out.println(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	 }
}

package com.cjwei;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @Title:  MyJedis.java   
 * @Package com.cjwei   
 * @Description:    TODO redis单实例+jedis(不使用配置文件加载)   
 * @author: cjwei    
 * @date:   2018年8月26日 下午8:22:55   
 *
 */
public class MyJedis {
	
	//1.连接单机的redis服务
	@Test
	public void linkRedis(){
		//jedis客户端关联到redis服务器 
		Jedis jedis = new Jedis("192.168.1.110", 6379);
		//操作redis服务器
		jedis.auth("cjwei123");
		jedis.set("address", "beijing's sunyi");
		//获取值
		String address = jedis.get("address");
		
		System.out.println(address);
	}
	
	
	//2.使用连接池连接redis服务
	@Test
	public void linkPoolRedis(){
		//配置连接池
		JedisPoolConfig conf = new JedisPoolConfig();
		//设置最大连接数
		conf.setMaxTotal(20);
		//最大空闲数
		conf.setMaxIdle(2);
	
		//使用连接池连接redis服务器
		JedisPool pool = new JedisPool(conf, "192.168.1.110", 6379);
		//获取redis连接资源
		Jedis jedis = pool.getResource();
		jedis.auth("cjwei123");
	/*	jedis.set("test:count","1");
		jedis.incr("test:count"); //incr-可以不用set key,直接实现递增效果
*/		
		//防止键冲突
		String isExists = jedis.set("test","1", "NX", "EX", 10); //OK /Null
		if(isExists != null || jedis.incr("test") <= 5){
		//通过	
			
		}else{
			
		}
		System.out.println(isExists);
	}
	
	@Test
	public void testHash(){
		//jedis客户端关联到redis服务器 
		Jedis jedis = new Jedis("192.168.1.110", 6379);
		//操作redis服务器
		jedis.auth("cjwei123");
		Map<String, String> map = jedis.hgetAll("user:1");
		String name = jedis.hget("user:1", "name");
		
		System.out.println("-----------------"+(709/900));
	}
	
	@Test
	public void testList(){
		//jedis客户端关联到redis服务器 
		Jedis jedis = new Jedis("192.168.1.110", 6379);
		//操作redis服务器
		jedis.auth("cjwei123");
		List<String> list  = jedis.lrange("user:1article", 0, -1);
		for(String key : list){
			Map<String, String> map = jedis.hgetAll(key);
			System.out.println(map.toString());
		}
	}

}

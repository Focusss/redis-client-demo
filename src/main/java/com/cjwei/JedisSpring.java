package com.cjwei;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ZParams;
import redis.clients.jedis.BinaryClient.LIST_POSITION;

/**
 * 
 * @Title:  MyJedis.java   
 * @Package com.cjwei   
 * @Description:    TODO redis单实例+jedis(使用配置文件加载)   
 * @author: cjwei    
 * @date:   2018年8月26日 下午8:22:55   
 *
 */
public class JedisSpring {
	
	private Jedis jedis;
	
	@Before
	public void springAndJedisLinkRedis(){
		
		//加载jedis配置文件
		ApplicationContext app = new ClassPathXmlApplicationContext("jedis.xml");
		//获取jedisPool代理对象
		JedisPool jedisPool = (JedisPool) app.getBean("jedisPool");
		
		//获取资源对象
		jedis = jedisPool.getResource();
		
	}
	
	//缓存
	//@Test
	public void testString1(){
	   String userKey = "user:1";
	   String userInfo = jedis.get(userKey);
	   if(userInfo == null){
		   jedis.setex(userKey, 3600, "mike");
	   }
	   System.out.println(userInfo);
	}
	
	
	//计数器
	//@Test
	public void testString2(){
		String countKey = "count:1";
		Long count =  jedis.incr(countKey);
		System.out.println(count);
	}
	
	//接口限速
	//@Test
	public void testString3() {
		String mobileKey = "limit:13512345678";
		String isExist = jedis.set(mobileKey,"1" , "NX", "EX", 3600);
		for (int i = 0; i < 10; i++) {
			if(isExist != null || jedis.incr(mobileKey) < 6){
				System.out.println("访问成功");
			}else{
				System.out.println("访问被拦截");
			}
		}
		System.out.println(jedis.get(mobileKey));
	}
	
	//@Test
	public void testHash1() {
		String userKey = "human:1";
		/*jedis.hset(userKey,"name","tom");
		String name = jedis.hget(userKey, "name");
		System.out.println(name);*/
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "tom");
		map.put("age", "10");
		jedis.hmset(userKey,map );
		List<String> list = jedis.hmget(userKey, "name","age");
		System.out.println(list);
		System.out.println(jedis.hgetAll(userKey));
	}
	
	
	//@Test
	public void testList1() {
		String userKey = "list:1";
		//jedis.rpush(userKey, "a","b","c");
		jedis.ltrim(userKey, 0, 3);
		jedis.linsert(userKey,LIST_POSITION.BEFORE, "b", "java");
		System.out.println(jedis.lrange(userKey, 0, -1));
		
	}

	//@Test
	public void tesHashAndtList() {
		String userArticleKey = "user:1:article";
		List<String> list = jedis.lrange(userArticleKey, 0, 9);
		if(list == null || list.size() == 0){
			for (int i = 0; i < 20; i++) {
				Map<String,String> map = new HashMap<String, String>();
				map.put("name", "mike"+i);
				jedis.hmset("article:"+i, map);
				jedis.lpush(userArticleKey, "article:"+i);
			}
		}else{
			for (String articleKey : list) {
                 System.out.println(jedis.hgetAll(articleKey));
			}
		}
	}
	
	//@Test
	public void tesSet() {
		String setKey = "mySet";
		String setKey1 = "youSet";
		jedis.sadd(setKey, "a","b","c");
		jedis.sadd(setKey1, "b","c","d");
		System.out.println(jedis.smembers(setKey));
		System.out.println("集合元素个数："+jedis.scard(setKey));
		System.out.println(jedis.sismember(setKey, "a"));
		System.out.println(jedis.srandmember(setKey, 2));
		System.out.println("交集："+jedis.sinter(setKey,setKey1));
		System.out.println("并集："+jedis.sunion(setKey,setKey1));
		System.out.println("差集："+jedis.sdiff(setKey,setKey1));
		
	}
	
	//@Test
	public void tesZSet() {
		String setKey = "user:ranking";
		Map<String,Double> scoreMembers = new HashMap<String, Double>();
		scoreMembers.put("mike", 90d);
		scoreMembers.put("frank", 200d);
		scoreMembers.put("tom", 300d);
		scoreMembers.put("kris", 1d);
		scoreMembers.put("martin", 90d);
	    jedis.zadd(setKey, scoreMembers);
	    System.out.println(jedis.zcard(setKey));
	    System.out.println("正序排序："+jedis.zrangeByScore(setKey,0,500));
	    System.out.println("反序排序："+jedis.zrevrange(setKey,0,2));
	}
	
}

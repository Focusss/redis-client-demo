package com.cjwei;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.BinaryJedisCluster;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisClusterConnectionHandler;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.util.JedisClusterCRC16;

/**
 * 
 * @Title:  MyJedisCluster.java   
 * @Package com.cjwei   
 * @Description:    TODO Redis cluster集群架构+Jedis调用方式    
 * @author: cjwei    
 * @date:   2018年8月26日 下午6:30:09   
 *
 */
public class MyJedisCluster {
	
	//@Test
	public void linkRedisCluster(){
		//配置连接池
		JedisPoolConfig conf = new JedisPoolConfig();
		//设置最大连接数
		conf.setMaxTotal(20);
		//最大空闲数
		conf.setMaxIdle(2);	
		
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		
		nodes.add(new HostAndPort("192.168.1.110", 6480));
		nodes.add(new HostAndPort("192.168.1.110", 6481));
		nodes.add(new HostAndPort("192.168.1.110", 6482));
		nodes.add(new HostAndPort("192.168.1.110", 6483));
		nodes.add(new HostAndPort("192.168.1.110", 6484));
		nodes.add(new HostAndPort("192.168.1.110", 6485));
		
		JedisCluster jedisCluster = new JedisCluster(nodes, 2000,2000,5,"cjwei123",conf);
		
		jedisCluster.set("job", "chengxuyuangulishi");
		
		String job = jedisCluster.get("job");
		
		System.out.println(job);
		
	}
	
	//使用spring加载集群配置文件
	//@Test
	public void spirngJedisCluster(){
	
		JedisCluster jedisCluster = ClusterSingleton.getJedisCluster();
		
		jedisCluster.set("myjob", "wudangsan");
		
		String job = jedisCluster.get("myjob");
		
		System.out.println(job);
	}
	
	/**
	 * 删除主节点数据
	 */
	@Test
	public void delClusterByPattern(){
        JedisCluster jedisCluster = ClusterSingleton.getJedisCluster();
        Map<String, JedisPool> jedisPoolMap = jedisCluster.getClusterNodes();
        for(Entry<String, JedisPool> entry : jedisPoolMap.entrySet()){
        	if(entry.getKey().contains("127.0.0.1")){
        		continue;
        	}
            //获取每个节点的jedis连接
            Jedis jedis = entry.getValue().getResource();
            //只删除主节点的数据
            if (!isMaster(jedis)) {
 			  continue;
 		   }
            //使用pipeline删除指定前缀的数据
            Pipeline pipeline = jedis.pipelined();
            ScanParams scanParams = new ScanParams();
            scanParams.count(100);
            scanParams.match("my*");
            String cursor="0";
            while(true){
               ScanResult<String>  scanResult = jedis.scan(cursor, scanParams);   
               List<String> keyList = scanResult.getResult();
               for(String key : keyList){
             	  pipeline.del(key);
               }
               pipeline.syncAndReturnAll();
               cursor = scanResult.getStringCursor();
               if("0".equals(cursor)){
             	  break;
               }
            }
         }
        System.out.println(jedisCluster.get("myjob"));
	}
	
	private boolean isMaster(Jedis jedis){
		String info  = jedis.info("replication");
		if(info.contains("role:master")){
			return true;
		}
		return false;
	}
}

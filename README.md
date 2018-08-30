# Redis 架构简介

&ensp;&ensp;项目里用到了`Redis Cluster`和`Redis Sentinel`两种集群架构方式进行实验

## Redis cluster

- **主节点**

master1 | master2 | master3 
---|--- |---
192.168.1.110:6480 | 192.168.1.110:6481 | 192.168.1.110:6482 

- **从节点**

slave1| slave2 | slave3
---|--- |---
192.168.1.110:6483 | 192.168.1.110:6484 | 192.168.1.110:6485

![redis-cluster](https://github.com/Focusss/redis-client-demo/blob/master/images/redis-cluster.png)  

&ensp;&ensp;若在redis cluster集群搭建过程中，遇到以下问题：
> -  Cannot assign requested address
> -  Could not get a resource from the pool  

可以参考我博客--[[阿里云上使用redis集群的一些问题](https://focusss.github.io/2018/08/30/阿里云上使用redis集群的一些问题)]文中的解决方案
## Redis Sentinel
- **哨兵节点**

sentinel1 | sentinel2 | sentinel3 
---|--- |---
192.168.1.110:26379  | 192.168.1.110:26380 | 192.168.1.110:26381

- **master1主从集群**

master1 | slave1-1 | slave1-2
---|--- |---
192.168.1.110:6379  | 192.168.1.110:6380 | 192.168.1.110:6381 

- **master2主从集群**

master2 | slave2-1 | slave2-2
---|--- |---
 192.168.1.110:6479 | 192.168.1.110:6480 | 192.168.1.110:6481 


![redis-sentinel](https://github.com/Focusss/redis-client-demo/blob/master/images/redis-sentinel.png)


&ensp;&ensp;在哨兵模式的架构下，目前项目里只实现了，对单个主从集群（即master1）的调用；后续将实现对多个主从集群的分片调用  

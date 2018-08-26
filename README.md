# Redis 架构简介

&ensp;&ensp;项目里用到了`Redis Cluster`和`Redis Sentinel`两种集群架构方式进行实验

## Redis cluster
master1 | master2 | master3 | slave1| slave2 | slave3
---|--- |---|--- |---|---
192.168.1.110:6480  | 192.168.1.110:6481 | 192.168.1.110:6482 | 192.168.1.110:6483 | 192.168.1.110:6484 | 192.168.1.110:6485

![redis-cluster](https://github.com/Focusss/redis-client-demo/images/redis-cluster.png)
## Redis Sentinel
- **哨兵节点**

sentinel1 | sentinel2 | sentinel3 
---|--- |---
192.168.1.110:26379  | 192.168.1.110:26380 | 192.168.1.110:26381

- **主从节点**

master1 | master2 | slave1-1 | slave1-2| slave2-1 | slave2-2
---|--- |---|--- |---|---
192.168.1.110:6379  | 192.168.1.110:6380 | 192.168.1.110:6381 | 192.168.1.110:6479 | 192.168.1.110:6480 | 192.168.1.110:6481

![redis-sentinel](https://github.com/Focusss/redis-client-demo/images/redis-sentinel.png)

` 在在哨兵模式的架构下，目前项目里只实现了，对单个主从集群（即master1）的调用；后续将实现对多个主从集群的分片调用`
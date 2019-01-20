# Hbase安装与配置

[TOC]

### 一、Hbase基本概念

[Hbase官网](http://hbase.apache.org/)

### 二、安装Hadoop

### 三、Hbase下载与安装

- 版本：1.2.0
- 安装包：hbase-1.2.0-cdh5.7.0.tar.gz

```shell
## 下载
wget http://archive.cloudera.com/cdh5/cdh/5/hbase-1.2.0-cdh5.7.0.tar.gz

## 解压缩
tar -zxf /root/demos/package/hbase-1.2.0-cdh5.7.0.tar.gz -C /root/demos/software/

## 建立软连接
ln -s /root/demos/software/hbase-1.2.0-cdh5.7.0/ /opt/
mv hbase-1.2.0-cdh5.7.0 hbase

## 配置环境变量
vim /etc/profile
export HBASE_HOME=/opt/hbase
export PATH=$HBASE_HOME/bin:$PATH
source /etc/profile
```

**配置**

```shell
## 配置环境变量
## 配置hbase-env.sh
cd $HBASE_HOME/conf/
vim hbase-env.sh

## 配置hbase-site.xml
cd $HBASE_HOME/conf/
vim hbase-site.xml
```

**hbase-env.sh**

```shell
## 设置jdk home
export JAVA_HOME=/opt/jdk/

## 设置使用独立的ZK
export HBASE_MANAGES_ZK=false
```

**hbase-site.xml**

```xml
<configuration>
    <property>
        <name>hbase.rootdir</name>
        <value>hdfs://localhost:9000/hbase</value>
      </property>
    <property>
        <name>hbase.cluster.distributed</name>
        <value>true</value>
    </property>
    <property>
        <name>hbase.zookeeper.quorum</name>
        <value>localhost:2181</value>
    </property>
    <property>
        <name>zookeeper.znode.parent</name>
        <value>/hbase</value>
    </property>
    <property>
        <name>zookeeper.znode.rootserver</name>
        <value>root-region-server</value>
    </property>
</configuration>
```

参数说明：

| 参数                      | 说明                                       |
| ------------------------- | ------------------------------------------ |
| hbase.rootdir             | hbase的存储目录，设置为hdfs根目录下的hbase |
| hbase.cluster.distributed | true:分布式模式（使用独立的ZK）            |
| base.zookeeper.quorum     | ZK的地址                                   |
| zookeeper.znode.parent    | ZK中的根目录                               |

### 四、启动Hbase

```shell
## 进入bin目录
cd $HBASE_HOME/bin/

## 启动
./start-hbase.sh

## 使用shell命令
hbase shell
```

**访问web页面**

```url
http://47.98.189.100:60010
```


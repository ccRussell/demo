# Kafka环境搭建

[TOC]

### 一、Kafka介绍

[Kafka官网](http://kafka.apache.org/)

### 二、Kafka安装

**版本：** 1.0.0

**安装包：** kafka_2.12-1.0.0.tgz

**关于安装包的说明：**

- 2.12代表此安装包是使用2.12版本的scala编译的，并不代表kafka的版本
- 1.0.0代表kafka的版本
- 使用1.0.0版本是因为1.0.0是一个里程碑的版本
- 目前公司使用的大多数为0.80版本和0.90版本

**解压缩安装：**

```shell
## 将安装包解压缩
tar -zxf /root/demos/package/kafka_2.12-1.0.0.tgz -C /root/demos/software/

## 建立软连接
ln -s /root/demos/software/kafka_2.12-1.0.0/ /opt/
mv /opt/kafka_2.12-1.0.0 kafka /opt/kafka

## 配置环境变量
vim /etc/profile
export KAFKA_HOME=/opt/kafka
export PATH=$KAFKA_HOME/bin:$PATH

## 使环境变量生效
source /etc/profile

## 修改kafka配置文件server.properties
vim /opt/kafka/conf/server.properties
```

**我们需要关注的配置文件如下：**

```properties
## kafka broker的id
broker.id=0

## kafka本地的地址和端口号
listeners=PLAINTEXT://:9092

## kafka log的地址
log.dirs=/opt/kafka/kafka-logs

## kafka需要使用zk
zookeeper.connect=localhost:2181
```

**启动Kafka**

```shell
## 由于已经建立了环境变量，不必到bin目录下执行

## 指定配置文件启动kafka
kafka-server-start.sh -daemon ../config/server.properties

## 查看进程
jps -lm | grep kafka
```

注意：

- 启动kafka之前请确保zk已经启动
- 这时可以在zk UI上看到关于kafka节点的信息了

### 三、Kafka常见shell命令

```shell
## 进入到kafka的安装目录
cd $KAFKA_HOME/bin

## 启动kafka
kafka-server-start.sh -daemon /opt/kafka/config/server.properties

## 创建topic
kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic kafka_streaming_topic

## 发送topic消息（生产者）
kafka-console-producer.sh --broker-list localhost:9092 --topic kafka_streaming_topic

## 消费topic消息（消费者）
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic kafka_streaming_topic

## 查看所有的topic命令
kafka-topics.sh --list --zookeeper localhost:2181

## 增加topic分区数
kafka-topics.sh --zookeeper localhost:2181  --alter --topic kafka_streaming_topic --partitions 10
```

### 四、Kafka管理UI部署

通zk类似，虽然安装了kafka服务端，但是我们对当前服务器中有多少个topic，多少个group等等信息并不了解，每次需要查看还需要输入一些shell命令，因此我们需要一个web UI页面来帮助我们了解当前kafka集群的信息

**KafkaOffsetMonitor**

我们使用github上开源的一个项目：https://github.com/quantifind/KafkaOffsetMonitor/releases/tag/v0.2.1

```shell
## 进入到package目录,下载jar包
cd /root/demos/package
wget https://github.com/quantifind/KafkaOffsetMonitor/releases/download/v0.2.1/KafkaOffsetMonitor-assembly-0.2.1.jar

## 将其复制到software目录下
cp /root/demos/package/KafkaOffsetMonitor-assembly-0.2.1.jar /opt/demos/software/kafka-offset-monitor/

## 启动
java -cp KafkaOffsetMonitor-assembly-0.2.1.jar com.quantifind.kafka.offsetapp.OffsetGetterWeb --zk 127.0.0.1:2181 --port 8088  --refresh 5.seconds --retain 1.days

## 浏览器访问
47.98.189.100/8088
```

### 五、总结

目前我们已经在机器上完成了kafka的安装（单机版）。但是我们目前与kafka的交互仅限于使用shell命令。我们搭建kafka的目的是学习使用它，而后面我们就使用java api来编写生产者和消费者的demo帮助大家更好的理解kafka


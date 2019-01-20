## Jstorm和kafka对接

### kafka相关命令

```shell
## 进入到kafka的安装目录
cd $KAFKA_HOME/bin

## 启动kafka
kafka-server-start.sh -daemon ../config/server.properties

## 创建topic
kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic kafka_streaming_topic

## 发送topic消息
kafka-console-producer.sh --broker-list localhost:9092 --topic kafka_streaming_topic

## 消费topic消息
kafka-console-consumer.sh --zookeeper localhost:2181 --topic kafka_streaming_topic

## 查看所有的topic命令
kafka-topics.sh --list --zookeeper localhost:2181

## 增加topic分区数
kafka-topics.sh --zookeeper localhost:2181  --alter --topic kafka_streaming_topic --partitions 10

## 删除topic

```

### kafka中的一些概念

#### broker

消息中间件处理节点。一个kafka节点就是一个broker，与机器节点无关；如果一台机器上部署一台kafka节点，那么该机器就只有一个broker，当然一台机器也可以部署多个kafka节点，也就会有多个broker。

#### group



#### topic

一类消息的标识，是用户使用kafka的最小单位，是一个逻辑概念。

#### partiton

物理概念，是kafka保存消息的最小单位。一个topic可以由多个partition组成，每个partiton的表现形式就是一个文件夹，命名方式为：<topic-name>-<partiton-id>。用户在创建topic的时候可以为该topic指定具体的partiton数量。

用户写入数据的时候不用关心具体的partiton，只需要将数据写入tpoic，kafka会将topic和partition对应起来。

#### message

partition的最小单位，用来传递数据。partition是由一条条message组成，它主要包括三个属性：offset、messageSize和data。












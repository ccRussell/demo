## Kafka基本概念和相关命令

### kafka基本概念

#### broker

消息中间件处理节点。一个kafka节点就是一个broker，与机器节点无关；如果一台机器上部署一台kafka节点，那么该机器就只有一个broker，当然一台机器也可以部署多个kafka节点，也就会有多个broker。

#### topic

一类消息的标识，是用户使用kafka的最小单位，是一个逻辑概念。

#### partiton

物理概念，是kafka保存消息的最小单位。**一个topic可以由多个partition组成**，每个partiton的表现形式就是一个文件夹，命名方式为：<topic-name>-<partiton-id>。用户在创建topic的时候可以为该topic指定具体的partiton数量。

用户写入数据的时候不用关心具体的partiton，只需要将数据写入tpoic，kafka会将topic和partition对应起来。

#### consumer group

consumer接收数据的时候是按照group来接收，kafka确保每一个partition的数据只能由同一个group中的同一个consumer来进行消费。如果想要重复消费，则需要建立新的group进行，ZK中保存这每个topic下的每个partition在每个group中消费的offset 。

对于同一个topic，kafka会对每一个group发送消息。

group是在创建consumer的过程中指定的，如果group不存在则自动创建group。也就是在生产者发送消息的时候只会指定topic。然后由group去订阅某个topic，在这个group下的consumer 实例会去消费这个topic，这个topic下的所有partitons会被consumer分配。假设我有4台机器去消费一个topic，这个topic有12个partiton，那么每台机器最好起3个线程，这样正好有12个线程去消费12个topic。

#### consumer

消息的消费者。每一个comsumer group是一个topic的订阅者，而其中的每个consumer则是消息的具体消费者;

在同一个group中，一个partition的消息只能被一个consumer消费。

#### message

partition的最小单位，用来传递数据。partition是由一条条message组成，它主要包括三个属性：offset、messageSize和data。

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














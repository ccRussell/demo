#Kakfa编程示例-Producer

### 一、引入pom依赖

```xml
<dependency>
  <groupId>org.apache.kafka</groupId>
  <artifactId>kafka_2.12</artifactId>
  <version>1.0.0</version>
</dependency>
```

这个pom会在你的项目工程中引入两个jar包：

1、org.apache.kafka:kafka-clients:1.0.0

2、org.apache.kafka:kafka_2.12:1.0.0

### 二、编写示例代码

**代码如下**

```java
package com.russell.kafka.producer;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @author liumenghao
 * @Date 2018/12/31
 */
public class KafkaMessageProducer {

    /**
     * 序列化方法
     */
    private static final String DEFAULT_SERIALIZER_CLASS = "kafka.serializer.StringEncoder";

    /**
     * kafka api中的生产者
     */
    private KafkaProducer kafkaProducer;

    /**
     * kafka生产者构造方法
     *
     * @param brokerList kafka节点信息
     * @param serializerClass 序列化消息的方式
     */
    public KafkaMessageProducer(String brokerList, String serializerClass) {
        Properties producerConfig;

        if (StringUtils.isBlank(serializerClass)) {
            producerConfig = createProducerConfig(brokerList, DEFAULT_SERIALIZER_CLASS);
        } else {
            producerConfig = createProducerConfig(brokerList, serializerClass);
        }
        kafkaProducer = new KafkaProducer(producerConfig);
    }

    /**
     * kafka生产者构造方法
     *
     * @param brokerList kafka节点信息
     */
    public KafkaMessageProducer(String brokerList) {

        Properties producerConfig = createProducerConfig(brokerList, DEFAULT_SERIALIZER_CLASS);

        kafkaProducer = new KafkaProducer(producerConfig);
    }

    /**
     * 发送消息到kafka
     *
     * @param topic 消息被发送到的topic
     * @param value 发送的消息的内容
     */
    public void sendMessage(String topic, String value) {
        ProducerRecord data = new ProducerRecord(topic, value);
        kafkaProducer.send(data);
    }

    /**
     * 关闭kafka,释放资源
     */
    public void close() {
        kafkaProducer.close();
    }

    /**
     * 创建kafka配置类
     *
     * @param brokerList
     * @param serializerClass
     * @return
     */
    private Properties createProducerConfig(String brokerList, String serializerClass) {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokerList);
        props.put("serializer.class", serializerClass);
        props.put("request.required.acks", "1");
        return props;
    }
}

```

**关键的几个类**

1、org.apache.kafka.clients.producer.KafkaProducer

生产者的构造方法类

2、org.apache.kafka.clients.producer.ProducerRecord

发送到kafka的数据的模型抽象

### 三、代码测试


## Kakfa编程示例-Producer

### 生产者

#### pom引入

```xml
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka_2.10</artifactId>
    <version>0.8.2.1</version>
    <exclusions>
        <exclusion>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
        </exclusion>
        <exclusion>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
        </exclusion>
        <exclusion>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

#### producer

```java
// 一个kafka生产者关心的两个参数：kafka broker地址、序列化消息的方式
package com.russell.kafka.producer;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author liumenghao
 * @Date 2018/12/31
 */
public class KafkaProducer {

    /**
     * 序列化方法
     */
    private static final String DEFAULT_SERIALIZER_CLASS = "kafka.serializer.StringEncoder";
    /**
     * kafka api中的生产者
     */
    private Producer producer;

    public KafkaProducer(String brokerList, String serializerClass){
        ProducerConfig config;

        if (StringUtils.isBlank(serializerClass)) {
            config = createProducerConfig(brokerList, DEFAULT_SERIALIZER_CLASS);
        } else {
            config = createProducerConfig(brokerList, serializerClass);
        }
        producer = new Producer(config);
    }
	
    /**
     * 构造方法，使用类中默认的序列化方法
     */
    public KafkaProducer(String brokerList) {

        ProducerConfig config = createProducerConfig(brokerList, DEFAULT_SERIALIZER_CLASS);

        producer = new Producer(config);
    }
	
    /**
     * 发送消息的具体方法
     */
    public void sendMessage(String topic, String value) {
        KeyedMessage data = new KeyedMessage(topic, value);
        producer.send(data);
    }

    public void close() {
        producer.close();
    }

    /**
     * 创建kafka配置类
     *
     * @param brokerList
     * @param serializerClass
     * @return
     */
    private ProducerConfig createProducerConfig(String brokerList, String serializerClass){
        Properties props = new Properties();
        props.put("metadata.broker.list", brokerList);
        props.put("serializer.class", serializerClass);
        props.put("request.required.acks", "1");
        ProducerConfig config = new ProducerConfig(props);
        return config;
    }
```


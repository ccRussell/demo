package com.russell.demo.kafka.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @author liumenghao
 * @Date 2019/1/13
 */
public class KafkaMessageProducer {

    /**
     * 序列化方法
     */
    private static final String DEFAULT_SERIALIZER_CLASS = "org.apache.kafka.common.serialization.StringSerializer";

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
        props.put("key.serializer", serializerClass);
        props.put("value.serializer", serializerClass);
        props.put("request.required.acks", "1");
        return props;
    }
}

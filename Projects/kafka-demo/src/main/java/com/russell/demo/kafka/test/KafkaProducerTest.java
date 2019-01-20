package com.russell.demo.kafka.test;

import com.russell.demo.kafka.util.KafkaMessageProducer;

import static com.russell.demo.kafka.common.Constant.KAFKA_BROKER;

/**
 * 往kafka中写消息的测试类
 *
 * @author liumenghao
 * @Date 2018/12/31
 */
public class KafkaProducerTest {

    private static final String TOPIC = "kafka_streaming_topic";

    private static int count;

    public static void main(String[] args) throws Exception{
        KafkaMessageProducer producer = new KafkaMessageProducer(KAFKA_BROKER);
        while (true) {
            Thread.sleep(1000);
            producer.sendMessage(TOPIC, "russell" + " " + count);
            System.out.println("11");
            count++;
        }
    }
}

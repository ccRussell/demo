package com.russell.demo.opentsdb.test;

import com.russell.demo.opentsdb.builder.Metric;
import com.russell.demo.opentsdb.builder.MetricBuilder;
import com.russell.demo.opentsdb.factory.TsdbClientFactory;
import com.russell.demo.opentsdb.response.Response;
import com.russell.demo.opentsdb.util.ExpectResponse;
import com.russell.demo.opentsdb.util.HttpClientImpl;

/**
 * @author liumenghao
 * @Date 2019/1/13
 */
public class TsdbClientWriteDataTest {

    public static void main(String[] args) throws Exception{
        String tsdbUrl = "http://47.98.189.100:4242";
        HttpClientImpl tsdbClient = TsdbClientFactory.getInstance(tsdbUrl, 1);
        MetricBuilder builder = MetricBuilder.getInstance();
        buildMetric(builder);
        Response response = tsdbClient.pushMetrics(builder, ExpectResponse.SUMMARY);
        System.out.println(response.getStatusCode());
    }

    public static void buildMetric(MetricBuilder builder){
        Long itemId = 173666L;
        Long timestamp = System.currentTimeMillis();
        Double val = 1d;
        String group1 = "bd-prod-iron10";
        Metric metricValue = builder.addMetric("skynet" + itemId);
        metricValue.setDataPoint(timestamp, val);
        metricValue.addTag("group" + 1,  group1);
    }
}

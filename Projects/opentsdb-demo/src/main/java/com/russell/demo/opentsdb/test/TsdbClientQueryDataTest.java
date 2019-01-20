package com.russell.demo.opentsdb.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.russell.demo.opentsdb.factory.TsdbClientFactory;
import com.russell.demo.opentsdb.request.Filter;
import com.russell.demo.opentsdb.request.Query;
import com.russell.demo.opentsdb.request.QueryBuilder;
import com.russell.demo.opentsdb.request.SubQueries;
import com.russell.demo.opentsdb.response.SimpleHttpResponse;
import com.russell.demo.opentsdb.util.Aggregator;
import com.russell.demo.opentsdb.util.HttpClientImpl;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author liumenghao
 * @Date 2019/1/16
 */
public class TsdbClientQueryDataTest {

    public static void main(String[] args) throws Exception {
        String tsdbUrl = "http://47.98.189.100:4242";
        HttpClientImpl tsdbClient = TsdbClientFactory.getInstance(tsdbUrl, 1);
        QueryBuilder builder = buildQuery();
        System.out.println(builder.build());
        SimpleHttpResponse response = tsdbClient.pushQueries(builder);
        String content = response.getContent();
        System.out.println(content);
        int statusCode = response.getStatusCode();
        if (statusCode == 200) {
            JSONArray jsonArray = JSON.parseArray(content);
            for (Object object : jsonArray) {
                JSONObject json = (JSONObject) JSON.toJSON(object);
                String metric = json.getString("metric");
                String tag = json.getString("tags");
                String dps = json.getString("dps");

                Map<Long, Double> dpsMap = JSON.parseObject(dps, new TypeReference<TreeMap<Long, Double>>() {
                });
                System.out.println(metric);
                System.out.println(tag);
                System.out.println(JSON.toJSONString(dpsMap));
            }
        }
    }

    /**
     * 构建查询对象
     * 1、metric
     * 2、aggregator
     * 3、downSample
     * 4、filter
     * 5、start
     * 6、end
     *
     * @return
     */
    private static QueryBuilder buildQuery() {
        QueryBuilder builder = QueryBuilder.getInstance();
        Query query = builder.getQuery();
        // 单位秒，代表时间间隔为5分钟
        Integer timeSpan = 300;

        String metric = "skynet" + 173666L;
        String aggregator = Aggregator.sum.toString();
        String downSample = buildDownSample(timeSpan, aggregator);
        Long start = bucketStartTime(System.currentTimeMillis() - 30 * 60 * 1000L, timeSpan);
        Long end = Long.sum(start, 30 * 60 * 1000L);
        Filter filter = Filter.builder()
                .tagk("group1")
                .type("literal_or")
                .filter("bd-prod-iron10")
                .build();

        SubQueries subQuery = new SubQueries();
        subQuery.addMetric(metric)
                .addAggregator(aggregator)
                .addDownsample(downSample)
                .addFilter(filter);

        query.addStart(start)
                .addEnd(end)
                .addSubQuery(subQuery);
        return builder;
    }

    /**
     * 构造下采样的参数
     *
     * @param timeSpan
     * @param aggregator
     * @return
     */
    private static String buildDownSample(Integer timeSpan, String aggregator) {
        Integer interval = 1;
        String intervalType = "m-";
        switch (timeSpan) {
            //1分钟
            case 60: {
                interval = 1;
                intervalType = "m-";
                break;
            }
            //5分钟
            case 300: {
                interval = 5;
                intervalType = "m-";
                break;
            }
            //10分钟
            case 600: {
                interval = 10;
                intervalType = "m-";
                break;
            }
            //30分钟
            case 1800: {
                interval = 30;
                intervalType = "m-";
                break;
            }
            //1小时
            case 3600: {
                interval = 1;
                intervalType = "h-";
                break;
            }
            //3小时
            case 10800: {
                interval = 3;
                intervalType = "h-";
                break;
            }
            //6小时
            case 21600: {
                interval = 6;
                intervalType = "h-";
                break;
            }
            //12小时
            case 43200: {
                interval = 12;
                intervalType = "h-";
                break;
            }
            //1天
            case 86400: {
                interval = 1;
                intervalType = "d-";
                break;
            }
            //3天
            case 259200: {
                interval = 3;
                intervalType = "d-";
                break;
            }
            //7天
            case 604800: {
                interval = 7;
                intervalType = "w-";
                break;
            }
            //一个月
            case 2592000: {
                interval = 30;
                intervalType = "n-";
                break;
            }
            default: {
                break;
            }

        }
        return interval + intervalType + aggregator;
    }

    /**
     * 获取时间bucket的开始时间
     * 根据timeSpan对时间进行分桶
     *
     * @return
     */
    private static Long bucketStartTime(Long startTime, Integer timeSpan) {
        return startTime - startTime % timeSpan;
    }


}

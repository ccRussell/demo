package com.russell.demo.opentsdb.util;

import com.russell.demo.opentsdb.builder.MetricBuilder;
import com.russell.demo.opentsdb.request.QueryBuilder;
import com.russell.demo.opentsdb.response.Response;
import com.russell.demo.opentsdb.response.SimpleHttpResponse;

import java.io.IOException;

/**
 * @author liumenghao
 * @Date 2019/1/13
 */
public interface HttpClient extends Client {

    /**
     * 发出写入opentsdb的http调用
     *
     * @param builder
     * @param exceptResponse
     * @return
     * @throws IOException
     */
    Response pushMetrics(MetricBuilder builder, ExpectResponse exceptResponse) throws IOException;

    /**
     * 发出查询opentsdb的调用
     *
     * @param builder
     * @param exceptResponse
     * @return
     * @throws IOException
     */
    SimpleHttpResponse pushQueries(QueryBuilder builder, ExpectResponse exceptResponse) throws IOException;
}
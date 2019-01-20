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
public interface Client {

    /**
     * opentsdb写数据api的前缀
     */
    String PUT_POST_API = "/api/put";

    /**
     * oepntsdb查数据api的前缀
     */
    String QUERY_POST_API = "/api/query";

    Response pushMetrics(MetricBuilder builder) throws IOException;

    SimpleHttpResponse pushQueries(QueryBuilder builder) throws IOException;
}
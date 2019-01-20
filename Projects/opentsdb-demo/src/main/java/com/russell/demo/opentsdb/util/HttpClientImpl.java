package com.russell.demo.opentsdb.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.russell.demo.opentsdb.builder.MetricBuilder;
import com.russell.demo.opentsdb.request.QueryBuilder;
import com.russell.demo.opentsdb.response.ErrorDetail;
import com.russell.demo.opentsdb.response.Response;
import com.russell.demo.opentsdb.response.SimpleHttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * http客户端的实现
 *
 * @author liumenghao
 * @Date 2019/1/13
 */
@Slf4j
public class HttpClientImpl implements HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientImpl.class);

    /**
     * 访问的url
     */
    private String serviceUrl;

    private Gson mapper;

    private PoolingHttpClient httpClient = new PoolingHttpClient();

    /**
     * 构造方法
     *
     * @param serviceUrl
     * @param maxTotalConnections
     */
    public HttpClientImpl(String serviceUrl, int maxTotalConnections) {
        this.serviceUrl = serviceUrl;

        /**
         * 创建一个Gson实例
         */
        GsonBuilder builder = new GsonBuilder();
        mapper = builder.create();

        httpClient.setMaxTotalConnections(maxTotalConnections);
        httpClient.setMaxConnectionsPerRoute(maxTotalConnections);
        httpClient.build();
    }

    public HttpClientImpl(String serviceUrl, int maxTotalConnections, int connectTimeout, int waitTimeout, int readTimeout) {
        this.serviceUrl = serviceUrl;

        GsonBuilder builder = new GsonBuilder();
        mapper = builder.create();

        httpClient.setMaxTotalConnections(maxTotalConnections);
        httpClient.setMaxConnectionsPerRoute(maxTotalConnections);
        httpClient.setConnectTimeout(connectTimeout);
        httpClient.setWaitTimeout(waitTimeout);
        httpClient.setReadTimeout(readTimeout);
        httpClient.build();
    }

    @Override
    public Response pushMetrics(MetricBuilder builder) throws IOException {
        return pushMetrics(builder, ExpectResponse.STATUS_CODE);

    }

    @Override
    public Response pushMetrics(MetricBuilder builder, ExpectResponse expectResponse) throws IOException {
        checkNotNull(builder);

        SimpleHttpResponse response = httpClient.doPost(buildUrl(serviceUrl, PUT_POST_API, expectResponse),
                builder.build());

        return getResponse(response);
    }

    @Override
    public SimpleHttpResponse pushQueries(QueryBuilder builder) throws IOException {
        return pushQueries(builder, ExpectResponse.STATUS_CODE);

    }

    @Override
    public SimpleHttpResponse pushQueries(QueryBuilder builder,
                                          ExpectResponse expectResponse) throws IOException {
        checkNotNull(builder);

        SimpleHttpResponse response = httpClient.doPost(buildUrl(serviceUrl, QUERY_POST_API, expectResponse),
                builder.build());

        return response;
    }

    /**
     * 构造查询opentsdb的url
     *
     * @param serviceUrl
     * @param postApiEndPoint
     * @param expectResponse
     * @return
     */
    private String buildUrl(String serviceUrl, String postApiEndPoint,
                            ExpectResponse expectResponse) {
        String url = serviceUrl + postApiEndPoint;

        switch (expectResponse) {
            case SUMMARY:
                url += "?summary";
                break;
            case DETAIL:
                url += "?details";
                break;
            default:
                break;
        }
        System.out.println(url);
        return url;
    }

    private  Response getResponse(SimpleHttpResponse httpResponse) {
        Response response = new Response(httpResponse.getStatusCode());
        String content = httpResponse.getContent();
        if (StringUtils.isNotEmpty(content)) {
            if (!response.isSuccess()) {
                try {
                    ErrorDetail errorDetail = mapper.fromJson(content, ErrorDetail.class);
                    response.setErrorDetail(errorDetail);
                } catch (Exception e) {
                    logger.error("序列化error detail 失败, content: {}", content);
                }
            }
        }
        return response;
    }
}
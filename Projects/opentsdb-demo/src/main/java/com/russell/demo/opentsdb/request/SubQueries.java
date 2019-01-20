package com.russell.demo.opentsdb.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class SubQueries implements Cloneable {

    /**
     *
     */
    private String aggregator;

    /**
     * 查询的指标
     */
    private String metric;
    private Boolean rate = false;
    private Map<String, String> rateOptions;

    /**
     * 采样周期
     */
    private String downSample;

    /**
     * 所有的标签
     */
    private Map<String, String> tags = new HashMap<>();

    /**
     * 过滤器
     */
    private List<Filter> filters = new ArrayList<>();

    public SubQueries addAggregator(String aggregator) {
        this.aggregator = aggregator;
        return this;
    }

    public SubQueries addMetric(String metric) {
        this.metric = metric;
        return this;
    }

    public SubQueries addDownsample(String downSample) {
        this.downSample = downSample;
        return this;
    }

    /**
     * Tags are converted to filters in 2.2
     */
    @Deprecated
    public SubQueries addTag(Map<String, String> tag) {
        this.tags.putAll(tag);
        return this;
    }

    /**
     * Tags are converted to filters in 2.2
     */
    @Deprecated
    public SubQueries addTag(String tag, String value) {
        this.tags.put(tag, value);
        return this;
    }

    public SubQueries addFilter(Filter filter) {
        this.filters.add(filter);
        return this;
    }

    public SubQueries addAllFilter(List<Filter> filters) {
        this.filters.addAll(filters);
        return this;
    }
}

/*
 * Copyright 2013 Proofpoint Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.russell.demo.opentsdb.builder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;

/**
 * 构造向opentsdb写数据的json
 *
 * @author liumenghao
 * @Date 2019/1/13
 */
public class MetricBuilder {
    private List<Metric> metrics = new ArrayList<Metric>();
    private transient Gson mapper;

    private MetricBuilder() {
        GsonBuilder builder = new GsonBuilder();
        mapper = builder.create();
    }

    /**
     * Returns a new metric builder.
     *
     * @return metric builder
     */
    public static MetricBuilder getInstance() {
        return new MetricBuilder();
    }

    /**
     * Adds a metric to the builder.
     *
     * @param metricName metric name
     * @return the new metric
     */
    public Metric addMetric(String metricName) {
        Metric metric = new Metric(metricName);
        metrics.add(metric);
        return metric;
    }

    /**
     * Returns a list of metrics added to the builder.
     *
     * @return list of metrics
     */
    public List<Metric> getMetrics() {
        return metrics;
    }

    /**
     * Returns the JSON string built by the builder. This is the JSON that can
     * be used by the client add metrics.
     *
     * @return JSON
     * @throws IOException if metrics cannot be converted to JSON
     */
    public String build() throws IOException {
        for (Metric metric : metrics) {
            // 检查每一个metric是否都存在至少一个tag
            checkState(metric.getTags().size() > 0, metric.getName()
                    + " must contain at least one tag.");
        }
        return mapper.toJson(metrics);
    }
}

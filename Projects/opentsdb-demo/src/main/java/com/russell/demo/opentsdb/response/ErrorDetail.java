package com.russell.demo.opentsdb.response;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * List of errors returned by OpenTSDB.
 */
@Data
public class ErrorDetail {
    private Integer success;
    private Integer failed;
    private List<ErrorDetailEntity> errors;

    public ErrorDetail(List<ErrorDetailEntity> errors) {
        this.errors = errors;
    }

    public ErrorDetail(Integer success, Integer failed) {
        this.success = success;
        this.failed = failed;
    }

    public ErrorDetail(Integer success, Integer failed, List<ErrorDetailEntity> errors) {
        this.success = success;
        this.failed = failed;
        this.errors = errors;
    }

    @Data
    public static class ErrorDetailEntity {
        private DataPoint datapoint;
        private String error;
    }

    @Data
    public static class DataPoint {
        private String metric;
        private long timestamp;
        private Object value;
        private Map<String, String> tags = new HashMap<String, String>();
    }
}
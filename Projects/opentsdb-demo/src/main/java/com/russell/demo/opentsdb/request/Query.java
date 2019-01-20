package com.russell.demo.opentsdb.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Query {

    /**
     * 查询的开始时间
     */
    private long start;

    /**
     * 查询的结束时间
     */
    private long end;

    /**
     * 子查询
     */
    private List<SubQueries> queries = new ArrayList<SubQueries>();
    private Boolean noAnnotations = false;
    private Boolean globalAnnotations = false;
    private Boolean msResolution = false;
    private Boolean showTSUIDs = false;
    private Boolean showSummary = false;
    private Boolean showQuery = false;
    private Boolean delete = false;

    public Query() {
    }

    public Query(long start) {
        this.start = start;
    }

    public Query addSubQuery(SubQueries subQueries) {
        this.queries.add(subQueries);
        return this;
    }

    public Query addStart(long start) {
        this.start = start;
        return this;
    }

    public Query addEnd(long end) {
        this.end = end;
        return this;
    }
}

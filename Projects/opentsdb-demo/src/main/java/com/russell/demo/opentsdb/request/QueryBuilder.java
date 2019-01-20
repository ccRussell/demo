package com.russell.demo.opentsdb.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkState;

public class QueryBuilder {
    private Query query = new Query();
    private transient Gson mapper;

    private QueryBuilder() {
        GsonBuilder builder = new GsonBuilder();
        mapper = builder.create();
    }

    public static QueryBuilder getInstance() {
        return new QueryBuilder();
    }

    public Query getQuery() {
        return this.query;
    }

    public String build() throws IOException {
        // 判断metric数必须大于零
        checkState(query.getStart() > 0, " must contain start.");
        checkState(query.getQueries() != null, " must contain at least one subQuery.");
        return mapper.toJson(query);
    }
}

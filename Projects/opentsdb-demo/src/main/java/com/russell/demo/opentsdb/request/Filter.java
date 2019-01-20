package com.russell.demo.opentsdb.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Filter {
    private String type;
    private String tagk;
    private String filter;
    private Boolean groupBy = false;
}

package com.russell.demo.opentsdb.response;

import lombok.Data;

@Data
public class SimpleHttpResponse {

    /**
     * 状态码
     */
    private int statusCode;

    /**
     * 返回的内容
     */
    private String content;

    /**
     * 判断返回是否成功
     *
     * @return
     */
    public boolean isSuccess() {
        return statusCode == 200 || statusCode == 204;
    }
}

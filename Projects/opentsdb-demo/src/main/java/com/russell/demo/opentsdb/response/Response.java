package com.russell.demo.opentsdb.response;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response returned by the OpenTSDB server.
 */
@Data
@NoArgsConstructor
public class Response {

    /**
     * 返回的状态码
     */
    private int statusCode;
    private ErrorDetail errorDetail;

    public Response(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isSuccess() {
        return statusCode == 200 || statusCode == 204;
    }
}
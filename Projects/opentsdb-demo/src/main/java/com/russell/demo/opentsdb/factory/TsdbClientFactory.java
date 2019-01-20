package com.russell.demo.opentsdb.factory;

import com.russell.demo.opentsdb.util.HttpClientImpl;

/**
 * @author liumenghao
 * @Date 2019/1/13
 */
public class TsdbClientFactory {

    private volatile static HttpClientImpl tsdbClient;

    /**
     * 单例方法获取实例
     *
     * @param tsdbUrl
     * @param maxTotalConnections
     * @return
     * @throws Exception
     */
    public static HttpClientImpl getInstance(String tsdbUrl, int maxTotalConnections) throws Exception{
        if(tsdbClient == null){
            synchronized (TsdbClientFactory.class){
                if(tsdbClient == null){
                    try {
                        tsdbClient = new HttpClientImpl(tsdbUrl, maxTotalConnections);
                    }catch (Exception e){
                        throw new Exception(e.getMessage(), e);
                    }
                }
            }
        }
        return tsdbClient;
    }
}

# OpenTSDB写数据编程示例

[TOC]

### 一、Http API

[官网写数据API文档](http://opentsdb.net/docs/build/html/api_http/put.html)

#### api/put

put请求允许通过http请求在OpenTSDB中存储数据，而且只能通过与POST方法关联的内容来执行

为了节省带宽，put API允许客户端在单个请求中存储多个数据点。多个数据点之间不必以任何方式相关，各个数据点之间都是独立处理的。假如一次性存储100个数据点，其中一个数据有错误，但是仍然可以存入99个数据点。至于如何确定存储失败的数据点的详细信息，我们会在**响应体**小节中进行介绍

虽然API支持单请求多个数据点，但是API在所有请求完成之前都不会返回。因此单个请求的数据点数并不是越多越好。官网文档建议每个请求包含50个数据点。而且在使用http客户端的时候建议开启keep-alive属性，这样可以服用服务器的连接

### 二、请求参数

首先明确一点，api/put使用的是POST请求。

可以提供一些查询字符串参数来改变对put请求的响应

| 参数名称 | 是否必须 | 描述                                             | 示例            |
| -------- | -------- | ------------------------------------------------ | --------------- |
| summary  | 否       | 是否返回概要性的信息                             | api/put?summary |
| details  | 否       | 是否返回详细的信息                               | api/put?details |
| sync     | 否       | 是否等待所有的数据点存入到db后才返回结果（同步） | api/put?sync    |

下面是写数据时需要指定的一些参数：

| 参数名称  | 数据类型               | 是否必须 | 描述               | 示例             |
| --------- | ---------------------- | -------- | ------------------ | ---------------- |
| metric    | String                 | 是       | 表征该数据点的指标 | sys.cpu.nice     |
| timestamp | Integer                | 是       | 时间戳             | 1365465600       |
| value     | Integer，String，Float | 是       | 数据的值           | 42.5             |
| tags      | Map                    | 是       | 表征该数据点的标签 | {"host":"web01"} |

举两个例子让大家更加直观的了解写数据时的json参数

**单请求单数据点**：

```json
{
    "metric": "sys.cpu.nice",
    "timestamp": 1346846400,
    "value": 18,
    "tags": {
       "host": "web01",
       "dc": "lga"
    }
}
```

**单请求多数据点**：

```json
[
    {
        "metric": "sys.cpu.nice",
        "timestamp": 1346846400,
        "value": 18,
        "tags": {
           "host": "web01",
           "dc": "lga"
        }
    },
    {
        "metric": "sys.cpu.nice",
        "timestamp": 1346846400,
        "value": 9,
        "tags": {
           "host": "web02",
           "dc": "lga"
        }
    }
]
```

### 三、响应体

### 四、Java客户端


# OpenTsdb查询操作

我们将时间序列数据存入openTsdb，主要目的还是为了查询。但是在实际工作中，我们大部分情况下并不需要将时间序列所有的数据取出来展示，而是希望做一些预处理，例如分组，聚合，采样等等。而openTsdb恰恰为我们提供了多样的数据处理方式。本文我们就对一些典型的数据处理方式进行介绍。

## 一、查询参数

所有的数据预处理都是在查询数据的时候完成的，用户只需要在查询的时候指定特定参数即可。下面我们看下主要的查询参数都有哪些：

| 参数        | 数据类型       | 是否必填 | 描述                                   | 示例值        |
| ----------- | -------------- | -------- | -------------------------------------- | ------------- |
| Start Time  | String Integer | 是       | 查询范围的开始时间                     | 24h-ago       |
| End Time    | String Integer | 否       | 查询范围的结束时间<br />默认为当前时间 | 1h-ago        |
| Metric      | String         | 是       | 指标                                   | sys.spu.user  |
| Aggregation | String         | 是       | 聚合函数                               | sum           |
| Filter      | String         | 否       | 过滤器                                 | host=*,dc=lax |
| DownSampler | String         | 是       | 采样                                   | 1h-avg        |

其中Start Time，End Time以及Metric没有什么需要说明的，为了定位数据，这三个参数是必不可少的。同时这也告诉我们openTsdb的一次查询的最大粒度是Metric，也就是说我们不能通过一次查询获取多个Metric的数据。下面我们会着重讲解Filter、Aggregation以及Downsampler的含义以及使用场景。

> 注：如果想要了解openTsdb关于查询API的详细信息，请参考：
>
> http://opentsdb.net/docs/build/html/api_http/query/index.html

## 二、查询过滤器（Query Filter）

任何一款数据库产品都会提供各种形式的过滤器供我们查询全量数据的子集。openTsdb也不例外。我们上面已经提到在查询的时候需要指定起始时间以及Metric，这其实也是一种过滤方式。不过openTsdb中的过滤器主要是针对tagV的过滤，假设我们有这样一组数据：

| TS#  | Metric         | Tags                  | Value @ T1 |
| ---- | -------------- | --------------------- | ---------- |
| 1    | sys.cpu.system | dc=dal host=web01     | 3          |
| 2    | sys.cpu.system | dc=dal host=web02     | 2          |
| 3    | sys.cpu.system | dc=dal host=web03     | 10         |
| 4    | sys.cpu.system | host=web01            | 1          |
| 5    | sys.cpu.system | host=web01 owner=jdoe | 4          |
| 6    | sys.cpu.system | dc=lax host=web01     | 8          |
| 7    | sys.cpu.system | dc=lax host=web02     | 4          |

上面这组数据代表着同一个timestamp（T1）和Metric，但是不同tags的多条时间序列。下面我们通过例子来说明filter的使用方式。

**Example1:**```http://host:4242/q?start=1h-ago&m=sum:sys.cpu.system{host=web01}```

其中的参数m=sum:sys.cpu.system先不用关心，这是Aggregation的内容。我们主要注意：

> {host=web01}

这就是openTsdb的使用方式，它代表我们只要查询host=web01的数据集合，这就相当于SQL中的

> WHERE host=web01;

| TS#  | Metric         | Tags                  | Value @ T1 |
| ---- | -------------- | --------------------- | ---------- |
| 1    | sys.cpu.system | dc=dal host=web01     | 3          |
| 4    | sys.cpu.system | host=web01            | 1          |
| 5    | sys.cpu.system | host=web01 owner=jdoe | 4          |
| 6    | sys.cpu.system | dc=lax host=web01     | 8          |

**Example2:**```http://host:4242/q?start=1h-ago&m=sum:sys.cpu.system{host=web01,dc=dal}```

如果要指定多个查询参数，则使用逗号分隔即可，类似于SQL中的AND

> WHERE host=web01 AND dc=dal;

查询数据如下：

| TS#  | Metric         | Tags              | Value @ T1 |
| ---- | -------------- | ----------------- | ---------- |
| 1    | sys.cpu.system | dc=dal host=web01 | 3          |

**Example3:**```http://host:4242/q?start=1h-ago&m=sum:sys.cpu.system{owner=jdoe|dc=dal}```

“|”则相当于SQL中的OR连接符，这个很好理解，这里我就不贴数据了。

openTsdb中的过滤器的使用方式非常简单，我们只需要按照给定的语法使用即可。这里不做过多介绍。如果需要详细了解，可以参考官方文档：

> http://opentsdb.net/docs/build/html/user_guide/query/filters.html

## 三、聚合（Aggregations）

聚合是openTsdb提供的一个非常强大的功能，它能将多个时间序列的值根据给定的聚合函数动态的聚合为一个时间序列。为什么要使用聚合呢？我们在openTsdb中通过tag存储了不同维度的数据，但是我们一次查询中想要观察的其实只是某一个维度的数据。而且对于数据分析场景 ，大部分情况下，我们只关心一定粒度聚合的数据，而非每一行原始数据的细节情况。

以上述数据为例，我通过filter取出了所有host=web01的数据，但是大部分情况下，这并不是我们想要的。通常我们需要的是对所有host=web01的数据做一个聚合处理，例如求和，平均值，最大最小值等等。就好比:

> 我想得到某个时间点下的所有host=web01的cpu使用率的平均值

我们通过具体例子来体会下Aggregations的使用：

**Example1:**```http://host:4242/q?start=1h-ago&m=sum:sys.cpu.system{host=web01}```

借用filter中的一个query，这里的**m=sum:sys.cpu.system**则指定了聚合函数（sum）以及Metric，所以这个查询返回的数据格式应该如下：

| Time Series Included | Tags       | Aggregated Tags | Value @ T1 |
| -------------------- | ---------- | --------------- | ---------- |
| 1, 4, 5, 6           | host=web01 |                 | 16         |

前面我们一直使用SQL举例，看到这里你应该发现，这个聚合+filter其实类似于SQL中的group by，下面我们将```http://host:4242/q?start=1h-ago&m=sum:sys.cpu.system{host=web01}```解析成SQL语句：

```sql
SELECT SUM(value) FROM ts_table WHERE start = "1h-ago" AND end = "now"
AND metric = "sys.cpu.system"
AND host = "web-01"
GROUP BY timestamp;
```

group by的参数中一定是有**timestamp**的，因为对于时间序列数据，对不同时间点的数据做聚合没有意义。

如果想要进一步了解关于聚合的详细信息，请参考官方文档：

> http://opentsdb.net/docs/build/html/user_guide/query/aggregators.html

## 四、下采样（Downsampler）

下采样功能的目的是为了返回更少的数据。假定这样一种场景，温度传感器每秒采集一次温度数据存储到openTsdb。在查询的时候，如果时间范围为一个小时，则会返回3600个点，这时绘图还相对比较容易。但是如果需要看一周的数据，则会返回604800个数据点，这时图表可能会变得杂乱无章。而且，对于温度数据，我们想要了解的可能是一个趋势，是否存在波峰或者波谷，而并不关心具体到每秒的数据。我们可能只需要每一个小时绘制一个数据点，而这个数据点是这一个小时内所有数据的平均值。这就是openTsdb提供的下采用功能。

在上面的例子中我们最后提到了两个点：**每一个小时**，**所有数据的平均值**；这两个值正是我们使用下采样功能时所需要给定的两个参数：

- **Interval**:采样的时间间隔（一个时间桶）
- **Aggregation Function**:聚合函数，告诉openTsdb需要对时间区间内的数据如何处理

我们通过一个示例来体会下downsampler的使用。有两组时间序列A和B，两组时间序列都是每10s采集一次数据，我们希望每隔30s做一次采样，并且聚合函数使用sum。

| Time Series             | t0               | t0+10s | t0+20s | t0+30s           | t0+40s | t0+50s | t0+60 |
| ----------------------- | ---------------- | ------ | ------ | ---------------- | ------ | ------ | ----- |
| A                       | 5                | 5      | 10     | 15               | 20     | 5      | 1     |
| A `sum` Downsampled     | 5 + 5 + 10 = 20  |        |        | 15 + 20 + 5 = 40 |        |        | 1 = 1 |
| B                       | 10               | 5      | 20     | 15               | 10     | 0      | 5     |
| B `sum` Downsampled     | 10 + 5 + 20 = 35 |        |        | 15 + 10 + 0 = 25 |        |        | 5 = 5 |
| `sum` Aggregated Result | 55               |        |        | 65               |        |        | 6     |

这里需要注意的就是downsample中的Aggregate Function和m=sum:sys.cpu.system的Aggregate Function的区别。

> 下采样中的聚合是针对相同metric，相同tags，不同timestamp的数据点做的聚合；
> 而前面的聚合则是针对相同metric，相同timestamp，不同tags之间的聚合；

如果想要进一步了解下采样的内容，请参考官方文档：

> http://opentsdb.net/docs/build/html/user_guide/query/downsampling.html

| metric | timestamp           | tag1                  | tag2          | value(count) |
| ------ | ------------------- | --------------------- | ------------- | ------------ |
| 2366   | 2018-12-07 00:00:00 | application=pay-login | side=provider | 10           |
| 2366   | 2018-12-07 00:00:00 | application=skynet-bi | side=provider | 5            |
| 2366   | 2018-12-07 00:00:10 | application=pay-login | side=provider | 6            |
| 2366   | 2018-12-07 00:00:10 | application=skynet-bi | side=provider | 12           |
| 2366   | 2018-12-07 00:00:20 | application=pay-login | side=provider | 3            |
| 2366   | 2018-12-07 00:00:20 | application=skynet-bi | side=provider | 12           |


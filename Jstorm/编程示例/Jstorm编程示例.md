## Jstorm编程示例

### 建立Maven项目

### 引入相关jar包

```xml
<properties>
    <jstorm.version>2.2.1</jstorm.version>
</properties>

<dependencies>
    <dependency>
        <groupId>com.alibaba.jstorm</groupId>
        <artifactId>jstorm-core</artifactId>
        <version>${jstorm.version}</version>
        <!-- 上线打包打开 -->
        <scope>provided</scope>
    </dependency>

    <dependency>
        <groupId>io.netty</groupId>
        <artifactId>netty-all</artifactId>
        <version>4.1.6.Final</version>
    </dependency>
</dependencies>
```

###编写spout

```java
// 模拟数据源，单词产生器

@Slf4j
public class WordSpout extends BaseRichSpout {

    private SpoutOutputCollector collector;

    @Setter
    private static volatile boolean open = true;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void nextTuple() {
        String values = WordGenerator.generateString();
        log.info("spout values : " + values);
        this.collector.emit(Constants.WORD_SPOUT, new Values(values));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(Constants.WORD_SPOUT, new Fields(Constants.WORD_SPOUT_FIELDS));
    }
}
```



###编写bolt

```java
// 对单词进行处理的bolt（对单词进行格式化处理）

@Slf4j
public class WordFormatBolt extends BaseRichBolt {

private OutputCollector collector;

@Override
public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
    this.collector = collector;
}

@Override
public void execute(Tuple input) {
    try {
        String allWords = input.getStringByField(Constants.WORD_SPOUT_FIELDS);
        log.info("WordFormat : tuple.getStringByField() : " + allWords);
        if(allWords != null){
            String[] words = allWords.split(",");
            for(String word : words){
                this.collector.emit(Constants.WORD_FORMAT,new Values(word));
            }
        }
    } catch (Exception e) {
        log.error("WordFormat Error", e);
    }
    collector.ack(input);
}

@Override
public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declareStream(Constants.WORD_FORMAT, new Fields(Constants.WORD_FORMAT_FIELDS));
}
}


// 对单词进行计数
@Slf4j
public class WordCountBolt extends BaseRichBolt {

private OutputCollector collector;

@Override
public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
    this.collector = collector;
}

@Override
public void execute(Tuple input) {
    try {
        String word = input.getString(0);
        if (Constants.wordCounter.containsKey(word)) {
            Constants.wordCounter.put(word, Constants.wordCounter.get(word) + 1);
        } else {
            Constants.wordCounter.put(word, 1);
        }
    } catch (Exception e) {
        log.error("wordBolt Error", e);
    }

    collector.ack(input);
}

@Override
public void declareOutputFields(OutputFieldsDeclarer declarer) {

}

@Override
public void cleanup(){
    log.info("Word Count Result--------:");

    List<Map.Entry<String,Integer>> entries = new ArrayList<>(Constants.wordCounter.entrySet());
    Collections.sort(entries, Collections.reverseOrder(Map.Entry.comparingByValue()));

    for (Map.Entry<String,Integer> entry: entries) {
        if(entry.getValue()>2) {
            log.info(entry.getKey()+ "----" + entry.getValue());
        }
    }
    log.info("Word Count End -------");
}
```



### 编写topology

```java
@Slf4j
public class WordCountTopology {

    public static void main(String[] args) throws Exception{
        // 根据参数判断是本地运行模式还是集群模式
        boolean distribute = Boolean.parseBoolean(args[0].split("=")[1]);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("wordSpout", new WordSpout(),1);
        builder.setBolt("wordFormat", new WordFormatBolt(), 2).localOrShuffleGrouping("wordSpout",Constants.WORD_SPOUT);
        builder.setBolt(Constants.WORD_BOLT, new WordCountBolt(), 4).localOrShuffleGrouping("wordFormat",Constants.WORD_FORMAT);

        Config config = new Config();
        config.setDebug(false);
        config.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);

        if(distribute){
            StormSubmitter.submitTopology("wordCountTopology", config, builder.createTopology());
        }else {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("wordCountTopology", config, builder.createTopology());
            try {
                Thread.sleep(10 * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cluster.killTopology("wordCountTopology");
            cluster.shutdown();
        }
    }
}
```

### 本地调试模式

Jstorm提供了本地调试模式，也就是说不需要本地安装Jstorm即可对程序进行调试。

1、将Jstorm的jar包的scope注释掉

```xml
<dependency>
    <groupId>com.alibaba.jstorm</groupId>
    <artifactId>jstorm-core</artifactId>
    <version>${jstorm.version}</version>
    <!-- 上线打包打开 -->
    <!--<scope>provided</scope>-->
</dependency>
```

2、使用LocalCluster提交拓扑

```java
LocalCluster cluster = new LocalCluster();
cluster.submitTopology("wordCountTopology", config, builder.createTopology());
```

3、运行一段时间后将任务kill（非必须）

```java
Thread.sleep(10 * 60 * 1000);
cluster.killTopology("wordCountTopology");
cluster.shutdown();
```



### 集群模式

```java
StormSubmitter.submitTopology("wordCountTopology", config, builder.createTopology());
```

### 提交拓扑到集群运行

```shell
## 进入到bin目录
cd $JSTORM_HOME/bin

## 提交拓扑
./jstorm jar ~/projects/jstorm-demo/target/jstorm-demo-1.0-SNAPSHOT.jar com.russell.jstorm.WordCountTopology distribute=true

## 停止进程
./jstorm kill WordCountTopology
```




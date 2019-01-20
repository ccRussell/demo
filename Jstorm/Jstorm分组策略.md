## Jstorm分组策略

在一个实际的拓扑任务中，每一个spout和bolt都会有多个线程执行。因此他们之间的数据传递策略就显的尤为重要。分组（grouping）就是通过制定某种策略来决定spout和bolt或者bolt和bolt之间数据传递的规则。

本文就介绍jstorm提供的八种分组策略

### 1. fieldsGrouping

```java
/**
 * The stream is partitioned by the fields specified in the grouping.
 * 
 * @param componentId
 * @param fields
 * @return
 */
public T fieldsGrouping(String componentId, Fields fields);

/**
 * The stream is partitioned by the fields specified in the grouping.
 * 
 * @param componentId
 * @param streamId
 * @param fields
 * @return
 */
public T fieldsGrouping(String componentId, String streamId, Fields fields);
```

按照字段进行分组，字段相同的被归为同一组，然后被同一个task进行处理

### 2. localOrShuffleGrouping

```java
/**
 * There are 3 kind of tasks.
 * 
 * (1) The tasks run on the same worker process, this kind of tasks have the highest priority.
 * (2) The tasks run on the same node, but not in the same worker, this kind of task have the second  priority.
 * (3) The tasks run on other nodes, this kind of task have the lowest priority
 *
 * @param componentId
 * @return
 */
public T localOrShuffleGrouping(String componentId);

/**
 * There are 3 kind of tasks.
 * 
 * (1) The tasks run on the same worker process, this kind of tasks have the highest priority.
 * (2) The tasks run on the same node, but not in the same worker, this kind of task have the second  priority.
 * (3) The tasks run on other nodes, this kind of task have the lowest priority
 * 
 * @param componentId
 * @return
 */
public T localOrShuffleGrouping(String componentId, String streamId);
```


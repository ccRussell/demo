## Jstorm概念与编程模型

### 1. 集群架构

#### nimbus

Jstorm集群的主控节点。负责接收client提交的topology，分发代码给工作节点，监控集群中运行任务的状态等工作

#### supervisor

Jstorm集群的工作节点。supervisor通过ZK来监听nimbus分配过来的任务，然后根据此启动或者停止worker进程

#### worker

工作进程，Jstorm集群任务调度的最小单位。每个worker工作进程执行一个topology任务的子集。单个topoloy的任务由分布在多个supervisor工作节点上的多个worker工作进程协同处理

#### task

任务执行线程。Jstorm任务执行的最小单位。每个task实际就是一个线程，其中会执行一个spout或者bolt。

### 2. 编程模型

#### topology

topology是一个在jstorm中运行的任务的抽象表达，在jstorm的topology中，有两种组件：spout和bolt

#### spout

jstorm认为每个stream都有一个stream源，也就是数据的源头，它将这个源头抽象为spout

#### bolt

jstorm将tuple的处理逻辑抽象为bolt，bolt可以消费任意数量的输入流

### 3. 任务提交和调度

#### component的并行度

在拓扑中设置spout和bolt的时候需要为其设置并行度，并行度的概念就是需要jstorm启动多少个task来运行这个组件（spout或者bolt）

这里我们使用一个实例来了解上述概念之间的关系：

假设有一个4台机器的Jstorm集群，其中有一台主控节点nimbus，3台supervisor节点。这个集群上提交了一个testTopology的拓扑任务，拓扑包括一个spout和bolt，并发度分别设置为2和4。将任务的执行worker数设置为3，也就是需要3个工作进程来执行这个任务。

jstorm的任务调度算法会将worker均匀的分布在集群中的supervisor中，在例子中，一个supervisor会分配到一个工作进程。由于拓扑包含一个spout和bolt，每个组件的并行度为6，一共需要6个task来执行。tasks会被均匀的分配到不同的worker。本例中每个worker会被分配2个线程。最终的架构如下：

![jstorm任务执行架构](/Users/liumenghao/github/demo/Jstorm/picture/jstorm任务执行架构.png)

#### 创建一个拓扑（包含一个spout和bolt）

```java
TopologyBuilder builder = new TopologyBuilder();
// 设置一个spout，并行度为3
builder.setSpout(testSpout, new TestSpout(), 2);
// 设置一个bnolt，并行度为3
builder.setBolt(testBolt, new TestBolt(), 4);
// 创建拓扑
StormTopology testTopology = builder.createTopology();
```






















## Hadoop安装与配置

### 下载与安装

```shell
## 下载hadoop安装包
wget http://archive.cloudera.com/cdh5/cdh/5/hadoop-2.6.0-cdh5.7.0.tar.gz

## 解压
tar -zvxf hadoop-2.6.0-cdh5.7.0.tar.gz
```

### 搭建伪分布式运行环境（单机）

```shell
## 进入配置文件目录
cd $HADOOP_HOME/etc/hadoop

## 修改jdk目录
vim hadoop-env.sh
export JAVA_HOME=$JAVA_HOME

## 修改core-site.xml
vim core-site.xml

## 修改hdfs-site.xml
vim hdfs-site.xml
```

**core-site.xml**

```xml
<configuration>
<property>
    <name>fs.defaultFS</name>
    <value>hdfs://localhost:9000</value>
</property>
<property>
    <name>hadoop.tmp.dir</name>
    <value>/Users/liumenghao/bigData/temp/</value>>
</property>
</configuration>
```

配置参数说明：

|    配置参数    |           说明            |
| :------------: | :-----------------------: |
|  fs.defaultFS  | hadoo访问目录节点NameNode |
| hadoop.tmp.dir |   Hadoop的临时存放目录    |

**hdfs-site.xml**

```xml
<configuration>
    <property>
        <name>dfs.replication</name>
        <value>1</value>
    </property>
</configuration>
```

配置参数说明：

|    配置参数     |                说明                 |
| :-------------: | :---------------------------------: |
| dfs.replication | 数据在hdfs中的备份数量（默认是3份） |

### 设置SSH免密登陆localhost

```shell
## 检查是否可以免密登陆
ssh localhost

## 如果不可以，则使用如下命令进行设置（仅限Mac系统）
ssh-keygen -t rsa 
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
chmod og-wx ~/.ssh/authorized_keys
chmod 750 $HOME
```

### 启动Hadoop

```shell
## 格式化namenode
cd $HADOOP_HOME/bin/
./hdfs namenode fomat

## 启动hdfs
cd $HADOOP_HOME/sbin/
./start-dfs.sh
```

### 访问web界面

```url
http://localhost:50070/
```


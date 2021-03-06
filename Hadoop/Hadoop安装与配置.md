# Hadoop安装与配置

[TOC]

### 一、基本概念

[Hadoop官网](http://hadoop.apache.org/)

一、下载与安装

- 版本：2.6.0
- 安装包：hadoop-2.6.0-cdh5.7.0.tar.gz

```shell
## 解压缩
tar -zxf hadoop-2.6.0-cdh5.7.0.tar.gz -C /root/demos/software/

## 设置软连接
ln -s /root/demos/software/hadoop-2.6.0-cdh5.7.0/ /opt/
mv hadoop-2.6.0-cdh5.7.0 hadoop

## 配置环境变量
vim /etc/profile
export HADOOP_HOME=/opt/hadoop
export PATH=$HADOOP_HOME/bin:$PATH
source /etc/profile
```

### 二、配置与启动

####修改配置文件

**修改配置文件**

```shell
## 进入配置文件目录
cd $HADOOP_HOME/etc/hadoop

## 修改jdk目录
vim hadoop-env.sh
export JAVA_HOME=/opt/jdk/

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
    <value>/root/temp/</value>>
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

#### 启动Hadoop

**设置SSH免密登陆**

```shell
## 检查是否可以免密登陆
ssh localhost

## 如果不可以，则使用如下命令进行设置
## 生成公钥和私钥（安装git时已经完成）
ssh-keygen -t rsa -C "727769385@qq.com"
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys

## 修改权限（我使用的root账号，不用设置权限）
chmod 700 ~/.ssh
chmod 644 ~/.ssh/authorized_keys

## 验证
ssh localhost
```

**启动Hadoop**

```shell
## 格式化namenode
cd $HADOOP_HOME/bin/
./hdfs namenode -format

## 启动hdfs
cd $HADOOP_HOME/sbin/
./start-dfs.sh
```

### 三、访问WEB页面

```shell
http://localhost:50070/
```

### 四、总结




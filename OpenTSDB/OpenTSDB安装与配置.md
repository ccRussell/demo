# OpenTSDB安装与配置

[TOC]

### 一、OpenTSDB基本概念

[Opentsdb官网](http://opentsdb.net/docs/build/html/index.html)

### 二、下载与安装

**运行时依赖**

- Linux操作系统；Windows操作系统（安装虚拟机）；Mac OS
- JDK1.6或者更高版本(JDK1.8)
- Hbase 0.92或者更高版本(1.2.0)
- GnuPlot 4.2或者更高版本（画图使用）
- autoconf
- automake
- Zookeeper

**版本信息**

- 版本号：2.3.1
- 安装包（源码）：opentsdb-2.3.1.tar.gz

**下载与安装**

```shell
## 安装运行时依赖
yum install autoconf
yum install automake
yum install gnuplot

## 解压缩源码包到指定目录
tar -zxf /root/demos/package/opentsdb-2.3.1.tar.gz -C /root/demos/software/

## 建立软连接
ln -s /root/demos/software/opentsdb-2.3.1/ /opt/
mv /opt/opentsdb-2.3.1 /opt/opentsdb
```

**编译源码**

在编译的时候本来是按照[官网的安装流程](http://opentsdb.net/docs/build/html/installation.html#id1)进行安装的，后来发现使用./build.sh时候会报错，后来参考一篇[博文](https://blog.csdn.net/liu16659/article/details/81038756)安装成功

```shell
## 进入解压缩的目录
cd /opt/opentsdb

## 官网是直接./build.sh,我们需要如下三步
mkdir build 
cp -r third_party ./build 
./build.sh

## 编译成功的标志是在openTSDB父目录下生成build文件夹，并且会生成一个jar包：tsdb-2.3.0.jar
```

### 三、配置与启动

**配置opentsdb**

```shell
## 创建opentsdb所需要的表tsdb, tsdb-uid, tsdb-tree 和 tsdb-meta。这里需要说明下，个人操作的时候发现需要将hadoop和hbase启动，否则创建的表会有问题
 /opt/hadoop/sbin/start-dfs.sh
 /opt/hbase/bin/start-hbase.sh
 cd /opt/opentsdb/
## 生成表
env COMPRESSION=NONE HBASE_HOME=/opt/hbase ./src/create_table.sh

## 将opentsdb.conf移动到对应位置
mv /opt/opentsdb/src/opentsdb.conf /opt/opentsdb/build/

## 修改配置文件
vim /opt/opentsdb/build/opentsdb.conf
```

**配置文件**

```properties
# --------- NETWORK ----------
# The TCP port TSD should use for communications
# *** REQUIRED ***
tsd.network.port = 4242

# ----------- HTTP -----------
# The location of static files for the HTTP GUI interface.
# *** REQUIRED ***
tsd.http.staticroot = ./staticroot

# Where TSD should write it's cache files to
# *** REQUIRED ***
tsd.http.cachedir = /root/data/opentsdb_temp


# --------- CORE ----------
# Whether or not to automatically create UIDs for new metric types, default
# is False
tsd.core.auto_create_metrics = true

# Name of the HBase table where data points are stored, default is "tsdb"
tsd.storage.hbase.data_table = tsdb

# Path under which the znode for the -ROOT- region is located, default is "/hbase"
tsd.storage.hbase.zk_basedir = /hbase

# A comma separated list of Zookeeper hosts to connect to, with or without
# port specifiers, default is "localhost"
tsd.storage.hbase.zk_quorum = localhost
```

**启动opentsdb**

```shell
## 启动前需要启动hbase
## 启动opentsdb
cd /opt/opentsdb/build/
./tsdb tsd
```

**查看webUI**

```shell
47.98.189.100:4242
```




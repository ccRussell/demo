# Mysql环境搭建

### 一、Mysql安装

去官网进行下载：https://dev.mysql.com/downloads/mysql/5.6.html#downloads

我是直接下载的tar包，通过解压缩安装。个人不喜欢使用yum或者其他源进行安装，因为并非专业运维人员，无法驾驭。

**版本：**5.6.42

**安装包：**mysql-5.6.42-linux-glibc2.12-x86_64.tar.gz

**解压缩：**

```shell
## 首先检查机器上是否安装有mysql
yum list installed | grep mysql
我的机器出现：mysql-libs.x86_64    5.1.73-8.el6_8     @base

## 移除已经安装的mysql及其依赖，防止后面冲突
yum -y remove mysql-libs.x86_64

## 解压安装包
tar -zxf mysql-5.6.42-linux-glibc2.12-x86_64.tar.gz -C ../software/

## 建立软连接
ln -s /root/demos/software/mysql-5.6.42-linux-glibc2.12-x86_64/ /opt/
mv /opt/mv mysql-5.6.42-linux-glibc2.12-x86_64 /opt/mysql 

```

```shell
## 在/etc目录下建立配置文件my.cnf（mysql启动时默认先在/etc/目录下寻找配置文件）
cp /opt/mysql/my-default.cnf /etc/my.cnf

## 修改配置文件my.cnf（配置文件内容如附录）
vim /etc/my.cnf

## 进入mysql安装目录，安装数据库
cd /opt/mysql
./scripts/mysql_install_db --user=root --basedir=/opt/mysql --datadir=/opt/mysql/data
```

此时mysql已经安装完毕，接下来我们就需要配置mysql，例如将其添加到系统服务，方便启动。或者添加到环境变量，方便使用命令

**配置mysql：**

```shell
## 复制启动脚本到资源目录
cp ./support-files/mysql.server /etc/rc.d/init.d/mysqld

## 将mysqld服务添加到系统服务
chkconfig --add mysqld

## 检查mysqld服务是否已经生效
chkconfig --list mysqld

## 输出如下结果
mysqld         	0:关闭	1:关闭	2:启用	3:启用	4:启用	5:启用	6:关闭

## 以后就使用service命令控制mysql的启动和停止
service mysqld start
service mysqld stop
```



### 二、Mysql配置

###三、Mysql客户端
# 关于cm
cloudera manager是一套大数据平台的管理方案
核心是管理服务器，该服务器承载管理控制台的Web服务器和应用程序逻辑，并负责安装软件，配置，启动和停止服务，以及管理上的服务运行群集。

Cloudera Manager Server由以下几个部分组成：
　　Agent：安装在每台主机上。该代理负责启动和停止的过程，拆包配置，触发装置和监控主机。
　　Management Service：由一组执行各种监控，警报和报告功能角色的服务。
　　Database：存储配置和监视信息。通常情况下，多个逻辑数据库在一个或多个数据库服务器上运行。例如，Cloudera的管理服务器和监控角色使用不同的逻辑数据库。
　　Cloudera Repository：软件由Cloudera 管理分布存储库。
　　Clients：是用于与服务器进行交互的接口：
　　Admin Console ：基于Web的用户界面与管理员管理集群和Cloudera管理。
    API ：与开发人员创建自定义的Cloudera Manager应用程序的API。

# 环境准备
理论上cm服务端可以独立部署在集群中，资源要求很小，为简约资源，直接部署到运行环境中
master  192.168.12.237

agent需要在所有机器上安装
agent 192.168.12.237 
agent 192.168.12.238
agnet 192.168.12.239


## 部署

### 基础环境
yum install -y net-tools vim crul wget lrzsz gcc  psmisc ntp tree

### selinux
sed -i "s/SELINUX=enforcing/SELINUX=disabled/g" /etc/selinux/config
grep SELINUX /etc/selinux/config
reboot

### 防火墙
systemctl stop firewalld
systemctl disable firewalld


### jdk安装@所有主机
details@others/jdk.md

### hosts配置@所有主机
vim /etc/hosts
#192.168.12.236 hadoop01

### ntp服务配置@所有主机
details@others/ntp.md


### mysql@cm管理数据库所在主机
details@others/mysql.env.md

### 熵池服务@所有主机
为了减少熵池不够带来的随机数服务问题，需要安装rng-tools
details#others/entropy.md

### 安装CM@ cm服务器端
mkdir -p /data/pack
cd /data/pack
rz
#cloudera-manager-server-5.14.3-1.cm5143.p0.4.el7.x86_64.rpm
#cloudera-manager-server-db-2-5.14.3-1.cm5143.p0.4.el7.x86_64.rpm
#cloudera-manager-daemons-5.14.3-1.cm5143.p0.4.el7.x86_64.rpm
yum --nogpgcheck localinstall cloudera-manager-daemons-*.rpm -y
yum --nogpgcheck localinstall cloudera-manager-server-*.rpm -y
vim /etc/default/cloudera-scm-server
#export CMF_JDBC_DRIVER_JAR="/usr/share/java/mysql-connector-java.jar:/usr/share/java/oracle-connector-java.jar"


#数据库配置（创建并初始化）
/usr/share/cmf/schema/scm_prepare_database.sh mysql cm -h localhost -uroot -pdcjet --scm-host localhost scm scm scm
vim /etc/cloudera-scm-server/db.properties

## 安装CM agent @所有cm的客户端
mkdir -p /data/pack
cd /data/pack
rz
#cloudera-manager-agent-5.14.3-1.cm5143.p0.4.el7.x86_64.rpm
#cloudera-manager-daemons-5.14.3-1.cm5143.p0.4.el7.x86_64.rpm

yum --nogpgcheck localinstall cloudera-manager-daemons-*.rpm -y
yum --nogpgcheck localinstall cloudera-manager-agent*.x86_64.rpm -y

vim /etc/cloudera-scm-agent/config.ini
`
server_host
`
sudo service cloudera-scm-agent start


## start cm server
service cloudera-scm-server start
需要等2分钟左右，访问
http://XXXXXX:7180/ 
admin/admin

选择使用Parcel
选择
CDH5.14.2(可以选择使用离线方式)
参考CDH5离线包安装.env.md



# 常用的使用命令
service cloudera-scm-server start
service cloudera-scm-agent start



 
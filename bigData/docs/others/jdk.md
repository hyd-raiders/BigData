## 卸载JDK
1. 通过如下命令查看当前JDK的相关内容
rpm -qa | grep java
2. 如果出现如下内容
java-1.7.0-openjdk-1.7.0.9-2.3.4.1.el6_3.x86_64
java-1.6.0-openjdk-1.6.0.0-1.50.1.11.5.el6_3.x86_64
java-1.6.0-openjdk-devel-1.6.0.0-1.50.1.11.5.el6_3.x86_64
tzdata-java-2012j-1.el6.noarch
libvirt-java-devel-0.4.9-1.el6.noarch
libvirt-java-0.4.9-1.el6.noarch
java-1.7.0-openjdk-devel-1.7.0.9-2.3.4.1.el6_3.x86_64
3. 那么依次通过如下命令进行删除
rpm -e --nodeps xxx
其中xxx即为2中出现的数据,比如如下就是删除的2个,上述第二条出现几条,执行几次.
rpm -e --nodeps java-1.7.0-openjdk-1.7.0.9-2.3.4.1.el6_3.x86_64
rpm -e --nodeps java-1.6.0-openjdk-1.6.0.0-1.50.1.11.5.el6_3.x86_64
4. 当删除完成后,再次运行第一条所述的指令,确定是否卸载干净,如果卸载干净,第一条指令不会输出任何内容.


## JDK的安装
1. 在Oracle的官网:http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html 下载对应的JDK包.当前我因为是64位的CentOS,所以下载的为:
需要注意的是 rpm原本是Red Hat Linux发行版专门用来管理Linux各项套件的程序. 推荐下载tar.gz 然后自己配置.
下载完成后,将 jdk-8u162-linux-x64.tar.gz 放在某个目录待用.

2. 上传
mkdir -p /data/pack
cd /data/pack
rz
#jdk-8u162-linux-x64.tar.gz

3. 安装
tar xvzf jdk-8u162-linux-x64.tar.gz

mkdir -p /usr/java
mv jdk1.8.0_162 /usr/java/jdk1.8

4. 环境变量
vim /etc/profile 
`
#set java enviroment 
JAVA_HOME=/usr/java/jdk1.8
JRE_HOME=/usr/java/jdk1.8/jre
CLASS_PATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JRE_HOME/lib
PATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin
export JAVA_HOME JRE_HOME CLASS_PATH PATH
`
source /etc/profile
5. 确认版本
java -version

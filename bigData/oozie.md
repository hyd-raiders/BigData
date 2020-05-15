
## cdh 安装 ext2.2
cd /var/lib/oozie
rz
# ext-2.2.zip
unzip ext-2.2.zip

oozie服务重启


## 例子
find / -name oozie-examples.tar.gz
cd /data/workspace/
cp /opt/cloudera/parcels/CDH-5.14.2-1.cdh5.14.2.p0.3/share/doc/oozie-4.1.0+cdh5.14.2+484/oozie-examples.tar.gz /data/workspace/
ls

tar xvzf oozie-examples.tar.gz 
su hdfs
hadoop fs -mkdir /demo
hadoop fs -put examples /demo/examples
hadoop fs -ls /demo/examples

vim /data/workspace/examples/apps/map-reduce/job.properties
####
nameNode=hdfs://hadoop3:8020
jobTracker=hadoop3:8021
queueName=default
examplesRoot=examples

oozie.wf.application.path=${nameNode}/demo/${examplesRoot}/apps/map-reduce/workflow.xml
outputDir=map-reduce

####

oozie job -oozie http://hadoop3:11000/oozie -config examples/apps/map-reduce/job.properties -run

访问 http://hadoop3:11000/oozie/  或者 hue中查看任务




## 提交spark_demo


### 环境准备，只要执行一次

hadoop fs -ls /user/oozie/share/lib/

export tempDir=lib_20180704195752

hadoop dfs -mkdir -p /user/oozie/share/lib/${tempDir}/spark2


find / -name spark2
hadoop fs -put /opt/cloudera/parcels/SPARK2-2.2.0.cloudera2-1.cdh5.12.0.p0.232957/lib/spark2/jars/* /user/oozie/share/lib/${tempDir}/spark2
hadoop fs -ls /user/oozie/share/lib/${tempDir}/spark2
hadoop fs -ls /user/oozie/share/lib/${tempDir}/spark/oozie-sharelib-spark*

hadoop fs -cp /user/oozie/share/lib/${tempDir}/spark/oozie-sharelib-spark* /user/oozie/share/lib/${tempDir}/spark2
hadoop fs -ls /user/oozie/share/lib/${tempDir}/spark2/oozie-sharelib-spark*
##提交 hive配置到spark2中
find / -name hive-site.xml
hadoop fs -put /etc/hive/conf.cloudera.hive/hive-site.xml /user/oozie/share/lib/${tempDir}/spark2

##提交 hbase组件到spark2中
## 为了在spark中使用hbase，需要在spark2中加上hbase的jar包
hadoop fs -put /opt/cloudera/parcels/CDH-5.14.2-1.cdh5.14.2.p0.3/lib/hbase/hbase*.jar  /user/oozie/share/lib/${tempDir}/spark2
hadoop fs -put /opt/cloudera/parcels/CDH-5.14.2-1.cdh5.14.2.p0.3/lib/hbase/lib/htrace-core*  /user/oozie/share/lib/${tempDir}/spark2



##提交其他第三方包其他jar  /oozie/share/lib/spark2/jars为 oozie.libpath 中配置的路径

#hadoop fs -mkdir -p /oozie/share/lib/spark2/jars
hadoop fs -put /home/sparkjars/fastjson-1.2.47.jar /user/oozie/share/lib/${tempDir}/spark2

#hadoop fs -mv /user/oozie/share/lib/lib_20180504004606/spark2

oozie admin --oozie http://localhost:11000/oozie -sharelibupdate
oozie admin --oozie http://localhost:11000/oozie -shareliblist
#sqoop的odbc组件
hadoop fs -ls /user/oozie/share/lib/
hadoop fs -put -f /var/lib/sqoop/ojdbc6.jar  /user/oozie/share/lib/${tempDir}/sqoop/
hadoop fs -ls /user/oozie/share/lib/${tempDir}/sqoop/ojdbc6.jar



oozie admin --oozie http://localhost:11000/oozie -sharelibupdate
oozie admin --oozie http://localhost:11000/oozie -shareliblist


### 提交
hadoop fs -mkdir -p /spark/
cd /data/workspace/
rz
#oozie_spark_demo.zip
unzip oozie_spark_demo.zip
su hdfs
hadoop fs -rm /spark/oozie_spark_demo
hadoop fs -put /data/workspace/oozie_spark_demo /spark/oozie_spark_demo
hadoop fs -ls /spark/oozie_spark_demo

oozie job -oozie http://hadoop3:11000/oozie -config /data/workspace/oozie_spark_demo/job.properties -run


## http api 调用

###create job

echo '<?xml version="1.0" encoding="utf-8" ?><configuration><property><name>user.name</name><value>hdfs</value></property><property><name>nameNode</name><value>hdfs://hadoop3:8020</value></property><property><name>jobTracker</name><value>hadoop3:8032</value></property><property><name>master</name><value>yarn</value></property><property><name>mode</name><value>cluster</value></property><property><name>oozie.use.system.libpath</name><value>true</value></property><property><name>firstParam</name><value>abcddeeeffff</value></property><property><name>oozie.wf.application.path</name><value>hdfs://hadoop3:8020/spark/oozie_spark_demo</value></property><property><name>oozie.action.sharelib.for.spark</name><value>spark2</value></property><property><name>security_enabled</name><value>False</value></property></configuration>'|curl -X POST -H 'Content-type:application/xml;charset=UTF-8' -d @- http://hadoop3:11000/oozie/v1/jobs?jobType=spark2 

###start job 
curl -H 'Content-type:application/xml;charset=UTF-8' -X PUT http://hadoop3:11000/oozie/v2/job/0000014-180518181004884-oozie-oozi-W?action=start


###rerun job

curl -H 'Content-type:application/xml;charset=UTF-8' -X PUT -d '<?xml version="1.0" encoding="utf-8" ?><configuration><property><name>oozie.wf.rerun.skip.nodes</name><value></value></property><property><name>oozie.wf.rerun.failnodes</name><value>false</value></property></configuration>' http://hadoop3:11000/oozie/v2/job/0000014-180518181004884-oozie-oozi-W?action=rerun



## 接口调用日志：
 vim oozie-cmf-oozie-OOZIE_SERVER-hadoop3.log.out
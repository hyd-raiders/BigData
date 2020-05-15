# job化
job化是指将执行的任务变成 定时任务

## job的内容
sqoop 导入任务
spark 日志分析任务


## 操作
### 修改配置
修改oozie_ztyth中的job.properties
### 提交
hadoop fs -mkdir -p /spark/
mkdir -p /data/workspace/sqoop
cd /data/workspace/sqoop
rz
#sqoop

su hdfs
hadoop fs -rm -f -r /spark/oozie_ztyth_log
hadoop fs -mkdir -p /spark/oozie_ztyth_log
hadoop fs -put -f /data/workspace/sqoop/* /spark/oozie_ztyth_log
hadoop fs -ls /spark/oozie_ztyth_log

oozie job -oozie http://hadoop3:11000/oozie -config /data/workspace/sqoop/job.properties -run
#挂起
oozie job -oozie http://hadoop3:11000/oozie  -suspend 0000001-180703111019453-oozie-oozi-C
#重开
oozie job -oozie  http://hadoop3:11000/oozie  -resume 0000001-180703111019453-oozie-oozi-C
#kill 
oozie job -oozie  http://hadoop3:11000/oozie  -kill 0000000-180703111019453-oozie-oozi-C

#查看任务
http://hadoop3:11000/oozie/


#特别注意
workflow.xml 中的command的参数非常重要，非常容易出问题


### 提交 all job
mkdir -p /data/workspace/ztythLog
cd /data/workspace/ztythLog
rz
#all中所有文件，包括lib中的文件
su hdfs
hadoop fs -rm -f -r /spark/oozie_ztyth_log
hadoop fs -mkdir -p /spark/oozie_ztyth_log
hadoop fs -put -f /data/workspace/ztythLog/* /spark/oozie_ztyth_log
hadoop fs -ls /spark/oozie_ztyth_log

#debug run
#oozie job -oozie http://hadoop3:11000/oozie -config /data/workspace/ztythLog/job.debug.properties -run
#spark only run
oozie job -oozie http://hadoop3:11000/oozie -config /data/workspace/ztythLog/job.spark.properties -run
#release run
#oozie job -oozie http://hadoop3:11000/oozie -config /data/workspace/ztythLog/job.properties -run
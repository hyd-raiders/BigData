## Cloudera 安装Parcel提示主机运行状况不良

rm -f /var/lib/cloudera-scm-agent/cm_guid
systemctl restart cloudera-scm-agent


## spark2 无法访问hive
yarn需要在所有主机上部署gateway


## message:Version information not found in metastore.
hive.metastore.schema.verification 取消


## spark写代码时，禁止使用类中的成员变量
不然会出现莫名其妙的问题


## impala查询数据为旧数据
impala默认会做缓存，每次更新后，需要重新加载元数据
//重新加载所有库中的所有表
INVALIDATE METADATA
//重新加载指定的某个表
INVALIDATE METADATA [table]


## not allowed, not in Oozie's whitelist
# Error: E0901 : E0901: NameNode [hadoop3:8020] not allowed, not in Oozie's whitelist. Allowed values are: [hadoop2:8020]
oozie-site.xml 的 Oozie Server 高级配置代码段（安全阀） 中添加
<property>
<name>oozie.service.HadoopAccessorService.nameNode.whitelist</name>
<value>hadoop3:8020</value>
</property>
  
<property>    
<name>oozie.service.HadoopAccessorService.jobTracker.whitelist</name>
 <value>hadoop3:8032</value>
 </property>



 #hbase region 自动关闭问题
 修改cm配置，修改堆栈值，修改超时时间（hbase及zookeeper均需要修改）


 ## oozie job.properties 文件 第一行配置需要为注释
 特定情况下，第一行会读不到






1、cm中配置 hive 中的hbase启动

1.5、安装 hbase 和hive

cp /opt/cloudera/parcels/CDH-5.14.2-1.cdh5.14.2.p0.3/lib/hive/lib/hive-hbase-handler-1.1.0-cdh5.14.2.jar /opt/cloudera//parcels/CDH-5.14.2-1.cdh5.14.2.p0.3/lib/hbase/

2、配置hive-site.xml （可以界面配置，见下面)
<property>
<name>hive.aux.jars.path</name>
<value>file:///opt/cloudera//parcels/CDH-5.14.2-1.cdh5.14.2.p0.3/lib/hbase/hive-hbase-handler-1.1.0-cdh5.14.2.jar,file:///opt/cloudera/parcels/CDH-5.14.2-1.cdh5.14.2.p0.3/lib/hbase/lib/hbase-common-1.2.0-cdh5.14.2.jar,file:///opt/cloudera/parcels/CDH-5.14.2-1.cdh5.14.2.p0.3/lib/hbase/lib/zookeeper.jar
</value>
</property>

或者 在  gateway中配置 
Gateway 日志记录高级配置代码段（安全阀）
内容 同2
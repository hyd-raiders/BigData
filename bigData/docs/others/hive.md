## hive简介
hive是基于Hadoop的一个数据仓库工具，可以将结构化的数据文件映射为一张数据库表，并提供简单的sql查询功能，可以将sql语句转换为MapReduce任务进行运行。 其优点是学习成本低，可以通过类SQL语句快速实现简单的MapReduce统计，不必开发专门的MapReduce应用，十分适合数据仓库的统计分析。

## 部署安装
CM直接部署

## hive on hdfs
默认方式支持hdfs

## hive on hbase
    cp /opt/cloudera/parcels/CDH-5.14.2-1.cdh5.14.2.p0.3/lib/hive/lib/hive-hbase-handler-1.1.0-cdh5.14.2.jar /opt/cloudera/parcels/CDH-5.14.2-1.cdh5.14.2.p0.3/lib/hbase/

    cm配置hive支持hbase

    配置hive-site.xml （可以界面配置，见下面)
    <property>
    <name>hive.aux.jars.path</name>
    <value>file:///opt/cloudera//parcels/CDH-5.14.2-1.cdh5.14.2.p0.3/lib/hbase/hive-hbase-handler-1.1.0-cdh5.14.2.jar,file:///opt/cloudera/parcels/CDH-5.14.2-1.cdh5.14.2.p0.3/lib/hbase/lib/hbase-common-1.2.0-cdh5.14.2.jar,file:///opt/cloudera/parcels/CDH-5.14.2-1.cdh5.14.2.p0.3/lib/hbase/lib/zookeeper.jar
    </value>
    </property>

    或者 CM中配置
    Gateway 日志记录高级配置代码段（安全阀）
    内容与hive-site.xml  一直


## 访问方式
hive 或者采用beeline@others/beeline.md
通过hue直接访问hive

## 常用命令
参考sql

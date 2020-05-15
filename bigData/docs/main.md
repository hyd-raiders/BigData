# 总体技术选型
整套大数据方案选择采用以CDH5为主的hadoop发行版
hadoop总体管理使用cloudera manager(CM)
除此之外还会加入一下扩展的工具

## 基础知识要求
至少能够熟练使用linux的常用shell命令和操作、了解系统的主要组件
至少懂得java编程
不怕遇到坑的精神
良好的英文阅读能力

## 基础环境说明
centos 7.x
java8
scala 2.11.12
python 3.6.5


## 工具选项情况如下：
CM 
CDH 5.14.2  Hadoop 2.6.0
CDS 2.2.0
APACHE_PHOENIX 4.13.2
oozie 4.1.0
impala 2.11.0
hive 1.1.0
sqoop 1

## start here
请按照下列顺序进行部署和使用

开发环境：
1、CM环境搭建.env.md 
2、CDH5离线包安装.env.md
3、CDS2离线包安装.env.md
4、CM的使用.env.md

生产环境：
todo

## 工具使用简单教程

1、CM使用文档
    CM是整个hadoop集群及工具的界面的管理工具，用于管理整个集群中各组件的使用   
    可参考 others/CLOUDERA-Manager_中文手册.pdf
    请自己尝试使用，操作非常简单

2、hdfs简单使用
    hdfs是hadoop的文件系统，是整套系统的基础，大家只需要简单实用即可
    参考 others/hdfs.md

3、hbase简单使用
    hbase是基于hdfs上面的分布式数据库，针对随机读写要求的业务可使用，需要知道简单原理和常用操作，有特殊的数据访问api接口
    参考 others/hbase.md

4、hive使用
    hive是数据仓库工具，可通过sql方式访问hdfs或者hbase中的数据，主要用于数据的批量操作，推荐使用hive on hdfs，hbase不建议使用hive（推荐phoenix）
    参考 others/hive.md

5、impala使用
    impala是一个基于hive结构的sql查询工具，可以迅速查出数据，在bi方面，或者hive数据量不大情况下，可直接使用
    参考 others/impala.md

6、spark使用
    spark作为hadoop体系的核心计算框架存在，即支持批量计算、流式计算、sql访问，spark的学习使用非常重要，选型使用2.2.0版本
    参考 others/spark.md
7、hue使用
    Hue是一个开源的Apache Hadoop UI系统，由Cloudera Desktop演化而来
    其实是一套大数据工具的统一图形化操作界面，学习非常简单，建议直接使用即可

    主要支持一下功能
    SQL编辑器，支持Hive, Impala, MySQL, Oracle, PostgreSQL, SparkSQL, Solr SQL, Phoenix…
    搜索引擎Solr的各种图表
    Spark和Hadoop的友好界面支持
    支持调度系统Apache Oozie，可进行workflow的编辑、查看
    详细参考：http://gethue.com/
8、kylin使用
    kylin是一套基于hive的BI工具，数据经过它几分钟到几小时的build之后，它能在亚秒内查询巨大的表。
    参考 kylin使用.doc
9、oozie使用

10、phoenix使用
    phoenix是构建的Hbase之上的，使用标准的SQL操作Hbase，可以做联机事务处理，拥有低延迟的特性。
    java程序可以直接通过jdbc方式访问hbase数据
    参考Phoenix使用.doc
    demo参考PhoenixDemo.zip
11、sqoop使用
    sqoop是从关系型数据库到hdfs相互导数据的工具，使用也非常简单
    参考others/sqoop.md

## 生产环境
### oozie 配置
Java 堆栈大小 ： 调高 建议200M以上

### impala配置
Impala Daemon 内存限制  ：  调高 建议1G 

 
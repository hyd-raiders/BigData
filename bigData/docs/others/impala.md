## impala介绍
Impala是Cloudera公司主导开发的新型查询系统，它提供SQL语义，能查询存储在Hadoop的HDFS和HBase中的PB级大数据。已有的Hive系统虽然也提供了SQL语义，但由于Hive底层执行使用的是MapReduce引擎，仍然是一个批处理过程，难以满足查询的交互性。相比之下，Impala的最大特点也是最大卖点就是它的快速。

## 部署
CM直接部署

## 登录机器
impala-shell 
进行 sql操作

同样支持 hue中直接使用


## odbc支持
如果需要程序中直接调用impala，可以通过odbc方式，odbc的使用是需要在客户端安装对应的driver

linux 简单参考 others/linux_impala_odbc.md
windows的使用跟普通的odbc一样，需要先安装ClouderaImpalaODBC64.msi ，然后配置DSN，代码如TestOdbcImpala.zip


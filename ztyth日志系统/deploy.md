# 部署文档
该文档为总体部署文档,请按照顺序部署

## kafka、zookeeper、logstash的部署
参考设计文档

## filebeat部署
参考设计文档


## 数据初始化

#上传导入文件
mkdir -p /data/pack
cd /data/pack
rz
#/data/pack/en2cnMap.csv

#登录hive 创建表
CREATE TABLE IF NOT EXISTS t_bd_ztyth_en2cn( tablename String, columnname String,comments String)
 row format delimited fields terminated by ',' ;

#导入中英文转换数据
load data local inpath '/data/pack/en2cnMap.csv' overwrite into table t_bd_ztyth_en2cn;

#确认数量
SELECT count(*) FROM t_bd_ztyth_en2cn;


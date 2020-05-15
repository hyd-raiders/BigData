# mssql到导入例子

## mssqljdbc组件安装 
mkdir -p /data/pack
cd /data/pack
rz 

 tar xvzf sqljdbc_4.2.8112.200_enu.tar.gz 
 cp sqljdbc_4.2/enu/jre8/sqljdbc42.jar /var/lib/sqoop/

## mssql 导入数据到 hdfs
sqoop import  --connect 'jdbc:sqlserver://192.168.12.240:1433;username=dcjet240;password=Dcjet$888;database=NJDataMonitor_BD' \
          --query "SELECT * FROM dbo.AAA WHERE 1=1 AND \$CONDITIONS" \
	--split-by C4 --target-dir /user/sqoop/T_TEST 

## mssql 导入数据到 hive
sqoop import --connect 'jdbc:sqlserver://192.168.12.240:1433;username=dcjet240;password=Dcjet$888;database=NJDataMonitor_BD' --table AAA -m 1 --hive-import --target-dir /user/sqoop/T_hive_test


## mssql 导入数据到 hive 多线程

sqoop import --connect 'jdbc:sqlserver://192.168.12.240:1433;username=dcjet240;password=Dcjet$888;database=HA2000_BD' --table ENTRY_HEAD --hive-import -m 5  --split-by ENTRY_ID  -hive-drop-import-delims --fields-terminated-by "\001" --verbose  -- --schema risk --where "rdate='2012-03-39'"

或者
sqoop import --connect jdbc:mysql://localhost/gamewave --username root --password 123456 --table log --hive-import -m 1  --where 'logtime<10:00:00'

sqoop import --connect jdbc:mysql://localhost/gamewave --username root --password 123456 --table log --hive-import -m 1  --where 'logtime>=10:00:00'

## 其他常用命令
sqoop list-databases --connect jdbc:mysql://localhost:3306 --username root --password 123456
sqoop list-tables --connect 'jdbc:sqlserver://192.168.12.240:1433;username=dcjet240;password=Dcjet$888;database=HA2000_BD' -- --schema risk

## 导入所有

sqoop-import-all-tables --connect 'jdbc:sqlserver://192.168.12.240:1433;username=sa;password=dcjet@888;database=HA2000' --hive-import --hive-database risk --hive-drop-import-delims --fields-terminated-by "\001" --verbose --hive-overwrite --autoreset-to-one-mapper --compress --hive-home /data/hive/risk -- --schema risk


# oracle 导入
## ojdbc6组件安装 
#官网下载ojdbc6.jar
mkdir -p /data/pack
cd /data/pack
rz 
mv ojdbc6.jar /var/lib/sqoop/


## 导入命令 
su hdfs 

sqoop import --connect "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=192.168.10.128)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=orcl)))" --username ztyth_qy --password dcjet --table T_ZTYTH_HB_HEAD --hive-import --hive-table t_ztyth_hb_head -m 5  --split-by OID  -hive-drop-import-delims --fields-terminated-by "\001" --verbose

sqoop import --connect "jdbc:oracle:thin:@192.168.10.128:1521:orcl" --username ztyth_qy --password dcjet --table T_ZTYTH_HB_HEAD --hive-import --hive-table t_ztyth_hb_head -m 5  --split-by OID  -hive-drop-import-delims --fields-terminated-by "\001" --verbose --hive-overwrite --delete-target-dir

ps： 表名必须大写， 如果表不属于用户，表名前需要加上 大写用户名.表名 如 XYLIU.T_YOUR_TABLE



## 常用参数说明
--table：表名 oracle中必须大写
--hive-import   hive方式导入，没有的话即为hdfs导入
--hive-table  hive中的目标表
-m 是指并行任务数量，越多，导入越快
--split-by 并行任务的分割字段，最好根据有索引的来
-hive-drop-import-delims 删除数据中的分割字段，避免导入后数据不正确
--fields-terminated-by "\001"  字段间分割，避免导入后数据不正确
--verbose  显示命令行的打印信息
--hive-overwrite  覆盖式导入
--delete-target-dir 删除之前的导入文件 、可用append方式代替
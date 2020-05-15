
# mssql

## server
## client
mkdir -p /data/pack
cd /data/pack
rz
#sqljdbc_6.0.8112.200_enu.tar.gz
#sqoop-sqlserver-1.0.tar.gz

tar xvzf sqljdbc_4.2.8112.200_enu.tar.gz
cp sqljdbc_4.2/enu/jre8/sqljdbc42.jar /var/lib/sqoop2/sqljdbc4.jar
rm -rf sqljdbc_4.2

chown sqoop2:sqoop2 /var/lib/sqoop2/sqljdbc4.jar
chmod 755 /var/lib/sqoop2/sqljdbc4.jar



#json 
cd /opt/cloudera/parcels/CDH/lib/hadoop/client
sudo ln -s ../../hadoop-hdfs/lib/jackson-mapper-asl-1.8.8.jar
sudo ln -s ../../hadoop-hdfs/lib/jackson-core-asl-1.8.8.jar

# 使用
sqoop2

show version --all
set server --host localhost


show connector 
show connector -all

## mssql link
create link -c 1
mssql-link  
com.microsoft.sqlserver.jdbc.SQLServerDriver  #confirm right class
jdbc:microsoft:sqlserver://192.168.12.240:1433;databasename=HA2000_BD
jdbc:sqlserver://192.168.12.240:1433;databasename=HA2000_BD

dcjet240
Dcjet$888
protocol=tcp

show link

## hdfs link
create link -c 3
hdfs-link
http://hadoop3:8020/


show link
# job from link 2 to link 3
create job -f 2 -t 3


show job
## 设置错误显示
set option --name verbose --value true
start job -j 1 -s 
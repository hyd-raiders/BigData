## 下载离线包
http://archive.cloudera.com/spark2/parcels/2.2.0.cloudera2/SPARK2-2.2.0.cloudera2-1.cdh5.12.0.p0.232957-el7.parcel
http://archive.cloudera.com/spark2/parcels/2.2.0.cloudera2/SPARK2-2.2.0.cloudera2-1.cdh5.12.0.p0.232957-el7.parcel.sha1
http://archive.cloudera.com/spark2/parcels/2.2.0.cloudera2/manifest.json


## 上传安装
mkdir -p /data/pack
cd /data/pack
rz
#SPARK2-2.2.0.cloudera2-1.cdh5.12.0.p0.232957-el7.parcel
#SPARK2-2.2.0.cloudera2-1.cdh5.12.0.p0.232957-el7.parcel.sha1
#manifest.json
#SPARK2_ON_YARN-2.2.0.cloudera2.jar

mv SPARK2-2.2.0.cloudera2-1.cdh5.12.0.p0.232957-el7.parcel /opt/cloudera/parcel-repo/
mv SPARK2-2.2.0.cloudera2-1.cdh5.12.0.p0.232957-el7.parcel.sha1 /opt/cloudera/parcel-repo/SPARK2-2.2.0.cloudera2-1.cdh5.12.0.p0.232957-el7.parcel.sha
mv manifest.json /opt/cloudera/parcel-repo/
mv SPARK2_ON_YARN-2.2.0.cloudera2.jar /opt/cloudera/csd/

重启scm服务
systemctl restart cloudera-scm-server
#页面操作激活使用



## spark2-shell 环境变量配置@所有spark机器
find / -name spark-env.sh
cp /opt/cloudera/parcels/CDH-5.14.2-1.cdh5.14.2.p0.3/etc/spark/conf.dist/spark-env.sh /opt/cloudera/parcels/SPARK2-2.2.0.cloudera2-1.cdh5.12.0.p0.232957/etc/spark2/conf.dist/

vim /opt/cloudera/parcels/SPARK2-2.2.0.cloudera2-1.cdh5.12.0.p0.232957/etc/spark2/conf.dist/spark-env.sh 
`
export export SPARK_DIST_CLASSPATH=$(${HADOOP_HOME}/bin/hadoop classpath)
`


## 为了在spark中使用hbase，需要在spark2中加上hbase的jar包
cp /opt/cloudera/parcels/CDH-5.14.2-1.cdh5.14.2.p0.3/lib/hbase/hbase*.jar /opt/cloudera/parcels/SPARK2-2.2.0.cloudera2-1.cdh5.12.0.p0.232957/lib/spark2/jars/
cp /opt/cloudera/parcels/CDH-5.14.2-1.cdh5.14.2.p0.3/lib/hbase/lib/htrace-core* /opt/cloudera/parcels/SPARK2-2.2.0.cloudera2-1.cdh5.12.0.p0.232957/lib/spark2/jars/

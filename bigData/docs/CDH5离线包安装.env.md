## 下载离线包
http://archive.cloudera.com/cdh5/parcels/5.14.2/CDH-5.14.2-1.cdh5.14.2.p0.3-el7.parcel
http://archive.cloudera.com/cdh5/parcels/5.14.2/CDH-5.14.2-1.cdh5.14.2.p0.3-el7.parcel.sha1
http://archive.cloudera.com/cdh5/parcels/5.14.2/manifest.json

## 上传安装
mkdir -p /data/pack
cd /data/pack
rz
#CDH-5.14.2-1.cdh5.14.2.p0.3-el7.parcel
#CDH-5.14.2-1.cdh5.14.2.p0.3-el7.parcel.sha1
#manifest.json

mv CDH-5.14.2-1.cdh5.14.2.p0.3-el7.parcel /opt/cloudera/parcel-repo/
mv CDH-5.14.2-1.cdh5.14.2.p0.3-el7.parcel.sha1 /opt/cloudera/parcel-repo/CDH-5.14.2-1.cdh5.14.2.p0.3-el7.parcel.sha

mv manifest.json /opt/cloudera/parcel-repo/

systemctl restart cloudera-scm-server

## scala安装
details@others/scala.md

## python升级
details@python2.7to3.6.md

#页面操作激活使用


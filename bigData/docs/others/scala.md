## 上传
mkdir -p /data/pack
cd /data/pack
rz
tar -zxf scala-2.11.12.tgz
mv scala-2.11.12 /usr/scala

## 配置
vim /etc/profile

export SCALA_HOME=/usr/scala
export PATH=$PATH:$SCALA_HOME/bin

source /etc/profile
scala -version
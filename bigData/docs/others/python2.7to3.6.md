## 基础包
yum install -y gcc openssl-devel

mkdir -p /data/pack
cd /data/pack
rz
tar xzvf Python-3.6.5.tgz
cd Python-3.6.5
./configure -prefix=/usr/local/python3

make && make install
/usr/local/python3/bin/python3.6 -V

## 替换旧Python
mv /usr/bin/python /usr/bin/python_old
ln -s /usr/local/python3/bin/python3.6 /usr/bin/python
ll /usr/bin/python  
cd ..
rm -rf Python-3.6.5

## 修改yum配置
vim /usr/bin/yum 
修改头为
#!/usr/bin/python_old

## 修改防火墙
vim /usr/sbin/firewalld
同样修改

## 其他修改
如遇到python执行问题，请做相应修改
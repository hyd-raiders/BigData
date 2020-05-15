mkdir -p /data/pack
cd /data/pack

rz

## netcat-0.7.1.tar.gz
tar -zxvf netcat-0.7.1.tar.gz 
cd netcat-0.7.1

./configure --prefix=/usr
make && make install
cd .. && rm -rf netcat-0.7.1

nc -help
nc -l 9999
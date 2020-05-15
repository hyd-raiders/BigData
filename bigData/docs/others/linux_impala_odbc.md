## 为了使impala支持odbc操作，在impalad的机器上面，安装组件

yum install unixODBC*
yum install -y cyrus-sasl

mkdir -p /data/pack
cd /data/pack
rz
##ClouderaImpalaODBC-2.5.41.1029-1.el7.x86_64.rpm

yum --nogpgcheck localinstall ClouderaImpalaODBC-2.5.41.1029-1.el7.x86_64.rpm

ls /opt/cloudera/impalaodbc
yum list | grep ClouderaImpalaODBC

vim /etc/profile 
export LD_LIBRARY_PATH=/usr/local/lib:/opt/cloudera/impalaodbc/lib/64


find / -name odbc.ini
find / -name odbcinst.ini
find / -name cloudera.impalaodbc.ini

mkdir -p /usr/local/odbc/
cp /opt/cloudera/impalaodbc/Setup/odbc.ini /usr/local/odbc/
cp /opt/cloudera/impalaodbc/Setup/odbcinst.ini /usr/local/odbc/
cp /opt/cloudera/impalaodbc/lib/64/cloudera.impalaodbc.ini /etc/

vim /etc/profile
export ODBCINI=/usr/local/odbc/odbc.ini
export ODBCSYSINI=/usr/local/odbc/odbcinst.ini
export CLOUDERAIMPALAODBCINI=/etc/cloudera.impalaodbc.ini

source /etc/profile


## config
vim /usr/local/odbc/odbc.ini
edit []

vim /usr/local/odbc/odbcinst.ini 
...


## check
isql -v "Sample Cloudera Impala DSN 64"

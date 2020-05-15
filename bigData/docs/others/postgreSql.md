hive 有 稳定性要求，故可采用postgresql  #无法成功

# 安装  server@db服务器  client@所有服务器
yum install -y https://download.postgresql.org/pub/repos/yum/9.3/redhat/rhel-7-x86_64/pgdg-centos93-9.3-3.noarch.rpm
#Install the client packages:
yum install -y postgresql93

yum remove postgresql93
#Optionally install the server packages:
yum install -y postgresql93-server
#Optionally initialize the database and enable automatic start:

/usr/pgsql-9.3/bin/postgresql93-setup initdb
systemctl enable postgresql-9.3
systemctl start postgresql-9.3

# 登录设置密码
su - postgres
psql

ALTER USER postgres WITH PASSWORD 'dcjet';

退出：\q

备注其他：列出所有库\l  列出所有用户\du 列出库下所有表\d

# 修改远程登录
vim /var/lib/pgsql/9.3/data/pg_hba.conf
`
#local   replication     all                                     peer
#host    replication     all             127.0.0.1/32            ident
#host    replication     all             ::1/128                 ident
host     all        all             0.0.0.0/0                trust
`

vim /var/lib/pgsql/9.3/data/postgresql.conf
`
listen_address ='*'
port =5432
`

systemctl restart postgresql-9.3

# 验证
su - postgres
psql

pgAdmin连接实验



# 安装 客户端@所有机器

mkdir -p /data/pack
cd /data/pack
rz
#postgresql-42.2.2.jar

mkdir -p /usr/share/java/
cp postgresql-42.2.2.jar /usr/share/java/postgresql.jar


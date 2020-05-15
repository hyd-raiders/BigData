## hbase概述
HBase是一种构建在HDFS之上的分布式、面向列的存储系统。在需要实时读写、随机访问超大规模数据集时，可以使用HBase。

## hbase内部存储结构
这部分需要自己查资料了解，必须理解,会影响使用

## hbase架构
可参考网上资料

## 安装
CM中直接安装，注意需要依赖zookeeper

## 简单使用
hbase shell


1. 创建表

语法：create <table>, {NAME => <family>, VERSIONS => <VERSIONS>}
创建一个User表，并且有一个info列族

hbase(main):002:0> create 'User','info'
0 row(s) in 1.5890 seconds

=> Hbase::Table - User
3. 查看所有表
list

4. 查看表详情
describe 'User'
desc  'test1'
hbase(main):025:0> desc 'User'
Table User is ENABLED
User
COLUMN FAMILIES DESCRIPTION
{NAME => 'info', BLOOMFILTER => 'ROW', VERSIONS => '1', IN_MEMORY => 'false', KEEP_DELETED_CELLS => 'FALSE', DATA_BLOCK_ENCODING => 'NONE', TTL => 'FORE
VER', COMPRESSION => 'NONE', MIN_VERSIONS => '0', BLOCKCACHE => 'true', BLOCKSIZE => '65536', REPLICATION_SCOPE => '0'}
1 row(s) in 0.0380 seconds
5. 表修改

删除指定的列族
alter 'User', 'delete' => 'info'
1. 插入数据

语法：put <table>,<rowkey>,<family:column>,<value>

put 'User', 'row1', 'info:name', 'xiaoming'
put 'User', 'row2', 'info:age', '18'
put 'User', 'row3', 'info:sex', 'man'

2. 根据rowKey查询某个记录

语法：get <table>,<rowkey>,[<family:column>,....]

get 'User', 'row2'
get 'User', 'row3', 'info:sex'
get 'User', 'row1', {COLUMN => 'info:name'}

3. 查询所有记录

语法：scan <table>, {COLUMNS => [ <family:column>,.... ], LIMIT => num}
扫描所有记录

scan 'User'
scan 'User', {LIMIT => 2}
scan 'User', {STARTROW => 'row2'}
scan 'User', {STARTROW => 'row2', ENDROW => 'row2'}
scan 'User', {STARTROW => 'row2', ENDROW => 'row3'}

count <table>, {INTERVAL => intervalNum, CACHE => cacheNum}

INTERVAL设置多少行显示一次及对应的rowkey，默认1000；CACHE每次去取的缓存区大小，默认是10，调整该参数可提高查询速度

5. 删除

删除列
delete 'User', 'row1', 'info:age'

deleteall 'User', 'row2'

删除表中所有数据
truncate 'User'

表管理
1. 禁用表
disable 'User'
desc 'User'
scan 'User', {STARTROW => 'row2', ENDROW => 'row3'}

2. 启用表

enable 'User'

3. 测试表是否存在
exists 'User'

4. 删除表

删除前，必须先disable

drop 'TEST.USER'

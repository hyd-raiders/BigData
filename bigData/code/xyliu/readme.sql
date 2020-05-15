# hive

 --初始化表结构
 create table if not exists JOB  
(  
     ID string comment 'job id',  
     NAME string comment 'job name',
     content string comment 'job content'  
)  
comment 'job table'  
location '/com/dcjet/common'; 


-- 初始化数据
insert into JOB VALUES ('risk.demo','risk演示项目','2010-01-01|2018-01-01|3205240081,3205240233,3205331799');


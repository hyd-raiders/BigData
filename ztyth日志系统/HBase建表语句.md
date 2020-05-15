# HBase 建表语句：
create  'T_BD_ZTYTH_LOG', 'OVERVIEW', 'DETAIL'

put 'T_BD_ZTYTH_LOG', '1', 'OVERVIEW:SYSTEMID', '1'
put 'T_BD_ZTYTH_LOG', '1', 'OVERVIEW:CREATEDATE', '21'
put 'T_BD_ZTYTH_LOG', '1', 'OVERVIEW:PAGECODE', '3'
put 'T_BD_ZTYTH_LOG', '1', 'OVERVIEW:PAGENAME', '4'
put 'T_BD_ZTYTH_LOG', '1', 'OVERVIEW:FUNCTIONCODE', '5'
put 'T_BD_ZTYTH_LOG', '1', 'OVERVIEW:FUNCTIONNAME', '6'
put 'T_BD_ZTYTH_LOG', '1', 'OVERVIEW:IP', '7'
put 'T_BD_ZTYTH_LOG', '1', 'OVERVIEW:CORPCODE', '8'
put 'T_BD_ZTYTH_LOG', '1', 'OVERVIEW:USERNAME', '9'
put 'T_BD_ZTYTH_LOG', '1', 'OVERVIEW:RECORDID', '10'

put 'T_BD_ZTYTH_LOG', '1', 'DETAIL:MESSAGE', '11'

# PHOENIX创建视图
CREATE VIEW  IF NOT EXISTS  "T_BD_ZTYTH_LOG" (
    "OID" VARCHAR  PRIMARY KEY,
    "OVERVIEW"."SYSTEMID" VARCHAR,    
    "OVERVIEW"."CREATEDATE" VARCHAR,
    "OVERVIEW"."PAGECODE" VARCHAR,
    "OVERVIEW"."PAGENAME" VARCHAR,
    "OVERVIEW"."FUNCTIONCODE" VARCHAR,
    "OVERVIEW"."FUNCTIONNAME" VARCHAR,
    "OVERVIEW"."IP" VARCHAR,
    "OVERVIEW"."CORPCODE" VARCHAR,
    "OVERVIEW"."USERNAME" VARCHAR,
    "DETAIL"."MESSAGE" VARCHAR,
    "OVERVIEW"."RECORDID" VARCHAR
) AS SELECT * FROM T_BD_ZTYTH_LOG;


#通过phoenix 建表
因为存在cloumn_qualifer的转化问题，暂时不适用建表方式，采用视图方式

#SELECT oid,systemId,createDate,pageCode,pageName,functionCode,functionName,ip,corpCode,username,message,'"+recordId+"' recordId from " + tempView

<!-- CREATE TABLE IF NOT EXISTS "t_bd_ztyth_log" (
    "oid" VARCHAR  primary key,
    "overview"."systemId" VARCHAR,    
    "overview"."createDate" VARCHAR,
    "overview"."pageCode" VARCHAR,
    "overview"."pageName" VARCHAR,
    "overview"."functionCode" VARCHAR,
    "overview"."functionName" VARCHAR,
    "overview"."ip" VARCHAR,
    "overview"."corpCode" VARCHAR,
    "overview"."username" VARCHAR,
    "detail"."message" VARCHAR,
    "overview"."recordId" VARCHAR
) COMPRESSION='GZ'; -->


<!-- CREATE TABLE IF NOT EXISTS  "T_BD_ZTYTH_LOG" (
    oid VARCHAR,
    systemId VARCHAR,    
    createDate VARCHAR,
    pageCode VARCHAR,
    pageName VARCHAR,
    functionCode VARCHAR,
    functionName VARCHAR,
    ip VARCHAR,
    corpCode VARCHAR,
    username VARCHAR,
    message VARCHAR,
    recordId VARCHAR
CONSTRAINT PK PRIMARY KEY(oid)) COMPRESSION='GZ';



CREATE TABLE IF NOT EXISTS  "T_BD_ZTYTH_LOG" (
    oid ,
    systemId ,    
    createDate ,
    pageCode ,
    pageName ,
    functionCode ,
    functionName ,
    ip ,
    corpCode ,
    username ,
    message ,
    recordId 
CONSTRAINT PK PRIMARY KEY(oid)) COMPRESSION='GZ';


UPSERT INTO T_BD_ZTYTH_LOG(oid,systemId,createDate,pageCode,pageName,functionCode,functionName,ip,corpCode,username,message,recordId) 
VALUES('1','2','3','4','5','6','7','8','9','10','11','12'); 

UPSERT INTO "t_bd_ztyth_log"("oid","overview"."systemId" ,    
    "overview"."createDate" ,
    "overview"."pageCode" ,
    "overview"."pageName" ,
    "overview"."functionCode" ,
    "overview"."functionName" ,
    "overview"."ip" ,
    "overview"."corpCode" ,
    "overview"."username" ,
    "detail"."message" ,
    "overview"."recordId" ) 
VALUES('1','2','3','4','5','6','7','8','9','10','11','12');  -->
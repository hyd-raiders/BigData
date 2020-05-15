package com.dcjet.ztythLog

//spark2-shell --jars /home/sparkjars/fastjson-1.2.47.jar
import java.util.UUID

import com.alibaba.fastjson.JSON
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.joda.time.DateTime

/**
  * 提交脚本
  * spark2-submit --class com.dcjet.ztythLog.LogAnalysisDBParquet --master yarn --deploy-mode cluster --jars /home/sparkjars/fastjson-1.2.47.jar /data/workspace/spark/ztythloganalyse.jar debug /data/logHDFS/apollo.archive/20181024081926.parquet/
  */
object LogAnalysisDBParquet {

  case class LogInfo(oid:String,systemId:String,createDate:String,pageCode:String,
                     pageName:String,functionCode:String,functionName:String,
                     ip:String,corpCode:String,username:String,isDelete:String,
                     logType:String,message:String)
  //业务配置
  case class SelConfig( tableName :String,moduleName:String ,moduleCode:String, opType :String, opTypeCode:String, remark:String,pkField:String)
  case class ConfigField(name:String,cnName:String)
  case class LinkConfig( bizName:String ,tableName :String,linkField:String,headTableName:String,headLinkField:String,headFields:List[ConfigField],fileds:List[ConfigField])
  //删除的日志信息临时类
  case class LogInfoForDel(oid:String,systemId:String,createDate:String,pageCode:String,
                           pageName:String,functionCode:String,functionName:String,
                           ip:String,corpCode:String,username:String,isDelete:String,
                           logType:String,message:String, tableName:String , deleteRow:String ,PriKeyValue:String,linkOid:String )
  //更新的日志信息临时类
  case class LogInfoForUpdate(oid:String,systemId:String,createDate:String,pageCode:String,
                              pageName:String,functionCode:String,functionName:String,
                              ip:String,corpCode:String,username:String,
                              message:String,tableName:String,UpdateColumns:String,PriKeyValue:String,UpdateColumnsCn:String)

  //record 对象
  case class Record (recordId:String, startDate:String,var endDate:String,var totalCount:BigInt,var  bizCount:BigInt,var delCount:BigInt,var updateCount:BigInt,var remark:String,originFile:String, var archivePath:String)
  //内部配置类
  case class InnerConfig( isArchive:Boolean, isSaveToTable:Boolean,isDebug:Boolean,isDeleteOriginFile:Boolean)

  //调试错误类
  case class LogErrorResultInfo(result:String,line:String,message:String)

  //正常运行配置
  //var iConfig:InnerConfig = InnerConfig(true,true,false,true)
  //调试配置
  //var iConfig = InnerConfig(false,true,true,true)
  /**
    *
    * @param content
    * @return
    */
  def processNull(content: Object):String={
      if(content != null)
        content.toString()
      else
        ""
  }
  /**
    * 记录日志方法
    * @param message
    */
  def log(message:String):Unit={
    println(message);
  }

  //缓存
  //var cacheEn2CnMap:Map[String,String]= Map()

  /**
    * 删除日志的英中文转换方法
    * @param tableName
    * @param deleteRow
    * @return
    */
  def en2cnForDelete(tableName:String,deleteRow:String,cacheEn2CnMap:Map[String,String]):String = {
    //val tableName = "T_ZTYTH_HB_DCR_BYPRO"
    // val deleteRow = "{\"INSERT_USER\":\"lixy\",\"UNIT\":\"\",\"INSERT_TIME\":\"2018/6/27 14:57:11\",\"G_NAME\":\"33\",\"TREAT_MODE\":\"3\",\"UPDATE_USER\":\"\",\"OID\":\"a3fca0ee-510e-4383-a27d-7bcaa44ef676\",\"UPDATE_TIME\":\"\",\"CURR\":\"132\",\"HB_NO\":\"B23032000123\",\"TOTAL_VALUE\":\"33\",\"ENTRY_ID\":\"\",\"QTY\":\"3\",\"REMARK\":\"\"}"
    val delObj = JSON.parseObject(deleteRow);
    var resultString = "";
    val it = delObj.keySet().iterator()
    while (it.hasNext){
      val item = it.next()
      val key = tableName+"|"+item.toString()
      if(cacheEn2CnMap.contains(key)) {
        val cnString = cacheEn2CnMap(key)
        resultString += cnString + "(" + item.toString() + ")" + ":" + convertDateString(delObj.get(item).toString()) + ",  "
      }
      else
      {
        resultString += item.toString() +":" + convertDateString(delObj.get(item).toString()) + ",  "
        //log("en2cnForDelete|cacheEn2CnMap不包含key：" + key)
      }
    }
    resultString
  }

  /**
    * 将/Date(1539742793000+0800)/ 格式的日期转换为 yyyy-MM-dd HH:mm:ss 格式
    * @param oldString
    * @return
    */
  def convertDateString(oldString:String):String={
    import java.text.SimpleDateFormat
    import java.util.Date

    if(oldString.startsWith("/Date(")){
      var str = oldString.substring(6,oldString.length - 2);
      str = str.substring(0,str.length - 5);
      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(str.toLong))
    }
    else{
      oldString
    }
  }

  /**
    * 更新方法的英中文转换方法
    * @param tableName
    * @param updateColumn
    * @return
    */
  def en2cnForUpdate(tableName:String,updateColumn:String,cacheEn2CnMap:Map[String,String]):String = {
    val jsonArray  = JSON.parseArray(updateColumn)
    val length = jsonArray.size();
    var resultString = "";
    var i = 0
    for(i<- 0 until length){
      var item = jsonArray.getJSONObject(i)
      var oldValue = convertDateString(item.get("OldValue").toString())
      var newValue = convertDateString(item.get("NewValue").toString())
      if(oldValue != newValue){
        var key = tableName+"|"+ item.get("ColumnName").toString()
        if(cacheEn2CnMap.contains(key)) {
          var cnString = cacheEn2CnMap(key)
          resultString += cnString + "(" + item.get("ColumnName").toString() + ") " + "修改前：" + oldValue + " 修改后：" + newValue + ",  "
        }
        else {
          resultString +=  item.get("ColumnName").toString() + " " + "修改前：" + oldValue + " 修改后：" + newValue + ",  "
          //log("en2cnForUpdate|cacheEn2CnMap不包含key:" + key)
        }
      }
    }
    resultString
  }

  /**
    * 通过map方式存储 小数据量时可以使用，数据量一大就不行，会有zookeeper连接数限制
    * @param df
    */
  def saveByMap(df:DataFrame): Unit = {
    val conf = HBaseConfiguration.create()
    val jobConf = new JobConf(conf)
    jobConf.set("hbase.zookeeper.quorum", "192.168.112.191,192.168.112.192,192.168.112.193")
    jobConf.set("zookeeper.znode.parent", "/hbase")
    jobConf.set(org.apache.hadoop.hbase.mapred.TableOutputFormat.OUTPUT_TABLE, "T_BD_ZTYTH_LOG")
    jobConf.setOutputFormat(classOf[org.apache.hadoop.hbase.mapred.TableOutputFormat])

    df.rdd.map(x => {
      //以 创建时间 和 oid为rowkey
      val put = new org.apache.hadoop.hbase.client.Put(Bytes.toBytes(x.getAs("createDate").toString() + x.getAs("oid").toString()))
      //put.setId(x.getAs("oid").toString())
      //"SELECT oid,systemId,createDate,pageCode,pageName,functionCode,functionName,ip,corpCode,username,message,'"+recordId+"' recordId from " + tempView
      //put.addColumn(Bytes.toBytes("overview"),Bytes.toBytes("oid"),Bytes.toBytes(x.getAs("oid").toString()))
      put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("SYSTEMID"), Bytes.toBytes(x.getAs("systemId").toString()))
      put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("CREATEDATE"), Bytes.toBytes(x.getAs("createDate").toString()))
      put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("PAGECODE"), Bytes.toBytes(x.getAs("pageCode").toString()))
      put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("PAGENAME"), Bytes.toBytes(x.getAs("pageName").toString()))
      put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("FUNCTIONCODE"), Bytes.toBytes(x.getAs("functionCode").toString()))
      put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("FUNCTIONNAME"), Bytes.toBytes(x.getAs("functionName").toString()))
      put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("IP"), Bytes.toBytes(x.getAs("ip").toString()))
      put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("CORPCODE"), Bytes.toBytes(x.getAs("corpCode").toString()))
      put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("USERNAME"), Bytes.toBytes(x.getAs("username").toString()))
      put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("RECORDID"), Bytes.toBytes(x.getAs("recordId").toString()))
      put.addColumn(Bytes.toBytes("DETAIL"), Bytes.toBytes("MESSAGE"), Bytes.toBytes(x.getAs("message").toString()))
      (new org.apache.hadoop.hbase.io.ImmutableBytesWritable, put)
    }).saveAsHadoopDataset(jobConf)
  }


  /**
    * 持久化方法 可以执行
    * @param df 需要保持的df
    */
  def saveByPartition(df:DataFrame): Unit = {
    df.rdd.foreachPartition {
      y => {
        val myConf = HBaseConfiguration.create()
        myConf.set("hbase.zookeeper.quorum", "192.168.112.191,192.168.112.192,192.168.112.193")
        myConf.set("hbase.zookeeper.property.clientPort", "2181")
        myConf.set("hbase.defaults.for.version.skip", "true")

        val myTable = new org.apache.hadoop.hbase.client.HTable(myConf, "T_BD_ZTYTH_LOG")
        myTable.setAutoFlush(false, false) //关键点1
        myTable.setWriteBufferSize(5 * 1024 * 1024) //关键点2
        y.foreach { x => {
          //以 创建时间 和 oid为rowkey
          val put = new org.apache.hadoop.hbase.client.Put(Bytes.toBytes(x.getAs("createDate").toString()+ x.getAs("oid").toString()))
          //put.addColumn(Bytes.toBytes("overview"),Bytes.toBytes("oid"),Bytes.toBytes(x.getAs("oid").toString()))
          put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("SYSTEMID"), Bytes.toBytes(x.getAs("systemId").toString()))
          put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("CREATEDATE"), Bytes.toBytes(x.getAs("createDate").toString()))
          put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("PAGECODE"), Bytes.toBytes(x.getAs("pageCode").toString()))
          put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("PAGENAME"), Bytes.toBytes(x.getAs("pageName").toString()))
          put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("FUNCTIONCODE"), Bytes.toBytes(x.getAs("functionCode").toString()))
          put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("FUNCTIONNAME"), Bytes.toBytes(x.getAs("functionName").toString()))
          put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("IP"), Bytes.toBytes(x.getAs("ip").toString()))
          put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("CORPCODE"), Bytes.toBytes(x.getAs("corpCode").toString()))
          put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("USERNAME"), Bytes.toBytes(x.getAs("username").toString()))
          put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("RECORDID"), Bytes.toBytes(x.getAs("recordId").toString()))
          put.addColumn(Bytes.toBytes("DETAIL"), Bytes.toBytes("MESSAGE"), Bytes.toBytes(x.getAs("message").toString()))
          myTable.put(put)
        }
        }
        myTable.flushCommits() //关键点3
      }

    }
  }

  /**
    * 持久化方法  未测试
    * @param df 需要保持的df
    */
  def saveByBlukLoad(df:DataFrame): Unit = {

    val tempFilePath ="/hbase/ztyth_" +  DateTime.now().toString("yyyyMMddHHmmss")
    val conf = HBaseConfiguration.create()
    val tableName = "T_BD_ZTYTH_LOG"
    val table = new org.apache.hadoop.hbase.client.HTable(conf, tableName)

    conf.set(org.apache.hadoop.hbase.mapred.TableOutputFormat.OUTPUT_TABLE, tableName)
    val job = org.apache.hadoop.mapreduce.Job.getInstance(conf)
    job.setMapOutputKeyClass (classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable])
    job.setMapOutputValueClass (classOf[org.apache.hadoop.hbase.client.Put])
    org.apache.hadoop.hbase.mapreduce.HFileOutputFormat.configureIncrementalLoad (job, table)

    val rdd = df.rdd.map(x => {
      //以oid为rowkey
      val put = new org.apache.hadoop.hbase.client.Put(Bytes.toBytes(x.getAs("oid").toString()))
      //put.setId(x.getAs("oid").toString())
      //"SELECT oid,systemId,createDate,pageCode,pageName,functionCode,functionName,ip,corpCode,username,message,'"+recordId+"' recordId from " + tempView
      //put.addColumn(Bytes.toBytes("overview"),Bytes.toBytes("oid"),Bytes.toBytes(x.getAs("oid").toString()))
      put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("SYSTEMID"), Bytes.toBytes(x.getAs("systemId").toString()))
      put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("CREATEDATE"), Bytes.toBytes(x.getAs("createDate").toString()))
      put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("PAGECODE"), Bytes.toBytes(x.getAs("pageCode").toString()))
      put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("PAGENAME"), Bytes.toBytes(x.getAs("pageName").toString()))
      put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("FUNCTIONCODE"), Bytes.toBytes(x.getAs("functionCode").toString()))
      put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("FUNCTIONNAME"), Bytes.toBytes(x.getAs("functionName").toString()))
      put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("IP"), Bytes.toBytes(x.getAs("ip").toString()))
      put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("CORPCODE"), Bytes.toBytes(x.getAs("corpCode").toString()))
      put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("USERNAME"), Bytes.toBytes(x.getAs("username").toString()))
      put.addColumn(Bytes.toBytes("OVERVIEW"), Bytes.toBytes("RECORDID"), Bytes.toBytes(x.getAs("recordId").toString()))
      put.addColumn(Bytes.toBytes("DETAIL"), Bytes.toBytes("MESSAGE"), Bytes.toBytes(x.getAs("message").toString()))
      (new org.apache.hadoop.hbase.io.ImmutableBytesWritable, put)
    })
    // Save Hfiles on HDFS
    rdd.saveAsNewAPIHadoopFile(tempFilePath, classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable], classOf[org.apache.hadoop.hbase.client.Put], classOf[org.apache.hadoop.hbase.mapreduce.HFileOutputFormat], conf)
    //Bulk load Hfiles to Hbase
    val bulkLoader = new org.apache.hadoop.hbase.mapreduce.LoadIncrementalHFiles(conf)
    bulkLoader.doBulkLoad(new org.apache.hadoop.fs.Path(tempFilePath), table)
  }

  /**
    * 持久化方法
    * @param df 需要保持的df
    */
  def save(df:DataFrame): Unit = {
    val isSaveToHive = false
    if(isSaveToHive) {
      df.write.mode("append").saveAsTable("t_bd_ztyth_log")
    }
    else {
      //saveByMap(df)
      saveByPartition(df)
    }
  }

  /**
    * main方法
    * @param args
    */
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().enableHiveSupport().appName("ztythLog").master("yarn-cluster").getOrCreate()

    var filePath:String = "/data/logHDFS/apollo.archive/20181024081926.parquet/"
    if(args.length>0 && args(0) == "debug") {
      filePath = args(1)
    }
    else{
      log("请在debug摸试下操作，退出执行")
      return
    }

    import org.joda.time._
    import spark.implicits._

    var record: Record= null

    record = Record(DateTime.now().toString("yyyyMMddHHmmss"),
      DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),null,0,0,0,0,"","","")


    var logPath : String = filePath+"part-*.parquet"

    var parquetDF: DataFrame = spark.read.parquet(logPath)

    //val originLines:org.apache.spark.rdd.RDD[String] = parquetDF.rdd

    //  var cacheEn2CnMap:Map[String,String]= Map()
    val en2cnDf = spark.sql("select concat(tablename,'|',columnname) as enString,comments as cnString from default.t_bd_ztyth_en2cn")
    val reduceMap : String =en2cnDf.map(row => (row.get(0).toString() + "&" +row.get(1).toString() )).reduce((k1,k2) =>  k1 + "#" + k2)
    var cacheEn2CnMap:Map[String,String]= Map()
    val broadcast = spark.sparkContext.broadcast(reduceMap)

    broadcast.value.split("\\#").foreach(row =>{
      //println(row.get(0).toString()  + "  " + row.get(1).toString())
      val list = row.split("\\&")
      cacheEn2CnMap += (list(0) -> list(1))
    })

    import com.alibaba.fastjson.JSON

    var originalSource = parquetDF.rdd.map(line => {

      //try{

          // 1.转json 2.取出system_id 3.取出时间 4.取出其他信息内容
          val lineJson = JSON.parseObject(line(0).toString())
          //系统标识
          val fields = lineJson.get("fields")
          val systemId = JSON.parseObject(fields.toString()).get("system_id").toString()

          var message = lineJson.get("message").toString();
          val t = message.substring(5, message.indexOf("日志内容"));
          //操作时间
          val createDate = t.substring(0, t.indexOf(","))
          val logContent = message.substring(message.indexOf("日志内容") + 5)

          val jsonContent = JSON.parseObject(logContent)
          val eventType = jsonContent.get("EventType").toString()

          val logType = jsonContent.get("LogType").toString()

          //记录主键
          val oid = UUID.randomUUID().toString()

          val pageCode = processNull(jsonContent.get("PageCode"))
          val pageName = processNull(jsonContent.get("PageName"))
          val functionCode = processNull(jsonContent.get("FunctionCode"))
          val functionName = processNull(jsonContent.get("FunctionName"))
          val doPerson = processNull(jsonContent.get("DoPerson")).split('|')(0)
          val ip = processNull(jsonContent.get("DoPersonIP"))
          val corpCode = processNull(jsonContent.get("CorpCode"))

          message = processNull(jsonContent.get("Message"))

          var ltype = "other"
          var isDelete = "true"
          if (eventType == "BusinessOperation") {
              ltype = "business"
              isDelete = "false"
          }
          if (logType == "Data" && message !="") {
              val jsonMessage = JSON.parseObject(message)
              if (jsonMessage.containsKey("DeleteRow")) {
                ltype = "delete"
              }
              if (jsonMessage.containsKey("UpdateColumns")) {
                ltype = "update"
              }
              val tableName = jsonMessage.get("TableName").toString().toUpperCase()
              val updateTables = Array("T_ZTYTH_HB_HEAD",
                "T_ZTYTH_HB_HEAD_CHANGE",
                "T_ZTYTH_ENTRY_HEAD_E",
                "T_ZTYTH_ENTRY_HEAD_I",
                "T_ZTYTH_HB_DCR_HEAD",
                "T_ZTYTH_HB_IMG",
                "T_ZTYTH_HB_IMG_CHANGE",
                "T_ZTYTH_HB_EXG",
                "T_ZTYTH_HB_EXG_CHANGE",
                "T_ZTYTH_HB_CONSUME",
                "T_ZTYTH_HB_CONSUME_CHANGE",
                "T_ZTYTH_ENTRY_LIST_I",
                "T_ZTYTH_ENTRY_LIST_E",
                "T_ZTYTH_HB_DCR_IMP_IMG",
                "T_ZTYTH_HB_DCR_BASIC_INFO",
                "T_ZTYTH_HB_DCR_BYPRO",
                "T_ZTYTH_HB_DCR_SCRAP",
                "T_ZTYTH_HB_DCR_IMP_IMG")
              if (updateTables.contains(tableName)) {
                isDelete = "false"
              }
              val deleteTables = Array("T_ZTYTH_HB_HEAD",
                "T_ZTYTH_ENTRY_HEAD_E",
                "T_ZTYTH_ENTRY_HEAD_I",
                "T_ZTYTH_HB_DCR_HEAD",
                "T_ZTYTH_HB_IMG",
                "T_ZTYTH_HB_IMG_CHANGE",
                "T_ZTYTH_HB_EXG",
                "T_ZTYTH_HB_EXG_CHANGE",
                "T_ZTYTH_HB_CONSUME",
                "T_ZTYTH_HB_CONSUME_CHANGE",
                "T_ZTYTH_ENTRY_LIST_I",
                "T_ZTYTH_ENTRY_LIST_E",
                "T_ZTYTH_HB_DCR_BYPRO",
                "T_ZTYTH_HB_DCR_SCRAP")
              if (deleteTables.contains(tableName)) {
                isDelete = "false"
              }
          }
          //LogErrorResultInfo("1",line,"")
            LogInfo(oid, systemId, createDate, pageCode, pageName, functionCode, functionName, ip, corpCode, doPerson, isDelete, ltype, message)
//        }
//        catch{
//          case (ex:Exception) => {
//            LogErrorResultInfo("2",line,"")
//          }
//        }
    })

    //originalSource.filter(x => x.result == "2").count()

    //var errorLine = originalSource.filter(x => x.result == "2").collect().take(1)

    //var line = errorLine(0).line

    //记录总行数
    record.totalCount = originalSource.count()

    //做缓存
    originalSource.cache()

    var isSuccess = false
    try {
      log("开始执行processBusinessLog")
      processBusinessLog(originalSource,spark,record)
      log("开始执行processDeleteLog")
      processDeleteLog(originalSource,spark,record,cacheEn2CnMap)
      log("开始执行processUpdateLog")
      processUpdateLog(originalSource,spark,record,cacheEn2CnMap)
      isSuccess= true
    } catch {
      case (ex:Exception) => {
        record.remark = ex.toString()
        log(ex.toString())
      }
    }

    record.endDate = DateTime.now().toString("yyyy-MM-dd HH:mm:ss")
    Seq(record).toDF().write.mode("append").saveAsTable("t_bd_record")
  }


  /**
    * 处理通用日志
    * @param originalSource
    * @param spark
    */
  def processBusinessLog(originalSource:org.apache.spark.rdd.RDD[LogInfo],spark:SparkSession, record:Record) : Unit = {
      import spark.implicits._
      val businessLog = originalSource.filter(x => x.isDelete == "false" && x.logType == "business")

      val businessLogDF: org.apache.spark.sql.DataFrame  = businessLog.toDF()

      //记录行数
      record.bizCount = businessLogDF.count()

      val systemName = "ztyth"
      val tempView = systemName + "_businesslog"

      var recordId = record.recordId

      businessLogDF.createOrReplaceTempView(tempView)

      val sqlText = "SELECT oid,systemId,createDate,pageCode,pageName,functionCode,functionName,ip,corpCode,username,message,'"+recordId+"' recordId from " + tempView
      //val tableName = "t_bd_" + systemName + "_log"
      //spark.sql(sqlText).write.mode("append").saveAsTable(tableName)
      save(spark.sql(sqlText))
      log("通用业务日志清洗入库完毕")
  }

  /**
    * 处理删除日志
    * @param originalSource
    * @param spark
    */
  def processDeleteLog(originalSource:org.apache.spark.rdd.RDD[LogInfo],spark:SparkSession , record:Record, cacheEn2CnMap:Map[String,String]) : Unit = {
      import spark.implicits._
      //表头配置
      val delHeadSelConfigList = List(
        SelConfig("T_ZTYTH_HB_HEAD","一体化备案","HB_INFO","删除","DELETE_HEAD","删除表头信息","OID"),
        SelConfig("T_ZTYTH_ENTRY_HEAD_E","出口报关单","ENTRY_E","删除","DELETE_HEAD","删除报关单信息","OID"),
        SelConfig("T_ZTYTH_ENTRY_HEAD_I","进口报关单","ENTRY_I","删除","DELETE_HEAD","删除报关单信息","OID"))
      //表体自身配置
      val delBodySelConfigList = List(
        SelConfig("T_ZTYTH_HB_IMG","一体化备案","HB_INFO","料件删除","DELETE_IMG","删除料件信息","OID"),
        SelConfig("T_ZTYTH_HB_IMG_CHANGE","一体化备案","HB_INFO","料件删除","DELETE_IMG","删除料件信息","OID"),
        SelConfig("T_ZTYTH_HB_EXG","一体化备案","HB_INFO","成品删除","DELETE_EXG","删除成品信息","OID"),
        SelConfig("T_ZTYTH_HB_EXG_CHANGE","一体化备案","HB_INFO","成品删除","DELETE_EXG","删除成品信息","OID"),
        SelConfig("T_ZTYTH_HB_CONSUME","一体化备案","HB_INFO","单损耗删除","DELETE_CONSUME","删除单损耗信息","OID"),
        SelConfig("T_ZTYTH_HB_CONSUME_CHANGE","一体化备案","HB_INFO","单损耗删除","DELETE_CONSUME","删除单损耗信息","OID"),
        SelConfig("T_ZTYTH_ENTRY_LIST_I","进口报关单","ENTRY_I", "表体删除","DELETE_LIST","删除表体信息","OID"),
        SelConfig("T_ZTYTH_ENTRY_LIST_E","出口报关单","ENTRY_E","表体删除","DELETE_LIST","删除表体信息","OID"),
        SelConfig("T_ZTYTH_HB_DCR_BYPRO","一体化核销","HB_DCR","副产品申报表删除","DELETE_BYPRO","删除副产品申报表信息","HB_NO"),
        SelConfig("T_ZTYTH_HB_DCR_SCRAP","一体化核销","HB_DCR","边角料申报表删除","DELETE_SCRAP","删除边角料申报表信息","HB_NO")
      )
      val delBodyLinkConfigMap = Map(
        "T_ZTYTH_HB_IMG" -> LinkConfig("料件","T_ZTYTH_HB_IMG","HEAD_OID","T_ZTYTH_HB_HEAD","OID",List(ConfigField("COMP_NO","企业编码"),ConfigField("COP_EMS_NO","企业内部编号"),ConfigField("HB_NO","手册编号")),null),
        "T_ZTYTH_HB_IMG_CHANGE" -> LinkConfig("料件","T_ZTYTH_HB_IMG_CHANGE","HEAD_CHANGE_OID","T_ZTYTH_HB_HEAD_CHANGE","OID",List(ConfigField("COMP_NO","企业编码"),ConfigField("COP_EMS_NO","企业内部编号"),ConfigField("HB_NO","手册编号")),null),
        "T_ZTYTH_HB_EXG" -> LinkConfig("成品","T_ZTYTH_HB_EXG","HEAD_OID","T_ZTYTH_HB_HEAD","OID",List(ConfigField("COMP_NO","企业编码"),ConfigField("COP_EMS_NO","企业内部编号"),ConfigField("HB_NO","手册编号")),null),
        "T_ZTYTH_HB_EXG_CHANGE" -> LinkConfig("成品","T_ZTYTH_HB_EXG_CHANGE","HEAD_CHANGE_OID","T_ZTYTH_HB_HEAD_CHANGE","OID",List(ConfigField("COMP_NO","企业编码"),ConfigField("COP_EMS_NO","企业内部编号"),ConfigField("HB_NO","手册编号")),null),
        "T_ZTYTH_HB_CONSUME" -> LinkConfig("单耗","T_ZTYTH_HB_CONSUME","HEAD_OID","T_ZTYTH_HB_HEAD","OID",List(ConfigField("COMP_NO","企业编码"),ConfigField("COP_EMS_NO","企业内部编号"),ConfigField("HB_NO","手册编号")),null),
        "T_ZTYTH_HB_CONSUME_CHANGE" -> LinkConfig("单耗","T_ZTYTH_HB_CONSUME_CHANGE","HEAD_CHANGE_OID","T_ZTYTH_HB_HEAD_CHANGE","OID",List(ConfigField("COMP_NO","企业编码"),ConfigField("COP_EMS_NO","企业内部编号"),ConfigField("HB_NO","手册编号")),null),
        "T_ZTYTH_ENTRY_LIST_I" -> LinkConfig("进口表体","T_ZTYTH_ENTRY_LIST_I","HEAD_OID","T_ZTYTH_ENTRY_HEAD_I","OID",List(ConfigField("HB_NO","手册编号"),ConfigField("ENTRY_ID","报关单号"),ConfigField("COP_EMS_NO","企业内部编号")),null),
        "T_ZTYTH_ENTRY_LIST_E" -> LinkConfig("出口表体","T_ZTYTH_ENTRY_LIST_E","HEAD_OID","T_ZTYTH_ENTRY_HEAD_E","OID",List(ConfigField("HB_NO","手册编号"),ConfigField("ENTRY_ID","报关单号"),ConfigField("COP_EMS_NO","企业内部编号")),null),
        "T_ZTYTH_HB_DCR_BYPRO" -> LinkConfig("副产品申报表","T_ZTYTH_HB_DCR_BYPRO","HB_NO","T_ZTYTH_HB_DCR_HEAD","HB_NO",List(ConfigField("HB_NO","手册编号"),ConfigField("TRADE_CODE","经营单位编码")),null),
        "T_ZTYTH_HB_DCR_SCRAP" -> LinkConfig("边角料申报表","T_ZTYTH_HB_DCR_SCRAP","HB_NO","T_ZTYTH_HB_DCR_HEAD","HB_NO",List(ConfigField("HB_NO","手册编号"),ConfigField("TRADE_CODE","经营单位编码")),null)
      )

      //筛选出所有的日志
      val delLog = originalSource.filter(x => x.isDelete == "false" && x.logType == "delete")

      //记录行数
      record.delCount = delLog.count()
      //缓存数据
      //delLog.cache();

      // Array(LogInfo(apollo,2018-06-26 15:51:18,ZTYTH-HbImg,手册设立料件, , ,::1,9999999999,lixy,false,delete,{"TableName":"T_ZTYTH_HB_IMG_CHANGE","DeleteRow":{"OID":"56246157-91b3-420e-afec-c927ce261574","HB_NO"
      // :"C23031122010","G_NO":"4","USE_TYPE":"2","CODE_T":"7406101000","G_NAME":"铜粉","G_MODEL":"","UNIT":"035","UNIT_1":"035","COUNTRY_CODE":"112","DEC_PRICE":"2","CURR":"502","QTY":"2","MAIN_FLAG":"1","NOTE":"
      // ","MODIFY_MARK":"3","APPR_AMT":"4","DUTY_MODE":"3","HEAD_CHANGE_OID":"b030bf93-36fa-4e43-913a-7209204ef788","INSERT_TIME":"2018/6/26 15:51:12","INSERT_USER":"lixy","DBSOURCE":"0","UPDATE_TIME":"","UPDATE_USER":""},
      // "PriKeyValue":"56246157-91b3-420e-afec-c927ce261574"}))


      //    val newDelLog1 = delLog.filter(x=> {
      //      val jsonContent = JSON.parseObject(x.message)
      //      delBodyLinkConfigMap.contains(jsonContent.get("TableName").toString())})

      val newDelLog = delLog.map(line =>{
        //val line = delLog.take(1)(0)
        val jsonContent = JSON.parseObject(line.message)
        //获取linkOid
        var lOid ="";

        if(delBodyLinkConfigMap.contains(jsonContent.get("TableName").toString()))
        {
          val linkConfig = delBodyLinkConfigMap(jsonContent.get("TableName").toString())
          val drJson = JSON.parseObject(jsonContent.get("DeleteRow").toString())
          if(drJson!=null)
          {
            val lf = drJson.get(linkConfig.linkField)
            if(lf!=null)
              lOid = lf.toString()
          }
        }
        //中英转换
        val drString = en2cnForDelete(jsonContent.get("TableName").toString(),jsonContent.get("DeleteRow").toString(),cacheEn2CnMap )
        LogInfoForDel(line.oid,line.systemId,line.createDate,line.pageCode,line.pageName,line.functionCode,line.functionName,line.ip,line.corpCode,line.username,line.isDelete,line.logType,"",jsonContent.get("TableName").toString()
          ,drString,jsonContent.get("PriKeyValue").toString(),lOid);
      })

      //表头
      for (delHead <- delHeadSelConfigList) {
        // val delHead = SelConfig("T_ZTYTH_ENTRY_HEAD_I","进口报关单","ENTRY_I","删除","DELETE_HEAD","删除报关单信息","OID")
        //获取含有表头的数据
        val headLog = newDelLog.filter(x=>x.tableName == delHead.tableName) //delLog.filter(x=> x.message.indexOf("\"TableName\":\"" + delHead.tableName) > -1 )

        val headLogDF : org.apache.spark.sql.DataFrame  = headLog.toDF()

        val systemName = "ztyth"
        val tempView = systemName + "_delHeadLog"

        headLogDF.createOrReplaceTempView(tempView)
        //headLogDF.write.mode("overwrite").saveAsTable("t_temp_head")

        //系统ID(systemId) 创建时间(记录时间)(createDate) 模块Code(pageCode) 模块名称(pageName) 操作类型Code(functionCode) 操作类型(functionName) IP 企业编码(corpCode)	用户名(username)	查看详情(message)
        val sqlText = "SELECT oid,systemId,createDate, '"+ delHead.moduleCode+"' as pageCode, '" + delHead.moduleName+ "' as pageName,'"+ delHead.opTypeCode+"' as functionCode,'" + delHead.opType+"' as functionName,ip,corpCode,username,concat_ws('','" + delHead.remark  + "',':',deleteRow) as message, '"+ record.recordId +"' as recordId  from " + tempView
        //val tableName = "t_bd_" + systemName + "_log"
        val headResult = spark.sql(sqlText)
        //headResult.write.mode("append").saveAsTable(tableName)
        save(headResult)
        log("表头删除"+ delHead.tableName +"日志清洗入库完毕")
      }

      log("表头删除日志清洗入库完毕")
      //表体
      for(delBody <- delBodySelConfigList)
      {
        //val delBody =SelConfig("T_ZTYTH_HB_IMG","一体化备案","HB_INFO","料件删除","DELETE_IMG","删除料件信息","OID")
        val bodyLog = newDelLog.filter(x=>x.tableName == delBody.tableName) //delLog.filter(x=> x.message.indexOf("\"TableName\":\"" + delHead.tableName) > -1 )
        val bodyLogDF : org.apache.spark.sql.DataFrame  = bodyLog.toDF()

        val linkConfig = delBodyLinkConfigMap(delBody.tableName)

        var headSql =" "
        if(linkConfig!=null && linkConfig.headFields.length >0)
        {
            headSql = ""
            linkConfig.headFields.map( f => {
              headSql += "'" + f.cnName + ":',t." + f.name + ","
            })
            headSql = headSql.substring(0,headSql.length-1) //+ ") as headMsg"
        }
        val systemName = "ztyth"
        val tempView = systemName + "_delBodyLog"
        bodyLogDF.createOrReplaceTempView(tempView)
        //bodyLogDF.write.mode("overwrite").saveAsTable("t_temp_body")
        //参考 ：系统ID(systemId) 创建时间(记录时间)(createDate) 模块Code(pageCode) 模块名称(pageName) 操作类型Code(functionCode) 操作类型(functionName) IP 企业编码(corpCode)	用户名(username)	查看详情(message)
        //参考：
        //spark.sql("SELECT log.systemId,log.createDate, '' as pageCode, '一体化备案' as pageName,'' as functionCode,'成品删除' as functionName,log.ip,log.corpCode,log.username,concat('删除成品信息| 表头信息:', '企业编码:',t.COMP_NO,'企业内部编号:',t.COP_EMS_NO,'手册编号:',t.HB_NO,'主表信息:',log.deleteRow) as message, '123' as recordId  from ztyth_delBodyLog  log LEFT JOIN  default.T_ZTYTH_HB_HEAD t ON (log.linkOid = t.OID)")
        val sqlText = "SELECT log.oid,log.systemId,log.createDate, '" + delBody.moduleCode+ "' as pageCode, '" + delBody.moduleName+ "' as pageName,'"+ delBody.opTypeCode+"' as functionCode,'" + delBody.opType+"' as functionName,log.ip,log.corpCode,log.username,concat_ws('','" + delBody.remark  + "| 表头信息:', " + headSql +",'| 主表信息:',log.deleteRow) as message, '" + record.recordId + "' as recordId  from " + tempView + "  log LEFT JOIN default."+ linkConfig.headTableName +" t ON (log.linkOid = t." + linkConfig.headLinkField+")"
        //println(delBody.tableName)
        //println(sqlText)
        val br = spark.sql(sqlText)
        //br.write.mode("append").saveAsTable("t_bd_ztyth_log")
        save(br)
        log("表体删除"+ delBody.tableName +"日志清洗入库完毕")
      }
      log("表体删除日志清洗入库完毕")

  }

  def processUpdateLog(originalSource:org.apache.spark.rdd.RDD[LogInfo],spark:SparkSession, record:Record,cacheEn2CnMap:Map[String,String]) : Unit = {
    import spark.implicits._
    var recordId = record.recordId

    //表头
    val updateHeadSelConfigList = List(
      SelConfig("T_ZTYTH_HB_HEAD","一体化备案","HB_INFO","表头修改","UPDATE_HEAD","修改表头信息","OID"),
      SelConfig("T_ZTYTH_HB_HEAD_CHANGE","一体化备案","HB_INFO","表头修改","UPDATE_HEAD","修改表头信息","OID"),
      SelConfig("T_ZTYTH_ENTRY_HEAD_E","出口报关单","ENTRY_E","表头修改","UPDATE_HEAD","修改表头信息","OID"),
      SelConfig("T_ZTYTH_ENTRY_HEAD_I","进口报关单","ENTRY_I","表头修改","UPDATE_HEAD","修改表头信息","OID"),
      SelConfig("T_ZTYTH_HB_DCR_HEAD","一体化核销","HB_DCR","报核申报单","UPDATE_HEAD","保存报核申报单信息","HB_NO")
    )

    //表体
    val updateBodySelConfigList = List(
      SelConfig("T_ZTYTH_HB_IMG","一体化备案","HB_INFO","料件修改","UPDATE_IMG","修改料件信息","OID"),
      SelConfig("T_ZTYTH_HB_IMG_CHANGE","一体化备案","HB_INFO","料件修改","UPDATE_IMG","修改料件信息","OID"),
      SelConfig("T_ZTYTH_HB_EXG","一体化备案","HB_INFO","成品修改","UPDATE_EXG","修改成品信息","OID"),
      SelConfig("T_ZTYTH_HB_EXG_CHANGE","一体化备案","HB_INFO","成品修改","UPDATE_EXG","修改成品信息","OID"),
      SelConfig("T_ZTYTH_HB_CONSUME","一体化备案","HB_INFO","单损耗修改","UPDATE_CONSUME","修改单损耗信息","OID"),
      SelConfig("T_ZTYTH_HB_CONSUME_CHANGE","一体化备案","HB_INFO","单损耗修改","UPDATE_CONSUME","修改单损耗信息","OID"),
      SelConfig("T_ZTYTH_ENTRY_LIST_I","进口报关单","ENTRY_I","表体修改","UPDATE_LIST","修改表体信息","OID"),
      SelConfig("T_ZTYTH_ENTRY_LIST_E","出口报关单","ENTRY_E","表体修改","UPDATE_LIST","修改表体信息","OID"),
      SelConfig("T_ZTYTH_HB_DCR_IMP_IMG","一体化核销","HB_DCR","进口料件汇总表","UPDATE_IMP_IMG","修改进口料件汇总表","HB_NO"),
      SelConfig("T_ZTYTH_HB_DCR_BASIC_INFO","一体化核销","HB_DCR","手册基本信息","UPDATE_BASIC_INFO","修改手册基本信息","HB_NO"),
      SelConfig("T_ZTYTH_HB_DCR_BYPRO","一体化核销","HB_DCR","副产品申报表","UPDATE_BYPRO","修改副产品申报表","OID"),
      SelConfig("T_ZTYTH_HB_DCR_SCRAP","一体化核销","HB_DCR","边角料申报表","UPDATE_SCRAP","修改边角料申报表","OID"),
      SelConfig("T_ZTYTH_HB_DCR_BALANCE","一体化核销","HB_DCR","成品退换平衡表申报单","UPDATE_BALANCE","修改成品退换平衡表申报单","OID")
    )

    //筛选出所有的日志
    val updateLog = originalSource.filter(x => x.isDelete == "false" && x.logType == "update")

    //记录count
    record.updateCount = updateLog.count()

    val newUpdateLog = updateLog.map(line =>{
      val jsonContent = JSON.parseObject(line.message)
      //中英转换
      var updateColumn = jsonContent.get("UpdateColumns").toString()
      var tableName= jsonContent.get("TableName").toString()
      var updateColumnCn = en2cnForUpdate(tableName,updateColumn,cacheEn2CnMap)

      LogInfoForUpdate(line.oid,line.systemId,line.createDate,line.pageCode,line.pageName,line.functionCode,line.functionName,line.ip,line.corpCode,line.username,
        "",tableName,updateColumn,jsonContent.get("PriKeyValue").toString(),updateColumnCn)
    })

    var updateHeadConfigMap = Map(
      "T_ZTYTH_HB_HEAD" -> List(ConfigField("COMP_NO","企业编码"), ConfigField("COP_EMS_NO","企业内部编号"),ConfigField("HB_NO","手册编号"),ConfigField("OID","主键OID")),
      "T_ZTYTH_HB_HEAD_CHANGE" -> List(ConfigField("COMP_NO","企业编码"), ConfigField("COP_EMS_NO","企业内部编号"),ConfigField("HB_NO","手册编号"),ConfigField("OID","主键OID")),
      "T_ZTYTH_ENTRY_HEAD_E" ->List(ConfigField("HB_NO","手册编号"),ConfigField("OID","主键OID")),
      "T_ZTYTH_ENTRY_HEAD_I" ->List(ConfigField("HB_NO","手册编号"),ConfigField("OID","主键OID")),
      "T_ZTYTH_HB_DCR_HEAD" ->List(ConfigField("HB_NO","手册编号"),ConfigField("TRADE_CODE","经营单位编码"))
    )

    for (updateHead <- updateHeadSelConfigList) {
      var headFiledList = updateHeadConfigMap(updateHead.tableName)
      var headSql = ""
      if(headFiledList!=null && headFiledList.length >0)
      {
        headFiledList.map(f => {
          headSql += "'" + f.cnName + ":',t." + f.name + ",',',"
        })
        headSql = headSql.substring(0,headSql.length-5) //+ ") as headMsg"
      }
      //获取含有表头的数据
      val headLog = newUpdateLog.filter(x=>x.tableName == updateHead.tableName) //delLog.filter(x=> x.message.indexOf("\"TableName\":\"" + delHead.tableName) > -1 )
      val headLogDF : org.apache.spark.sql.DataFrame  = headLog.toDF()

      val systemName = "ztyth"
      val tempView = systemName + "_updateHeadLog"
      headLogDF.createOrReplaceTempView(tempView)
      //headLogDF.write.mode("overwrite").saveAsTable("t_temp")
      //系统ID(systemId) 创建时间(记录时间)(createDate) 模块Code(pageCode) 模块名称(pageName) 操作类型Code(functionCode) 操作类型(functionName) IP 企业编码(corpCode)	用户名(username)	查看详情(message)
      val sqlText = "SELECT log.oid,log.systemId,log.createDate, '"+updateHead.moduleCode+"' as pageCode, '" + updateHead.moduleName+ "' as pageName," +
        "'"+updateHead.opTypeCode+"' as functionCode,'" + updateHead.opType+"' as functionName,log.ip,log.corpCode,log.username," +
        "concat_ws('','" + updateHead.remark  + "','|',"+headSql+",'| 修改信息:',log.updateColumnsCn) as message, '"+recordId+"' as recordId  " +
        "from " + tempView +" as log left join default."+updateHead.tableName +" t on log.priKeyValue = t."+updateHead.pkField
      //val tableName = "t_bd_" + systemName + "_log"
      val headResult = spark.sql(sqlText)
      //headResult.write.mode("append").saveAsTable(tableName)
      save(headResult)
      log("表头"+ updateHead.tableName +"修改日志清洗入库完毕")
    }
    log("表头修改日志清洗入库完毕")

    val updateBodyLinkConfigMap = Map(
      "T_ZTYTH_HB_IMG" -> LinkConfig("料件","T_ZTYTH_HB_IMG","HEAD_OID","T_ZTYTH_HB_HEAD","OID",List(ConfigField("COMP_NO","企业编码"),ConfigField("COP_EMS_NO","企业内部编号"),ConfigField("HB_NO","手册编号")),null),
      "T_ZTYTH_HB_IMG_CHANGE" -> LinkConfig("料件","T_ZTYTH_HB_IMG_CHANGE","HEAD_CHANGE_OID","T_ZTYTH_HB_HEAD_CHANGE","OID",List(ConfigField("COMP_NO","企业编码"),ConfigField("COP_EMS_NO","企业内部编号"),ConfigField("HB_NO","手册编号")),null),
      "T_ZTYTH_HB_EXG" -> LinkConfig("成品","T_ZTYTH_HB_EXG","HEAD_OID","T_ZTYTH_HB_HEAD","OID",List(ConfigField("COMP_NO","企业编码"),ConfigField("COP_EMS_NO","企业内部编号"),ConfigField("HB_NO","手册编号")),null),
      "T_ZTYTH_HB_EXG_CHANGE" -> LinkConfig("成品","T_ZTYTH_HB_EXG_CHANGE","HEAD_CHANGE_OID","T_ZTYTH_HB_HEAD_CHANGE","OID",List(ConfigField("COMP_NO","企业编码"),ConfigField("COP_EMS_NO","企业内部编号"),ConfigField("HB_NO","手册编号")),null),
      "T_ZTYTH_HB_CONSUME" -> LinkConfig("单耗","T_ZTYTH_HB_CONSUME","HEAD_OID","T_ZTYTH_HB_HEAD","OID",List(ConfigField("COMP_NO","企业编码"),ConfigField("COP_EMS_NO","企业内部编号"),ConfigField("HB_NO","手册编号")),null),
      "T_ZTYTH_HB_CONSUME_CHANGE" -> LinkConfig("单耗","T_ZTYTH_HB_CONSUME_CHANGE","HEAD_CHANGE_OID","T_ZTYTH_HB_HEAD_CHANGE","OID",List(ConfigField("COMP_NO","企业编码"),ConfigField("COP_EMS_NO","企业内部编号"),ConfigField("HB_NO","手册编号")),null),
      "T_ZTYTH_ENTRY_LIST_I" -> LinkConfig("进口表体","T_ZTYTH_ENTRY_LIST_I","HEAD_OID","T_ZTYTH_ENTRY_HEAD_I","OID",List(ConfigField("HB_NO","手册编号"),ConfigField("ENTRY_ID","报关单号"),ConfigField("COP_EMS_NO","企业内部编号")),null),
      "T_ZTYTH_ENTRY_LIST_E" -> LinkConfig("出口表体","T_ZTYTH_ENTRY_LIST_E","HEAD_OID","T_ZTYTH_ENTRY_HEAD_E","OID",List(ConfigField("HB_NO","手册编号"),ConfigField("ENTRY_ID","报关单号"),ConfigField("COP_EMS_NO","企业内部编号")),null),
      "T_ZTYTH_HB_DCR_IMP_IMG" -> LinkConfig("进口料件汇总表","T_ZTYTH_HB_DCR_HEAD","HB_NO","T_ZTYTH_HB_DCR_HEAD","HB_NO",List(ConfigField("HB_NO","手册编号"),ConfigField("TRADE_CODE","经营单位编码")),List(ConfigField("G_NO","料件项号"))),
      "T_ZTYTH_HB_DCR_BASIC_INFO" -> LinkConfig("手册基本信息","T_ZTYTH_HB_DCR_BASIC_INFO","HB_NO","T_ZTYTH_HB_DCR_HEAD","HB_NO",List(ConfigField("HB_NO","手册编号"),ConfigField("TRADE_CODE","经营单位编码")),null),
      "T_ZTYTH_HB_DCR_BYPRO" -> LinkConfig("副产品申报表","T_ZTYTH_HB_DCR_BYPRO","HB_NO","T_ZTYTH_HB_DCR_HEAD","HB_NO",List(ConfigField("HB_NO","手册编号"),ConfigField("TRADE_CODE","经营单位编码")),null),
      "T_ZTYTH_HB_DCR_SCRAP" -> LinkConfig("边角料申报表","T_ZTYTH_HB_DCR_SCRAP","HB_NO","T_ZTYTH_HB_DCR_HEAD","HB_NO",List(ConfigField("HB_NO","手册编号"),ConfigField("TRADE_CODE","经营单位编码")),null),
      "T_ZTYTH_HB_DCR_BALANCE" -> LinkConfig("成品退换平衡表申报单","T_ZTYTH_HB_DCR_BALANCE","HB_NO","T_ZTYTH_HB_DCR_HEAD","HB_NO",List(ConfigField("HB_NO","手册编号"),ConfigField("TRADE_CODE","经营单位编码")),List(ConfigField("EXG_NO","成品项号")))
    )

    for (updateBody <- updateBodySelConfigList) {
      val bodyLog = newUpdateLog.filter(x=>x.tableName == updateBody.tableName)
      val bodyLogDF : org.apache.spark.sql.DataFrame  = bodyLog.toDF()

      val linkConfig = updateBodyLinkConfigMap(updateBody.tableName)
      var headSql =" "
      if(linkConfig!=null && linkConfig.headFields !=null && linkConfig.headFields.length >0)
      {
        linkConfig.headFields.map(f => {
          headSql += "'" + f.cnName + ":',t." + f.name + ",',',"
        })
      }
      if(linkConfig ==null || linkConfig.fileds ==null || linkConfig.fileds.length == 0){
        headSql = headSql.substring(0,headSql.length-5) //+ ") as headMsg"
      }
      var selfSql =""
      if(linkConfig!=null && linkConfig.fileds != null && linkConfig.fileds.length >0)
      {
        linkConfig.fileds.map(f => {
          selfSql += "'" + f.cnName + ":',s." + f.name + ",',',"
        })
        selfSql = selfSql.substring(0,selfSql.length-5) //+ ") as headMsg"
      }
      val systemName = "ztyth"
      val tempView = systemName + "_updateBodyLog"
      bodyLogDF.createOrReplaceTempView(tempView)

      val sqlText = "SELECT log.oid,log.systemId,log.createDate, '" + updateBody.moduleCode+ "' as pageCode, '" + updateBody.moduleName +
        "' as pageName,'"+ updateBody.opTypeCode+"' as functionCode,'" + updateBody.opType+"' as functionName," +
        " log.ip,log.corpCode,log.username,concat_ws('','" + updateBody.remark  + "| 表头信息:'," + headSql + selfSql +
        ",'| 主键OID:',log.priKeyValue,'| 修改信息:',log.updateColumnsCn) as message, '"+recordId+"' as recordId  from " + tempView + " as log " + "" +
        "LEFT JOIN default."+ updateBody.tableName +" s ON (log.priKeyValue = s."+ updateBody.pkField +") " +
        "LEFT JOIN default."+ linkConfig.headTableName +" t ON (s."+linkConfig.linkField+" = t."+linkConfig.headLinkField+")"

      val br = spark.sql(sqlText)
      //br.write.mode("append").saveAsTable("t_bd_ztyth_log")
      save(br)
      log("表体"+ updateBody.tableName +"修改日志清洗入库完毕")
    }
    log("表体修改日志清洗入库完毕")
  }

}

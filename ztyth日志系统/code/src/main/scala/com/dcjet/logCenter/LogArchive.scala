package com.dcjet.logCenter

import org.apache.spark.sql.SparkSession
import org.joda.time.DateTime



object LogArchive {

  case class Config(name:String,rootPath:String,systemId:String,tgtDays:BigInt, remark:String)
  case class Record (recordId:String, configName:String, startDate:String,var endDate:String,var totalCount:BigInt,var remark:String,var originFile:String, var archivePath:String)

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().enableHiveSupport().appName("logArchive").master("yarn-cluster").getOrCreate()

    import spark.implicits._
    //初始化配置数据
    //Seq(Config("janus197","data/logHDFS","janus197",3,"test")).toDF().write.mode("append").saveAsTable("t_bd_logArchive_config")
    //Seq(Config("janus198","data/logHDFS","janus197",3,"test")).toDF().write.mode("append").saveAsTable("t_bd_logArchive_config")

    //读取备份配置
    val configs  = spark.sql("select * from t_bd_logArchive_config")//.toDF().as[Config]
    configs.map(configRow => {
      val config =Config(configRow.get(0).toString(),configRow.get(1).toString(),configRow.get(2).toString(), BigInt(configRow.get(3).toString()),configRow.get(4).toString())
      try {
        archive(config, spark);
      } catch {
        case (ex: Exception) => {
          println("归档" + config.name + "出问题" + ex);
        }
      }
      ""
    })
  }

  /**
    * 获取最新的几个文件
    * @param spark
    * @return
    */
  def getPendingFilePath(spark:SparkSession,config : Config) : List[String] = {
    import org.joda.time._
    val date = DateTime.now()

    var list:List[String] = List()
    val hdfs = org.apache.hadoop.fs.FileSystem.get(spark.sparkContext.hadoopConfiguration)
    val max = config.tgtDays.toInt + 1
    for( days <- Range(1,  max) ){
      val fileName:String =  config.rootPath +"/" + config.systemId+"/" + date.minusDays(days).toString("yyyy-MM-dd") + ".log"
      //println(fileName)
      if(hdfs.exists(new org.apache.hadoop.fs.Path( fileName))){
        list = list.::(fileName)
      }
    }
    list
  }

  /**
    * 备份
    */
  def archive(config : Config,spark:SparkSession) : Unit={
    import spark.implicits._
    var record = Record(DateTime.now().toString("yyyyMMddHHmmss"), config.name,DateTime.now().toString("yyyyMMddHHmmss"),null,0,"","","");
    val files = getPendingFilePath(spark,config)
    if(files.length>0) {
      record.originFile = files.reduce(_+"," + _)
      val originLines:org.apache.spark.rdd.RDD[String] = spark.sparkContext.textFile(record.originFile)

      //得到归档名称
      val archiveName =  config.rootPath+"/"+config.systemId+".archive/" + org.joda.time.DateTime.now().toString("yyyyMMddHHmmss") + ".parquet"
      //生成归档文件
      originLines.toDF().write.option("compression","gzip").parquet(archiveName)
      record.archivePath= archiveName
      //删除原文件
      val hdfs = org.apache.hadoop.fs.FileSystem.get(spark.sparkContext.hadoopConfiguration)
      for(f:String <-files)
      {
        hdfs.delete(new org.apache.hadoop.fs.Path(f),true)
      }
      record.endDate = DateTime.now().toString("yyyy-MM-dd HH:mm:ss")
      //保存归档记录
      Seq(record).toDF().write.mode("append").saveAsTable("t_bd_logArchive_record")
    }
  }

}

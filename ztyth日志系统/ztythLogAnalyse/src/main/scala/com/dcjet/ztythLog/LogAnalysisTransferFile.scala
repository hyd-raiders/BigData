package com.dcjet.ztythLog

//spark2-shell --jars /home/sparkjars/fastjson-1.2.47.jar

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.joda.time.DateTime

import org.joda.time.format._

/**
  * 文件压缩转移工具
  * spark2-submit --class com.dcjet.ztythLog.LogAnalysisTransferFile --master yarn --deploy-mode cluster /data/workspace/spark/ztythloganalyse.jar debug ztyth_apollo_179#ztyth_apollo_180 2018-10-22 3
  */
object LogAnalysisTransferFile {

  /**
    * 记录日志方法
    * @param message
    */
  def log(message:String):Unit={
    println(message);
  }

  /**
    * main方法
    * @param args
    */
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().enableHiveSupport().appName("ztythLog").master("yarn-cluster").getOrCreate()

    import spark.implicits._

    var serverList: String = ""
    var beginDate: String = ""
    var daysCount:Int = 0
    if(args.length>0 && args(0) == "debug") {
      serverList = args(1)
      beginDate = args(2)
      daysCount = Integer.parseInt(args(3))
    }
    else{
      log("请在debug摸试下操作，退出执行")
      return
    }


    val hdfs = org.apache.hadoop.fs.FileSystem.get(spark.sparkContext.hadoopConfiguration)

    val date = DateTime.parse(beginDate+" 0:00:01",DateTimeFormat.forPattern("yyyy-MM-dd H:mm:ss"))

    val serverAry = serverList.split("\\#")
    var files:List[String] = null
    var logPath:String = ""
    var originLines:org.apache.spark.rdd.RDD[String] = null
    var archiveName = ""

    for (j <- 1 to daysCount) {
      files = List()
      for(i <- 0 until serverAry.length) {
        val fileName: String = "/data/logHDFS/" + serverAry(i) + "/" + date.minusDays(j).toString("yyyy-MM-dd") + ".log"
        if (hdfs.exists(new org.apache.hadoop.fs.Path(fileName))) {
          files = files.::(fileName)
        }
      }
      if(files.length !=0){
        logPath =files.reduce(_+"," + _)
        originLines = spark.sparkContext.textFile(logPath)
        archiveName = "/data/logHDFS/apollo.archive/" + date.minusDays(j).toString("yyyyMMdd") + ".parquet"
        originLines.toDF().write.option("compression","gzip").parquet(archiveName)
        for(f:String <-files)
        {
          hdfs.delete(new org.apache.hadoop.fs.Path(f),true)
        }
        log("成功压缩转移日志文件:"+logPath)
      }
      else{
        log(date.minusDays(j).toString("yyyy-MM-dd") + "没有原日志文件")
      }
    }
  }


}

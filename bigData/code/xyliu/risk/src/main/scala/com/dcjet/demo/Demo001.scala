package com.dcjet.demo

object Demo001 {
  def main(args: Array[String]) {

    val spark = org.apache.spark.sql.SparkSession.builder().enableHiveSupport().appName("test001").master("yarn-cluster").getOrCreate()
    println("demo001")
    if(args.length > 0) { println(args(0)) }

    spark.sparkContext.stop()
  }
}

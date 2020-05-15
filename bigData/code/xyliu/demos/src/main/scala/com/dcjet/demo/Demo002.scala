package com.dcjet.demo

import org.apache.avro.generic.GenericData.StringType
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructField


/**
  * 新的spark开发推荐使用 Datasets
  *  Spark SQL, DataFrames and Datasets 是spark core的主流操作api，必须学会
  */
object Demo002 {
  def main(args: Array[String]): Unit = {
    val spark =org.apache.spark.sql.SparkSession.builder().enableHiveSupport().appName("demo002").master("yarn-cluster").getOrCreate()
    // For implicit conversions like converting RDDs to DataFrames
    import spark.implicits._
    //import spark.sql

    /**
      * 造数据
       su hdfs
       cd
       vim data.json
      *
       [{"name":"abc","code":"sdfsdfdsfdsfdfsdf","age":78,"page":12},{"name":"bcd","code":"aaaaa","age":78,"page":33}]
      * 输入数据
       alias h='hadoop fs'
       h -mkdir -p /temp
       h -put -f data.json /temp
      * */

    //read json
    val dataframe1= spark.read.json("/temp/data.json")
    dataframe1.show()

    dataframe1.printSchema()
    dataframe1.select("name").show()
    dataframe1.filter($"age" > 23).show()
    dataframe1.groupBy("age").count().show()

    //sql
    /**
      * 创建数据
      hive创建表及数据
      create table demo001(
    id      int
   ,name    string
   ,hobby   string
   ,age     int
)
row format delimited
fields terminated by ','
collection items terminated by '-'
map keys terminated by ':'
;
      insert into demo001 values(1,'lili','coomputer game',23);
      insert into demo001 values(2,'lucy','basketball',22);
      insert into demo001 values(3,'rue','sg',100);

      */
    //查询hive
    spark.sql("select * from demo001").show()

    val dataframe2= spark.read.json("/temp/data.json")
    dataframe2.printSchema()
    //创建临时视图
    dataframe2.createOrReplaceTempView("demo_data")
    spark.sql("select * from demo_data").show()
    //全局临时视图
    dataframe2.createOrReplaceGlobalTempView("demo_data2")
    spark.sql("select * from global_temp.demo_data2").show()

    spark.newSession().sql("select * from global_temp.demo_data2").show()

    //case class 样例类 2.1以后出现的类，主要支持模式匹配，需要自行理解
    case class  Person(name:String , age :Int)

    //创建DataSet
    val ccDs = Seq(Person("rue",12)).toDS()
    ccDs.show()
    Seq(1,2,3).toDS().map(_ + 2).collect().foreach(x=> print(x.toString + ' '))

    //DataFrames  to DS (注意schema）
    case class Demo(age:BigInt,name:String,code:String,page:BigInt)
    spark.read.json("/temp/data.json").as[Demo].show()

    //dataframe 反射
    /**
      * 造数据
        su hdfs
       cd
       vim people.txt
      *
Michael, 29
Andy, 30
Justin, 19

      * 输入数据
       alias h='hadoop fs'
       h -mkdir -p /temp
       h -put -f people.txt /temp
       h -cat /temp/people.txt
      */
     case class  Person1(name:String , age :Int)
     val dataframe3 =spark.sparkContext.textFile("/temp/people.txt").map(_.split(',')).map(attrs => Person1(attrs(0),attrs(1).trim().toInt)).toDF()
     dataframe3.createOrReplaceTempView("p1")
     val rsDf1= spark.sql("select * from p1")
     rsDf1.show()
    rsDf1.map(m => "name:" + m(0)).show()
    rsDf1.map(m=>"name:" + m.getAs[String]("name")).show()

    implicit val mapEncoder = org.apache.spark.sql.Encoders.kryo[Map[String, Any]]
    rsDf1.map(m=> m.getValuesMap[Any](List("name","age"))).collect()
    //res10: Array[Map[String,Any]] = Array(Map(name -> Michael, age -> 29), Map(name -> Andy, age -> 30), Map(name -> Justin, age -> 19))

    //自己创建dataframe with schema
    val rdd20= spark.sparkContext.textFile("/temp/people.txt")
    val schemaString = "name age"

    import org.apache.spark.sql.types._
    val fields = schemaString.split(" ").map(f=> StructField(f,StringType,true))
    val schema = StructType(fields)
    val rowRdd= rdd20.map(_.split(",")).map(attrs => org.apache.spark.sql.Row(attrs(0),attrs(1).trim))
    val dataframe4= spark.createDataFrame(rowRdd,schema)
    dataframe4.show()

    //Untyped User-Defined Aggregate Functions 用户自动聚合函数 请自学
    //Type-Safe User-Defined Aggregate Functions

    //数据源
    //dataframe4.write.parquet("/temp/1.parquet")
    //dataframe4.write.json("/temp/1.json")
    //dataframe4.write.saveAsTable("abc")
    //spark.read.load("/temp/1.parquet")
    //...
    spark.sql("SELECT * FROM parquet.`/temp/1.parquet`")
    /*dataframe4.write.mode("error").save("")
    append
    overwrite
    ignore*/

    //json

  }
}

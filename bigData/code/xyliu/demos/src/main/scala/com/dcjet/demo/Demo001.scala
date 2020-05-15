package com.dcjet.demo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * RDD 操作，RDD虽然已经过时，也不推荐使用，但对于理解mapreduce的过程和常用封装有好处，可以快速过一遍
  */
object Demo001 {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("demo001").setMaster("local")
    val sc= new SparkContext(conf)


    //并行化集合
    val data= Array(1,2,3,4,5)
    val distData =sc.parallelize(data)

    //basic
    /**
    * 造数据
    * su hdfs
    * cd
    * vim data.txt
    * 输入数据
    * alias h='hadoop fs'
    * h -mkdir -p /temp
    * h -put data.txt /temp
    * */

    val lines = sc.textFile("/temp/data.txt")  //  /temp/   or /temp/*.txt
    val lineLength = lines.map(s=>s.length)
    val totalLength = lineLength.reduce((a,b)=>a+b)
    //lineLength.persist()
    lineLength.collect().foreach { println }
    println(totalLength)

    //function1
    object myFunction{ def fun1(s:Int):Int ={ s + 2}}
    val frs = distData.map(s=>myFunction.fun1(s))  //ParallelCollectionRDD
    frs.collect().foreach(println)
    val frs2 = lineLength.map(s=>myFunction.fun1(s)) //MapPartitionsRDD
    frs2.collect().foreach(println)

    //分区
    val someRDD = sc.parallelize(1 to 100, 4)
    someRDD.getNumPartitions

    //键值处理
    val lines1 = sc.textFile("/temp/*.txt")
    val words = lines1.flatMap(s => s.split(' '))
    val pairs = words.map(s=> (s,1))
    val counts = pairs.reduceByKey((a,b) => a+b)
    counts.collect().foreach(println)

    /**
     * Transformations
     */
    val lines2= sc.textFile("/temp/")
    val words2 = lines2.flatMap(s => s.split(' '))
    //map 略
    println(lines2.map(s=> s.split(' ')).collect().length)
    //flatMap 略
    println(words2.collect().length)
    //filter
    println(words2.filter(s => { s != "abc"}).collect().length)
    //mapPartitions  效率优于 map
    var rdd1 = sc.makeRDD(1 to 10,3)
    var rdd2= rdd1.mapPartitions(x => {
      var res = List[(Int, Int)]()
      while (x.hasNext)
      {
        val cur = x.next()
        res.::=(cur,cur*2)
      }
      res.iterator
    })
    println(rdd2.collect().mkString)
    //mapPartitionsWithIndex
    val rdd3 = sc.makeRDD(1 to 5, 2)
    val rdd4 = rdd3.mapPartitionsWithIndex(( i, x)=>{
       var res= List[String]()

       while (x.hasNext)
         {
           var temp = i.toString +  "|" +x.next().toString
           res.::=(temp)
         }
        res.iterator
    })
    rdd4.collect()

    // sample 取样
    val rdd5 =sc.parallelize(1 to 10 ,2)
    val rdd6 = rdd5.sample(true,0.5,4)
    rdd6.collect().foreach(x => print(x + " "))

    //union
    val rdd7 =sc.parallelize(1 to 10 ,2)
    val rdd8 =sc.parallelize(11 to 20 ,3)
    rdd7.union(rdd8).collect().foreach(x=>print(x + " "))

    //intersection
    val rdd9 =sc.parallelize(1 to 10 ,2)
    val rdd10 =sc.parallelize(5 to 15 ,3)
    rdd9.intersection(rdd10).collect().foreach(x=>print(x + " "))

    //distinct
    sc.makeRDD(1 to 10,2).union(sc.makeRDD(5 to 15,2)).distinct(2).collect().foreach(x=>print(x + " "))

    //groupByKey
    sc.parallelize(1 to 10,2).union(sc.makeRDD( 5 to 15,2)).map(x=>(x,1)).groupByKey(2).collect().foreach(x=>print(x + " "))

    //reduceByKey
    sc.parallelize(1 to 10,2).union(sc.makeRDD( 5 to 15,2)).map(x=>(x,1)).reduceByKey(_+_).collect().foreach(x=>print(x + " "))

    //aggregateByKey
    val rdd11 = sc.parallelize(1 to 10,1)
    rdd11.aggregate((1,0))((acc,number)=>( acc._1 + number,acc._2 + 1),(par1,par2)=>(par1._1 + par2._1,par1._2+par2._2))
    // (1,0) 初始值
    //(acc,number)=>( acc._1 + number,acc._2 + 1)  每个分区的函数    acc是指上一次运算结果 number即每次rdd中的输入值
    //(par1,par2)=>(par1._1 + par2._1,par1._2+par2._2)  是所有分区的操作函数

    //sortBy
    sc.makeRDD(1 to 10,2).union(sc.makeRDD(5 to 15,2)).sortBy(x=>x,false).collect().foreach(x=>print(x + " "))

    //join
    val rdd12 = sc.parallelize(List((1, 3), (1, 3), (1, 4), (2, 3)))
    val rdd13 = sc.parallelize(List((1, 2), (1, 4), (1, 7), (2, 2)))
    rdd12.join(rdd13).collect().foreach(x=>print(x + " "))

    //cogroup
    sc.parallelize(List((1, 3), (1, 3), (1, 4), (2, 3))).cogroup(sc.parallelize(List((1, 2), (1, 4), (1, 7), (2, 2)))).collect().foreach(x=>print(x + " "))

    //cartesian 笛卡尔计算
    sc.makeRDD(1 to 5,2).cartesian(sc.makeRDD(6 to 10,1)).collect().foreach(x=>print(x + " "))

    //pipe  略

    //coalesce
    //repartition
    //repartitionAndSortWithinPartitions

    /**
     * actions 前面操作中大体都用到过
     */

    /**
      * Broadcast  全局变量
      */
    val b1= sc.broadcast(1)
    b1.value

    /**
      * Accumulators
      */
    val acc = sc.longAccumulator("abc")
     sc.makeRDD(1 to 10,2).map(x=> acc.add(x)).collect()
     println(acc.value)
  }

  }







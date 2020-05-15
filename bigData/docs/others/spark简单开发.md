## 创建工程
依次点击File->New->Project，选择Scala->SBT，下一步，打开如下窗口：
java sdk 选择 1.8
sbt 无要求
scala 选择 2.11.x

## 修改sbt配置
编辑build.sbt:
`
name := "spark1"

version := "0.1"

scalaVersion := "2.11.12"

libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.2.0"
libraryDependencies += "org.apache.spark" %% "spark-hive" % "2.2.0"

`
#根据跳出的提示刷新项目，会有点慢
refresh project

## 创建object
src->scala右击新建包（如com.dcjet.demo)  再建scala类,选择类型为object

## 编写内容：
`
package com.dcjet.demo
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.sql.hive.HiveContext

/**
  *
  * test
  */
object Test1 {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("test").setMaster("local[1]")
    val sc = new SparkContext(conf)
    val sqlContext = new HiveContext(sc)
    sqlContext.table("ENTRY_HEAD") // 库名.表名 的格式
      .registerTempTable("entry")  // 注册成临时表
    sqlContext.sql(
      """
        | select *
        |   from entry
        |  limit 10
      """.stripMargin).show()
    sc.stop()
  }
}

`
## 编译打包配置

代码编写完成后，进行打包（配置jar包，build）:

1、配置jar包：File->Project Structure，选择Artifacts，点击+号：
2、Jar -> From modules with dependencies...
3、Moduels和Main Class根据真实情况选择
4、JAR files from libraries 选择 copy to the output......

## 编译打包
配置jar包完成后，Build->Build Artifacts,等待build完成。


## 提交并执行
mkdir -p /data/workspace/spark
cd /data/workspace/spark
rz
#选择你打包后的jar包
su hdfs
spark2-submit --class com.dcjet.demo.Test1 /data/workspace/spark/你的jar.jar 


spark-shell
但是对于第三代jar包，需要先将第三方依赖（jar包）导入到spark-shell下面才行

spark2-shell --jars /home/sparkjars/fastjson-1.2.47.jar

## 取消spark2-shell的 ctrl c 退出命令

vim $(which spark2-shell)
`
#!/bin/bash
trap '' INT
`
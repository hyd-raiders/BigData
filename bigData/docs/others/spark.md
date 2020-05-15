## spark 是什么
Apache Spark 是专为大规模数据处理而设计的快速通用的计算引擎。Spark是UC Berkeley AMP lab (加州大学伯克利分校的AMP实验室)所开源的类Hadoop MapReduce的通用并行框架，Spark，拥有Hadoop MapReduce所具有的优点；但不同于MapReduce的是——Job中间输出结果可以保存在内存中，从而不再需要读写HDFS，因此Spark能更好地适用于数据挖掘与机器学习等需要迭代的MapReduce的算法。
基本上spark在各种使用场景中均能替代MapReduce


## 架构和原理
    网上内容极多，可以自行了解，但是深刻的理解必须是在实践中慢慢体会理解的。


## 部署
CM中直接部署

## 内容提纲挈领
    spark分为spark core、spark sql、spark streaming、mllib、graphx
    spark core用于批处理，代替mapreduce的计算框架
    spark sql用于采用sql方式调用hdfs、hive （sqark也支持直接采用hive组件调用hive、hdfs，但推荐直接使用spark sql）
    spark streaming 用于流式计算
    mllib 用于机器学习
    graphx 为图计算


## spark core：shell方式调试
    spark shell 支持scala方式和python方式
    这里推荐scala方式：
        su hdfs
        spark2-shell 登录交互式界面，运行代码（运行的代码直接采用后面demo中的代码）

## spark core：ide开发方式
    spark core的运行，真实环境是以jar包方式运行的，提交方式如下，一次提交就是运行一次job：
        su hdfs
        spark2-submit --class dcjet.demo --master yarn --deploy-mode cluster /data/workspace/spark/spark2.jar 

    spark ide环境搭建详见：sparkIDE.md
    spark 简单开发例子：spark简单开发.md
    spark 正式开发例子：spark正式开发.md（待完善）
    spark api文档：参考官方文档http://spark.apache.org/docs/2.2.0/ （强烈建议通读一遍）
    spark快速进阶请查看： spark进阶.md
    官方例子：spark-examples
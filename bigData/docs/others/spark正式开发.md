## 如何调试
直接登录spark服务器
su hdfs
spark2-shell 登录交互式界面，运行代码,直接进行调试

## 其他如简单开发

## 
`
package dcjet
import org.apache.spark.sql.SparkSession

object demo {
  def main(args: Array[String]) {
    val ss = org.apache.spark.sql.SparkSession.builder().enableHiveSupport().appName("demo").master("yarn-cluster").getOrCreate()
    //val ss = org.apache.spark.sql.SparkSession.builder().enableHiveSupport().appName("demo").master("local").getOrCreate()
    val rs = ss.sql("select ENTRY_ID,I_E_PORT ,I_E_DATE ,D_DATE ,STATUS_RESULT,TRADE_NAME ,OWNER_NAME ,TRADE_COUNTRY ,TRADE_MODE ,GROSS_WT ,NET_WT ,NOTE_S from ENTRY_HEAD where TRADE_MODE NOT IN ( '0200', '0245', '0255','0258', '0345', '0400','0446', '0456', '0500','0642', '0644', '0654','0657', '0744', '0844','0845', '1139', '9639','9700', '9800', '9839' ) and  I_E_FLAG = 'E'")
    //filter 条件

    val ewRs = ss.sql("SELECT  ENTRY_ID ,MAX(CREATE_DATE) CLEARANCE_DATE FROM ENTRY_WORKFLOW WHERE STEP_ID = '80000000' GROUP BY ENTRY_ID")
    val dcRs = ss.sql("SELECT  ENTRY_ID ,MAX(DOCU_CODE) DOCU_CODE FROM  ENTRY_CERTIFICATE GROUP BY ENTRY_ID")

    //join
    val rs1 = rs.join(ewRs, rs("ENTRY_ID") === ewRs("ENTRY_ID"),"left").drop(ewRs("ENTRY_ID"))
    val rs2 = rs1.join(dcRs,rs1("ENTRY_ID") === dcRs("ENTRY_ID"), "left").drop(dcRs("ENTRY_ID"))
    val frs = rs2.sort("CLEARANCE_DATE")

    //save to hdfs
    //frs.write.format("json").save("/out/demo")
    //save to hive
    frs.write.mode("overwrite").saveAsTable("dcjet_demo")
  }
}

`

## 编译打包出 spark2.jar

## 提交运行
mkdir -p /data/workspace/spark
cd /data/workspace/spark
rz

su hdfs
spark2-submit --class dcjet.demo --master yarn --deploy-mode cluster /data/workspace/spark/spark2.jar 
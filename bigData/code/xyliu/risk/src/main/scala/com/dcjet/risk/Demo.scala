package com.dcjet.risk

object Demo {
  def main(args: Array[String]) {

    //jobID
    var jobId = "risk.demo"
    //if(args.length > 0) { jobId = args(0) }
    val spark = org.apache.spark.sql.SparkSession.builder().enableHiveSupport().appName(jobId).master("yarn-cluster").getOrCreate()
    //val spark = org.apache.spark.sql.SparkSession.builder().enableHiveSupport().appName("risk.demo").master("local").getOrCreate()
    val jobFrame = spark.sql("select content from job where id = '" + jobId +"'")

    //保存到全局
    var cds = spark.sparkContext.broadcast(jobFrame.first()(0).toString().split('|'))
    //print(cds.value(0))

    var sql=  """
                SELECT ENTRY_HEAD.ENTRY_ID, ENTRY_HEAD.I_E_FLAG, ENTRY_HEAD.DECL_PORT, ENTRY_HEAD.I_E_PORT, ENTRY_HEAD.D_DATE, ENTRY_HEAD.TRADE_CO,
           |       ENTRY_HEAD.TRADE_NAME, ENTRY_HEAD.OWNER_CODE, ENTRY_HEAD.OWNER_NAME, ENTRY_HEAD.TRADE_COUNTRY, ENTRY_HEAD.TRADE_MODE,
           |	   ENTRY_HEAD.CONTAINER_NO, ENTRY_HEAD.GROSS_WT, ENTRY_HEAD.NET_WT, ENTRY_HEAD.MANUAL_NO, ENTRY_HEAD.RELATIVE_ID,
           |	   ENTRY_HEAD.EDI_REMARK, ENTRY_HEAD.RELATIVE_MANUAL_NO, ENTRY_LIST.G_NO, ENTRY_LIST.CODE_TS,
           |	   ENTRY_LIST.G_NAME, ENTRY_LIST.G_MODEL, ENTRY_LIST.ORIGIN_COUNTRY, ENTRY_LIST.CONTR_ITEM,
           |	   ENTRY_LIST.G_QTY, ENTRY_LIST.G_UNIT, ENTRY_LIST.QTY_1, ENTRY_LIST.UNIT_1, ENTRY_LIST.QTY_2, ENTRY_LIST.UNIT_2,
           |	   ENTRY_LIST.USD_PRICE, ENTRY_LIST.DUTY_VALUE, ENTRY_LIST.REAL_DUTY, ENTRY_LIST.REAL_TAX, ENTRY_LIST.REAL_REG, ENTRY_LIST.REAL_OIL,
           |       ENTRY_LIST.REAL_ANTI,ENTRY_LIST.PRDT_NO
           |  FROM ENTRY_HEAD_BD ENTRY_HEAD
           | INNER JOIN ENTRY_LIST_BD ENTRY_LIST ON ENTRY_HEAD.ENTRY_ID = ENTRY_LIST.ENTRY_ID
              """.stripMargin
    sql += " WHERE ENTRY_HEAD.D_DATE Between '" + cds.value(0) +"' And '" + cds.value(1) + "' AND ENTRY_HEAD.TRADE_CO IN ( " + cds.value(2).split(",").map( f=> "'" + f + "'").mkString(",") +")"

    val rs_total = spark.sql(sql)

    rs_total.write.mode("overwrite").saveAsTable("dcjet_demo_bd")
  }
}

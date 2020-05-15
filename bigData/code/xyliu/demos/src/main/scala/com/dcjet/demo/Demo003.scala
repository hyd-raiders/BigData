package com.dcjet.demo

/**
  * 查验情况统计
  */
object Demo003 {
  def main(args: Array[String]): Unit = {
    val spark =org.apache.spark.sql.SparkSession.builder().enableHiveSupport().appName("demo003").master("yarn-cluster").getOrCreate()

    //本异地报关单数据510010000企业进出口数据
    var df1 = spark.sql("""SELECT ENTRY_HEAD.ENTRY_ID, ENTRY_HEAD.I_E_FLAG, ENTRY_HEAD.DECL_PORT, ENTRY_HEAD.I_E_PORT, ENTRY_HEAD.D_DATE, ENTRY_HEAD.TRADE_CO,
                          |       ENTRY_HEAD.TRADE_NAME, ENTRY_HEAD.OWNER_CODE, ENTRY_HEAD.OWNER_NAME, ENTRY_HEAD.TRADE_COUNTRY, ENTRY_HEAD.TRADE_MODE,
                          |	   ENTRY_HEAD.CONTAINER_NO, ENTRY_HEAD.GROSS_WT, ENTRY_HEAD.NET_WT, ENTRY_HEAD.MANUAL_NO, ENTRY_HEAD.RELATIVE_ID,
                          |	   ENTRY_HEAD.EDI_REMARK, ENTRY_HEAD.RELATIVE_MANUAL_NO, ENTRY_LIST.G_NO, ENTRY_LIST.CODE_TS,
                          |	   ENTRY_LIST.G_NAME, ENTRY_LIST.G_MODEL, ENTRY_LIST.ORIGIN_COUNTRY, ENTRY_LIST.CONTR_ITEM,
                          |	   ENTRY_LIST.G_QTY, ENTRY_LIST.G_UNIT, ENTRY_LIST.QTY_1, ENTRY_LIST.UNIT_1, ENTRY_LIST.QTY_2, ENTRY_LIST.UNIT_2,
                          |	   ENTRY_LIST.USD_PRICE, ENTRY_LIST.DUTY_VALUE, ENTRY_LIST.REAL_DUTY, ENTRY_LIST.REAL_TAX, ENTRY_LIST.REAL_REG, ENTRY_LIST.REAL_OIL,
                          |       ENTRY_LIST.REAL_ANTI,ENTRY_LIST.PRDT_NO
                          |  FROM ENTRY_HEAD
                          | INNER JOIN ENTRY_LIST ON ENTRY_HEAD.ENTRY_ID = ENTRY_LIST.ENTRY_ID """)
    //todo 条件
    df1.createOrReplaceTempView("_510010000")
    //510010002备案信息基础表
    spark.sql(
      """
        |SELECT COMPANY_REL.TRADE_CO AS 企业编码, COMPANY_REL.CUSTOMS_CODE AS 主管海关, COMPANY_REL.CONTAC_CO AS 联系人, COMPANY_REL.TEL_CO AS 联系电话, COMPANY_REL.LAW_MAN_TEL AS 法人电话, COMPANY_REL.ID_NUMBER AS 居留证号  FROM COMPANY_REL
      """.stripMargin).createOrReplaceTempView("_510010002")
    // todo  条件strSql = strSql & "WHERE (dbo.COMPANY_REL.CONTAC_CO='" & strCONTAC_CO & "' and  dbo.COMPANY_REL.CUSTOMS_CODE like '23%') OR (dbo.COMPANY_REL.TEL_CO='" & strTEL_CO & "' and  dbo.COMPANY_REL.CUSTOMS_CODE like '23%')  OR (dbo.COMPANY_REL.LAW_MAN_TEL='" & StrLAW_MAN_TEL & "' and  dbo.COMPANY_REL.CUSTOMS_CODE like '23%') OR  (dbo.COMPANY_REL.ID_NUMBER='" & StrID_NUMBER & "' and  dbo.COMPANY_REL.CUSTOMS_CODE like '23%')"


    //"510010003进出口基础表"=”本异地报关单数据“加上条件“D_DATE>=[Forms]![510010002备案信息基础表]![起始日期]”
  }
}

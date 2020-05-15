SELECT * 
		   FROM 
		      (
                SELECT      H.ENTRY_ID ,--海关编号
                            H.I_E_PORT ,--进出口岸代码
                            H.I_E_DATE ,--进出口日期
							H.I_E_FLAG,
                            H.D_DATE ,--申报日期
							H.STATUS_RESULT,
                            W.CLEARANCE_DATE ,--结关日期
							CONVERT(VARCHAR(7), W.CLEARANCE_DATE, 120) CLEARANCE_MONTH ,
                            H.TRADE_NAME ,--经营单位名称
                            H.OWNER_NAME ,--经营单位名称
                            H.TRADE_COUNTRY ,--贸易国别（起/抵运地）
                            H.TRADE_MODE ,--监管方式
                            H.GROSS_WT ,--毛重
                            H.NET_WT ,--净重
                            H.NOTE_S ,--备注
							D.DOCU_CODE,
                            ROW_NUMBER() OVER ( ORDER BY W.CLEARANCE_DATE ) RN
                  FROM      HA2000_BD.risk.ENTRY_HEAD H 
                  LEFT JOIN  HA2000_BD.risk.CUSTOMS C ON H.DECL_PORT = C.CUSTOMS_CODE
				  LEFT JOIN ( SELECT  ENTRY_ID ,
                                        MAX(CREATE_DATE) CLEARANCE_DATE
                                FROM    HA2000_BD.risk.ENTRY_WORKFLOW
                                WHERE   STEP_ID = '80000000'
                                GROUP BY ENTRY_ID
                                ) W ON H.ENTRY_ID = W.ENTRY_ID
                    LEFT JOIN ( SELECT  ENTRY_ID ,
                                        MAX(DOCU_CODE) DOCU_CODE
                                FROM    HA2000_BD.risk.ENTRY_CERTIFICATE
                                GROUP BY ENTRY_ID
                                ) D ON H.ENTRY_ID = D.ENTRY_ID
             )H
                  WHERE     H.TRADE_MODE NOT IN ( '0200', '0245', '0255',
                                                      '0258', '0345', '0400',
                                                      '0446', '0456', '0500',
                                                      '0642', '0644', '0654',
                                                      '0657', '0744', '0844',
                                                      '0845', '1139', '9639',
                                                      '9700', '9800', '9839' )
                            AND H.I_E_FLAG = 'E'
                            --AND H.DOCU_CODE = 'P'
                            --AND H.CLEARANCE_MONTH BETWEEN '2008-01-01'
                            --                      AND     '2017-12-31'
                            AND ( CASE WHEN SUBSTRING(H.STATUS_RESULT, 74, 1) = '1'
                                            AND SUBSTRING(H.STATUS_RESULT, 100,
                                                          1) NOT IN ( '5', '6' )
                                       THEN 'A'
                                       ELSE 'O'
                                  END = ''
                                  OR '' = ''
                                  OR '' IS NULL
                                )
                            --AND ( '2300' = ''
                            --      OR '2300' = '00'
                            --      OR '2300' = '2300'
                            --      OR '2300' IS NULL
                            --      OR ( '2300' <> '00'
                            --           AND LEN('2300') = 2
                            --           AND C.SUB_CUSTID = '2303'
                            --         )
                            --      OR ( '2300' <> '2300'
                            --           AND LEN('2300') = 4
                            --           AND C.CUSTOMS_CODE = '2303'
                            --         )
                            --    )

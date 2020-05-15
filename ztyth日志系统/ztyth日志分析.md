# 总体说明

## 开发环境准备
    业务库：ztyth_qy/dcjet@192.168.10.128

    kafka1: 192.168.12.6:10022
    kafka2: 192.168.12.6:20022

    logstash 部署于 192.168.12.6:10022


## 业务表
    --一体化备案相关的表
t_ztyth_hb_head/t_ztyth_hb_head_change、t_ztyth_hb_img/t_ztyth_hb_img_change、t_ztyth_hb_exg/t_ztyth_hb_exg_change、
t_ztyth_hb_consume/t_ztyth_hb_consume_change

--进出口报关单相关的表
t_ztyth_entry_head_e/t_ztyth_entry_list_e、t_ztyth_entry_head_i/t_ztyth_entry_list_i
--一体化核销相关的表
t_ztyth_hb_dcr_head、t_ztyth_hb_dcr_imp_img_edit、t_ztyth_hb_dcr_basic_info_edit、t_ztyth_hb_dcr_bypro、
t_ztyth_hb_dcr_scrap、t_ztyth_hb_dcr_balance_edit
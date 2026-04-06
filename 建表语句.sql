-- hsyh_db.dwa_base_bgdjxx definition

CREATE TABLE `dwa_base_bgdjxx` (
`nsrsbh` varchar(50) DEFAULT NULL,
`nsrmc` varchar(200) DEFAULT NULL,
`bgxm_dm` varchar(30) DEFAULT NULL,
`bgxmmc` varchar(60) DEFAULT NULL,
`bgqnr` varchar(2000) DEFAULT NULL,
`bghnr` varchar(3000) DEFAULT NULL,
`xgrq` varchar(40) DEFAULT NULL,
`updata_time` timestamp NULL DEFAULT NULL,
KEY `nsrindex` (`nsrsbh`,`xgrq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin /*T! SHARD_ROW_ID_BITS=4 PRE_SPLIT_REGIONS=3 */;


-- hsyh_db.dwa_base_djxx definition

CREATE TABLE `dwa_base_djxx` (
`nsrsbh` varchar(50) DEFAULT NULL,
`nsrmc` varchar(200) DEFAULT NULL,
`zzjgdm` varchar(50) DEFAULT NULL,
`nsrztmc` varchar(20) DEFAULT NULL,
`zcdz` varchar(500) DEFAULT NULL,
`dhhm` varchar(50) DEFAULT NULL,
`yydz` varchar(500) DEFAULT NULL,
`djzclxdm` varchar(10) DEFAULT NULL,
`djzclxmc` varchar(50) DEFAULT NULL,
`nsrlx` varchar(20) DEFAULT NULL,
`zyrs` double DEFAULT NULL,
`zczb` double DEFAULT NULL,
`zcbz` varchar(50) DEFAULT NULL,
`swjgdm` varchar(30) DEFAULT NULL,
`swjgmc` varchar(50) DEFAULT NULL,
`sykjzd` varchar(20) DEFAULT NULL,
`sykjzdmc` varchar(40) DEFAULT NULL,
`fddbrxm` varchar(50) DEFAULT NULL,
`fddbrgddh` varchar(60) DEFAULT NULL,
`fddbrsfzjhm` varchar(60) DEFAULT NULL,
`fddbrsfzjzldm` varchar(20) DEFAULT NULL,
`fddbrsfzjzl` varchar(60) DEFAULT NULL,
`fddbryddh` varchar(40) DEFAULT NULL,
`bsrxm` varchar(40) DEFAULT NULL,
`bsrgddh` varchar(60) DEFAULT NULL,
`bsrsfzjhm` varchar(60) DEFAULT NULL,
`bsrsfzjlxdm` varchar(20) DEFAULT NULL,
`bsrsfzjlx` varchar(40) DEFAULT NULL,
`bsryddh` varchar(40) DEFAULT NULL,
`cwfzrxm` varchar(60) DEFAULT NULL,
`cwfzryddh` varchar(40) DEFAULT NULL,
`cwfzrgddh` varchar(60) DEFAULT NULL,
`cwfzrsfzjhm` varchar(60) DEFAULT NULL,
`cwfzrsfzjzldm` varchar(20) DEFAULT NULL,
`cwfzrsfzjzl` varchar(40) DEFAULT NULL,
`kyslrq` varchar(30) DEFAULT NULL,
`cktsqybs` varchar(20) DEFAULT NULL,
`updata_time` timestamp NULL DEFAULT NULL,
KEY `nsrindex` (`nsrsbh`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin /*T! SHARD_ROW_ID_BITS=4 PRE_SPLIT_REGIONS=3 */;


-- hsyh_db.dwa_base_llxx definition

CREATE TABLE `dwa_base_llxx` (
`nsrsbh` varchar(50) DEFAULT NULL,
`nsrmc` varchar(200) DEFAULT NULL,
`kjzdzz_dm` varchar(10) DEFAULT NULL,
`kjzdzz_bdmc` varchar(50) DEFAULT NULL,
`ssqq` varchar(30) DEFAULT NULL,
`ssqz` varchar(30) DEFAULT NULL,
`ewbhxh` varchar(10) DEFAULT NULL,
`hmc` varchar(100) DEFAULT NULL,
`bqje` double DEFAULT NULL,
`sqje` double DEFAULT NULL,
`bnljje` double DEFAULT NULL,
`byje` double DEFAULT NULL,
`je` double DEFAULT NULL,
KEY `nsrindex` (`nsrsbh`,`ssqq`,`ssqz`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin /*T! SHARD_ROW_ID_BITS=4 PRE_SPLIT_REGIONS=3 */;


-- hsyh_db.dwa_base_lrbxx definition

CREATE TABLE `dwa_base_lrbxx` (
`nsrsbh` varchar(50) DEFAULT NULL,
`nsrmc` varchar(200) DEFAULT NULL,
`kjzdzz_dm` varchar(10) DEFAULT NULL,
`kjzdzz_bdmc` varchar(50) DEFAULT NULL,
`ssqq` varchar(30) DEFAULT NULL,
`ssqz` varchar(30) DEFAULT NULL,
`ewbhxh` varchar(10) DEFAULT NULL,
`hmc` varchar(100) DEFAULT NULL,
`bqje` double DEFAULT NULL,
`sqje` double DEFAULT NULL,
`bnljje` double DEFAULT NULL,
`byje` double DEFAULT NULL,
`bnljs` double DEFAULT NULL,
`bys` double DEFAULT NULL,
`updata_time` timestamp NULL DEFAULT NULL,
KEY `nsrindex` (`nsrsbh`,`ssqq`,`ssqz`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin /*T! SHARD_ROW_ID_BITS=4 PRE_SPLIT_REGIONS=3 */;


-- hsyh_db.dwa_base_sbxx definition

CREATE TABLE `dwa_base_sbxx` (
`nsrsbh` varchar(50) DEFAULT NULL,
`nsrmc` varchar(200) DEFAULT NULL,
`zsxmdm` varchar(20) DEFAULT NULL,
`zsxmmc` varchar(20) DEFAULT NULL,
`sbrq` date DEFAULT NULL,
`sssqq` date DEFAULT NULL,
`sssqz` date DEFAULT NULL,
`qbxse` double DEFAULT NULL,
`xxse` double DEFAULT NULL,
`jxse` double DEFAULT NULL,
`ysxse` double DEFAULT NULL,
`ynse` double DEFAULT NULL,
`ytse` double DEFAULT NULL,
`rptFlg` varchar(10) DEFAULT NULL,
`mdtbfckxse` double DEFAULT NULL,
`mdtytse` double DEFAULT NULL,
`yzpzzlmc` varchar(200) DEFAULT NULL,
`updata_time` timestamp NULL DEFAULT NULL,
KEY `nsrindex` (`nsrsbh`,`sssqq`,`sssqz`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin /*T! SHARD_ROW_ID_BITS=4 PRE_SPLIT_REGIONS=3 */;


-- hsyh_db.dwa_base_sdzc definition

CREATE TABLE `dwa_base_sdzc` (
`nsrsbh` varchar(50) DEFAULT NULL,
`nsrmc` varchar(200) DEFAULT NULL,
`kpyf` varchar(9) DEFAULT NULL,
`sf` decimal(29,2) DEFAULT NULL,
`df` decimal(29,2) DEFAULT NULL,
`updata_time` timestamp NULL DEFAULT NULL,
KEY `nsrindex` (`nsrsbh`,`kpyf`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin /*T! SHARD_ROW_ID_BITS=4 PRE_SPLIT_REGIONS=3 */;


-- hsyh_db.dwa_base_syqyqs definition

CREATE TABLE `dwa_base_syqyqs` (
`nsrsbh` varchar(50) DEFAULT NULL,
`nsrmc` varchar(200) DEFAULT NULL,
`khmc` varchar(200) DEFAULT NULL,
`jyje` decimal(32,2) DEFAULT NULL,
`xsehz` decimal(32,2) DEFAULT NULL,
`nd` varchar(10) DEFAULT NULL,
`zb` varchar(40) DEFAULT NULL,
`xh` int(20) DEFAULT NULL,
`updata_time` timestamp NULL DEFAULT NULL,
KEY `nsrindex` (`nsrsbh`,`nsrmc`,`nd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin /*T! SHARD_ROW_ID_BITS=4 PRE_SPLIT_REGIONS=3 */;


-- hsyh_db.dwa_base_tzfxx definition

CREATE TABLE `dwa_base_tzfxx` (
`nsrsbh` varchar(50) DEFAULT NULL,
`nsrmc` varchar(500) DEFAULT NULL,
`tzfmc` varchar(100) DEFAULT NULL,
`tzbl` double DEFAULT NULL,
`zjzldm` varchar(20) DEFAULT NULL,
`zjzlmc` varchar(100) DEFAULT NULL,
`zjhm` varchar(80) DEFAULT NULL,
`tzfjjxzdm` varchar(20) DEFAULT NULL,
`tzfjjxzmc` varchar(50) DEFAULT NULL,
`updata_time` timestamp NULL DEFAULT NULL,
KEY `nsrindex` (`nsrsbh`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin /*T! SHARD_ROW_ID_BITS=4 PRE_SPLIT_REGIONS=3 */;


-- hsyh_db.dwa_base_wfwzxx definition

CREATE TABLE `dwa_base_wfwzxx` (
`nsrsbh` varchar(50) DEFAULT NULL,
`nsrmc` varchar(500) DEFAULT NULL,
`djrq` varchar(30) DEFAULT NULL,
`wfss` varchar(3000) DEFAULT NULL,
`sswfsd_dm` varchar(20) DEFAULT NULL,
`sswfsdmc` varchar(200) DEFAULT NULL,
`sswflxmc` varchar(50) DEFAULT NULL,
`sswfxwclzt_dm` varchar(10) DEFAULT NULL,
`sswflx_dm` varchar(10) DEFAULT NULL,
`sswfxwclztmc` varchar(50) DEFAULT NULL,
`updata_time` timestamp NULL DEFAULT NULL,
KEY `nsrindex` (`nsrsbh`,`djrq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin /*T! SHARD_ROW_ID_BITS=4 PRE_SPLIT_REGIONS=3 */;


-- hsyh_db.dwa_base_xyqyqs definition

CREATE TABLE `dwa_base_xyqyqs` (
`nsrsbh` varchar(50) DEFAULT NULL,
`nsrmc` varchar(200) DEFAULT NULL,
`khmc` varchar(200) DEFAULT NULL,
`jyje` decimal(32,2) DEFAULT NULL,
`xsehz` decimal(32,2) DEFAULT NULL,
`nd` varchar(10) DEFAULT NULL,
`zb` varchar(40) DEFAULT NULL,
`xh` int(20) DEFAULT NULL,
`updata_time` timestamp NULL DEFAULT NULL,
KEY `nsrindex` (`nsrsbh`,`nsrmc`,`nd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin /*T! SHARD_ROW_ID_BITS=4 PRE_SPLIT_REGIONS=3 */;


-- hsyh_db.dwa_base_xyxx definition

CREATE TABLE `dwa_base_xyxx` (
`nsrsbh` varchar(50) DEFAULT NULL,
`nsrmc` varchar(500) DEFAULT NULL,
`xyjb` varchar(10) DEFAULT NULL,
`nd` varchar(20) DEFAULT NULL,
`fz` bigint(50) DEFAULT NULL,
`updata_time` timestamp NULL DEFAULT NULL,
KEY `nsrindex` (`nsrsbh`,`nd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin /*T! SHARD_ROW_ID_BITS=4 PRE_SPLIT_REGIONS=3 */;


-- hsyh_db.dwa_base_yffyxx definition

CREATE TABLE `dwa_base_yffyxx` (
`nsrsbh` varchar(50) DEFAULT NULL,
`nsrmc` varchar(200) DEFAULT NULL,
`ndyffyxj` double DEFAULT NULL,
`bnfyhje` double DEFAULT NULL,
`bnzbhje` double DEFAULT NULL,
`nd` varchar(10) DEFAULT NULL,
`updata_time` timestamp NULL DEFAULT NULL,
KEY `nsrindex` (`nsrsbh`,`nd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin /*T! SHARD_ROW_ID_BITS=4 PRE_SPLIT_REGIONS=3 */;


-- hsyh_db.dwa_base_zcfzxx definition

CREATE TABLE `dwa_base_zcfzxx` (
`nsrsbh` varchar(50) DEFAULT NULL,
`nsrmc` varchar(200) DEFAULT NULL,
`kjzdzz_dm` varchar(20) DEFAULT NULL,
`kjzdzz_bdmc` varchar(50) DEFAULT NULL,
`ssqq` varchar(30) DEFAULT NULL,
`ssqz` varchar(30) DEFAULT NULL,
`zcxmmc` varchar(100) DEFAULT NULL,
`ewbhxh` varchar(10) DEFAULT NULL,
`qmye_zc` double DEFAULT NULL,
`ncye_zc` double DEFAULT NULL,
`qyxmmc` varchar(200) DEFAULT NULL,
`qmye_qy` double DEFAULT NULL,
`ncye_qy` double DEFAULT NULL,
`ncs_qy` double DEFAULT NULL,
`qms_qy` double DEFAULT NULL,
`ncs_zc` double DEFAULT NULL,
`qms_zc` double DEFAULT NULL,
`updata_time` timestamp NULL DEFAULT NULL,
KEY `nsrindex` (`nsrsbh`,`ssqq`,`ssqz`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin /*T! SHARD_ROW_ID_BITS=4 PRE_SPLIT_REGIONS=3 */;

-- hsyh_db.dwa_base_zsxx definition

CREATE TABLE `dwa_base_zsxx` (
`nsrsbh` varchar(50) DEFAULT NULL,
`nsrmc` varchar(200) DEFAULT NULL,
`skssqq` varchar(15) DEFAULT NULL,
`skssqz` varchar(15) DEFAULT NULL,
`zsxmdm` varchar(20) DEFAULT NULL,
`zsxmmc` varchar(50) DEFAULT NULL,
`skzldm` varchar(20) DEFAULT NULL,
`skzlmc` varchar(20) DEFAULT NULL,
`sjje` double DEFAULT NULL,
`sl1` varchar(50) DEFAULT NULL,
`jsyj` double DEFAULT NULL,
`jkrq` varchar(30) DEFAULT NULL,
`jkqx` varchar(30) DEFAULT NULL,
`dzsphm` varchar(60) DEFAULT NULL,
`zspmdm` varchar(30) DEFAULT NULL,
`zspmmc` varchar(300) DEFAULT NULL,
`updata_time` timestamp NULL DEFAULT NULL,
KEY `nsrindex` (`nsrsbh`,`skssqq`,`skssqz`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin /*T! SHARD_ROW_ID_BITS=4 PRE_SPLIT_REGIONS=3 */;


-- hsyh_db.dwa_base_zysp definition

CREATE TABLE `dwa_base_zysp` (
`nsrsbh` varchar(50) DEFAULT NULL,
`nsrmc` varchar(200) DEFAULT NULL,
`spmc` varchar(500) DEFAULT NULL,
`jyje` decimal(32,2) DEFAULT NULL,
`xsehz` decimal(32,2) DEFAULT NULL,
`nd` int(11) DEFAULT NULL,
`zb` varchar(20) DEFAULT NULL,
`xh` int(11) DEFAULT NULL,
`updata_time` timestamp NULL DEFAULT NULL,
KEY `nsrindex` (`nsrsbh`,`nd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin /*T! SHARD_ROW_ID_BITS=4 PRE_SPLIT_REGIONS=3 */;


-- hsyh_db.hsyh_EXCEPTION_INFO definition

CREATE TABLE `hsyh_EXCEPTION_INFO` (
`serialno` varchar(50) DEFAULT NULL COMMENT '流水号',
`entname` varchar(50) DEFAULT NULL COMMENT '企业名称',
`msg` text DEFAULT NULL COMMENT '异常信息',
`inputdate` varchar(30) DEFAULT NULL COMMENT '入库时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='交易异常信息';


-- hsyh_db.hsyh_dcm_trans_log definition

CREATE TABLE `hsyh_dcm_trans_log` (
`serialno` varchar(30) NOT NULL,
`nsrmc` varchar(200) DEFAULT NULL,
`nsrsbh` varchar(30) DEFAULT NULL,
`incTax` varchar(30) DEFAULT NULL,
`mainBusDownnow` varchar(30) DEFAULT NULL,
`zzsNse24m` varchar(30) DEFAULT NULL,
`zzsNse12m` varchar(30) DEFAULT NULL,
`tax3MIllegal_unDeal` varchar(30) DEFAULT NULL,
`befLasYearProfit` varchar(30) DEFAULT NULL,
`taxStatus` varchar(30) DEFAULT NULL,
`tax2YPro` varchar(30) DEFAULT NULL,
`tax3MIllegal` varchar(30) DEFAULT NULL,
`interTaxSale` varchar(30) DEFAULT NULL,
`ysxse24m` varchar(30) DEFAULT NULL,
`lasProfit` varchar(30) DEFAULT NULL,
`taxSaletenper` varchar(30) DEFAULT NULL,
`mainBusDownlast` varchar(30) DEFAULT NULL,
`befLasYearTax` varchar(30) DEFAULT NULL,
`over24MTax` varchar(30) DEFAULT NULL,
`YSXSRLast` varchar(30) DEFAULT NULL,
`taxSalethirtyper` varchar(30) DEFAULT NULL,
`taxsale3M` varchar(30) DEFAULT NULL,
`over6MTax` varchar(30) DEFAULT NULL,
`taxLev` varchar(30) DEFAULT NULL,
`YSXSRBef` varchar(30) DEFAULT NULL,
`YSXSE12M` varchar(30) DEFAULT NULL,
`oveTax` varchar(30) DEFAULT NULL,
`tax2YNetAss` varchar(30) DEFAULT NULL,
`lastYearTax` varchar(30) DEFAULT NULL,
`inputtime` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


-- hsyh_db.hsyh_qymd definition

CREATE TABLE `hsyh_qymd` (
`serialno` varchar(30) NOT NULL COMMENT '流水号',
`nsrmc` varchar(200) DEFAULT NULL COMMENT '企业名',
`nsrsbh` varchar(30) DEFAULT NULL COMMENT '统一社会信用代码',
`ifkjedai` varchar(10) DEFAULT NULL COMMENT '是否科技e贷 Y N ',
`hytype` varchar(10) DEFAULT NULL COMMENT '行业大类',
`hytypecode` varchar(10) DEFAULT NULL COMMENT '行业细类',
`xedoldcust` varchar(10) DEFAULT NULL COMMENT '是否xed续贷客户 Y N',
`inputtime` varchar(40) DEFAULT NULL COMMENT '入库时间',
`flag` varchar(10) DEFAULT NULL COMMENT '是否更新 Y:已更新',
`batchdate` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='交易流水表';


-- hsyh_db.hsyh_qymd_yps definition

CREATE TABLE `hsyh_qymd_yps` (
`nsrsbh` varchar(30) DEFAULT NULL COMMENT '统一社会信用代码',
`inputtime` varchar(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='名单记录表_已跑数';


-- hsyh_db.hsyh_trans_log definition

CREATE TABLE `hsyh_trans_log` (
`serialno` varchar(30) NOT NULL COMMENT '流水号',
`nsrmc` varchar(200) DEFAULT NULL COMMENT '企业名',
`nsrsbh` varchar(30) DEFAULT NULL COMMENT '统一社会信用代码',
`inputtime` varchar(200) DEFAULT NULL COMMENT '入库时间',
`rulecd` varchar(200) DEFAULT NULL COMMENT '触发编码',
`status` varchar(10) DEFAULT NULL COMMENT '交易状态 1-成功 2-失败 3-异常',
`admtrsltsts` varchar(2) DEFAULT NULL COMMENT '准入等级',
`score` varchar(20) DEFAULT NULL COMMENT '得分',
`batchdate` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='模型结果表';

#by 2025/12/25
ALTER TABLE hsyh_qymd ADD xed_type varchar(10) comment '业务类型 1:信e贷模型';
#alter table hsyh_qymd modify column xed_type varchar(10) comment '业务类型 1:信e贷模型';
ALTER TABLE hsyh_qymd ALTER COLUMN xed_type SET DEFAULT 'Y';


ALTER TABLE hsyh_qymd ADD kced_type varchar(10) comment '业务类型 1:科技e贷模型';
#alter table hsyh_qymd modify column kced_type varchar(10) comment '业务类型 1:科技e贷模型';
ALTER TABLE hsyh_qymd ALTER COLUMN kced_type SET DEFAULT 'N';

ALTER TABLE hsyh_trans_log ADD xed_type varchar(10) comment '业务类型 1:信e贷模型';
ALTER TABLE hsyh_trans_log ADD kced_type varchar(10) comment '业务类型 1:科创e贷模型';
ALTER TABLE hsyh_trans_log ADD ratio varchar(10) comment '增长率';
ALTER TABLE hsyh_trans_log ADD saleamount varchar(30) comment '销售收入区间值';

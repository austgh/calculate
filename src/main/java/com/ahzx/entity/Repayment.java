package com.ahzx.entity;

/**
 * @author think
 * @date 2026年04月04日 21:22
 */
public class Repayment {
    //默认还款日 0-31
    public int defaultPaymentDate;
    //还款周期
    //1.按月；2.	按双月 3.按季；4.一次；5.按半年；6.按年；7.指定周期； 8.按双周； 9.按季（固定）；10.按半年（固定）；11.按年（固定）；
    public int repaymentCycle;
    // 指定周期
    // 当还款周期为“指定周期”时，该字段需录入，与指定周期单位搭配使用。
    public int specifiedCycle;
    //指定周期单位
    // 1. 天 2. 月 3. 年 与指定周期搭配使用。
    public int specifiedCycleUnit;

    //区段期限标识 1.贷款期限 2.区段期限 3.指定期限
    public String segmentDesignatedTermUnit;

    //区段指定期限
    public String segmentSpecificDesignatedPeriod;
    //区段指定期限单位 1、天 2、月 3、年
    public String segmentSpecificDesignatedPeriodUnit;

    //区段指定金额标识  1.贷款余额 2.区段指定金额 3.指定每期还款额 4.尾款金额
    public String segmentDesignatedAmountIndicator;

    //区段指定金额
    //当指定区段归还本金时，该字段为必输。
    public int segmentSpecificDesignatedAmount;
    //首期标识
    // 01.最早放款当月开始还款 02.最早放款下月开始还款
    public String firstPeriodIndicator;
    //末期标识
    // 1.最后一期合并 2.最后一期拆分 3.自动顺延直至贷款到期
    public String finalPeriodIndicator;

    //宽限期天数
    // 宽限期内不计罚息和复息，宽限期内还款等同于还款日还款。超过宽限期仍未还款，则按照罚息利率补计宽限期内的罚息复息，且贷款逾期天数从结息日开始计算。
    public int gracePeriodDays;

    public int getDefaultPaymentDate() {
        return defaultPaymentDate;
    }

    public void setDefaultPaymentDate(int defaultPaymentDate) {
        this.defaultPaymentDate = defaultPaymentDate;
    }

    public int getRepaymentCycle() {
        return repaymentCycle;
    }

    public void setRepaymentCycle(int repaymentCycle) {
        this.repaymentCycle = repaymentCycle;
    }

    public int getSpecifiedCycle() {
        return specifiedCycle;
    }

    public void setSpecifiedCycle(int specifiedCycle) {
        this.specifiedCycle = specifiedCycle;
    }

    public int getSpecifiedCycleUnit() {
        return specifiedCycleUnit;
    }

    public void setSpecifiedCycleUnit(int specifiedCycleUnit) {
        this.specifiedCycleUnit = specifiedCycleUnit;
    }

    public String getSegmentDesignatedTermUnit() {
        return segmentDesignatedTermUnit;
    }

    public void setSegmentDesignatedTermUnit(String segmentDesignatedTermUnit) {
        this.segmentDesignatedTermUnit = segmentDesignatedTermUnit;
    }

    public String getSegmentSpecificDesignatedPeriod() {
        return segmentSpecificDesignatedPeriod;
    }

    public void setSegmentSpecificDesignatedPeriod(String segmentSpecificDesignatedPeriod) {
        this.segmentSpecificDesignatedPeriod = segmentSpecificDesignatedPeriod;
    }

    public String getSegmentSpecificDesignatedPeriodUnit() {
        return segmentSpecificDesignatedPeriodUnit;
    }

    public void setSegmentSpecificDesignatedPeriodUnit(String segmentSpecificDesignatedPeriodUnit) {
        this.segmentSpecificDesignatedPeriodUnit = segmentSpecificDesignatedPeriodUnit;
    }

    public String getSegmentDesignatedAmountIndicator() {
        return segmentDesignatedAmountIndicator;
    }

    public void setSegmentDesignatedAmountIndicator(String segmentDesignatedAmountIndicator) {
        this.segmentDesignatedAmountIndicator = segmentDesignatedAmountIndicator;
    }

    public int getSegmentSpecificDesignatedAmount() {
        return segmentSpecificDesignatedAmount;
    }

    public void setSegmentSpecificDesignatedAmount(int segmentSpecificDesignatedAmount) {
        this.segmentSpecificDesignatedAmount = segmentSpecificDesignatedAmount;
    }

    public String getFirstPeriodIndicator() {
        return firstPeriodIndicator;
    }

    public void setFirstPeriodIndicator(String firstPeriodIndicator) {
        this.firstPeriodIndicator = firstPeriodIndicator;
    }

    public String getFinalPeriodIndicator() {
        return finalPeriodIndicator;
    }

    public void setFinalPeriodIndicator(String finalPeriodIndicator) {
        this.finalPeriodIndicator = finalPeriodIndicator;
    }

    public int getGracePeriodDays() {
        return gracePeriodDays;
    }

    public void setGracePeriodDays(int gracePeriodDays) {
        this.gracePeriodDays = gracePeriodDays;
    }

    @Override
    public String toString() {
        return "Repayment{" +
                "defaultPaymentDate=" + defaultPaymentDate +
                ", repaymentCycle=" + repaymentCycle +
                ", specifiedCycle=" + specifiedCycle +
                ", specifiedCycleUnit=" + specifiedCycleUnit +
                ", segmentDesignatedTermUnit='" + segmentDesignatedTermUnit + '\'' +
                ", segmentSpecificDesignatedPeriod='" + segmentSpecificDesignatedPeriod + '\'' +
                ", segmentSpecificDesignatedPeriodUnit='" + segmentSpecificDesignatedPeriodUnit + '\'' +
                ", segmentDesignatedAmountIndicator='" + segmentDesignatedAmountIndicator + '\'' +
                ", segmentSpecificDesignatedAmount=" + segmentSpecificDesignatedAmount +
                ", firstPeriodIndicator='" + firstPeriodIndicator + '\'' +
                ", finalPeriodIndicator='" + finalPeriodIndicator + '\'' +
                ", gracePeriodDays=" + gracePeriodDays +
                '}';
    }
}

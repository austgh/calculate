package com.ahzx.service;

import com.ahzx.utils.CommUtils;

import java.math.BigDecimal;

/**
 * @author think
 * @date 2026年02月26日 17:48
 * Equal Monthly Installment （EMI）
 * 这是最普遍的缩写，特指每月还款金额固定。
 * <p>
 * 例句：The loan is repaid on an EMI basis. （这笔贷款采用等额本息方式偿还。）
 */
public class EMI {

    /*
     *
     *
     * @date 2026/2/26 18:56
     * @param ratio 月利率
     * @param total 总期数
     * @param amount 贷款本金
     * @return double
     */
    public static BigDecimal PMT(double ratio, int total, int amount) {
        return new BigDecimal(amount * ratio * Math.pow(1 + ratio, total) / (Math.pow(1 + ratio, total) - 1)).setScale(2,6);
    }

    //    2018年1月15日发放贷款1W，期限3个月，默认还款日为21日，年利率12%，偿还方式为等额本息
    public static void main(String[] args) {
        //月供
        BigDecimal monthlyPayment = PMT(0.12 / 12, 3, 10000);
        //首期利息
        BigDecimal firstInterest = new BigDecimal(10000 * 0.12 / 12 + 10000 * 0.12 / 360 * (21 - 15));
        System.out.println("首期利息" + firstInterest);
        //整周期利息
        BigDecimal fullPeriodInterest = new BigDecimal(10000 * 0.12 / 12);
        //首期应还本金
        BigDecimal firstInstallmentPrincipal = monthlyPayment.subtract(fullPeriodInterest).setScale(2, 6);
        //首期月供
        BigDecimal firstMonthlyPayment = firstInstallmentPrincipal.add(firstInterest);
        System.out.println("首期月供" + firstMonthlyPayment);

        //剩余本金
        BigDecimal remainingPrincipal = new BigDecimal(10000).subtract(firstInstallmentPrincipal);
        System.out.println("剩余本金" + remainingPrincipal);
        //第二期应还利息
        BigDecimal secondInterest = remainingPrincipal.multiply(new BigDecimal(0.12 / 12)).setScale(2, 6);
        System.out.println("第二期应还利息" + secondInterest);
        //第二期应还本金
        BigDecimal secondInstallmentPrincipal = monthlyPayment.subtract(secondInterest).setScale(2, 6);
        System.out.println("第二期应还本金" + secondInstallmentPrincipal);
        //第二期月供
        BigDecimal secondMonthlyPayment = secondInstallmentPrincipal.add(secondInterest);
        System.out.println("第二期月供" + secondMonthlyPayment);
        //剩余本金
        remainingPrincipal = remainingPrincipal.subtract(secondInstallmentPrincipal).setScale(2, 6);
        System.out.println("剩余本金" + remainingPrincipal);
        //第三期应还利息
        BigDecimal thirdInterest = remainingPrincipal.multiply(new BigDecimal(0.12 / 360 * 25)).setScale(2, 6);
        System.out.println("第三期应还利息" + thirdInterest);
        //第三期月供
        BigDecimal thirdMonthlyPayment = remainingPrincipal.add(thirdInterest);
        System.out.println("期末月供" + thirdMonthlyPayment);

        //等额本息 头尾 单算  其他的月供不变
        BigDecimal totalPayment = firstMonthlyPayment.add(secondMonthlyPayment).add(thirdMonthlyPayment).setScale(2, 6);
                //monthlyPayment.multiply(new BigDecimal(3)).setScale(2, 6);
        System.out.println("总还款金额" + totalPayment);
        BigDecimal totalInterest = totalPayment.subtract(new BigDecimal(10000));
        System.out.println("总利息" + totalInterest);
        System.out.println("等额本息");
        System.out.println("还款计划");
        System.out.println("序号\t还款日\t应还本金\t应还利息\t月供\t剩余本金");
        //计算两个日期之前的天数 算头不算尾
        int days = CommUtils.daysBetween(CommUtils.getDate("2018-01-15"), CommUtils.getDate("2018-02-21"));
        System.out.println(days);


    }

}

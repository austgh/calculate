package com.ahzx.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 等额本息还款计划计算器
 * 参数：贷款133万，年利率5.4%，30年，2021年2月20日放款，每月15日还款
 */
public class MortgageCalculator {

    public static void main(String[] args) {
        // 贷款参数
        BigDecimal principal = new BigDecimal("1330000");     // 贷款本金
        BigDecimal annualRate = new BigDecimal("0.054");      // 年利率 5.4%
        int totalYears = 30;                                   // 贷款年限
        LocalDate loanStartDate = LocalDate.of(2021, 2, 20);   // 放款日
        int paymentDay = 15;                                    // 每月还款日

        // 计算并打印还款计划
        generateRepaymentSchedule(principal, annualRate, totalYears, loanStartDate, paymentDay);
    }

    /**
     * 生成等额本息还款计划
     * @param principal     贷款本金
     * @param annualRate    年利率
     * @param totalYears    贷款年限
     * @param loanStartDate 放款日期
     * @param paymentDay    每月还款日
     */
    public static void generateRepaymentSchedule(BigDecimal principal, BigDecimal annualRate,
                                                 int totalYears, LocalDate loanStartDate, int paymentDay) {
        // 计算基本参数
        int totalMonths = totalYears * 12;                      // 总还款月数
        BigDecimal monthlyRate = annualRate.divide(new BigDecimal("12"), 10, RoundingMode.HALF_UP); // 月利率

        // 计算每月还款额 (等额本息公式)
        // M = P * [r*(1+r)^n] / [(1+r)^n - 1]
        BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyRate);
        BigDecimal powResult = onePlusRate.pow(totalMonths);    // (1+r)^n

        BigDecimal numerator = monthlyRate.multiply(powResult);  // r * (1+r)^n
        BigDecimal denominator = powResult.subtract(BigDecimal.ONE); // (1+r)^n - 1
        BigDecimal monthlyPayment = principal.multiply(numerator)
                .divide(denominator, 2, RoundingMode.HALF_UP); // 每月还款额，保留2位小数

        // 打印贷款概要
        System.out.println("========== 贷款概要 ==========");
        System.out.println("贷款本金: " + formatCurrency(principal));
        System.out.println("年利率: 5.4%");
        System.out.println("贷款期限: " + totalYears + "年 (" + totalMonths + "期)");
        System.out.println("放款日期: " + loanStartDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        System.out.println("每月还款日: " + paymentDay + "号");
        System.out.println("还款方式: 等额本息");
        System.out.println("每月还款额: " + formatCurrency(monthlyPayment));
        System.out.println();

        // 生成还款计划
        List<RepaymentRecord> schedule = new ArrayList<>();
        BigDecimal remainingPrincipal = principal;
        LocalDate paymentDate = getFirstPaymentDate(loanStartDate, paymentDay);

        BigDecimal totalInterest = BigDecimal.ZERO;

        for (int period = 1; period <= totalMonths; period++) {
            // 计算当月利息：剩余本金 * 月利率
            BigDecimal interest = remainingPrincipal.multiply(monthlyRate)
                    .setScale(2, RoundingMode.HALF_UP);

            // 计算当月本金：月供 - 利息
            BigDecimal principalPaid;
            if (period == totalMonths) {
                // 最后一期，本金直接等于剩余本金，避免因四舍五入导致的误差
                principalPaid = remainingPrincipal;
                // 重新计算最后一期月供（本金+利息）
                monthlyPayment = principalPaid.add(interest).setScale(2, RoundingMode.HALF_UP);
            } else {
                principalPaid = monthlyPayment.subtract(interest);
                // 处理可能出现的微小负数（当剩余本金很小时）
                if (principalPaid.compareTo(remainingPrincipal) > 0) {
                    principalPaid = remainingPrincipal;
                }
            }

            // 更新剩余本金
            remainingPrincipal = remainingPrincipal.subtract(principalPaid)
                    .setScale(2, RoundingMode.HALF_UP);

            // 累加总利息
            totalInterest = totalInterest.add(interest);

            // 创建还款记录
            RepaymentRecord record = new RepaymentRecord(
                    period,
                    paymentDate,
                    monthlyPayment,
                    interest,
                    principalPaid,
                    remainingPrincipal
            );
            schedule.add(record);

            // 更新下个还款日
            paymentDate = getNextPaymentDate(paymentDate);
        }

        // 打印前12期还款计划
        System.out.println("========== 前12期还款明细 ==========");
        System.out.printf("%-6s %-12s %-12s %-12s %-12s %-12s\n",
                "期数", "还款日", "月供(元)", "利息(元)", "本金(元)", "剩余本金(元)");

        for (int i = 0; i < Math.min(12, schedule.size()); i++) {
            RepaymentRecord record = schedule.get(i);
            System.out.printf("%-6d %-12s %-12s %-12s %-12s %-12s\n",
                    record.getPeriod(),
                    record.getPaymentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    formatCurrency(record.getMonthlyPayment()),
                    formatCurrency(record.getInterest()),
                    formatCurrency(record.getPrincipalPaid()),
                    formatCurrency(record.getRemainingPrincipal()));
        }

        // 打印贷款总结
        BigDecimal totalPayment = monthlyPayment.multiply(new BigDecimal(totalMonths))
                .setScale(2, RoundingMode.HALF_UP);

        System.out.println();
        System.out.println("========== 贷款总结 (30年总计) ==========");
        System.out.println("还款总额: " + formatCurrency(totalPayment));
        System.out.println("支付利息总额: " + formatCurrency(totalInterest));
        System.out.println("本金总额: " + formatCurrency(principal));
    }

    /**
     * 计算首次还款日期
     * 放款日为2021年2月20日，每月15日还款，则首次还款日为2021年3月15日
     */
    private static LocalDate getFirstPaymentDate(LocalDate loanStartDate, int paymentDay) {
        // 首次还款日为放款日的下个月15号
        LocalDate firstPayment = loanStartDate.plusMonths(1);
        return LocalDate.of(firstPayment.getYear(), firstPayment.getMonth(), paymentDay);
    }

    /**
     * 获取下一个还款日（下个月的同一天）
     */
    private static LocalDate getNextPaymentDate(LocalDate currentPaymentDate) {
        return currentPaymentDate.plusMonths(1);
    }

    /**
     * 格式化货币显示（保留两位小数，带千位分隔符）
     */
    private static String formatCurrency(BigDecimal amount) {
        return String.format("%,.2f", amount);
    }

    /**
     * 还款记录内部类
     */
    static class RepaymentRecord {
        private final int period;                 // 期数
        private final LocalDate paymentDate;      // 还款日
        private final BigDecimal monthlyPayment;  // 月供
        private final BigDecimal interest;        // 利息
        private final BigDecimal principalPaid;   // 归还本金
        private final BigDecimal remainingPrincipal; // 剩余本金

        public RepaymentRecord(int period, LocalDate paymentDate, BigDecimal monthlyPayment,
                               BigDecimal interest, BigDecimal principalPaid, BigDecimal remainingPrincipal) {
            this.period = period;
            this.paymentDate = paymentDate;
            this.monthlyPayment = monthlyPayment;
            this.interest = interest;
            this.principalPaid = principalPaid;
            this.remainingPrincipal = remainingPrincipal;
        }

        // Getters
        public int getPeriod() { return period; }
        public LocalDate getPaymentDate() { return paymentDate; }
        public BigDecimal getMonthlyPayment() { return monthlyPayment; }
        public BigDecimal getInterest() { return interest; }
        public BigDecimal getPrincipalPaid() { return principalPaid; }
        public BigDecimal getRemainingPrincipal() { return remainingPrincipal; }
    }
}
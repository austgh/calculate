package com.ahzx.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * @author think
 * @date 2026年04月11日 23:50
 *到期一次还本付息计算器
 *规则：到期日一次性偿还本金和利息，按日计息（实际天数），年利率/360
 */
public class LumpSumRepaymentCalculator {

    // 默认还款日（20号），本计算器在一次性还本付息中仅用于参考，实际还款日为到期日
    public static final int DEFAULT_REPAYMENT_DAY = 20;

    /**
     * 还款计划项
     */
    public static class RepaymentScheduleItem {
        private final LocalDate repaymentDate;
        private final BigDecimal amount;

        public RepaymentScheduleItem(LocalDate repaymentDate, BigDecimal amount) {
            this.repaymentDate = repaymentDate;
            this.amount = amount;
        }

        public LocalDate getRepaymentDate() {
            return repaymentDate;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        @Override
        public String toString() {
            return String.format("还款日期：%s，还款金额：%.2f", repaymentDate, amount);
        }
    }

    /**
     * 计算结果封装
     */
    public static class CalculationResult {
        private final LocalDate maturityDate;          // 到期日
        private final BigDecimal totalInterest;        // 总利息
        private final BigDecimal totalRepayment;       // 本息总额
        private final List<RepaymentScheduleItem> schedule; // 还款计划（仅一期）

        public CalculationResult(LocalDate maturityDate, BigDecimal totalInterest,
                                 BigDecimal totalRepayment, List<RepaymentScheduleItem> schedule) {
            this.maturityDate = maturityDate;
            this.totalInterest = totalInterest;
            this.totalRepayment = totalRepayment;
            this.schedule = schedule;
        }

        public LocalDate getMaturityDate() {
            return maturityDate;
        }

        public BigDecimal getTotalInterest() {
            return totalInterest;
        }

        public BigDecimal getTotalRepayment() {
            return totalRepayment;
        }

        public List<RepaymentScheduleItem> getSchedule() {
            return schedule;
        }

        @Override
        public String toString() {
            return String.format("到期日：%s，总利息：%.2f，还款总额：%.2f\n还款计划：%s",
                    maturityDate, totalInterest, totalRepayment, schedule.get(0));
        }
    }

    /**
     * 计算到期一次还本付息
     * @param principal     本金（单位：元）
     * @param annualRate    年利率（例如 12 表示 12%）
     * @param loanDate      放款日期
     * @param termMonths    期限（月）
     * @return              计算结果
     */
    public static CalculationResult calculate(BigDecimal principal, double annualRate,
                                              LocalDate loanDate, int termMonths) {
        // 1. 计算到期日：放款日期 + 期限月数
        LocalDate maturityDate = loanDate.plusMonths(termMonths);

        // 2. 计算实际天数（放款日到到期日，算头不算尾）
        long days = ChronoUnit.DAYS.between(loanDate, maturityDate);
        if (days < 0) {
            throw new IllegalArgumentException("到期日不能早于放款日");
        }

        // 3. 计算利息：本金 × 年利率 ÷ 360 × 实际天数
        BigDecimal rate = BigDecimal.valueOf(annualRate).divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_EVEN);
        BigDecimal dailyRate = rate.divide(BigDecimal.valueOf(360), 10, RoundingMode.HALF_EVEN);
        BigDecimal interest = principal.multiply(dailyRate).multiply(BigDecimal.valueOf(days))
                .setScale(2, RoundingMode.HALF_EVEN);

        // 4. 还款总额 = 本金 + 利息
        BigDecimal totalRepayment = principal.add(interest).setScale(2, RoundingMode.HALF_EVEN);

        // 5. 生成还款计划（仅一期，还款日为到期日）
        List<RepaymentScheduleItem> schedule = new ArrayList<>();
        schedule.add(new RepaymentScheduleItem(maturityDate, totalRepayment));

        return new CalculationResult(maturityDate, interest, totalRepayment, schedule);
    }

    // 示例运行
    public static void main(String[] args) {
        // 示例1：2018年1月15日发放1万元，期限5个月，年利率12%
        BigDecimal principal = new BigDecimal("10000");
        double annualRate = 12.0;
        LocalDate loanDate = LocalDate.of(2018, 1, 15);
        int termMonths = 5;

        CalculationResult result = calculate(principal, annualRate, loanDate, termMonths);
        System.out.println(result);
    }
}
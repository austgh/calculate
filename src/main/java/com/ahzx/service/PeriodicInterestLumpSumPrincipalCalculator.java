package com.ahzx.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * @author think
 * @date 2026年04月12日 0:15
 * 到期一次还本分期付息计算器
 * 规则：每期只还利息，到期日一次性偿还本金+最后一期利息
 * 计息方式：按日计息，年利率/360，按实际天数
 * 首期还款日：根据周期类型（非固定/固定季/半年/年）及默认还款日计算
 * 末期还款日：贷款到期日
 */
public class PeriodicInterestLumpSumPrincipalCalculator {

    // 默认还款日（可配置，默认20号）
    public static final int DEFAULT_REPAYMENT_DAY = 20;

    /**
     * 还款计划项
     */
    public static class RepaymentItem {
        private final LocalDate dueDate;        // 还款日
        private final BigDecimal interest;      // 利息金额
        private final BigDecimal principal;     // 本金金额（仅末期有本金）
        private final BigDecimal total;         // 本期还款总额

        public RepaymentItem(LocalDate dueDate, BigDecimal interest, BigDecimal principal, BigDecimal total) {
            this.dueDate = dueDate;
            this.interest = interest;
            this.principal = principal;
            this.total = total;
        }

        public LocalDate getDueDate() { return dueDate; }
        public BigDecimal getInterest() { return interest; }
        public BigDecimal getPrincipal() { return principal; }
        public BigDecimal getTotal() { return total; }

        @Override
        public String toString() {
            if (principal.compareTo(BigDecimal.ZERO) > 0) {
                return String.format("还款日：%s，利息：%.2f，本金：%.2f，总额：%.2f",
                        dueDate, interest, principal, total);
            } else {
                return String.format("还款日：%s，利息：%.2f，总额：%.2f",
                        dueDate, interest, total);
            }
        }
    }

    /**
     * 计算结果封装
     */
    public static class CalculationResult {
        private final List<RepaymentItem> schedule;   // 还款计划列表
        private final BigDecimal totalInterest;       // 总利息
        private final BigDecimal totalRepayment;      // 本息总额

        public CalculationResult(List<RepaymentItem> schedule, BigDecimal totalInterest, BigDecimal totalRepayment) {
            this.schedule = schedule;
            this.totalInterest = totalInterest;
            this.totalRepayment = totalRepayment;
        }

        public List<RepaymentItem> getSchedule() { return schedule; }
        public BigDecimal getTotalInterest() { return totalInterest; }
        public BigDecimal getTotalRepayment() { return totalRepayment; }

        public void printSchedule() {
            System.out.println("========== 还款计划 ==========");
            for (RepaymentItem item : schedule) {
                System.out.println(item);
            }
            System.out.printf("总利息：%.2f，本息总额：%.2f%n", totalInterest, totalRepayment);
        }
    }

    /**
     * 计算到期一次还本分期付息
     *
     * @param principal      贷款本金（元）
     * @param annualRate     年利率（例如 12 表示 12%）
     * @param loanDate       放款日期
     * @param termMonths     贷款期限（月）
     * @param periodMonths   还款周期（月）：1-月，3-季，6-半年，12-年
     * @param isFixedPeriod  是否为固定季/半年/年（季：3,6,9,12月；半年：6,12月；年：12月）
     * @param repaymentDay   默认还款日（1~31，若当月无此日则取最后一天）
     * @return               计算结果
     */
    public static CalculationResult calculate(BigDecimal principal, double annualRate,
                                              LocalDate loanDate, int termMonths,
                                              int periodMonths, boolean isFixedPeriod,
                                              int repaymentDay) {
        if(repaymentDay==0){
            repaymentDay=DEFAULT_REPAYMENT_DAY;//默认还款日
        }
        // 参数校验
        if (principal.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("本金必须大于0");
        if (annualRate <= 0) throw new IllegalArgumentException("年利率必须大于0");
        if (termMonths <= 0) throw new IllegalArgumentException("期限必须大于0");
        if (periodMonths <= 0 || periodMonths > 12) throw new IllegalArgumentException("周期必须在1~12之间");
        if (repaymentDay < 1 || repaymentDay > 31) throw new IllegalArgumentException("还款日必须在1~31之间");

        BigDecimal ratePerYear = BigDecimal.valueOf(annualRate).divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_EVEN);
        BigDecimal dailyRate = ratePerYear.divide(BigDecimal.valueOf(360), 10, RoundingMode.HALF_EVEN);

        LocalDate maturityDate = loanDate.plusMonths(termMonths);  // 到期日

        List<RepaymentItem> schedule = new ArrayList<>();
        BigDecimal totalInterest = BigDecimal.ZERO;

        LocalDate currentStartDate = loanDate;      // 当前计息起始日
        LocalDate currentDueDate;                    // 当前还款日

        // 计算首期还款日
        currentDueDate = calculateFirstDueDate(loanDate, periodMonths, isFixedPeriod, repaymentDay);

        // 循环生成还款计划，直到还款日超过或等于到期日
        while (currentDueDate.isBefore(maturityDate)) {
            // 计算当期利息（从 currentStartDate 到 currentDueDate，不含结束日）
            long days = ChronoUnit.DAYS.between(currentStartDate, currentDueDate);
            if (days <= 0) throw new RuntimeException("计息天数异常：" + days);
            BigDecimal interest = principal.multiply(dailyRate).multiply(BigDecimal.valueOf(days))
                    .setScale(2, RoundingMode.HALF_EVEN);

            // 本期只还利息，本金为0
            schedule.add(new RepaymentItem(currentDueDate, interest, BigDecimal.ZERO, interest));
            totalInterest = totalInterest.add(interest);

            // 准备下一期
            currentStartDate = currentDueDate;
            // 下一期还款日 = 当前还款日 + periodMonths 个月，并调整到 repaymentDay
            currentDueDate = addMonthsAndAdjustDay(currentDueDate, periodMonths, repaymentDay);
        }

        // 末期（到期日）：还本金 + 最后一期利息
        long finalDays = ChronoUnit.DAYS.between(currentStartDate, maturityDate);
        if (finalDays < 0) throw new RuntimeException("末期计息天数不能为负数");
        BigDecimal finalInterest = BigDecimal.ZERO;
        if (finalDays > 0) {
            finalInterest = principal.multiply(dailyRate).multiply(BigDecimal.valueOf(finalDays))
                    .setScale(2, RoundingMode.HALF_EVEN);
        }
        BigDecimal finalTotal = principal.add(finalInterest);
        schedule.add(new RepaymentItem(maturityDate, finalInterest, principal, finalTotal));
        totalInterest = totalInterest.add(finalInterest);
        BigDecimal totalRepayment = principal.add(totalInterest);

        return new CalculationResult(schedule, totalInterest, totalRepayment);
    }

    /**
     * 计算首期还款日（严格按照文档规则）
     *
     * @param loanDate       放款日
     * @param periodMonths   周期月数
     * @param isFixedPeriod  是否为固定季/半年/年
     * @param repaymentDay   默认还款日
     * @return               首期还款日
     */
    private static LocalDate calculateFirstDueDate(LocalDate loanDate, int periodMonths,
                                                   boolean isFixedPeriod, int repaymentDay) {
        if (!isFixedPeriod) {
            // 非固定季/半年/年（即普通月周期）
            LocalDate currentMonthDefault = adjustToRepaymentDay(loanDate, repaymentDay);
            if (loanDate.isBefore(currentMonthDefault)) {
                // 放款日在当月默认还款日之前 -> 当月默认还款日
                return currentMonthDefault;
            } else {
                // 放款日在当月默认还款日（含）之后 -> 放款月份+周期+默认还款日
                return addMonthsAndAdjustDay(loanDate, periodMonths, repaymentDay);
            }
        } else {
            // 固定季/半年/年
            // 首先找到放款日所属的固定周期默认还款日
            LocalDate currentPeriodDefault = getFixedPeriodDefaultDate(loanDate, periodMonths, repaymentDay);
            if (loanDate.isBefore(currentPeriodDefault)) {
                // 放款日在当前周期默认还款日之前 -> 当前周期默认还款日
                return currentPeriodDefault;
            } else {
                // 放款日在当前周期默认还款日（含）之后 -> 下一个周期默认还款日
                return getNextFixedPeriodDefaultDate(currentPeriodDefault, periodMonths, repaymentDay);
            }
        }
    }

    /**
     * 获取固定季/半年/年的当前周期默认还款日
     * 季：3,6,9,12月；半年：6,12月；年：12月
     */
    private static LocalDate getFixedPeriodDefaultDate(LocalDate loanDate, int periodMonths, int repaymentDay) {
        int year = loanDate.getYear();
        int month;
        if (periodMonths == 3) { // 季
            int quarter = (loanDate.getMonthValue() - 1) / 3; // 0,1,2,3
            month = (quarter + 1) * 3; // 3,6,9,12
        } else if (periodMonths == 6) { // 半年
            month = loanDate.getMonthValue() <= 6 ? 6 : 12;
        } else { // 年 (periodMonths == 12)
            month = 12;
        }
        return adjustToRepaymentDay(LocalDate.of(year, month, 1), repaymentDay);
    }

    /**
     * 获取下一个固定周期默认还款日
     */
    private static LocalDate getNextFixedPeriodDefaultDate(LocalDate currentPeriodDefault, int periodMonths, int repaymentDay) {
        LocalDate next;
        if (periodMonths == 3) {
            next = currentPeriodDefault.plusMonths(3);
        } else if (periodMonths == 6) {
            next = currentPeriodDefault.plusMonths(6);
        } else { // 年
            next = currentPeriodDefault.plusYears(1);
        }
        return adjustToRepaymentDay(next, repaymentDay);
    }

    /**
     * 给指定日期增加若干个月，并将日期调整为指定的 repaymentDay
     * 若该月不存在 repaymentDay（如2月30日），则取当月最后一天
     */
    private static LocalDate addMonthsAndAdjustDay(LocalDate date, int months, int repaymentDay) {
        LocalDate afterMonths = date.plusMonths(months);
        return adjustToRepaymentDay(afterMonths, repaymentDay);
    }

    /**
     * 将日期调整为当月的 repaymentDay，若该日不存在则取当月最后一天
     */
    private static LocalDate adjustToRepaymentDay(LocalDate date, int repaymentDay) {
        int year = date.getYear();
        int month = date.getMonthValue();
        int lastDayOfMonth = date.lengthOfMonth();
        int targetDay = Math.min(repaymentDay, lastDayOfMonth);
        return LocalDate.of(year, month, targetDay);
    }

    // ===================== 示例运行 =====================
    public static void main(String[] args) {
        // 示例1：贷款1万元，期限3个月，默认还款日21日，年利率12%
        BigDecimal principal = new BigDecimal("10000");
        double annualRate = 12.0;
        LocalDate loanDate = LocalDate.of(2018, 1, 15);
        int termMonths = 3;
        int periodMonths = 1;          // 月周期（非固定）
        boolean isFixedPeriod = false;  // 非固定季/半年/年
        int repaymentDay = 21;          // 示例中的21号

        CalculationResult result = calculate(principal, annualRate, loanDate, termMonths,
                periodMonths, isFixedPeriod, repaymentDay);
        result.printSchedule();

        System.out.println("\n注：上述结果严格按照文档规则计算（首期还款日为当月21日，但示例中为2月21日，存在差异）。");
        System.out.println("如需复现示例结果，请将首期还款日规则调整为“放款日之后第一个默认还款日”。");
    }
}

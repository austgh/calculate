package com.ahzx.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * @author think
 * @date 2026年04月12日 10:38
 * 分期付息分期还本计算器
 * 规则：还息周期和还本周期独立，还本周期是还息周期的整数倍（≥2倍）
 * 计息方式：按日计息（实际天数/360）
 * 首期还款：次月还款（放款日次月的指定还款日）
 * 末期合并：最后一期还清剩余本金
 * 默认还款日：放款日对日（可自定义）
 */
public class InstallmentInterestPrincipalCalculator {

    /**
     * 还款计划项
     */
    public static class RepaymentItem {
        private final int period;               // 期次
        private final LocalDate dueDate;        // 还款日期
        private final BigDecimal principal;     // 应还本金
        private final BigDecimal interest;      // 应还利息
        private final BigDecimal total;         // 当期还款总额

        public RepaymentItem(int period, LocalDate dueDate, BigDecimal principal, BigDecimal interest, BigDecimal total) {
            this.period = period;
            this.dueDate = dueDate;
            this.principal = principal;
            this.interest = interest;
            this.total = total;
        }

        public int getPeriod() { return period; }
        public LocalDate getDueDate() { return dueDate; }
        public BigDecimal getPrincipal() { return principal; }
        public BigDecimal getInterest() { return interest; }
        public BigDecimal getTotal() { return total; }

        @Override
        public String toString() {
            return String.format("期次 %d | 还款日 %s | 本金 %-8.2f | 利息 %-8.2f | 总额 %-8.2f",
                    period, dueDate, principal, interest, total);
        }
    }

    /**
     * 计算结果封装
     */
    public static class CalculationResult {
        private final List<RepaymentItem> schedule;
        private final BigDecimal totalInterest;
        private final BigDecimal totalRepayment;

        public CalculationResult(List<RepaymentItem> schedule, BigDecimal totalInterest, BigDecimal totalRepayment) {
            this.schedule = schedule;
            this.totalInterest = totalInterest;
            this.totalRepayment = totalRepayment;
        }

        public void printSchedule() {
            System.out.println("========== 还款计划 ==========");
            for (RepaymentItem item : schedule) {
                System.out.println(item);
            }
            System.out.printf("总利息：%.2f，本息总额：%.2f%n", totalInterest, totalRepayment);
        }
    }

    /**
     * 计算分期付息分期还本
     *
     * @param principal              贷款本金（元）
     * @param annualRate             年利率（例如 12 表示 12%）
     * @param loanDate               放款日期
     * @param termMonths             贷款期限（月）
     * @param interestPeriodMonths   还息周期（月）：1-月，2-双月，3-季，6-半年，12-年
     * @param principalPeriodMonths  还本周期（月）：必须是 interestPeriodMonths 的整数倍，且 ≥ 2倍
     * @param repaymentDayOfMonth    还款日（1~31），若为0则使用放款日对日
     * @return                       计算结果
     */
    public static CalculationResult calculate(BigDecimal principal, double annualRate,
                                              LocalDate loanDate, int termMonths,
                                              int interestPeriodMonths, int principalPeriodMonths,
                                              int repaymentDayOfMonth) {
        // 参数校验
        if (principal.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("本金必须大于0");
        if (annualRate <= 0) throw new IllegalArgumentException("年利率必须大于0");
        if (termMonths <= 0) throw new IllegalArgumentException("期限必须大于0");
        if (interestPeriodMonths <= 0) throw new IllegalArgumentException("还息周期必须大于0");
        if (principalPeriodMonths <= 0) throw new IllegalArgumentException("还本周期必须大于0");
        if (principalPeriodMonths % interestPeriodMonths != 0)
            throw new IllegalArgumentException("还本周期必须是还息周期的整数倍");
        if (principalPeriodMonths / interestPeriodMonths < 2)
            throw new IllegalArgumentException("还本周期至少是还息周期的2倍");

        // 日利率
        BigDecimal dailyRate = BigDecimal.valueOf(annualRate)
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_EVEN)
                .divide(BigDecimal.valueOf(360), 10, RoundingMode.HALF_EVEN);

        // 总还息期数 = 期限月数 / 还息周期月数
        int totalInterestPeriods = termMonths / interestPeriodMonths;
        if (termMonths % interestPeriodMonths != 0)
            throw new IllegalArgumentException("期限月数必须是还息周期的整数倍");

        // 还本间隔期数（每多少期还一次本）
        int principalInterval = principalPeriodMonths / interestPeriodMonths;
        // 还本次数
        int principalTimes = totalInterestPeriods / principalInterval;
        if (totalInterestPeriods % principalInterval != 0)
            throw new IllegalArgumentException("还本周期必须能整除总期数");

        // 每期计划还本额（等额本金）
        BigDecimal plannedPrincipalPerTime = principal.divide(BigDecimal.valueOf(principalTimes), 10, RoundingMode.HALF_EVEN);
        // 剩余本金
        BigDecimal remainingPrincipal = principal;

        // 生成还款日期列表（共 totalInterestPeriods 期）
        List<LocalDate> dueDates = new ArrayList<>();
        // 首期还款日：放款日次月的还款日
        LocalDate firstDue = loanDate.plusMonths(1);
        firstDue = adjustToRepaymentDay(firstDue, repaymentDayOfMonth, loanDate);
        dueDates.add(firstDue);
        // 后续期次：每增加 interestPeriodMonths 个月
        for (int i = 1; i < totalInterestPeriods; i++) {
            LocalDate prev = dueDates.get(i - 1);
            LocalDate next = prev.plusMonths(interestPeriodMonths);
            next = adjustToRepaymentDay(next, repaymentDayOfMonth, loanDate);
            dueDates.add(next);
        }
        // 最后一期替换为到期日
        LocalDate maturityDate = loanDate.plusMonths(termMonths);
        dueDates.set(totalInterestPeriods - 1, maturityDate);

        // 构建还款计划
        List<RepaymentItem> schedule = new ArrayList<>();
        BigDecimal totalInterest = BigDecimal.ZERO;
        LocalDate prevDate = loanDate;  // 上一个计息起始日

        for (int period = 1; period <= totalInterestPeriods; period++) {
            LocalDate currentDue = dueDates.get(period - 1);
            // 计息天数
            long days = ChronoUnit.DAYS.between(prevDate, currentDue);
            if (days < 0) throw new RuntimeException("计息天数异常");
            // 当期利息
            BigDecimal interest = remainingPrincipal.multiply(dailyRate).multiply(BigDecimal.valueOf(days))
                    .setScale(2, RoundingMode.HALF_EVEN);

            BigDecimal principalPaid = BigDecimal.ZERO;
            boolean isPrincipalPeriod = (period % principalInterval == 0) || (period == totalInterestPeriods);
            if (isPrincipalPeriod) {
                // 还本：如果是最后一期，还清剩余本金；否则还计划金额
                if (period == totalInterestPeriods) {
                    principalPaid = remainingPrincipal;
                } else {
                    // 注意精度：前几期用计划金额，最后一期调整
                    if (principalPaid.compareTo(BigDecimal.ZERO) == 0) {
                        // 首次还本时确定每期固定还本额（四舍五入到分）
                        principalPaid = plannedPrincipalPerTime.setScale(2, RoundingMode.HALF_EVEN);
                    }
                    // 避免超出剩余本金
                    if (principalPaid.compareTo(remainingPrincipal) > 0) {
                        principalPaid = remainingPrincipal;
                    }
                }
                remainingPrincipal = remainingPrincipal.subtract(principalPaid);
            }

            BigDecimal total = interest.add(principalPaid);
            schedule.add(new RepaymentItem(period, currentDue, principalPaid, interest, total));
            totalInterest = totalInterest.add(interest);
            prevDate = currentDue;
        }

        // 最终剩余本金应为0（允许极小误差）
        if (remainingPrincipal.abs().compareTo(new BigDecimal("0.01")) > 0) {
            // 调整最后一期本金
            RepaymentItem last = schedule.get(schedule.size() - 1);
            BigDecimal adjustPrincipal = last.getPrincipal().add(remainingPrincipal);
            BigDecimal adjustTotal = last.getInterest().add(adjustPrincipal);
            schedule.set(schedule.size() - 1, new RepaymentItem(last.getPeriod(), last.getDueDate(),
                    adjustPrincipal, last.getInterest(), adjustTotal));
        }

        BigDecimal totalRepayment = principal.add(totalInterest);
        return new CalculationResult(schedule, totalInterest, totalRepayment);
    }

    /**
     * 将日期调整为指定的还款日（若不存在则取当月最后一天）
     * @param date              待调整的日期
     * @param repaymentDayOfMonth 还款日（1~31），若为0则使用基准日期的日份（放款日对日）
     * @param referenceDate     基准日期（仅当 repaymentDayOfMonth == 0 时使用）
     */
    private static LocalDate adjustToRepaymentDay(LocalDate date, int repaymentDayOfMonth, LocalDate referenceDate) {
        int targetDay;
        if (repaymentDayOfMonth > 0) {
            targetDay = repaymentDayOfMonth;
        } else {
            targetDay = referenceDate.getDayOfMonth();
        }
        int lastDay = date.lengthOfMonth();
        int finalDay = Math.min(targetDay, lastDay);
        return LocalDate.of(date.getYear(), date.getMonth(), finalDay);
    }

    // ===================== 示例运行 =====================
    public static void main(String[] args) {
        // 示例：2018年1月15日发放贷款1W，期限6个月，还款日21日，年利率12%
        // 还息周期按月（1个月），还本周期按双月（2个月）
        BigDecimal principal = new BigDecimal("10000");
        double annualRate = 12.0;
        LocalDate loanDate = LocalDate.of(2018, 1, 15);
        int termMonths = 6;
        int interestPeriodMonths = 1;   // 月
        int principalPeriodMonths = 2;   // 双月
        int repaymentDayOfMonth = 21;    // 自定义还款日21号

        CalculationResult result = calculate(principal, annualRate, loanDate, termMonths,
                interestPeriodMonths, principalPeriodMonths, repaymentDayOfMonth);
        result.printSchedule();
    }
}
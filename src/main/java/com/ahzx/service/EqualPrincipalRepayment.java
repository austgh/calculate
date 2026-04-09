package com.ahzx.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * @author think
 * @date 2026年04月09日 23:12
 *
 *  等额本金还款计算器
 *  规则：
 *  - 次月还款，放款日对日（或指定每月固定还款日）
 *  - 按月计息，不足整月按实际天数（年利率/360 * 天数）
 *  - 整月利息 = 剩余本金 * 年利率 / 12
 *  - 末期合并：最后一期还清全部剩余本金 + 末期利息
 *
 */
public class EqualPrincipalRepayment {

    /**
     * 还款记录
     */
    public static class RepaymentRecord {
        private final int period;
        private final LocalDate dueDate;
        private final BigDecimal principal;      // 应还本金
        private final BigDecimal interest;       // 应还利息
        private final BigDecimal total;          // 月供总额
        private final BigDecimal remainingPrincipal; // 剩余本金

        public RepaymentRecord(int period, LocalDate dueDate,
                               BigDecimal principal, BigDecimal interest,
                               BigDecimal total, BigDecimal remainingPrincipal) {
            this.period = period;
            this.dueDate = dueDate;
            this.principal = principal;
            this.interest = interest;
            this.total = total;
            this.remainingPrincipal = remainingPrincipal;
        }

        @Override
        public String toString() {
            return String.format("期数:%d  还款日:%s  应还本金:%-10s  利息:%-10s  月供:%-10s  剩余本金:%-10s",
                    period, dueDate, principal, interest, total, remainingPrincipal);
        }
    }

    /**
     * 还款日策略：放款日对日（默认）
     */
    public interface DueDateStrategy {
        LocalDate getDueDate(LocalDate loanDate, int monthsAfterLoan, int totalMonths);
    }

    /**
     * 策略1：放款日对日。每月还款日为 loanDate 加上 monthsAfterLoan 个月后的同一天（自动处理月末）
     */
    public static class LoanDateOffsetStrategy implements DueDateStrategy {
        @Override
        public LocalDate getDueDate(LocalDate loanDate, int monthsAfterLoan, int totalMonths) {
            // 最后一期使用到期日（放款日 + totalMonths 个月）
            if (monthsAfterLoan == totalMonths) {
                return loanDate.plusMonths(totalMonths);
            }
            return loanDate.plusMonths(monthsAfterLoan);
        }
    }

    /**
     * 策略2：每月固定某一天（如21日）。除最后一期为到期日外，其余各期为 loanDate 加若干月后的该固定日。
     * 注意：若该月不存在指定日期（如31日），则取当月最后一天。
     */
    public static class FixedDayOfMonthStrategy implements DueDateStrategy {
        private final int dayOfMonth;

        public FixedDayOfMonthStrategy(int dayOfMonth) {
            this.dayOfMonth = dayOfMonth;
        }

        @Override
        public LocalDate getDueDate(LocalDate loanDate, int monthsAfterLoan, int totalMonths) {
            // 最后一期：贷款到期日
            if (monthsAfterLoan == totalMonths) {
                return loanDate.plusMonths(totalMonths);
            }
            // 中间期：loanDate 加 monthsAfterLoan 个月后的月份，取固定 dayOfMonth
            LocalDate base = loanDate.plusMonths(monthsAfterLoan);
            int lastDay = base.lengthOfMonth();
            int actualDay = Math.min(dayOfMonth, lastDay);
            return LocalDate.of(base.getYear(), base.getMonth(), actualDay);
        }
    }

    /**
     * 计算等额本金还款计划
     * @param principal   贷款本金
     * @param annualRate  年利率（如 0.12 表示 12%）
     * @param months      贷款期限（月）
     * @param loanDate    放款日期
     * @param strategy    还款日策略（null 则使用默认的放款日对日策略）
     * @return 还款计划列表
     */
    public static List<RepaymentRecord> compute(BigDecimal principal, BigDecimal annualRate,
                                                int months, LocalDate loanDate,
                                                DueDateStrategy strategy) {
        if (strategy == null) {
            strategy = new LoanDateOffsetStrategy();
        }
        List<RepaymentRecord> records = new ArrayList<>();

        // 每期应还本金（保留2位小数，最后一期补差）
        BigDecimal periodPrincipal = principal.divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);
        BigDecimal remaining = principal;

        // 生成各期还款日（最后一期为到期日）
        LocalDate[] dueDates = new LocalDate[months];
        for (int i = 1; i <= months; i++) {
            dueDates[i - 1] = strategy.getDueDate(loanDate, i, months);
        }

        LocalDate prevDueDate = loanDate; // 上一结息日（放款日或上期还款日）

        for (int i = 0; i < months; i++) {
            int periodNum = i + 1;
            LocalDate dueDate = dueDates[i];
            boolean isLast = (periodNum == months);

            // 应还本金：最后一期取剩余本金，否则取固定 periodPrincipal
            BigDecimal currPrincipal;
            if (isLast) {
                currPrincipal = remaining;
            } else {
                currPrincipal = periodPrincipal;
                // 避免因四舍五入导致最后一期负剩余，提前做减法验证
                if (currPrincipal.compareTo(remaining) > 0) {
                    currPrincipal = remaining;
                }
            }

            // 计算利息
            BigDecimal interest;
            if (isLast) {
                // 末期：按实际天数计息（从上期还款日到到期日）
                long days = ChronoUnit.DAYS.between(prevDueDate, dueDate);
                interest = remaining.multiply(annualRate)
                        .multiply(BigDecimal.valueOf(days))
                        .divide(BigDecimal.valueOf(360), 2, RoundingMode.HALF_UP);
            } else {
                // 非末期：判断是否为一个整月 + 可能的零头天（当还款日 > 放款日+整月时）
                LocalDate wholeMonthEnd = prevDueDate.plusMonths(1);
                if (dueDate.isAfter(wholeMonthEnd)) {
                    // 超出一个整月：整月利息 + 零头天利息
                    long extraDays = ChronoUnit.DAYS.between(wholeMonthEnd, dueDate);
                    BigDecimal wholeMonthInterest = remaining.multiply(annualRate)
                            .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
                    BigDecimal extraInterest = remaining.multiply(annualRate)
                            .multiply(BigDecimal.valueOf(extraDays))
                            .divide(BigDecimal.valueOf(360), 2, RoundingMode.HALF_UP);
                    interest = wholeMonthInterest.add(extraInterest);
                } else {
                    // 整月（对日或提前，一般是对日）
                    interest = remaining.multiply(annualRate)
                            .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
                }
            }

            BigDecimal total = currPrincipal.add(interest);
            remaining = remaining.subtract(currPrincipal);

            records.add(new RepaymentRecord(periodNum, dueDate, currPrincipal, interest, total, remaining));

            prevDueDate = dueDate;
        }

        return records;
    }

    // 示例测试（匹配您提供的示例：1月15日放款，期限3个月，年利率12%，固定还款日21日）
    public static void main(String[] args) {
        BigDecimal principal = new BigDecimal("10000");
        BigDecimal annualRate = new BigDecimal("0.12");
        int months = 3;
        LocalDate loanDate = LocalDate.of(2018, 1, 15);

        // 使用固定还款日21日策略（最后一期自动为到期日4月15日）
        FixedDayOfMonthStrategy strategy = new FixedDayOfMonthStrategy(21);
        List<RepaymentRecord> records = compute(principal, annualRate, months, loanDate, strategy);

        System.out.println("=== 等额本金还款计划（固定还款日21日，匹配示例）===");
        for (RepaymentRecord record : records) {
            System.out.println(record);
        }

        // 验证示例中的数值
        // 首期：2018-02-21，应还本金3333.33，整月利息100 + 零头天利息20 = 120，月供3453.33
        // 第二期：2018-03-21，剩余6666.67，整月利息66.67，月供3400.00
        // 末期：2018-04-15，剩余3333.34，计息25天利息27.78，月供3361.12
        System.out.println("\n--- 数值验证 ---");
        RepaymentRecord r1 = records.get(0);
        System.out.printf("首期月供: %.2f (期望3453.33)%n", r1.total);
        RepaymentRecord r2 = records.get(1);
        System.out.printf("第二期月供: %.2f (期望3400.00)%n", r2.total);
        RepaymentRecord r3 = records.get(2);
        System.out.printf("末期月供: %.2f (期望3361.12)%n", r3.total);
    }
}

package com.ahzx.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * @author think
 * @date 2026年04月12日 22:31
 * 不规则还款方式计算器
 * 支持手工录入还本计划（日期+本金），每次还本时一并结清期间利息
 * 计息方式：按日计息，实际天数/360
 */
public class IrregularRepaymentCalculator {

    // 默认还款日（仅用于文档中的首期规则，本实现中未强制使用）
    public static final int DEFAULT_REPAYMENT_DAY = 20;

    /**
     * 手工还本计划项
     */
    public static class PrincipalRepaymentPlan {
        private final LocalDate date;      // 还本日期
        private final BigDecimal amount;   // 还本金额

        public PrincipalRepaymentPlan(LocalDate date, BigDecimal amount) {
            this.date = date;
            this.amount = amount;
        }

        public LocalDate getDate() { return date; }
        public BigDecimal getAmount() { return amount; }
    }

    /**
     * 还款明细项（每期还本付息）
     */
    public static class RepaymentDetail {
        private final LocalDate date;          // 还款日期
        private final BigDecimal principal;    // 还本金额
        private final BigDecimal interest;     // 还息金额
        private final BigDecimal total;        // 本期总额

        public RepaymentDetail(LocalDate date, BigDecimal principal, BigDecimal interest, BigDecimal total) {
            this.date = date;
            this.principal = principal;
            this.interest = interest;
            this.total = total;
        }

        @Override
        public String toString() {
            return String.format("还款日 %s | 还本 %-8.2f | 利息 %-8.2f | 总额 %-8.2f",
                    date, principal, interest, total);
        }
    }

    /**
     * 计算结果
     */
    public static class CalculationResult {
        private final List<RepaymentDetail> details;  // 还款明细（按时间顺序）
        private final BigDecimal totalInterest;       // 总利息
        private final BigDecimal totalRepayment;      // 本息总额

        public CalculationResult(List<RepaymentDetail> details, BigDecimal totalInterest, BigDecimal totalRepayment) {
            this.details = details;
            this.totalInterest = totalInterest;
            this.totalRepayment = totalRepayment;
        }

        public void print() {
            System.out.println("========== 不规则还款明细 ==========");
            for (RepaymentDetail d : details) {
                System.out.println(d);
            }
            System.out.printf("总利息：%.2f，本息总额：%.2f%n", totalInterest, totalRepayment);
        }
    }

    /**
     * 计算不规则还款
     *
     * @param principal           贷款本金（元）
     * @param annualRate          年利率（如12表示12%）
     * @param loanDate            放款日期
     * @param maturityDate        到期日（若为null，则取最后一个还本日作为到期日）
     * @param repaymentPlanList   手工还本计划（按时间顺序，可乱序但会自动排序）
     * @return                    计算结果
     */
    public static CalculationResult calculate(BigDecimal principal, double annualRate,
                                              LocalDate loanDate, LocalDate maturityDate,
                                              List<PrincipalRepaymentPlan> repaymentPlanList) {
        // 参数校验
        if (principal.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("本金必须大于0");
        if (annualRate <= 0) throw new IllegalArgumentException("年利率必须大于0");
        if (loanDate == null) throw new IllegalArgumentException("放款日不能为空");
        if (repaymentPlanList == null || repaymentPlanList.isEmpty())
            throw new IllegalArgumentException("还本计划不能为空");

        // 按日期排序还本计划
        List<PrincipalRepaymentPlan> sortedPlans = new ArrayList<>(repaymentPlanList);
        sortedPlans.sort((a, b) -> a.getDate().compareTo(b.getDate()));

        // 检查第一个还本日不能早于放款日
        if (sortedPlans.get(0).getDate().isBefore(loanDate)) {
            throw new IllegalArgumentException("还本日不能早于放款日");
        }

        // 如果未提供到期日，则取最后一个还本日作为到期日
        if (maturityDate == null) {
            maturityDate = sortedPlans.get(sortedPlans.size() - 1).getDate();
        }

        // 日利率（保留10位小数，使用银行家舍入法）
        BigDecimal dailyRate = BigDecimal.valueOf(annualRate)
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_EVEN)
                .divide(BigDecimal.valueOf(360), 10, RoundingMode.HALF_EVEN);

        List<RepaymentDetail> details = new ArrayList<>();
        BigDecimal remainingPrincipal = principal;
        BigDecimal totalInterest = BigDecimal.ZERO;
        LocalDate previousDate = loanDate;

        // 遍历每个还本计划
        for (PrincipalRepaymentPlan plan : sortedPlans) {
            LocalDate currentDate = plan.getDate();
            if (currentDate.isBefore(previousDate)) continue; // 已排序，不会发生

            long days = ChronoUnit.DAYS.between(previousDate, currentDate);
            if (days < 0) throw new RuntimeException("日期顺序错误");

            // 计算期间利息
            BigDecimal interest = remainingPrincipal.multiply(dailyRate)
                    .multiply(BigDecimal.valueOf(days))
                    .setScale(2, RoundingMode.HALF_EVEN);

            BigDecimal principalPaid = plan.getAmount();
            // 还本金额不能超过剩余本金
            if (principalPaid.compareTo(remainingPrincipal) > 0) {
                principalPaid = remainingPrincipal;
            }

            BigDecimal total = principalPaid.add(interest);
            details.add(new RepaymentDetail(currentDate, principalPaid, interest, total));

            // 更新剩余本金和总利息
            remainingPrincipal = remainingPrincipal.subtract(principalPaid);
            totalInterest = totalInterest.add(interest);
            previousDate = currentDate;

            // 如果剩余本金为0，提前结束
            if (remainingPrincipal.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }
        }

        // 如果到期日大于最后一个还本日，且还有剩余本金，则需在到期日结清
        if (remainingPrincipal.compareTo(BigDecimal.ZERO) > 0 && maturityDate.isAfter(previousDate)) {
            long finalDays = ChronoUnit.DAYS.between(previousDate, maturityDate);
            if (finalDays > 0) {
                BigDecimal finalInterest = remainingPrincipal.multiply(dailyRate)
                        .multiply(BigDecimal.valueOf(finalDays))
                        .setScale(2, RoundingMode.HALF_EVEN);
                BigDecimal finalTotal = remainingPrincipal.add(finalInterest);
                details.add(new RepaymentDetail(maturityDate, remainingPrincipal, finalInterest, finalTotal));
                totalInterest = totalInterest.add(finalInterest);
                remainingPrincipal = BigDecimal.ZERO;
            }
        }

        // 最终剩余本金应为0（允许极小误差）
        if (remainingPrincipal.compareTo(BigDecimal.ZERO) != 0) {
            throw new RuntimeException("还本计划总额不等于贷款本金，剩余：" + remainingPrincipal);
        }

        BigDecimal totalRepayment = principal.add(totalInterest);
        return new CalculationResult(details, totalInterest, totalRepayment);
    }

    // ===================== 示例运行 =====================
    public static void main(String[] args) {
        // 示例数据：2018年1月15日发放1万元，年利率12%，还本计划如下
        BigDecimal principal = new BigDecimal("10000");
        double annualRate = 12.0;
        LocalDate loanDate = LocalDate.of(2018, 1, 15);
        LocalDate maturityDate = LocalDate.of(2018, 6, 15); // 期限5个月，到期日

        List<PrincipalRepaymentPlan> plan = new ArrayList<>();
        plan.add(new PrincipalRepaymentPlan(LocalDate.of(2018, 2, 10), new BigDecimal("3000")));
        plan.add(new PrincipalRepaymentPlan(LocalDate.of(2018, 5, 15), new BigDecimal("2000")));
        plan.add(new PrincipalRepaymentPlan(LocalDate.of(2018, 6, 15), new BigDecimal("5000")));

        CalculationResult result = calculate(principal, annualRate, loanDate, maturityDate, plan);
        result.print();
    }
}
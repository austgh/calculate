package com.ahzx.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author think
 * @date 2026年04月24日 15:41
 * 不规则还款方式计算器
 * 支持手工录入还本计划（日期+本金），每次还本时一并结清期间利息
 * 计息方式：按日计息，实际天数/360
 */
 public class IrregularRepaymentCalculator {

    // 默认还款日（20号）
    private static final int DEFAULT_PAYMENT_DAY = 20;

    /**
     * 还息周期枚举
     */
    public enum InterestPeriod {
        MONTHLY(1, false),
        BIMONTHLY(2, false),
        QUARTERLY(3, false),
        SEMI_ANNUALLY(6, false),
        ANNUALLY(12, false),
        FIXED_QUARTERLY(3, true),
        FIXED_SEMI_ANNUALLY(6, true),
        FIXED_ANNUALLY(12, true);

        private final int months;
        private final boolean fixed;

        InterestPeriod(int months, boolean fixed) {
            this.months = months;
            this.fixed = fixed;
        }

        public int getMonths() { return months; }
        public boolean isFixed() { return fixed; }
    }

    /**
     * 还款计划条目类型
     */
    public enum EntryType {
        INTEREST_ONLY,   // 仅还息
        PRINCIPAL_ONLY,  // 仅还本
        PRINCIPAL_AND_INTEREST  // 还本+息（到期日）
    }

    /**
     * 还款计划条目
     */
    public static class RepaymentScheduleEntry {
        private LocalDate date;
        private EntryType type;
        private BigDecimal principal;
        private BigDecimal interest;
        private BigDecimal total;
        private BigDecimal remainingPrincipal;

        // 构造器、getter/setter 省略，实际使用时使用 Lombok 或手动生成
        public RepaymentScheduleEntry(LocalDate date, EntryType type,
                                      BigDecimal principal, BigDecimal interest,
                                      BigDecimal remainingPrincipal) {
            this.date = date;
            this.type = type;
            this.principal = principal != null ? principal : BigDecimal.ZERO;
            this.interest = interest != null ? interest : BigDecimal.ZERO;
            this.total = this.principal.add(this.interest);
            this.remainingPrincipal = remainingPrincipal;
        }

        // getters...
        public LocalDate getDate() { return date; }
        public EntryType getType() { return type; }
        public BigDecimal getPrincipal() { return principal; }
        public BigDecimal getInterest() { return interest; }
        public BigDecimal getTotal() { return total; }
        public BigDecimal getRemainingPrincipal() { return remainingPrincipal; }

        @Override
        public String toString() {
            return String.format("%s | %-10s | 本金: %8.2f | 利息: %8.2f | 合计: %8.2f | 剩余本金: %8.2f",
                    date, type, principal, interest, total, remainingPrincipal);
        }
    }

    /**
     * 还本计划（手工录入）
     */
    public static class PrincipalRepayment {
        private LocalDate date;
        private BigDecimal amount;

        public PrincipalRepayment(LocalDate date, BigDecimal amount) {
            this.date = date;
            this.amount = amount;
        }

        public LocalDate getDate() { return date; }
        public BigDecimal getAmount() { return amount; }
    }

    /**
     * 计算请求参数
     */
    public static class CalculationRequest {
        private LocalDate disbursalDate;      // 放款日
        private BigDecimal principal;          // 贷款本金
        private BigDecimal annualInterestRate; // 年利率（例如 0.12 表示 12%）
        private LocalDate maturityDate;        // 到期日
        private int defaultPaymentDay;          // 默认还款日（通常为20）
        private InterestPeriod interestPeriod;  // 还息周期
        private List<PrincipalRepayment> principalRepayments; // 手工还本计划

        // 构造器 & builder 模式...
        private CalculationRequest() {}

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private CalculationRequest request = new CalculationRequest();
            public Builder disbursalDate(LocalDate date) { request.disbursalDate = date; return this; }
            public Builder principal(BigDecimal principal) { request.principal = principal; return this; }
            public Builder annualInterestRate(BigDecimal rate) { request.annualInterestRate = rate; return this; }
            public Builder maturityDate(LocalDate date) { request.maturityDate = date; return this; }
            public Builder defaultPaymentDay(int day) { request.defaultPaymentDay = day; return this; }
            public Builder interestPeriod(InterestPeriod period) { request.interestPeriod = period; return this; }
            public Builder principalRepayments(List<PrincipalRepayment> list) { request.principalRepayments = list; return this; }
            public CalculationRequest build() { return request; }
        }

        // getters...
        public LocalDate getDisbursalDate() { return disbursalDate; }
        public BigDecimal getPrincipal() { return principal; }
        public BigDecimal getAnnualInterestRate() { return annualInterestRate; }
        public LocalDate getMaturityDate() { return maturityDate; }
        public int getDefaultPaymentDay() { return defaultPaymentDay; }
        public InterestPeriod getInterestPeriod() { return interestPeriod; }
        public List<PrincipalRepayment> getPrincipalRepayments() { return principalRepayments; }
    }

    /**
     * 核心计算方法
     */
    public static List<RepaymentScheduleEntry> calculate(CalculationRequest request) {
        List<RepaymentScheduleEntry> schedule = new ArrayList<>();

        // 参数准备
        LocalDate disbursalDate = request.getDisbursalDate();
        BigDecimal principal = request.getPrincipal();
        BigDecimal annualRate = request.getAnnualInterestRate();
        LocalDate maturityDate = request.getMaturityDate();
        int defaultDay = request.getDefaultPaymentDay() > 0 ? request.getDefaultPaymentDay() : DEFAULT_PAYMENT_DAY;
        InterestPeriod period = request.getInterestPeriod();
        List<PrincipalRepayment> repayments = request.getPrincipalRepayments() != null ?
                request.getPrincipalRepayments() : Collections.emptyList();

        // 复制并排序还本计划（按日期升序）
        List<PrincipalRepayment> sortedRepayments = new ArrayList<>(repayments);
        sortedRepayments.sort(Comparator.comparing(PrincipalRepayment::getDate));

        // 1. 生成所有还息日（不包括到期日，因为到期日单独处理）
        List<LocalDate> interestDates = generateInterestDates(disbursalDate, maturityDate, period, defaultDay);

        // 2. 合并所有事件日期（还本日、还息日、到期日），并去重排序
        Set<LocalDate> eventDatesSet = new TreeSet<>();
        for (PrincipalRepayment r : sortedRepayments) {
            eventDatesSet.add(r.getDate());
        }
        eventDatesSet.addAll(interestDates);
        eventDatesSet.add(maturityDate);
        // 放款日本身不需要作为事件
        List<LocalDate> eventDates = new ArrayList<>(eventDatesSet);
        Collections.sort(eventDates);

        // 3. 初始化状态
        BigDecimal remainingPrincipal = principal;
        BigDecimal currentInterest = BigDecimal.ZERO;      // 累计待支付利息
        LocalDate lastInterestDate = disbursalDate;        // 上一结息日

        // 用于快速查找还本金额的映射
        Map<LocalDate, BigDecimal> principalMap = new HashMap<>();
        for (PrincipalRepayment r : sortedRepayments) {
            principalMap.merge(r.getDate(), r.getAmount(), BigDecimal::add);
        }

        // 4. 按时间顺序处理事件
        for (LocalDate eventDate : eventDates) {
            // 计算从上次结息日到本次事件日期的利息（按日计息）
            long days = ChronoUnit.DAYS.between(lastInterestDate, eventDate);
            if (days > 0) {
                BigDecimal dailyRate = annualRate.divide(BigDecimal.valueOf(360), 10, RoundingMode.HALF_EVEN);
                BigDecimal periodInterest = remainingPrincipal.multiply(dailyRate).multiply(BigDecimal.valueOf(days))
                        .setScale(2, RoundingMode.HALF_EVEN);
                currentInterest = currentInterest.add(periodInterest);
            }

            // 判断事件类型
            boolean isInterestDate = interestDates.contains(eventDate);
            boolean isPrincipalDate = principalMap.containsKey(eventDate);
            boolean isMaturity = eventDate.equals(maturityDate);

            // 处理还本（先还本，再根据事件类型决定是否支付利息）
            BigDecimal principalRepaid = BigDecimal.ZERO;
            if (isPrincipalDate) {
                principalRepaid = principalMap.get(eventDate);
                // 还本金额不能超过剩余本金
                if (principalRepaid.compareTo(remainingPrincipal) > 0) {
                    principalRepaid = remainingPrincipal;
                }
                remainingPrincipal = remainingPrincipal.subtract(principalRepaid);
                // 记录还本条目（还本时不支付利息）
                schedule.add(new RepaymentScheduleEntry(eventDate, EntryType.PRINCIPAL_ONLY,
                        principalRepaid, BigDecimal.ZERO, remainingPrincipal));
            }

            // 处理还息（还息日或到期日支付所有累计利息）
            if (isInterestDate || isMaturity) {
                if (currentInterest.compareTo(BigDecimal.ZERO) > 0) {
                    // 如果既是还息日又是还本日且是到期日，则合并为还本+息类型
                    EntryType type = EntryType.INTEREST_ONLY;
                    BigDecimal principalForEntry = BigDecimal.ZERO;
                    if (isMaturity && remainingPrincipal.compareTo(BigDecimal.ZERO) > 0) {
                        // 到期日：同时偿还剩余本金
                        principalForEntry = remainingPrincipal;
                        remainingPrincipal = BigDecimal.ZERO;
                        type = EntryType.PRINCIPAL_AND_INTEREST;
                    } else if (isPrincipalDate && !isMaturity) {
                        // 非到期日同一天既有还本又有还息：分开记录更方便，但按规则还本日不还息，所以两个独立条目
                        // 我们已经记录了还本条目，现在只记录还息条目
                        type = EntryType.INTEREST_ONLY;
                    }
                    schedule.add(new RepaymentScheduleEntry(eventDate, type,
                            principalForEntry, currentInterest, remainingPrincipal));
                    currentInterest = BigDecimal.ZERO; // 利息清零
                } else if (isMaturity && remainingPrincipal.compareTo(BigDecimal.ZERO) > 0) {
                    // 到期日无应计利息但仍有本金（例如计划中已还完，理论上不会发生）
                    schedule.add(new RepaymentScheduleEntry(eventDate, EntryType.PRINCIPAL_AND_INTEREST,
                            remainingPrincipal, BigDecimal.ZERO, BigDecimal.ZERO));
                    remainingPrincipal = BigDecimal.ZERO;
                }
            }

            // 更新结息日为本事件日
            lastInterestDate = eventDate;
        }

        // 最终检查：如果剩余本金不为0，则补充一条到期记录（正常情况下应该已被处理）
        if (remainingPrincipal.compareTo(BigDecimal.ZERO) > 0) {
            schedule.add(new RepaymentScheduleEntry(maturityDate, EntryType.PRINCIPAL_AND_INTEREST,
                    remainingPrincipal, currentInterest, BigDecimal.ZERO));
        }

        // 按日期排序
        schedule.sort(Comparator.comparing(RepaymentScheduleEntry::getDate));
        return schedule;
    }

    /**
     * 生成还息日列表（不含到期日）
     */
    private static List<LocalDate> generateInterestDates(LocalDate startDate, LocalDate maturityDate,
                                                         InterestPeriod period, int defaultDay) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate firstDate = computeFirstInterestDate(startDate, period, defaultDay);

        if (firstDate == null || firstDate.isAfter(maturityDate)) {
            return dates; // 无有效还息日
        }

        int monthsStep = period.getMonths();
        boolean fixed = period.isFixed();

        LocalDate current = firstDate;
        while (current.isBefore(maturityDate)) {
            dates.add(current);
            if (fixed) {
                // 固定季/半年/年：下一个还息日是下一个固定周期月份的同一天
                current = nextFixedDate(current, period, defaultDay);
            } else {
                // 非固定：加上周期月数
                LocalDate next = current.plusMonths(monthsStep);
                // 调整到默认还款日（如20号，若不存在则取当月最后一天）
                current = adjustToPaymentDay(next, defaultDay);
            }
            // 避免无限循环
            if (current.equals(maturityDate) || current.isAfter(maturityDate)) {
                break;
            }
        }
        return dates;
    }

    /**
     * 计算首期还息日
     */
    private static LocalDate computeFirstInterestDate(LocalDate disbursalDate, InterestPeriod period, int defaultDay) {
        if (period.isFixed()) {
            // 固定季/半年/年
            List<Integer> targetMonths;
            switch (period) {
                case FIXED_QUARTERLY:
                    targetMonths = Arrays.asList(3, 6, 9, 12);
                    break;
                case FIXED_SEMI_ANNUALLY:
                    targetMonths = Arrays.asList(6, 12);
                    break;
                case FIXED_ANNUALLY:
                    targetMonths = Collections.singletonList(12);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported fixed period: " + period);
            }
            return computeFirstFixedDate(disbursalDate, targetMonths, defaultDay);
        } else {
            // 非固定
            LocalDate currentMonthDefault = adjustToPaymentDay(
                    LocalDate.of(disbursalDate.getYear(), disbursalDate.getMonth(), 1), defaultDay);
            if (disbursalDate.isBefore(currentMonthDefault)) {
                return currentMonthDefault;
            } else {
                LocalDate nextPeriodStart = disbursalDate.plusMonths(period.getMonths());
                return adjustToPaymentDay(nextPeriodStart, defaultDay);
            }
        }
    }

    /**
     * 固定周期的首期还款日计算
     */
    private static LocalDate computeFirstFixedDate(LocalDate disbursalDate, List<Integer> targetMonths, int defaultDay) {
        int year = disbursalDate.getYear();
        int month = disbursalDate.getMonthValue();
        // 找到当前日期所在的或之后的第一个目标月份
        for (int m : targetMonths) {
            if (m >= month) {
                LocalDate candidate = adjustToPaymentDay(LocalDate.of(year, m, 1), defaultDay);
                if (disbursalDate.isBefore(candidate)) {
                    return candidate;
                } else {
                    // 放款日当天或之后 -> 下一个周期的第一个目标月份
                    break;
                }
            }
        }
        // 下一个年份的第一个目标月份
        int nextYear = year + 1;
        int firstTargetMonth = targetMonths.get(0);
        return adjustToPaymentDay(LocalDate.of(nextYear, firstTargetMonth, 1), defaultDay);
    }

    /**
     * 固定周期的下一个还息日
     */
    private static LocalDate nextFixedDate(LocalDate current, InterestPeriod period, int defaultDay) {
        List<Integer> targetMonths;
        switch (period) {
            case FIXED_QUARTERLY:
                targetMonths = Arrays.asList(3, 6, 9, 12);
                break;
            case FIXED_SEMI_ANNUALLY:
                targetMonths = Arrays.asList(6, 12);
                break;
            case FIXED_ANNUALLY:
                targetMonths = Collections.singletonList(12);
                break;
            default:
                throw new IllegalArgumentException();
        }
        int currentMonth = current.getMonthValue();
        int currentYear = current.getYear();
        for (int m : targetMonths) {
            if (m > currentMonth) {
                return adjustToPaymentDay(LocalDate.of(currentYear, m, 1), defaultDay);
            }
        }
        // 下一年第一个目标月
        return adjustToPaymentDay(LocalDate.of(currentYear + 1, targetMonths.get(0), 1), defaultDay);
    }

    /**
     * 将日期调整为指定还款日，如果该月没有这一天则取月末最后一天
     */
    private static LocalDate adjustToPaymentDay(LocalDate date, int paymentDay) {
        int year = date.getYear();
        int month = date.getMonthValue();
        int lastDayOfMonth = LocalDate.of(year, month, 1).lengthOfMonth();
        int day = Math.min(paymentDay, lastDayOfMonth);
        return LocalDate.of(year, month, day);
    }

    // ================= 测试示例 =================
    public static void main(String[] args) {
        // 示例数据：2018年1月15日放款1W，期限5个月，年利率12%，默认还款日20号，按月还息，手工还本计划
        CalculationRequest request = CalculationRequest.builder()
                .disbursalDate(LocalDate.of(2018, 1, 15))
                .principal(new BigDecimal("10000"))
                .annualInterestRate(new BigDecimal("0.12"))
                .maturityDate(LocalDate.of(2018, 6, 15))
                .defaultPaymentDay(20)
                .interestPeriod(InterestPeriod.MONTHLY)
                .principalRepayments(Arrays.asList(
                        new PrincipalRepayment(LocalDate.of(2018, 2, 10), new BigDecimal("3000")),
                        new PrincipalRepayment(LocalDate.of(2018, 5, 15), new BigDecimal("2000")),
                        new PrincipalRepayment(LocalDate.of(2018, 6, 15), new BigDecimal("5000"))
                ))
                .build();

        List<RepaymentScheduleEntry> schedule = calculate(request);
        System.out.println("还款计划明细：");
        for (RepaymentScheduleEntry entry : schedule) {
            System.out.println(entry);
        }

        // 计算总利息
        BigDecimal totalInterest = schedule.stream()
                .map(RepaymentScheduleEntry::getInterest)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.printf("\n总利息: %.2f 元\n", totalInterest);
    }

}

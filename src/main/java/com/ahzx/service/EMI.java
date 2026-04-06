package com.ahzx.service;

/**
 * @author think
 * @date 2026年02月26日 17:48
 * Equal Monthly Installment （EMI）
 * 这是最普遍的缩写，特指每月还款金额固定。
 *
 * 例句：The loan is repaid on an EMI basis. （这笔贷款采用等额本息方式偿还。）
 */
public class EMI {
    //首期应还本金
    double firstPrincipalDue;

    //首期利息
    double firstInterest;
    //月供
    double monthlyPayment;
    /*
    *
     *
     * @date 2026/2/26 18:56
     * @param ratio 月利率
     * @param total 总期数
     * @param amount 贷款本金
     * @return double
     */
    public static double PMT(double ratio,int total,int amount){
        return amount * ratio * Math.pow( 1 + ratio,total)/ (Math.pow( 1 + ratio,total) - 1);
    }
    //15号
   //public List<Map<String,Object>> mortizationSchedule(){
   //
   //}

//    2018年1月15日发放贷款1W，期限3个月，默认还款日为21日，年利率12%，偿还方式为等额本息
public  void main(String[] args) {
    //System.out.println("Hello world!");
    monthlyPayment= PMT(0.054 / 12, 360, 1330000);
}


}

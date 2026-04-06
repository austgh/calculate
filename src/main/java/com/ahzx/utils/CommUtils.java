package com.ahzx.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommUtils {
	public static String YYYYMMDD = "yyyyMMdd";
	public static String HHMMSS = "HHmmss";
	
	public static String getDate(String format) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	public static String getDate() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	public static String getFormatMillis() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(date);
	}
	
	
	public static String getDate(int i) {
		Calendar calendar = Calendar.getInstance();
		Date date = new Date(System.currentTimeMillis());
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, i);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(calendar.getTime());
	}

	public static String getYear(int i) {
		Calendar calendar = Calendar.getInstance();
		Date date = new Date(System.currentTimeMillis());
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, i);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return sdf.format(calendar.getTime());
	}
	
	public static String getYear(Date date,int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, i);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return sdf.format(calendar.getTime());
	}
	
	public static String getYear() {
		long curtime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return sdf.format(curtime);
	}

	public static String getMonth(int i) {
		Calendar calendar = Calendar.getInstance();
		Date date = new Date(System.currentTimeMillis());
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, i);
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		return sdf.format(calendar.getTime());
	}
	
	public static String getMonth() {
		long curtime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		return sdf.format(curtime);
	}
	
	public static String getDay() {
		long curtime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		return sdf.format(curtime);
	}
	

	public static String getDate(String year, String month, String day) {
		int day1=Integer.parseInt(day);
		int month1=Integer.parseInt(month);
		if (month1==2) {
			int year1 = Integer.parseInt(year);
			if ((year1 % 4 == 0 && year1 % 100 != 0) || year1 % 400 == 0) {
				if (day1 > 29) {
					day = "29";
				}
			} else {
				if (day1 > 28) {
					day = "28";
				}
			}
		} else if (month1==1 ||month1==3 || month1==5 ||month1==7 || month1==8
				|| month1==10|| month1==12) {
			if (day1 > 31) {
				day = "31";
			}
		} else if (month1==4||month1==6 || month1==9|| month1==11) {
			if (day1 > 30) {
				day = "30";
			}
		}else{
			month="12";
			day="31";
		}
		return year + "-" + month + "-" + day;
	}
	
	public static String getDateStr(String year, String month, String day) {
		String str=year + "-" + month + "-" + day;
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		Date dateStr=null;
		try {
			dateStr=dateFormat.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateFormat.format(dateStr);
	}
	/**
	 * 根据追加/减少年月日个数，获取字符串日期
	 * @param i 年
	 * @param j 月
	 * @param k 日
	 * @return 字符串日期
	 */
	public static String getDate(int i, int j, int k) {
		Calendar calendar = Calendar.getInstance();
		Date date = new Date(System.currentTimeMillis());
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, i);
		calendar.add(Calendar.MONTH, j);
		calendar.add(Calendar.DAY_OF_MONTH, k);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(calendar.getTime());
	}

	/**
	 * 日期格式字符串转换成时间戳
	 * @param date_str 字符串日期
	 * @param format 如：yyyy-MM-dd HH:mm:ss
	 */
	public static long date2TimeStamp(String date_str, String format)  {
		long time = 0L;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			time =  sdf.parse(date_str).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}
 
	public static String tsToDate(long timestamp, String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date = new Date(timestamp);
		return sdf.format(date);
	}

	/**
	 * 从日期中抽取月份
	 */
	private static Set<Integer> getMonthsFromDate(List<Map<String, Object>> list, String cloumn){
		Set<Integer> set = new HashSet<>();
		for (Map<String, Object> temp : list) {
			String date = (String) temp.get(cloumn);
			String month = date.substring(4, 6);
			int mon = Integer.parseInt(month);
			set.add(mon);
		}
		return set;
	}
	
	/**
	 * 判断一个List集合是否为空
	 */
	public static boolean isEmptyList(List<?> list){
		return null == list || list.isEmpty();
	}
	/**
	 * 判断一个Map集合是否为空
	 */
	public static boolean isEmptyMap(Map<?, ?> map){
		return null == map || map.isEmpty();
	}
	
	/**
	 * 判断一个字符串是否为空
	 */
	public static boolean isEmptyStr(Object object){
		return null == object || "".equals(object);
	}
	
	
	public static String getDateByMonth(int i) {
		Calendar calendar = Calendar.getInstance();
		Date date = new Date(System.currentTimeMillis());
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, i);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		return sdf.format(calendar.getTime()) + "-01";
	}
	
	public static String getDateByMonth(Date date,int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, i);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		return sdf.format(calendar.getTime()) + "-01";
	}
	/**
	 * 获取指定日期前推i个月的第一天
	 * */
	public static String getFirstDateByMonth(Date date,int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, i);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		return sdf.format(calendar.getTime()) + "-01";
	}
	
	public static String getLastDateByMonth(int i){
		Calendar calendar=Calendar.getInstance();
		Date date = new Date(System.currentTimeMillis());
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, i+1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(calendar.getTime());
	}
	
	/**
	 * 获取指定日期前推i个月的最后一天
	 * */
	public static String getLastDateByMonth(Date date, int i){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, i);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(calendar.getTime());
	}
	
	public static String getDateOfMonth(Date date, int i){
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		String dateStr=dateFormat.format(date);
		Calendar calendar=Calendar.getInstance();
		try {
			Date dtDate=dateFormat.parse(dateStr);
			calendar.setTime(dtDate);
			calendar.add(Calendar.MONTH,i);	
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateFormat.format(calendar.getTime());
		
	}
	
	public static String getDateOfMonth(int i){
		Calendar calendar=Calendar.getInstance();
		Date date = new Date(System.currentTimeMillis());
		calendar.setTime(date);
		calendar.add(Calendar.MONTH,i);
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(calendar.getTime());
	}
	
	public static Date getDate(String dateStr,String format) {
		try {
			return new SimpleDateFormat(format).parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 计算两个日期差的年份
	 * @param startTime 开始时间
	 * @param endTime	结束时间
	 * @return 相差年份
	 */
	public static int getYearsBetween(Date startTime,Date endTime) {
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(startTime);
		end.setTime(endTime);
		return (end.get(Calendar.YEAR)-start.get(Calendar.YEAR));

	}
	/**
	 * 计算两个日期差的年份
	 * @param startTime 开始时间
	 * @param endTime	结束时间
	 * @return 相差年份
	 */
	public static int getYearsBetween(String startTime,String endTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		
		try {
			return getYearsBetween(sdf.parse(startTime),sdf.parse(endTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1;
	}
	/**
	 * 取得日期类型为yyyyMM的日期距离现在的月数，
	 * 如果为空取0
	 */
	public static int getMonthNearNow(String dateStr) {
		if(isEmptyStr(dateStr)) return 0;
		Calendar now = Calendar.getInstance();
		Calendar start = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyyMM").parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		assert date != null;
		start.setTime(date);
		return (now.get(Calendar.YEAR)-start.get(Calendar.YEAR))*12+now.get(Calendar.MONTH)-start.get(Calendar.MONTH);
	}

	
	/**
	 * 最大逾期计算
	 * @return 最大的数字
	 */
	public static int getMaxChar(String str) {
		int max = 0;
		if(isEmptyStr(str)) return max;
		char[] loanOverDueArray = str.toCharArray();
		for (char c : loanOverDueArray) {
			String cString = String.valueOf(c);
			if(cString.matches("^[1-9]$")){
				max = Math.max(Integer.parseInt(cString), max);
			}
		}
		return max;
	}
	
	/**
	 * 多条记录最大逾期期数
	 */
	public static int getMaxChar(List<String> strs) {
		int max = 0;
		for (String string : strs) {
			int maxstr = getMaxChar(string);
			max = Math.max(maxstr, max);
		}
		return max;
	}
	
	/**
	 * 累计逾期次数计算
	 * @return 累计次数
	 */
	public static int getOverCount(String str){
		int count = 0;
		if(isEmptyStr(str)) return count;
		char[] loanOverDueArray = str.toCharArray();
		for (char c : loanOverDueArray) {
			String cString = String.valueOf(c);
			if(cString.matches("^[1-9]$")){
				count++;
			}
		}
		return count;
	}
	/**
	 * 多条记录逾期总次数
	 */
	public static int getOverCount(List<String> strs){
		int count = 0;
		for (String string : strs) {
			count = count + getOverCount(string);
		}
		return count;
	}

	/**
	 * 判断两个月份相差几个月，日期格式为yyyyMMdd
	 */
    public static int monthApart(String month1,String month2){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		try {
				Calendar bef=Calendar.getInstance();
				bef.setTime(sdf.parse(month1));
				Calendar aft=Calendar.getInstance();
				aft.setTime(sdf.parse(month2));
				int result=aft.get(Calendar.MONTH)-bef.get(Calendar.MONTH);
				int month=(aft.get(Calendar.YEAR)-bef.get(Calendar.YEAR))*12;
				return Math.abs(month+result);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		return 0;
	}
    /**
     * 判断数组中连续的0的个数是否大于等于times
     */
	public static Boolean ArrayISContinuationTimes(int[] array,int times){
		int count=0;
		for (int j : array) {
			if (j == 0) {
				count++;
				if (count >= times) {
					return true;
				}
			} else {
				count = 0;
			}
		}
		return false;
	}

	public static String getEaryMonth(int i) {
		Calendar calendar = Calendar.getInstance();
		Date date = new Date(System.currentTimeMillis());
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, i);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(calendar.getTime());
	}
    /**
     * 去除List中重复数据
     */
	public static List<String> removeDuplicate(List<String> list) {
		return new ArrayList<>(new HashSet<>(list));
	}
}

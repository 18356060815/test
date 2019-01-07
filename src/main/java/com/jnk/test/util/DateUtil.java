package com.jnk.test.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtil {
	public static String YYYY = "yyyy";
	public static String YYYY_MM = "yyyy-MM";
	public static String YYYY_MM_DD = "yyyy-MM-dd";
	public static String YYYY_MM_DD_HH_mm = "yyyy-MM-dd HH:mm";
	public static String YYYY_MM_DD_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public static String MM_DD_HH_mm_ss = "MM-dd HH:mm:ss";
	public static String HH_mm_ss = "HH:mm:ss";
	public static String HH_mm = "HH:mm";
	public static String DD = "dd";
	public static String mm = "mm";
	public static String ss = "ss";
	public static String YYYYMMDD = "yyyyMMdd";
	public static String YYYYMMDDHH = "yyyyMMddHH";
	public static String YYYYMMDDHHmm = "yyyyMMddHHmm";
	public static String YYYYMMDDHHmmss = "yyyyMMddHHmmss";
	public static String YYYYMMDDHHmmssSSS = "yyyyMMddHHmmssSSS";
	public static String YYYY_MM_DD_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss:SSS";
	public static String HHmmssSSS = "HHmmssSSS";
	public static String SSS = "SSS";
	public static String YMD_ZH = "yyyy年MM月dd日";
	public static String YM_ZH = "yyyy年MM月";

	public static String ForDate(Date date, String type) {
		SimpleDateFormat format = new SimpleDateFormat(type);
		return format.format(date);
	}

	public static Date StringToDate(String date, String type) {
		SimpleDateFormat format = new SimpleDateFormat(type);
		try {
			return format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int DateBetween(Date begin, Date end, String returnType) {
		long between = (end.getTime() - begin.getTime()) / 1000;
		long returnDate;
		if (returnType.equals("day")) {
			returnDate = between / (24 * 3600);
		} else if (returnType.equals("hour")) {
			returnDate = between / 3600;
		} else if (returnType.equals("minute")) {
			returnDate = between / 60;
		} else {
			returnDate = between;
		}
		return Math.abs((int) returnDate);
	}

	public static Date getDateAfterOrBefore(Date date, int type, int deff) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(type, deff);
		return calendar.getTime();
	}

	// 传入一个时间字符串，计算与今天的时间差，返回时间差，并且做分钟，小时，天，月判断
	public static Map<String, Object> timeBetweenNow(Date beginDate) {
		Map<String, Object> map = new HashMap<String, Object>();
		long time;
		String timeTag = "";
		Date nowDate = new Date();
		long diff = nowDate.getTime() - beginDate.getTime();
		time = diff / (1000 * 60);
		if (time > 60) {
			time = time / 60;
			if (time > 24) {
				time = time / 24;
				if (time > 30) {
					time = time / 30;
					timeTag = "月";
				} else {
					timeTag = "天";
				}
			} else {
				timeTag = "小时";
			}
		} else {
			timeTag = "分钟";
		}
		if (time == 0) {
			timeTag = "刚刚";
			map.put("timeTag", timeTag);
		} else {
			map.put("time", time);
			map.put("timeTag", timeTag);
		}
		return map;
	}
	
	public static String timeBetweenNowReturnStr(Date beginDate) {
		Map<String, Object> timeBetweenNow =timeBetweenNow(beginDate);
		String time_between_now = "";
		if (timeBetweenNow.get("time") == null) {
			time_between_now = timeBetweenNow.get("timeTag") + "";
		} else {
			time_between_now = timeBetweenNow.get("time") + "" + timeBetweenNow.get("timeTag") + "前";
		}
		return time_between_now;
	}

	public static Date getMonthFirstDay() {
		return StringToDate(ForDate(new Date(), YYYY_MM) + "-01", YYYY_MM_DD);
	}

	public static String getNowTimeStamp() {
		return new Date().getTime() + "";
	}

	
	  /* 
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }


	/*
	 * 将时间戳转换为时间
	 */
	public static String stampToDate(String s,boolean flag){
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lt = new Long(s);
		if(flag){
			lt=lt*1000;
		}
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}
    //时间加减
	public   static  String  getOneHoursAgoTime (String datype ,int times) {
		String oneHoursAgoTime =  "" ;
		int CalendarType=10;
		Calendar cal = Calendar. getInstance ();
		if(datype.equals("HOUR")){
			CalendarType=Calendar. HOUR;
		}
		else if(datype.equals("MINUTE")){
			CalendarType=Calendar.MINUTE;
		}

		else if(datype.equals("SECOND")){
			CalendarType=Calendar.SECOND;
		}

		else if(datype.equals("YEAR")){
			CalendarType=Calendar.YEAR;
		}
		else if(datype.equals("MONTH")){
			CalendarType=Calendar.MONTH;
		}
		else if(datype.equals("DATE")){
			CalendarType=Calendar.DATE;
		}
		else if(datype.equals("WEDNESDAY")){
			CalendarType=Calendar.WEDNESDAY;
		}
		cal.add(CalendarType, -times);
		//cal.set(CalendarType , CalendarType -(times) ) ; //把时间设置为当前时间-x小时，同理，也可以设置其他时间
		oneHoursAgoTime =  new  SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format(cal.getTime());//获取到完整的时间
		return  oneHoursAgoTime;
	}
	//获取年数
	public static String getSysYear() {
		Calendar date = Calendar.getInstance();
		String year = String.valueOf(date.get(Calendar.YEAR));
		return year;
	}

	//链得得
	public static  String getLiandedeLater(String later){
		String year="";
		if(!later.contains("年")){
			year=DateUtil.getSysYear();
		}
		String times=year+"-"+later.replace("月","-").replace("日","");
		if(!"".equals(later)&&later!=null){
			if(later.contains("分钟前")){
				later=later.replace("分钟前","");
				times=getOneHoursAgoTime("MINUTE",Integer.parseInt(later));

			}else  if(later.contains("小时前")){
				later=later.replace("小时前","");
				times=getOneHoursAgoTime("HOUR",Integer.parseInt(later));

			}else if(later.contains("昨天")){
				times=getOneHoursAgoTime("HOUR",12);

			}
		}
		return  times;
	}


	public static  String getHecaijingLater(String later){
		String times="";
		if(!"".equals(later)&&later!=null){
			 if(later.contains("秒前")){
				 later=later.replace("秒前","");
				 times=getOneHoursAgoTime("SECOND",Integer.parseInt(later));

			}
			else if(later.contains("分钟前")){
				 later=later.replace("分钟前","");
				 times=getOneHoursAgoTime("MINUTE",Integer.parseInt(later));

			}
			else if(later.contains("小时前")){
				later=later.replace("小时前","");
				times=getOneHoursAgoTime("HOUR",Integer.parseInt(later));


			}else if(later.contains("天前")){
				 later=later.replace("天前","");
				 times=getOneHoursAgoTime("DATE",Integer.parseInt(later));


			 }else if(later.contains("周前")){
				 later=later.replace("周前","");
				 times=getOneHoursAgoTime("WEDNESDAY",Integer.parseInt(later));


			 }else if(later.contains("月前")){
				 later=later.replace("月前","");
				 times=getOneHoursAgoTime("MONTH",Integer.parseInt(later));

			 }
			 else if(later.contains("年前")){
				 later=later.replace("年前","");
				 times=getOneHoursAgoTime("YEAR",Integer.parseInt(later));

			 }else {
			 	return later;
			 }
		}
		return  times;
	}



	public static void main(String[] args) throws ParseException {
		System.out.println(stampToDate("1542769958",true));
	}
}
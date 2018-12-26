package com.jnk.test.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CheckUtil {
    private static final Logger logger = LoggerFactory.getLogger(CheckUtil.class);


    public static String getTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }

    //判断不是分类标签的标签  但是属于推荐的新闻内容
    public static Boolean Sign(String types) {
        List list=new ArrayList();
        list.add("最新");
        list.add("头条");
        list.add("推荐");
        return  list.contains(types);
    }


    public static String getYYYY_MM_DD(int jday) {
        return dateToString(new Date(), jday).substring(0, 10);

    }
//       public static int[] getHms(){
//        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
//        Date date = new Date();
//        String x=format.format(date);
//        int [] s={Integer.parseInt(x.split(":")[0]),Integer.parseInt(x.split(":")[1])};
//        return s;
//       }




    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {

        return nowTime.getTime() >= beginTime.getTime() && nowTime.getTime() <= endTime.getTime();
    }


    public static String dateToString(Date time, int jday) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ctime = formatter.format(new Date(time.getTime() - jday * 24 * 60 * 60 * 1000L));
        return ctime;
    }


    public static String dateToString(Date time) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ctime = formatter.format(time);
        return ctime;
    }

    public static String dateToString(String  time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String  dBegin=null;
        try{
           dBegin = sdf.format(sdf.parse(time));
        }catch (Exception e){
            e.printStackTrace();
        }
        return dBegin;
    }

    public static String dateformat(String time) {
        time = time.replace("年", "-").replace("月", "-").replace("日", "");
        return time;
    }

    public static void sleep(int time) {
        try {
            new Thread().sleep(time);
        } catch (Throwable e) {
            logger.error(e.getMessage());
        }
    }


    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s,boolean is_1000){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        if(is_1000){
            lt=lt*1000;
        }
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }


    public static String addTime(Date date,int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE,minute);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /*try {
            Date date1 = sdf.parse(sdf.format(calendar.getTime()));
            return  date1;
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        return sdf.format(calendar.getTime());
    }

    public static void main(String[] args) {
        System.out.println(stampToDate("1542162345",true));
    }


}





















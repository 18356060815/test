package com.jnk.test.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CheckUtil {
    private static final Logger logger = LoggerFactory.getLogger(CheckUtil.class);



    public static String getTrace(Throwable t) {
        StringWriter stringWriter= new StringWriter();
        PrintWriter writer= new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer= stringWriter.getBuffer();
        return  buffer.toString();
    }

    public static String Sign(String dg){
        String dgstatus="";
        if(dg.equals("u-cir")){
            dgstatus="1";//非单关
        }else if(dg.equals("u-dan")){
            dgstatus="2";//单关
        }else  if(dg.equals("u-kong")){
            dgstatus="0";//未开售此玩法
        }
        else if(dg.equals("")){
            dgstatus="0";//未开售.不确定
        }
        return  dgstatus;
    }

    public static int SignWeek(String weekcn){
        int week=0;
        if(weekcn.equals("周一")||weekcn.equals("星期一")){
            week=1;
        }else if(weekcn.equals("周二")||weekcn.equals("星期二")){
            week=2;
        }else if(weekcn.equals("周三")||weekcn.equals("星期三")){
            week=3;
        }else if(weekcn.equals("周四")||weekcn.equals("星期四")){
            week=4;
        }else if(weekcn.equals("周五")||weekcn.equals("星期五")){
            week=5;
        }else if(weekcn.equals("周六")||weekcn.equals("星期六")){
            week=6;
        }else if(weekcn.equals("周日")||weekcn.equals("星期日")){
            week=7;
        }
        return  week;
    }




    public static String getYYYY_MM_DD(int jday){
        return dateToString(new Date(),jday).substring(0,10);

    }
//       public static int[] getHms(){
//        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
//        Date date = new Date();
//        String x=format.format(date);
//        int [] s={Integer.parseInt(x.split(":")[0]),Integer.parseInt(x.split(":")[1])};
//        return s;
//       }


    /**
     * 获取当前日期是星期几<br>
     *
     * @param
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Boolean isMatch) {
        String[] weekDays = {"7", "1", "2", "3", "4", "5", "6"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        System.out.println(w);
        if(isMatch){//足球
            if( weekDays[w].equals("1")||weekDays[w].equals("2")||weekDays[w].equals("3")||weekDays[w].equals("4")||weekDays[w].equals("5")){
                return "1";
            }else{
                return "0";
            }
        }else {//篮球
            if( weekDays[w].equals("1")||weekDays[w].equals("2")||weekDays[w].equals("5")){//竞彩篮球销售时间：
                // 周一/二/五 9:00-24:00;  1
                // 周三/四 07:30 - 24:00;  2
                // 周六/日 9:00 - 次日01:00。 0
                return "1";
            }else if( weekDays[w].equals("3")||weekDays[w].equals("4")){
                return "2";
            }else {
                return "0";
            }
        }

    }
    //判断当天可售时间段内
    public static Boolean fbisBelong(String  beginmorTime,String endmorTime){

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
        Date now =null;
        Date beginTime = null;
        Date endTime = null;
        try {
            now = df.parse(df.format(new Date()));
            beginTime = df.parse(beginmorTime);
            endTime = df.parse(endmorTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Boolean flag = belongCalendar(now, beginTime, endTime);
        return flag;
    }
    //判断当天可售 并且跨天的时间段 内
    public static Boolean isAfterBelong(String  beginmorTime,String endmorTime){

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
        Date now =null;
        Date beginTime = null;
        Date endTime = null;
        try {
            Format f = new SimpleDateFormat("yyyy-MM-dd");
            Date today = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(today);
            c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天
            Date tomorrow = c.getTime();
            System.out.println("明天是:" + f.format(tomorrow));

            now = df.parse(df.format(new Date()));


            if(Integer.parseInt(df.format(now).split(" ")[1].split(":")[0])<6){
                Calendar c1 = Calendar.getInstance();
                c1.setTime(today);
                c1.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天
                now = df.parse(df.format(c1.getTime()));
                System.out.println("凌晨6时以前今天是 : "+now);
            } else if(Integer.parseInt(df.format(now).split(" ")[1].split(":")[0])>Integer.parseInt(beginmorTime.split(":")[0])){
                now = df.parse(df.format(new Date()));

            }

            beginTime = df.parse(f.format(today)+" "+beginmorTime);
            endTime = df.parse(f.format(tomorrow)+" "+endmorTime);

            System.out.println("今   天 "+df.format(now));
            System.out.println("开始时间 "+df.format(beginTime));
            System.out.println("结束时间 "+df.format(endTime));


        } catch (Exception e) {
            e.printStackTrace();
        }

        Boolean flag = belongCalendar(now, beginTime, endTime);
        return flag;
    }


    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
       
        return nowTime.getTime() >= beginTime.getTime() && nowTime.getTime() <= endTime.getTime();
    }








    public static String dateToString(Date time,int jday){
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ctime = formatter.format(new Date(time.getTime() - jday * 24 * 60 * 60 * 1000L));
        return ctime;
    }


    public static String dateToString(Date time){
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyyMMdd");
        String ctime = formatter.format(time);
        return ctime;
    }

    public static String dateformat(String  time){
        time=time.replace("年","-").replace("月","-").replace("日","");
        return time;
    }

    public  static  void sleep(int time){
        try {
            new Thread().sleep(time);
        }catch (Throwable e){
            logger.error(e.getMessage());
        }
    }

    public static void main(String [] args) throws Exception{

       System.out.println(CheckUtil.isAfterBelong("09:00","03:00"));
    }
}





















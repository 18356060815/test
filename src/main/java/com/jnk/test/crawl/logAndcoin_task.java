//package com.jnk.test.crawl;
//
//import com.jnk.test.Service.DBUtil;
//import com.jnk.test.util.DateUtil;
//import com.jnk.test.util.HttpClientUtilPro;
//import net.sf.json.JSON;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import org.apache.commons.lang.StringUtils;
//import org.apache.http.conn.ConnectTimeoutException;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.net.SocketTimeoutException;
//import java.util.Calendar;
//import java.util.Date;
//
//public class logAndcoin_task {
//    private static final Logger logger = LoggerFactory.getLogger(logAndcoin_task.class);
//
//    @Autowired
//    DBUtil dbUtil;
//    public void clearLogData() {
//        logger.info("定时清理日志任务开始");
//        Date logDate = DateUtil.getDateAfterOrBefore(new Date(), Calendar.DATE, -1);
//        String date_str = DateUtil.ForDate(logDate, DateUtil.YYYYMMDD);
//        dbUtil.execute("drop table if exists admin_log_" + date_str);
//        dbUtil.execute("drop table if exists interface_log_" + date_str);
//        dbUtil.execute("create table admin_log_" + date_str + " as SELECT * from admin_user_log");
//        dbUtil.execute("TRUNCATE table admin_user_log");
//        dbUtil.execute("create table interface_log_" + date_str + " as SELECT * from interface_request_log");
//        dbUtil.execute("TRUNCATE table interface_request_log");
//        for (int i = 4; i <= 7; i++) {
//            String del_date_str = DateUtil.ForDate(DateUtil.getDateAfterOrBefore(new Date(), Calendar.DATE, -i),
//                    DateUtil.YYYYMMDD);
//            dbUtil.execute("drop table if exists admin_log_" + del_date_str);
//            dbUtil.execute("drop table if exists interface_log_" + del_date_str);
//        }
//        logger.info("定时清理日志任务结束");
//
//		/*logger.info("设置最后价格开始");
//		Calendar cal = Calendar.getInstance();
//		Date now = cal.getTime();
//		String yesterday_str = DateUtil.ForDate(DateUtil.getDateAfterOrBefore(now, Calendar.DATE, -1), DateUtil.YYYY_MM_DD);
//		//设置每天最后价格
//		setIs_day_last(yesterday_str);
//		//周一是第二天
//		if(cal.get(Calendar.DAY_OF_WEEK) == 2) {
//			//设置每周最后价格
//			setIs_week_last(yesterday_str);
//		}
//		if(cal.get(Calendar.DAY_OF_MONTH) == 1) {
//			//设置每月最后价格
//			setIs_month_last(yesterday_str);
//		}
//		if(cal.get(Calendar.DAY_OF_YEAR) == 1) {
//			//设置每年最后价格
//			setIs_year_last(yesterday_str);
//		}
//		logger.info("设置最后价格结束");*/
//    }
//
////    //获取币的交易量，涨跌幅
////    private static final String COINMARKET_TICKER_URL = "https://api.coinmarketcap.com/v2/ticker/?start=&limit=100&sort=id&structure=array";
////    /**
////     * 获取币的最新数据， 更新或插入virtual_currency_info表， 插入virtual_currency_history_price表
////     */
////    @Test
////    public void cralVirtualCcurrencyMarket() {
////        logger.info("获取COINMARKET_TICKER_URL接口数据开始");
////                String jsonData=HttpClientUtilPro.httpGetRequest(COINMARKET_TICKER_URL,1);
////
////                JSONObject jsonObjects = JSONObject.fromObject(jsonData);
////                JSONArray dataArrys = jsonObjects.getJSONArray("data");
////                System.out.println(dataArrys);
////                for (int i = 0; i < dataArrys.size(); i++) {
////                    JSONObject jsonObject = dataArrys.getJSONObject(i);
////                    String id = jsonObject.getString("id");//id
////                    String name = jsonObject.getString("name");//名字
////                    String symbol = jsonObject.getString("symbol");//简称
////                    String rank = jsonObject.getString("rank");//排名
////                    // String circulating_supply = jsonObject.getString("circulating_supply");
////                    // String total_supply = jsonObject.getString("total_supply");
////                    // String max_supply = jsonObject.getString("max_supply");
////                    JSONObject quotes = jsonObject.getJSONObject("quotes");
////                    JSONObject USD = quotes.getJSONObject("USD");
////                    String price = USD.getString("price");//交易量
////                    String volume_24h = USD.getString("volume_24h");
////                    String market_value = USD.getString("market_cap");
////
////
////                    String percent_change_1h = USD.getString("percent_change_1h");
////                    String percent_change_24h = USD.getString("percent_change_24h");
////                    String percent_change_7d = USD.getString("percent_change_7d");
////
////                    market_value = market_value == null ? "0" : market_value;
////                    market_value=Double.valueOf(market_value).longValue()+"";
////
////                    percent_change_1h = percent_change_1h == null ? "0" : percent_change_1h;
////                    percent_change_1h=Double.valueOf(percent_change_1h)+"";
////
////                    percent_change_24h = percent_change_24h == null ? "0" : percent_change_24h;
////                    percent_change_24h=Double.valueOf(percent_change_24h)+"";
////                    percent_change_7d = percent_change_7d == null ? "0" : percent_change_7d;
////                    percent_change_7d=Double.valueOf(percent_change_7d)+"";
////
////                    if(StringUtils.isNotBlank(volume_24h)) {
////                        volume_24h = volume_24h == null ? "0" : volume_24h;
////                        volume_24h=Double.parseDouble(volume_24h)+"";
////                    }else {
////                        volume_24h=0.0+"";
////                    }
////                    if (StringUtils.isNotBlank(price)) {
////                        price = price == null ? "0.0" : price;
////                        Double pri = Double.parseDouble(price);
////                        pri = vaildNumber(pri);
////                        String Now_price=pri+"";
////                    }
////
////                    boolean flag=dbUtil.findById(id);
////                    if(flag){
////                        dbUtil.execute("insert into virtual_currency_info " +
////                                "(`id`,`status`,`name`,`name_cn`,`symbol`,`website_addr`,`description`,`now_price`,`now_price_str`,`percent_change_now`," +
////                                "`percent_change_1h`,`percent_change_24h`,`percent_change_7d`,`market_value`,`volume_24h`,`price_from`,`is_default`,`sort_num`,`update_time`)" +
////                                " values " +
////                                "('"+id+"','up','"+name+"','"+name_cn+"','"+symbol+"','"+website_addr+"','"+name+"','"+name+"','"+name+"');");
////                    }else {
////                        dbUtil.execute("update");
////                    }
////                }
////
////
////        logger.info("获取COINMARKET_TICKER_URL接口数据结束");
////    }
////
////    public double vaildNumber(double price) {
////        String[] strs = String.valueOf(price).split("\\.");
////        if(strs.length > 0 && Long.valueOf(strs[0]) > 100 ) {
////            BigDecimal bg = new BigDecimal(price);
////            double result =  bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
////            return result;
////        }if(strs.length > 1 && Long.valueOf(strs[0]) == 0  && Long.valueOf(strs[1]) == 0){
////            return 0.0;
////        }else{
////            double d = Math.ceil(Math.log10(price < 0 ? -price: price));
////            int power = 4 - (int) d;
////            double magnitude = Math.pow(10, power);
////            long shifted = Math.round(price*magnitude);
////            double result = shifted/magnitude;
////            java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
////            nf.setGroupingUsed(false);
////            return result;
////        }
////    }
//     public static void main(String[] args) {
//
//
//
//
//    }
//
//
//
//}

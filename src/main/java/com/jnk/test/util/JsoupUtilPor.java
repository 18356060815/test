package com.jnk.test.util;

import net.sf.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.*;
import java.util.Map;
import static com.jnk.test.util.CheckUtil.getTrace;

public class JsoupUtilPor {
    private static final Logger logger = LoggerFactory.getLogger(JsoupUtilPor.class);

    private static String User_Agent="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36";
    private static int  TIME_OUT=8000;



    public  static Document getcharset(String url,String charset){
        Document document;
        try {
            Jsoup.connect(url).get();
            document=Jsoup.parse(new URL(url).openStream(), charset, url);
        }catch (Throwable e){
            logger.error(getTrace(e));
            CheckUtil.sleep(3000);
            document=getcharset(url,charset);
        }

        return document;

    }
    public  static Document get(String url){
        Document document;
         try {
             document=Jsoup.connect(url).header("User-Agent",User_Agent).timeout(TIME_OUT).get();
         }catch (Throwable e){
             logger.error(getTrace(e));
             CheckUtil.sleep(3000);
             document=get(url);

         }
        return document;

    }
    public  static Document post(String url,Map map){//
        Document document;
        try {
            document=Jsoup.connect(url).header("User-Agent",User_Agent).timeout(TIME_OUT).data(map).post();
        }catch (Throwable e){
            logger.error(getTrace(e));
            CheckUtil.sleep(3000);
            document=post(url,map);

        }
        return document;

    }

    public  static Document aokeget(String url,String Referer){
        Document document;
        try {
            document=Jsoup.connect(url).timeout(TIME_OUT)
                    .header("User-Agent",User_Agent)
                    .header("Host","www.okooo.com")
                    .header("Accept-Language","zh-CN,zh;q=0.8")
                    .header("Referer",Referer)
                    .header("Connection","keep-alive")
                    .get();
        }catch (Throwable e){
            logger.error(getTrace(e));
            CheckUtil.sleep(3000);
            return null;
        }
        return document;

    }

    public  static String  ignoreGet(String url){
        String s;
        try {
            s=Jsoup.connect(url).timeout(TIME_OUT)
                    .ignoreContentType(true).get().body().text().toString();
        }catch (Throwable e){
            logger.error(getTrace(e));
            CheckUtil.sleep(3000);
            s=ignoreGet(url);
        }
        return s;
    }
    public  static JSONObject ignoreGet(String url,String back){
        Document document;
        JSONObject jsonObject;
        try {
             document = Jsoup.connect(url).timeout(TIME_OUT)
                    .ignoreContentType(true)
                    .header("Accept","*/*")
                    .header("User-Agent", User_Agent)
                    .get();
             jsonObject=JSONObject.fromObject(document.body().text().replace(back,"").replace(");",""));

        }catch (Throwable e){
            logger.error(getTrace(e));
            CheckUtil.sleep(3000);
            jsonObject=ignoreGet(url,back);
        }
        return jsonObject;
    }

    public static void main(String[] args) {
        String s=get("http://webapi.http.zhimacangku.com/getip?num=1&type=2&pro=&city=0&yys=0&port=1&time=1&ts=0&ys=0&cs=0&lb=1&sb=0&pb=4&mr=1&regions=").text();
        JSONObject jsonObject=JSONObject.fromObject(s);
        System.out.println(jsonObject);
    }


}

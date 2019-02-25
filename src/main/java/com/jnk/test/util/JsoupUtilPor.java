package com.jnk.test.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.jnk.test.util.CheckUtil.getTrace;

public class JsoupUtilPor {
    private static final Logger logger = LoggerFactory.getLogger(JsoupUtilPor.class);

    private static String User_Agent="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36";
    private static int  TIME_OUT=8000;


    public  static Document get(String url,int requstCount){
        if(requstCount==3){
            return null;
        }
        Document document;
         try {
             requstCount++;
             Connection.Response response =Jsoup.connect(url).header("User-Agent",User_Agent).timeout(TIME_OUT).execute();
             int statusLine=response.statusCode();
             if(statusLine!=200) {
                 CheckUtil.sleep(2000);
                 document = get(url, requstCount);

             }else {
                 document=response.parse();
             }
         }catch (Throwable e){
             logger.error(getTrace(e));
             CheckUtil.sleep(2000);
             document=get(url,requstCount);

         }
        return document;

    }

    public  static Document ignoreget(String url,int requstCount){
        if(requstCount==3){
            return null;
        }
        Document document;
        try {
            requstCount++;
            Connection.Response response =Jsoup.connect(url).ignoreContentType(true).header("User-Agent",User_Agent).timeout(TIME_OUT).execute();
            int statusLine=response.statusCode();
            if(statusLine!=200) {
                CheckUtil.sleep(2000);
                document = get(url, requstCount);

            }else {
                document=response.parse();
            }
        }catch (Throwable e){
            logger.error(getTrace(e));
            CheckUtil.sleep(2000);
            document=get(url,requstCount);

        }
        return document;

    }

    public  static Document post(String url,Map map) {//
        Document document;
        try {
            document = Jsoup.connect(url).header("User-Agent", User_Agent).timeout(TIME_OUT).data(map).post();
        } catch (Throwable e) {
            logger.error(getTrace(e));
            CheckUtil.sleep(3000);
            document = post(url, map);

        }
        return document;

    }


    public static void main(String[] args) {
    }


}

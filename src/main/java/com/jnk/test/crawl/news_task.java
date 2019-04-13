package com.jnk.test.crawl;

import com.jnk.test.util.DateUtil;
import com.jnk.test.util.HttpClientUtilPro;
import com.jnk.test.util.JsoupUtilPor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.Date;

/********************************************************************old********************************************************************************************/


public class news_task {
    //readhub 获取资讯
    private static final String READHUB_URL = "https://api.readhub.me/blockchain?lastCursor=&pageSize=10";
    final static  int  RequestCount=1;


    //readhub 获取资讯
    @Test
    public  void getReadHub() throws InterruptedException {
        String jsondata=HttpClientUtilPro.httpGetRequest(READHUB_URL,RequestCount);
        System.out.println(jsondata);
        JSONObject jsonObject1=JSONObject.fromObject(jsondata);
        JSONArray dataArrys=jsonObject1.getJSONArray("data");

        for (int i = 0; i < dataArrys.size(); i++) {
            JSONObject jsonObject = dataArrys.getJSONObject(i);
            String from_site = jsonObject.getString("siteName");//来源  作者
            String summary = jsonObject.getString("summary");//简介
            String dateStr = jsonObject.getString("publishDate").replace("T", " ").substring(0, 19);//
            Date publish_time = DateUtil.StringToDate(dateStr, DateUtil.YYYY_MM_DD_HH_mm_ss);
            String title = jsonObject.getString("title");
            String href_addr = jsonObject.getString("url");

            //....
        }


    }











}

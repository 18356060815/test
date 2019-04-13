package com.jnk.test.crawl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jnk.test.Service.DBUtil;
import com.jnk.test.util.DateUtil;
import com.jnk.test.util.HttpClientUtilPro;
import com.jnk.test.util.JsoupUtilPor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Service
public class DownLoadvideo_task {
    final  static  String rootpath="";
    final  static  int httpGetRequest=1;

    @Autowired
    DBUtil dbUtil;
     @Test
     public void gethuoxingvideo() throws Exception{
         Map map=new HashMap();
         map.put("Type","15");
         map.put("PageIndex","1");
         map.put("PageSize","10");

         String data = HttpClientUtilPro.httpPostRequest("https://www.ihuoqiu.com/MAPI/GetArticleListData",map, httpGetRequest);
         data = JSON.parseObject(data, String.class);
         com.alibaba.fastjson.JSONObject jsonObjects = JSON.parseObject(data);
         com.alibaba.fastjson.JSONArray data1 = jsonObjects.getJSONArray("data");
         for(Object jsonObject:data1){
             JSONObject jsonObject1 = JSONObject.parseObject(jsonObject.toString());
             System.out.println(jsonObject1);
             Map map1=new HashMap();
             map1.put("Type","2");
             map1.put("data",jsonObject1.getString("data1"));
             String videodata = HttpClientUtilPro.httpPostRequest("https://www.ihuoqiu.com/MAPI/GetArticleInfoData", map1, httpGetRequest);
             JSONObject videojson = JSON.parseObject(videodata);
             JSONObject data2 = videojson.getJSONObject("data");
             System.out.println(data2);
             String title=data2.getString("Title");//标题
             System.out.println("标题 : " + title);

             String author=data2.getString("Author");//作者
             System.out.println("作者 : " + author);

             String pic_url=data2.getString("ImgUrl");//图片
             System.out.println("图片 : " + pic_url);

             String summary=data2.getString("ShortDescription");//简介
             System.out.println("简介 : " + summary);

             String PublishTime=data2.getString("UpdateTime");//时间
             System.out.println("时间 : " + PublishTime);

             String from_site = data2.getString("Souce");//资源
             System.out.println("资源 : " + from_site);

             String from_interface = "火球财经";//来源
             System.out.println("来源 : " + from_interface);

             String AudioAndVideoUrl=data2.getString("AudioAndVideoUrl");//视频地址
             System.out.println("视频地址 : " + AudioAndVideoUrl);

             HttpClientUtilPro.sendvedio(AudioAndVideoUrl,"F://"+data2.getString("ID")+".mp4");


         }

     }


    //核财经视频
     @Test
     public void  gethecaijingVedio(){
             Document document = JsoupUtilPor.get("https://www.hecaijing.com/shipin/", httpGetRequest);
             String type=document.select("ul.sub-head").select("a.active").attr("type");

             String data = HttpClientUtilPro.httpGetRequest("https://www.hecaijing.com/index/loadmore?type=" + type + "&pn=" + 1, httpGetRequest);
             System.out.println(data);
             JSONObject jsonObject = JSONObject.parseObject(data);
             JSONArray data1 = jsonObject.getJSONArray("data");
             for(Object object:data1){
                 JSONObject jsonObject1 = JSONObject.parseObject(object.toString());
                 String id=jsonObject1.getString("id");//id
                 String title=jsonObject1.getString("title");//标题
                 System.out.println("标题 : " + title);

                 String summary=jsonObject1.getString("describe");//简介
                 System.out.println("简介 : " + summary);

                 String videoUrl=jsonObject1.getString("videoUrl");//视频地址
                 System.out.println("视频地址 : " + videoUrl);

                 String PublishTime= DateUtil.getHecaijingLater(jsonObject1.getString("create"));//时间
                 System.out.println("时间 : " + PublishTime);

                 String href_addr="https://www.hecaijing.com/video/show/"+id+".html";
                 System.out.println("视频详情内容地址 : " + href_addr);

                 Document document1 = JsoupUtilPor.get(href_addr, 1);
                 Element element = document1.select("div.video-content").get(0);
                 String trimstr = element.select("p.user-info").text().trim();
                 String author=trimstr.split("·")[0];//作者
                 System.out.println("作者 : " + author);

                 String from_site_url = element.select("p.user-info").select("img").attr("src");//作者头像
                 System.out.println("作者头像 " + from_site_url);

                 if (title != null && summary != null && videoUrl != null && href_addr != null && author != null) {
                     String from_site = author;
                     String from_interface = "核财经";
                     String article_type="2";//视频
                     String types="视频";
                     dbUtil.insertAndQueryvedio("insert into news_info " +
                             "(`status`,`news_type_id`,`types`,`title`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`,`article_type`,`from_site_url`)" +
                             " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?);", title, href_addr, new Object[]{"up", "15", types, title, author, summary, videoUrl, href_addr, PublishTime, from_site,from_interface,PublishTime,article_type,from_site_url});

                 }


             }

     }

     @Test
     public void getlianpaivedio(){

         String data = HttpClientUtilPro.httpGetRequest("http://www.lianpai999.com/getColumnList", httpGetRequest);
         JSONObject jsonObject = JSONObject.parseObject(data);
         System.out.println(jsonObject);
         JSONArray data1 = jsonObject.getJSONArray("data");
         for(Object o:data1){
             JSONObject jsonObject1 = JSONObject.parseObject(o.toString());
             String cate_id = jsonObject1.getString("cate_id");
             String s = HttpClientUtilPro.httpGetRequest("http://www.lianpai999.com/getVideoList?cate_id=23&page=1", httpGetRequest);

         }


     }


}

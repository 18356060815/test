package com.jnk.test.crawl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jnk.test.util.DownloadVideo;
import com.jnk.test.util.FtpUtil;
import com.jnk.test.util.HttpClientUtilPro;
import com.jnk.test.util.JsoupUtilPor;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
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
     @Test
     public void gethuoxingvideo() throws Exception{
         Map map=new HashMap();
         map.put("Type","15");
         map.put("PageIndex","1");
         map.put("PageSize","10");

         String data = HttpClientUtilPro.httpPostRequest("https://www.ihuoqiu.com/MAPI/GetArticleListData",map, 1);
         data = JSON.parseObject(data, String.class);
         com.alibaba.fastjson.JSONObject jsonObjects = JSON.parseObject(data);
         com.alibaba.fastjson.JSONArray data1 = jsonObjects.getJSONArray("data");
         for(Object jsonObject:data1){
             JSONObject jsonObject1 = JSONObject.parseObject(jsonObject.toString());
             System.out.println(jsonObject1);
             Map map1=new HashMap();
             map1.put("Type","2");
             map1.put("data",jsonObject1.getString("data1"));
             String videodata = HttpClientUtilPro.httpPostRequest("https://www.ihuoqiu.com/MAPI/GetArticleInfoData", map1, 1);
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

     @Test
     public void  gethecaijingVedio(){
         Document document = JsoupUtilPor.get("https://www.hecaijing.com/shipin/", 1);
         String type=document.select("ul.sub-head").select("a.active").attr("type");

         for(int i=1;i<100;i++){
             String data = HttpClientUtilPro.httpGetRequest("https://www.hecaijing.com/index/loadmore?type=" + type + "&pn=" + i, 1);
             System.out.println(data);
             JSONObject jsonObject = JSONObject.parseObject(data);
             JSONArray data1 = jsonObject.getJSONArray("data");
             for(Object object:data1){
                 JSONObject jsonObject1 = JSONObject.parseObject(object.toString());
                 String title=jsonObject1.getString("title");//标题
                 String summary=jsonObject1.getString("describe");//简介
                 String videoUrl=jsonObject1.getString("videoUrl");//视频地址



             }
             System.exit(0);
         }

     }

}

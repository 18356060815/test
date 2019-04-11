package com.jnk.test.crawl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jnk.test.Service.DBUtil;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@Service
public class DownLoadvideo {
    final  static  int httpGetRequest=1;

    @Autowired
    DBUtil dbUtil;
    //核财经视频
    @Test
    public void  gethecaijingVedio(){
        Document document = JsoupUtilPor.get("https://www.hecaijing.com/shipin/", httpGetRequest);
        String type=document.select("ul.sub-head").select("a.active").attr("type");

        for(int i=1;i<100;i++){
            String data = HttpClientUtilPro.httpGetRequest("https://www.hecaijing.com/index/loadmore?type=" + type + "&pn=" + i, httpGetRequest);
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

                String PublishTime=jsonObject1.getString("publish_time");//时间
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

    }



    @Test
    public void getlianpaivedio(){

        String data = HttpClientUtilPro.httpGetRequest("http://www.lianpai999.com/getColumnList", httpGetRequest);
        JSONObject jsonObject = JSONObject.parseObject(data);
        JSONArray data1 = jsonObject.getJSONArray("data");
        for(Object o:data1){
            JSONObject jsonObject1 = JSONObject.parseObject(o.toString());
            String cate_id = jsonObject1.getString("cate_id");
            for (int i=1;i<100;i++){
                String s = HttpClientUtilPro.httpGetRequest("http://www.lianpai999.com/getVideoList?cate_id="+cate_id+"&page="+i, httpGetRequest);
                JSONObject jsonObject2 = JSONObject.parseObject(s);
                System.out.println(jsonObject2);
                System.exit(0);
            }

        }

    }

}

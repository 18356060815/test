package com.jnk.test.crawl;

import com.jnk.test.Service.DBUtil;
import com.jnk.test.util.CheckUtil;
import com.jnk.test.util.HttpClientUtilPro;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;
@RunWith(SpringRunner.class)
@SpringBootTest
@Service
public class AllNewNewsInfo {

    @Autowired
    DBUtil dbUtil;
    final static  int  RequestCount=1;

    @Test
    public void BabiteDownloadPage(){
            //巴比特  最新数据更新
            String rs= HttpClientUtilPro.httpGetRequest("https://app.blockmeta.com/w1/news/list?post_type=post&num=50",RequestCount);
            JSONObject jsonObject1=JSONObject.fromObject(rs);
            System.out.println(jsonObject1);
            JSONArray jsonArray1=jsonObject1.getJSONArray("list");
            for(Object  obj:jsonArray1){
                try {
                    JSONObject jsonObject2=JSONObject.fromObject(obj);
                    String title=jsonObject2.getString("title");//标题
                    title=title.replace("'","\\'");
                    System.out.println("标题 : "+title);
                    String author=jsonObject2.getJSONObject("author_info").getString("display_name");//作者
                    System.out.println("作者 : "+author);

                    author=author.replace("'","\\'");

                    String search_key="";//标签 xx,xx,xx
                    search_key=search_key.replace("'","\\'");
                    System.out.println("标签 : "+search_key);

                    String summary=jsonObject2.getString("desc");//简介
                    summary=summary.replace("'","\\'");
                    System.out.println("简介 : "+summary);

                    String pic_url=jsonObject2.getString("image");//图片链接
                    System.out.println("图片链接 : "+pic_url);

                    String href_addr="https://www.8btc.com/article/"+jsonObject2.getString("id");//新闻内容地址
                    System.out.println("新闻内容地址 : "+href_addr);

                    String PublishTime=jsonObject2.getString("post_date_format");//publishtime

                    if(PublishTime==null||PublishTime.equals("")){
                        PublishTime= CheckUtil.dateToString(new Date());//publishtime

                    }
                    System.out.println("publishtime : "+PublishTime);

                    String from_site=author;
//                    dbUtil.insertAndQuery( "insert into news_info " +
//                            "(`status`,`news_type_id`,`title`,`author`,`search_key`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
//                            " values (?,?,?,?,?,?,?,?,?,?,?,?);",title,href_addr,new Object[]{"up","12",title,author,search_key,summary,pic_url,href_addr,PublishTime,from_site,"BABITE_NEWS",PublishTime});
//                    //"('up','" + 12 + "','" + types + "','" + title + "','" + author + "','" + search_key + "','" + summary + "','" + pic_url + "','" + href_addr + "','" + PublishTime + "','" + from_site + "','XIHA_NEWS','"+PublishTime+"');", title,href_addr);

                }catch (Exception e){
                    e.printStackTrace();
                }
         }
    }
}

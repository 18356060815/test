package com.jnk.test.crawl;

import com.jnk.test.Service.DBUtil;
import com.jnk.test.util.CheckUtil;
import com.jnk.test.util.DateUtil;
import com.jnk.test.util.HttpClientUtilPro;
import com.jnk.test.util.JsoupUtilPor;
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
import java.util.*;

import static com.jnk.test.util.DateUtil.getHecaijingLater;
import static com.jnk.test.util.DateUtil.getLiandedeLater;

@RunWith(SpringRunner.class)
@SpringBootTest
@Service
public class AllNewNewsInfo {

    @Autowired
    DBUtil dbUtil;
    final static  int  RequestCount=1;

    //巴比特  最新数据更新
    @Test
    public void BabiteDownloadPage(){
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

                    JSONArray jsonArrays=jsonObject2.getJSONArray("tags");//标签 xx,xx,xx
                    String search_key="";
                    for(Object o:jsonArrays){
                        JSONObject jsonObject21=JSONObject.fromObject(o);
                        String name=jsonObject21.getString("name");
                        String slug=jsonObject21.getString("slug");
                        if("".equals(slug)){
                            search_key+=name+",";
                        }else if(!"".equals(name)&&!"".equals(slug)){
                            search_key+=((name+","+slug)+",");
                        }

                    }
                    search_key=search_key.replace("'","\\'");
                    if(!search_key.equals("")){
                        search_key=search_key.substring(0,search_key.length()-1);
                        System.out.println("标签 : "+search_key);
                    }


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

                    String from_site="BABITE_NEWS";
                    String from_interface="BABITE_NEWS";
                    String news_type_id="12";
                    dbUtil.insertAndQuery( "insert into news_info " +
                            "(`status`,`news_type_id`,`title`,`author`,`search_key`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`)" +
                            " values (?,?,?,?,?,?,?,?,?,?,?);",title,href_addr,new Object[]{"up",news_type_id,title,author,search_key,summary,pic_url,href_addr,PublishTime,from_site,from_interface});
                    //"('up','" + 12 + "','" + types + "','" + title + "','" + author + "','" + search_key + "','" + summary + "','" + pic_url + "','" + href_addr + "','" + PublishTime + "','" + from_site + "','XIHA_NEWS','"+PublishTime+"');", title,href_addr);

                }catch (Exception e){
                    e.printStackTrace();
                }
         }
    }




    @Test
    //链得得
    public  void LiandedeDownloadPage(){

        Document document= JsoupUtilPor.get("https://www.chaindd.com/column/3158465/1",RequestCount);
        Elements elements=document.select("div.mod-article-list").select("li");
        for(Element element:elements){
            String title=element.select("a.title").text();
            System.out.println("标题 "+title);

            String author=element.select("a.name").text().trim();
            System.out.println("作者 "+author);
            Elements elements1=element.select("div.tag").select("a");
            String search_key="";
            for(Element element1:elements1){
                search_key+=(element1.text()+",");
            }
            search_key=!search_key.equals("")?search_key.substring(0,search_key.length()-1):"";
            System.out.println("关键字 "+search_key);

            String summary=element.select("p.intro").text();
            System.out.println("简介 "+summary);

            String pic_url=element.select("div.pic").select("img").attr("src");
            System.out.println("图片 "+pic_url);

            String href_addr=element.select("a.title").attr("abs:href");
            System.out.println("新闻地址 "+href_addr);

            String PublishTime=element.select("span.author").text();
            PublishTime=PublishTime.split("•")[1];
            PublishTime=getLiandedeLater(PublishTime.trim());
            System.out.println("更新时间 "+PublishTime);
            System.out.println("-------------------------------------------");

            String from_site="LIANDEDE_NEWS";
            String from_interface="LIANDEDE_NEWS";
            String news_type_id="12";

            dbUtil.insertAndQuery( "insert into news_info " +
                    "(`status`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`)" +
                    " values (?,?,?,?,?,?,?,?,?,?,?)",title,href_addr,new Object[] {"up",news_type_id,title,search_key,author,summary,pic_url,href_addr,PublishTime,from_site,from_interface});

        }

    }



    @Test
    //挖链网
    public  void WalianDownloadPage(){
        Map map=new HashMap<>();
        map.put("channelId","26");
        map.put("rows","20");

            map.put("page",1);
            String  document= HttpClientUtilPro.httpPostRequest("https://api.walian.cn/api/article-lists",map,RequestCount);
            JSONObject jsonObject=JSONObject.fromObject(document);
            System.out.println(jsonObject);
            JSONObject jsonObject1=jsonObject.getJSONObject("data");
            JSONArray jsonArray=jsonObject1.getJSONArray("list");
            for(Object o:jsonArray){
                JSONObject jsonObject2=JSONObject.fromObject(o);
                String title=jsonObject2.getString("title");
                System.out.println("标题 "+title);

                String author=jsonObject2.getString("nickName");
                System.out.println("作者 "+author);

                Object jsonArrays1=jsonObject2.get("tags");

                String search_key="";
                if(!jsonArrays1.equals(null)){
                    JSONArray jsonArrays=JSONArray.fromObject(jsonArrays1);
                    for(Object oi:jsonArrays){
                        JSONObject jsonObject3=JSONObject.fromObject(oi);
                        String tagname=jsonObject3.getString("tagName");
                        search_key+=tagname+",";

                    }
                }

                if(!search_key.equals("")){
                    search_key=search_key.substring(0,search_key.length()-1);
                }
                System.out.println("关键字 "+search_key);

                String summary=jsonObject2.getString("intro");
                System.out.println("简介 "+summary);
                String pic_url=jsonObject2.getString("logo");
                System.out.println("图片 "+pic_url);

                String href_addr="https://www.walian.cn/news/"+jsonObject2.getString("id")+".html";
                System.out.println("新闻地址 "+href_addr);

                String PublishTime=jsonObject2.getString("pubTime");
                PublishTime= DateUtil.stampToDate(PublishTime,false);
                System.out.println("更新时间 "+PublishTime);

                String from_site="挖链网";
                String from_interface="挖链网";
                String news_type_id="12";
                dbUtil.insertAndQuery( "insert into news_info " +
                        "(`status`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`)" +
                        " values (?,?,?,?,?,?,?,?,?,?,?)",title,href_addr,new Object[] {"up",news_type_id,title,search_key,author,summary,pic_url,href_addr,PublishTime,from_site,from_interface});

            }


    }
    @Test
    //陀螺财经 test
    public void Tuoluocaijing(){
        String tuo= HttpClientUtilPro.httpGetRequest("https://www.tuoluocaijing.cn/api/article/get_list?page_num=1&limit=30&toutiao=1",RequestCount);
        JSONObject jsonObject=JSONObject.fromObject(tuo);
        JSONObject jsonObject1=jsonObject.getJSONObject("data");
        JSONArray  jsonArray=jsonObject1.getJSONArray("list");
        for(Object o:jsonArray) {
            JSONObject jsonObject2=JSONObject.fromObject(o);
            if(jsonObject2.getString("is_top").equals("1")){
                continue;
            }

            String title=jsonObject2.getString("title");
            System.out.println("标题 "+title);

            String author=jsonObject2.getString("edit_name");
            System.out.println("作者 "+author);


            String search_key="";
//            if(!jsonArrays1.equals(null)){
//                JSONArray jsonArrays=JSONArray.fromObject(jsonArrays1);
//                for(Object oi:jsonArrays){
//                    JSONObject jsonObject3=JSONObject.fromObject(oi);
//                    String tagname=jsonObject3.getString("tagName");
//                    search_key+=tagname+",";
//
//                }
//            }
//
//            if(!search_key.equals("")){
//                search_key=search_key.substring(0,search_key.length()-1);
//            }
            System.out.println("关键字 "+search_key);

            String summary=jsonObject2.getString("summary");
            System.out.println("简介 "+summary);
            String pic_url=jsonObject2.getString("thumbnail");
            pic_url=pic_url.split("\\?")[0];
            System.out.println("图片 "+pic_url);

            String href_addr="https://www.tuoluocaijing.cn/article/detail-"+jsonObject2.getString("id")+".html";
            System.out.println("新闻地址 "+href_addr);

            String PublishTime=jsonObject2.getString("audit_time");
            System.out.println("更新时间 "+PublishTime);

            String from_site="陀螺财经";
            String from_interface="陀螺财经";
            String news_type_id="12";
            dbUtil.insertAndQuery( "insert into news_info " +
                    "(`status`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`)" +
                    " values (?,?,?,?,?,?,?,?,?,?,?)",title,href_addr,new Object[] {"up",news_type_id,title,search_key,author,summary,pic_url,href_addr,PublishTime,from_site,from_interface});

        }
     }




    @Test
    //深链财经 推荐 最新
    public void shenliancaijing(){

            Map map=new HashMap();
            map.put("id",8);
            map.put("limit",40);
            map.put("page",1);
            //id=8&limit=12&page=3
            String data=HttpClientUtilPro.httpPostRequest("https://www.shenliancaijing.com/portal/index/centerporApi",map,RequestCount);
            JSONObject jsonObject=JSONObject.fromObject(data);
            System.out.println(jsonObject);
            if(jsonObject.getString("code").equals("202")){
                return;
            }
            JSONArray jsonArray=jsonObject.getJSONArray("data");

            for(Object o:jsonArray){
                JSONObject jsonObject2=JSONObject.fromObject(o);
                String title=jsonObject2.getString("post_title");
                System.out.println("标题 "+title);

                String author=jsonObject2.getString("post_source");
                System.out.println("作者 "+author);

                String search_key="";
                System.out.println("关键字 "+search_key);

                String summary=jsonObject2.getString("post_excerpt");
                System.out.println("简介 "+summary);

                String pic_url="https://www.shenliancaijing.com/upload/"+jsonObject2.getString("pic");
                System.out.println("图片 "+pic_url);

                String href_addr="https://www.shenliancaijing.com"+jsonObject2.getString("url");
                System.out.println("新闻地址 "+href_addr);

                String PublishTime=jsonObject2.getString("published_time");
                PublishTime=getHecaijingLater(PublishTime);
                System.out.println("更新时间2 "+PublishTime);

                String from_site="深链财经";
                String from_interface="深链财经";
                String news_type_id="12";
                dbUtil.insertAndQuery( "insert into news_info " +
                        "(`status`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                        " values (?,?,?,?,?,?,?,?,?,?,?,?)",title,href_addr,new Object[] {"up",news_type_id,title,search_key,author,summary,pic_url,href_addr,PublishTime,from_site,from_interface,PublishTime});


            }



        }
    //鸵鸟区块链
    @Test
    public void tuoniaoqukuailiang(){

        Document data = JsoupUtilPor.get("https://www.tuoniaox.com/content/news/list?type=2&topic=100116&user=0&id=NaN&per_page=1&types=NaN&time=NaN&status=NaN&location=NaN&enddate=NaN&date=NaN&bottom_id=298270&style=0&q=&tab=",RequestCount);
        Elements elements=data.select("div.op-list-wrapper").select("div.art-onesmallpic");
        for(Element element:elements){
            String title=element.select("h2.title").text();
            System.out.println("标题 "+title);

            String author=element.select("span.name").text();
            System.out.println("作者 "+author);

            Elements search_keys=element.select("span.des").select("a");
            List<String> list=new ArrayList<>();
            for(Element element1:search_keys){
                list.add(element1.text());
            }
            String search_key=CheckUtil.getSearchKey(list);
            System.out.println("关键字 "+search_key);

            String summary=element.select("p.content").text();
            System.out.println("简介 "+summary);

            String pic_url=element.select("a.fl").select("img").attr("src");
            System.out.println("图片 "+pic_url);

            String href_addr=element.select("a").attr("abs:href");
            System.out.println("新闻地址 "+href_addr);

            String PublishTime=element.select("span.time").text();
            PublishTime=DateUtil.getHecaijingLater(PublishTime);
            System.out.println("更新时间 "+PublishTime);
            String from_site="鸵鸟区块链";
            String from_interface="鸵鸟区块链";
            String news_type_id="12";
            dbUtil.insertAndQuery( "insert into news_info " +
                    "(`status`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                    " values (?,?,?,?,?,?,?,?,?,?,?,?)",title,href_addr,new Object[] {"up",news_type_id,title,search_key,author,summary,pic_url,href_addr,PublishTime,from_site,from_interface,PublishTime});

        }
    }


}

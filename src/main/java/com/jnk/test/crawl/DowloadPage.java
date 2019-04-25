package com.jnk.test.crawl;

import com.jnk.test.Service.DBUtil;
import com.jnk.test.util.*;
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
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.jnk.test.util.CheckUtil.getTrace;
import static com.jnk.test.util.DateUtil.getHecaijingLater;
import static com.jnk.test.util.DateUtil.getLiandedeLater;
import static com.jnk.test.util.DateUtil.getOneHoursAgoTime;

//
@RunWith(SpringRunner.class)
@SpringBootTest
@Service
public class DowloadPage {

    @Autowired
    DBUtil dbUtil;
    final static  int  RequestCount=1;
    //火球  HUOQIU_NEWS
    public void HuoqiuDownloadPage() {
        Document document = JsoupUtilPor.get("http://www.huoqiucaijing.com/Home/information",RequestCount);
        Elements elements = document.select("div.content_article_tab").select("label");
        for (Element element : elements) {
            String key = element.text();
            if (key.equals("最新") || key.equals("推荐") || key.equals("头条")) {
                continue;
            }
            int i = 0;//页面递增
            while (true) {//翻页
                i++;
            String url="http://www.huoqiucaijing.com"+element.attr("data-requestData")+"&pageIndex="+i;
            String v= HttpClientUtilPro.httpPostRequest(url,RequestCount);
            JSONObject jsonObject=JSONObject.fromObject(v);
            String  jsonArray=jsonObject.getString("msg");
            JSONArray jsonArray1=JSONArray.fromObject(jsonArray);

            if(jsonArray1.size()==0){
                break;
            }

            for(Object obj:jsonArray1) {
                    try {
                        JSONObject jsonObject1 = JSONObject.fromObject(obj);

                        JSONObject jsonObject2 = jsonObject1.getJSONObject("ArticleInfo");
                        String types = key;//标签词
                        String title = jsonObject2.getString("Title");//标题
                        title = title.replace("'", "\\'");
                        String author = jsonObject2.getString("Author");//作者
                        author = author.replace("'", "\\'");

                        String search_key = jsonObject2.getString("Tag");//标签 xx,xx,xx
                        search_key = search_key.replace("'", "\\'");

                        String summary = jsonObject2.getString("ShortDescription");//简介
                        summary = summary.replace("'", "\\'");

                        String pic_url = jsonObject2.getString("ImgUrl");//图片链接
                        String href_addr = "http://www.huoqiucaijing.com/Content/information?data=" + jsonObject1.getString("data1");//新闻内容地址

                        String PublishTime = jsonObject2.getString("UpdateTime");
                        if (PublishTime == null || PublishTime.equals("")) {
                            PublishTime = CheckUtil.dateToString(new Date());//publishtime

                        }
                        String from_site=author;
                        dbUtil.insertAndQuery( "insert into news_info " +
                                "(`status`,`news_type_id`,`types`,`title`,`author`,`search_key`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                                " values (?,?,?,?,?,?,?,?,?,?,?,?,?);",title,href_addr,new Object[]{"up","12",types,title,author,search_key,summary,pic_url,href_addr,PublishTime,from_site,"HUOQIU_NEWS",PublishTime});
                        //"('up','" + 12 + "','" + types + "','" + title + "','" + author + "','" + search_key + "','" + summary + "','" + pic_url + "','" + href_addr + "','" + PublishTime + "','" + from_site + "','XIHA_NEWS','"+PublishTime+"');", title,href_addr);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

        }
    }

    @Test
    public void BabiteDownloadPage() {
        ///巴比特 矿业全部 没有跑
        //19
        for(int i=1;i<30;i++){
            String data = HttpClientUtilPro.httpGetRequest("https://webapi.8btc.com/bbt_api/news/list?num=20&page="+i+"&cat_id=6", RequestCount);
            JSONObject jsonObject3 = JSONObject.fromObject(data);
            JSONArray jsonArray2 = jsonObject3.getJSONObject("data").getJSONArray("list");
            for (Object obj : jsonArray2) {
                try {
                    JSONObject jsonObject2 = JSONObject.fromObject(obj);
                    String types = "挖矿";//标签词
                    String title = jsonObject2.getString("title");//标题
                    System.out.println("标题 : "+title);
                    String author = jsonObject2.getJSONObject("author_info").getString("display_name");//作者
                    System.out.println("作者 : "+author);

                    JSONArray tags = jsonObject2.getJSONArray("tags");//标签
                    List list=new ArrayList();
                    for(Object object:tags){
                        JSONObject jsonObject = JSONObject.fromObject(object);
                        list.add(jsonObject.getString("name"));

                    }
                    String search_key =CheckUtil.getSearchKey(list);
                    System.out.println("关键字 : " + search_key);

                    String summary = jsonObject2.getString("desc");//简介
                    System.out.println("简介 : "+summary);

                    String pic_url = jsonObject2.getString("image");//图片链接
                    System.out.println("图片链接 : "+pic_url);

                    String href_addr = "https://www.8btc.com/article/" + jsonObject2.getString("id");//新闻内容地址
                    System.out.println("新闻内容地址 : "+href_addr);

                    String PublishTime = jsonObject2.getString("post_date_format");//publishtime
                    if (PublishTime == null || PublishTime.equals("")) {
                        PublishTime = CheckUtil.dateToString(new Date());//publishtime

                    }
                    System.out.println("publishtime : "+PublishTime);

                    if (title != null && summary != null && pic_url != null && href_addr != null && author != null) {
                        String from_site = author;
                        String from_interface = "BABITE_NEWS";

                        dbUtil.insertAndQuery("insert into news_info " +
                                "(`status`,`news_type_id`,`types`,`title`,`author`,`search_key`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                                " values (?,?,?,?,?,?,?,?,?,?,?,?,?);", title, href_addr, new Object[]{"up", "12", types, title, author, search_key, summary, pic_url, href_addr, PublishTime, from_site, from_interface,PublishTime});

                        //"('up','" + 12 + "','" + types + "','" + title + "','" + author + "','" + search_key + "','" + summary + "','" + pic_url + "','" + href_addr + "','" + PublishTime + "','" + from_site + "','BABITE_NEWS');", title,href_addr);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }


            }


        }


    }
    //https://webapi.8btc.com/bbt_api/news/list?num=20&page=2




    public  void JinseDownloadPage(){

         //金色财经 带标签数据 all JINSE_NEWS
       Document document=JsoupUtilPor.get("https://www.jinse.com/",RequestCount);
       Elements elements=document.select("ul.index-main-nav");
       Elements elements1=elements.select("a");

       for(Element element1 : elements1){
           String key=element1.attr("title");
           String url_end=element1.attr("@click");
           System.out.println(key);
           if (url_end==null||"".equals(url_end)){
            continue;
           }
           if (key.equals("最新") || key.equals("推荐") || key.equals("头条")) {
               continue;
           }
           url_end=url_end.substring(url_end.indexOf(", '")+3,url_end.indexOf("',"));
           System.out.println(url_end);
                    String  information_id="0";
                     a: while (true){
                       String url="https://api.jinse.com/v6/information/list?catelogue_key="+url_end+"&limit=23&information_id="+information_id+"&flag=down&version=9.9.9";
                       String s=HttpClientUtilPro.httpGetRequest(url,RequestCount);
                       JSONObject jsonObject=JSONObject.fromObject(s);
                       information_id=jsonObject.getString("bottom_id");
                       JSONArray jsonArray=jsonObject.getJSONArray("list");


                       if(jsonArray.size()!=0){
                                for(int i=0;i<jsonArray.size();i++){
                                    try {
                                        JSONObject jsonObject2=jsonArray.getJSONObject(i);
                                        String types=key;//标签词
                                        String title=jsonObject2.getString("title");//标题
                                        title=title.replace("'","\\'");
                                        String author=jsonObject2.getJSONObject("extra").getString("author");//作者
                                        author=author.replace("'","\\'");

                                        String search_key="";//标签 xx,xx,xx
                                        search_key=search_key.replace("'","\\'");
                                        String summary=jsonObject2.getJSONObject("extra").getString("summary");//简介
                                        summary=summary.replace("'","\\'");

                                        String pic_url=jsonObject2.getJSONObject("extra").getString("thumbnail_pic");//图片链接
                                        String href_addr=jsonObject2.getJSONObject("extra").getString("topic_url");//新闻内容地址
                                        String PublishTime=jsonObject2.getJSONObject("extra").getString("published_at");//publishtime
                                        System.out.println(PublishTime);

                                        if(PublishTime.equals("")||PublishTime==null){
                                            PublishTime=CheckUtil.dateToString(new Date());//publishtime
                                        }else {
                                            PublishTime=CheckUtil.stampToDate(PublishTime,true);
                                        }
                                        System.out.println(PublishTime);
                                        String from_site=author;
                                        dbUtil.insertAndQuery( "insert into news_info " +
                                                "(`status`,`news_type_id`,`types`,`title`,`author`,`search_key`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                                                " values (?,?,?,?,?,?,?,?,?,?,?,?,?);",title,href_addr,new Object[]{"up","12",types,title,author,search_key,summary,pic_url,href_addr,PublishTime,from_site,"JINSE_NEWS",PublishTime});
                                        //"('up','" + 12 + "','" + types + "','" + title + "','" + author + "','" + search_key + "','" + summary + "','" + pic_url + "','" + href_addr + "','" + PublishTime + "','" + from_site + "','XIHA_NEWS','"+PublishTime+"');", title,href_addr);

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                }
                       }else {
                            break a;
                       }
                    }

       }
    }



    public   void HuoXingDownloadPage(){

        //火星 all HUOXING_NEWS
        Document document = JsoupUtilPor.get("http://www.huoxing24.com/",RequestCount);
        Elements elements = document.select("ul#newsTabs").select("li");
        for (Element element : elements) {
            String key = element.text();
            System.out.println(key);
            if (key.equals("最新") || key.equals("推荐") || key.equals("头条")|| key.equals("火星号")) {
                continue;
            }
            String id = element.attr("data-id");


            Element element1 = document.select("div#loadMore" + id).get(0);//找到标签id所在的div
            String times = element1.attr("data-refreshtime");
            String page = element1.attr("data-pagecount");//data-pagecount
            System.out.println("时间:" + times + ":时间");
            if (" ".equals(times) || times == null || "".equals(times)) {
                times = element1.attr("data-time");
                if (" ".equals(times) || times == null || "".equals(times)) {
                    continue;
                }
            }
            System.out.println("时间 : " + times);
            for (int i = 2; i <= Integer.parseInt(page); i++) {
                String url = "http://www.huoxing24.com/info/news/shownews?currentPage=" + i + "&pageSize=20&channelId=" + id + "&refreshTime=" + times;
                System.out.println(url);
                String x = HttpClientUtilPro.httpGetRequest(url,RequestCount);

                JSONObject jsonObject = JSONObject.fromObject(x);

                JSONObject jsonObject1 = jsonObject.getJSONObject("obj");
                JSONArray jsonArray = jsonObject1.getJSONArray("inforList");
                for (int j = 0; j < jsonArray.size(); j++) {
                    try {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(j);
                    //
                        String types=key;//标签词
                        String title=jsonObject2.getString("title");//标题
                        title=title.replace("'","\\'");
                        System.out.println("标题 : "+title);

                        String author=jsonObject2.getString("author");//作者
                        author=author.replace("'","\\'");
                        System.out.println("作者 : "+author);

                        String search_key=jsonObject2.getString("tags");//标签 xx,xx,xx
                        search_key=search_key.replace("'","\\'");
                        System.out.println("标签 : "+search_key);

                        String summary=jsonObject2.getString("synopsis");//简介
                        summary=summary.replace("'","\\'");
                        System.out.println("简介 : "+summary);
                        String pic_url="";

                        pic_url=jsonObject2.getJSONObject("coverPic").getString("pc");//图片链接


                        String href_addr="http://www.huoxing24.com/newsdetail/"+jsonObject2.getString("id")+".html";//新闻内容地址
                        String PublishTime=jsonObject2.getString("publishTime");//publishtime
                        System.out.println("时间戳 : "+PublishTime);

                        if(PublishTime.equals("")||PublishTime==null){
                            PublishTime=CheckUtil.dateToString(new Date());//publishtime
                        }else {
                            PublishTime=CheckUtil.stampToDate(PublishTime,false);
                        }
                        System.out.println("时间 : "+PublishTime);
                        String from_site=author;
                        dbUtil.insertAndQuery( "insert into news_info " +
                                "(`status`,`news_type_id`,`types`,`title`,`author`,`search_key`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                                " values (?,?,?,?,?,?,?,?,?,?,?,?,?);",title,href_addr,new Object[]{"up","12",types,title,author,search_key,summary,pic_url,href_addr,PublishTime,from_site,"HUOXING_NEWS",PublishTime});
                        //"('up','" + 12 + "','" + types + "','" + title + "','" + author + "','" + search_key + "','" + summary + "','" + pic_url + "','" + href_addr + "','" + PublishTime + "','" + from_site + "','XIHA_NEWS','"+PublishTime+"');", title,href_addr);

                        if (j == jsonArray.size() - 1) {
                            times = jsonObject2.getString("publishTime");

                        }
                    }catch (Throwable e) {
                        e.printStackTrace();
                    }//

                }
            }

        }

    }

    @Test
    public  void JinNiuDownloadPage(){
        //金牛财经 JINNIU_NEWS
       String x = HttpClientUtilPro.httpGetRequest("http://www.jinniu.cn/prefix/info/medias/categories",RequestCount);
       JSONObject jsonObject=JSONObject.fromObject(x);
       JSONArray jsonArray=jsonObject.getJSONArray("data");//提取栏目列
        for(Object obj:jsonArray){
             JSONObject jsonObject1=JSONObject.fromObject(obj);
             String key=jsonObject1.getString("nameWeb");//获取电脑端的栏目名称 app端：nameApp
             String id=jsonObject1.getString("id");//获取id 用于访问栏目下新闻url拼接
            if (key.equals("最新") || key.equals("推荐") || key.equals("头条")|| key.equals("火星号")) {
                continue;
            }
            if(key.equals("深度")){
                key="观点";
            }
            int i=0;
            int PageSize;
            do{
               i++;
               String a= HttpClientUtilPro.httpGetRequest("http://www.jinniu.cn/prefix/info/medias/index?id="+id+"&page="+i,RequestCount);
               JSONObject jsonObject2=JSONObject.fromObject(a);
               int totalNum=jsonObject2.getJSONObject("data").getJSONObject("medias").getJSONObject("info").getInt("totalNum");
               PageSize=totalNum/10;
               if(totalNum%10>0){
                   PageSize+=1;
               }
               JSONArray jsonArray1=jsonObject2.getJSONObject("data").getJSONObject("medias").getJSONArray("list");
               for(Object  object:jsonArray1){
                   JSONObject jsonObject3=JSONObject.fromObject(object);

                   String types=key;//标签词
                   System.out.println("标签词 : "+types);

                   String title=jsonObject3.getJSONObject("media").getString("title");//标题
                   title=title.replace("'","\\'");
                   System.out.println("标题 : "+title);

                   String author=jsonObject3.getJSONObject("media").getString("editor");//作者
                   author=author.replace("'","\\'");
                   System.out.println("作者 : "+author);

                   JSONArray search_key_arr=jsonObject3.getJSONObject("media").getJSONArray("tags");//标签 xx,xx,xx
                   String search_key="";
                   if(search_key_arr.size()==0){
                       search_key="";
                   }else {
                       for(Object sk:search_key_arr){
                           search_key+=sk+",";
                       }
                       search_key=search_key.replace("'","\\'");
                       search_key=search_key.substring(0,search_key.length()-1);
                   }
                   System.out.println("标签 : "+search_key);


                   String summary=jsonObject3.getJSONObject("media").getString("articleSummary");//简介
                   summary=summary.replace("'","\\'");
                   System.out.println("简介 : "+summary);

                   String pic_url=jsonObject3.getJSONObject("media").getString("imageUrl");//图片链接
                   System.out.println("图片链接 : "+pic_url);


                   String href_addr="http://www.jinniu.cn/news/"+jsonObject3.getJSONObject("media").getString("id");//新闻内容地址
                   System.out.println("新闻内容地址 : "+href_addr);


                   String PublishTime=jsonObject3.getJSONObject("media").getString("createdAt");//publishtime

                   if(PublishTime.equals("")||PublishTime==null){
                       PublishTime=CheckUtil.dateToString(new Date());//publishtime
                   }else {
                       PublishTime=CheckUtil.stampToDate(PublishTime,false);
                   }
                   System.out.println("时间戳 : "+PublishTime);
                   String from_site=author;
                   dbUtil.insertAndQuery( "insert into news_info " +
                           "(`status`,`news_type_id`,`types`,`title`,`author`,`search_key`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                           " values (?,?,?,?,?,?,?,?,?,?,?,?,?);",title,href_addr,new Object[]{"up","12",types,title,author,search_key,summary,pic_url,href_addr,PublishTime,from_site,"JINNIU_NEWS",PublishTime});
                   //"('up','" + 12 + "','" + types + "','" + title + "','" + author + "','" + search_key + "','" + summary + "','" + pic_url + "','" + href_addr + "','" + PublishTime + "','" + from_site + "','XIHA_NEWS','"+PublishTime+"');", title,href_addr);

               }

            }while (i<PageSize);


        }

    }
    //BILAI_NEWS
    @Test
    public  void BilaiDownloadPage(){
        int i=0;
        while (true){
            i++;
            System.out.println("页数"+i);
            Document html=JsoupUtilPor.get("https://www.niubilai.com/index/index/ajaxpage?page="+i+"&cateid=71",RequestCount);
            Elements elements=html.select("div.story");

            for(Element element:elements){
                String title=element.select("div.thumbnail").select("img").attr("alt");//标题
                title=title.replace("'","\\'");
                System.out.println("标题 "+ title);

                String author=element.select("div.info-item-cr").select("p.bb").select("a").text().trim();//作者
                author=author.replace("'","\\'");
                System.out.println("作者 "+ author);

                String search_key=element.select("div.info-item-cr").select("p.bb").select("i").get(0).text().replace(" ",",");//关键字
                search_key=search_key.replace("'","\\'");
                System.out.println("关键字 "+ search_key);

                String summary=element.select("div.info-item-cr").select("p.tt").text();//简介
                summary=summary.replace("'","\\'");
                System.out.println("简介 "+ summary);

                String pic_url=" https://www.niubilai.com"+element.select("div.thumbnail").select("img").attr("src");//图片地址
                pic_url=pic_url.trim();
                pic_url=pic_url.replace("'","\\'");
                System.out.println("图片地址 "+ pic_url);

                String href_addr="https://www.niubilai.com"+element.select("div.thumbnail").select("a").attr("href");//新闻地址
                System.out.println("新闻地址 "+ href_addr);

                String PublishTime=element.select("div.info-item-cr").select("p.bb").select("i").get(1).text();//更新时间
                if(PublishTime.indexOf("分钟前")!=-1){
                    PublishTime=PublishTime.replace("• ","").replace("分钟前","").trim();
                    PublishTime=CheckUtil.addTime(new Date(),-Integer.parseInt(PublishTime)).toString();
                }
                else if(PublishTime.indexOf("小时前")!=-1){
                    PublishTime=PublishTime.replace("• ","").replace("小时前","").trim();
                    PublishTime=CheckUtil.addTime(new Date(),-(Integer.parseInt(PublishTime)*60)).toString();
                }
                else {
                    PublishTime=CheckUtil.dateToString(PublishTime.replace("• ",""));
                }

                System.out.println("更新时间 "+ PublishTime);
                System.out.println("--------");

                String from_site=author;
                //BILAI_NEWS
                dbUtil.insertAndQuery( "insert into news_info " +
                        "(`status`,`news_type_id`,`title`,`author`,`search_key`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                        " values (?,?,,?,?,?,?,?,?,?,?,?,?);",title,href_addr,new Object[]{"up","12",title,author,search_key,summary,pic_url,href_addr,PublishTime,from_site,"BILAI_NEWS",PublishTime});
                //"('up','" + 12 + "','" + types + "','" + title + "','" + author + "','" + search_key + "','" + summary + "','" + pic_url + "','" + href_addr + "','" + PublishTime + "','" + from_site + "','XIHA_NEWS','"+PublishTime+"');", title,href_addr);

            }

        if(i==149){//149页
            return;
        }
        }

    }
    //嘻哈财经  //all  未跑
    @Test
    public  void XihaDownloadPage(){
        Document document=JsoupUtilPor.get("http://www.xiha.top/",RequestCount);
        Elements elements=document.select("ul.news-nav").select("li");
        for(Element element:elements){
             if(dbUtil.queryIsexists(element.text().trim())||CheckUtil.Sign(element.text().trim())){//判断是否在标签库  或者 是否是推荐最新头条
                 //while
                 Map map=new HashMap();
                 String cid=element.attr("data-cid");
                 System.out.println(cid);
                 map.put("cid",cid);
                 map.put("stale_ids","252,706,853,26495");
                 map.put("page",1);
                 Map heardMap=new HashMap();
                 heardMap.put("X-Requested-With","XMLHttpRequest");

                 String jsondata=HttpClientUtilPro.httpPostRequest("http://www.xiha.top/portal/index/ajaxgetarticle.html",heardMap,map,1);
                 JSONObject jsonObject=JSONObject.fromObject(jsondata);
                 System.out.println(jsonObject);
                 if("1".equals(jsonObject.getString("code"))){
                     JSONArray jsonArray=jsonObject.getJSONObject("data").getJSONArray("list");
                     for(Object obj:jsonArray){
                         JSONObject jsonObject1=JSONObject.fromObject(obj);
                         String types=jsonObject1.getString("c_name");//类型 types
                         System.out.println("类型 "+ types);

                         String title=jsonObject1.getString("post_title");//标题
                         System.out.println("标题 "+ title);

                         String author=jsonObject1.getString("user_nickname");//作者
                         System.out.println("作者 "+ author);

                         String search_key="";//关键字
                         System.out.println("关键字 "+ search_key);

                         String summary=jsonObject1.getString("post_excerpt");//简介
                         System.out.println("简介 "+ summary);

                         String pic_url=jsonObject1.getString("thumbnail");//图片
                         System.out.println("图片 "+ pic_url);

                         String href_addr="http://www.xiha.top"+jsonObject1.getString("url");//新闻地址
                         System.out.println("新闻地址 "+ href_addr);

                         String PublishTime=jsonObject1.getString("published_time");//更新时间
                         System.out.println("更新时间 "+ PublishTime);
                         String from_site=author;
                         dbUtil.insertAndQuery( "insert into news_info " +
                                 "(`status`,`news_type_id`,`types`,`title`,`author`,`search_key`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                                 " values (?,?,?,?,?,?,?,?,?,?,?,?,?);",title,href_addr,new Object[]{"up","12",types,title,author,search_key,summary,pic_url,href_addr,PublishTime,from_site,"XIHA_NEWS",PublishTime});
                                 //"('up','" + 12 + "','" + types + "','" + title + "','" + author + "','" + search_key + "','" + summary + "','" + pic_url + "','" + href_addr + "','" + PublishTime + "','" + from_site + "','XIHA_NEWS','"+PublishTime+"');", title,href_addr);

                     }

                 }

             }

        }

    }



    //知乎 区块链  比特币 话题资讯文章
    @Test
    public void ZhihuDownloadPage(){
        //比特币翻页ur
        String btcnexturl="https://www.zhihu.com/api/v4/topics/19600228/feeds/top_activity?include=data%5B%3F%28target.type%3Dtopic_sticky_module%29%5D.target.data%5B%3F%28target.type%3Danswer%29%5D.target.content%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%3Bdata%5B%3F%28target.type%3Dtopic_sticky_module%29%5D.target.data%5B%3F%28target.type%3Danswer%29%5D.target.is_normal%2Ccomment_count%2Cvoteup_count%2Ccontent%2Crelevant_info%2Cexcerpt.author.badge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Dtopic_sticky_module%29%5D.target.data%5B%3F%28target.type%3Darticle%29%5D.target.content%2Cvoteup_count%2Ccomment_count%2Cvoting%2Cauthor.badge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Dtopic_sticky_module%29%5D.target.data%5B%3F%28target.type%3Dpeople%29%5D.target.answer_count%2Carticles_count%2Cgender%2Cfollower_count%2Cis_followed%2Cis_following%2Cbadge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Danswer%29%5D.target.annotation_detail%2Ccontent%2Chermes_label%2Cis_labeled%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%3Bdata%5B%3F%28target.type%3Danswer%29%5D.target.author.badge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Darticle%29%5D.target.annotation_detail%2Ccontent%2Chermes_label%2Cis_labeled%2Cauthor.badge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Dquestion%29%5D.target.annotation_detail%2Ccomment_count%3B&limit=10&after_id=5037.20139&offset=5";
        //区块链翻页url
        String nexturl="https://www.zhihu.com/api/v4/topics/19901773/feeds/top_activity?include=data%5B%3F%28target.type%3Dtopic_sticky_module%29%5D.target.data%5B%3F%28target.type%3Danswer%29%5D.target.content%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%3Bdata%5B%3F%28target.type%3Dtopic_sticky_module%29%5D.target.data%5B%3F%28target.type%3Danswer%29%5D.target.is_normal%2Ccomment_count%2Cvoteup_count%2Ccontent%2Crelevant_info%2Cexcerpt.author.badge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Dtopic_sticky_module%29%5D.target.data%5B%3F%28target.type%3Darticle%29%5D.target.content%2Cvoteup_count%2Ccomment_count%2Cvoting%2Cauthor.badge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Dtopic_sticky_module%29%5D.target.data%5B%3F%28target.type%3Dpeople%29%5D.target.answer_count%2Carticles_count%2Cgender%2Cfollower_count%2Cis_followed%2Cis_following%2Cbadge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Danswer%29%5D.target.annotation_detail%2Ccontent%2Chermes_label%2Cis_labeled%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%3Bdata%5B%3F%28target.type%3Danswer%29%5D.target.author.badge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Darticle%29%5D.target.annotation_detail%2Ccontent%2Chermes_label%2Cis_labeled%2Cauthor.badge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Dquestion%29%5D.target.annotation_detail%2Ccomment_count%3B&limit=10&after_id=5037.61467&offset=5";
        String [] arr={btcnexturl,nexturl};
        for(int i=0;i<arr.length;i++){
           String  s=arr[i];
            while (true){
                String zhihu=HttpClientUtilPro.httpGetRequest(s,RequestCount);
                JSONObject jsonObject=JSONObject.fromObject(zhihu);
                JSONObject nextjson=jsonObject.getJSONObject("paging");

                if(nextjson.getString("is_end").equals("false")){
                    s=nextjson.getString("next");
                    System.out.println(s);
                }else {
                    System.out.println("没更多文章了");
                    break;
                }
                JSONArray jsonArray=jsonObject.getJSONArray("data");
                for(Object obj:jsonArray){
                    JSONObject jsonObject1=JSONObject.fromObject(obj);
                    if(jsonObject1.getJSONObject("target").getString("type").equals("article")){//判断是文章还是问答

                        String title=jsonObject1.getJSONObject("target").getString("title");//标题
                        System.out.println("标题 "+ title);

                        String author=jsonObject1.getJSONObject("target").getJSONObject("author").getString("name");//作者
                        System.out.println("作者 "+ author);

                        //String search_key="";//关键字
                        //System.out.println("关键字 "+ search_key);
                        //excerpt
                        String summary=jsonObject1.getJSONObject("target").getString("excerpt");//简介
                        summary=Jsoup.parse(summary).text();
                        System.out.println("简介 "+ summary);
                        //image_url
                        String pic_url=jsonObject1.getJSONObject("target").getOrDefault("image_url","").toString();//图片
                        System.out.println("图片 "+ pic_url);

                        String href_addr=jsonObject1.getJSONObject("target").getString("url");//新闻地址
                        System.out.println("新闻地址 "+ href_addr);

                        String PublishTime=jsonObject1.getJSONObject("target").getString("updated");//更新时间
                        PublishTime=CheckUtil.stampToDate(PublishTime,true);
                        System.out.println("更新时间 "+ PublishTime);
                        String from_site=author;
                        String zhihu_kind="article";
                        String from_interface="ZHIHU_NEWS";
                        String news_type_id="16";

                        //jdbcTemplate.update("INSERT INTO USER VALUES(?, ?, ?, ?)",
                        // new Object[] {user.getId(), user.getName(), user.getSex(), user.getAge()});

                        dbUtil.insertAndQuery( "insert into news_info " +
                                "(`status`,`zhihu_kind`,`news_type_id`,`title`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                                " values (?,?,?,?,?,?,?,?,?,?,?,?)",title,href_addr,new Object[] {"up",zhihu_kind,news_type_id,title,author,summary,pic_url,href_addr,PublishTime,from_site,from_interface,PublishTime});

                        // "('up','" + zhihu_kind + "','" + news_type_id + "','" + title + "','" + author + "','" + summary + "','" + pic_url + "','" + href_addr + "','" + PublishTime + "','" + from_site + "','"+from_interface+"','"+PublishTime+"');", title,href_addr);

                    }else {
                        continue;
                    }
                }
            }
        }


    }

//    String title=jsonObject1.getJSONObject("target").getString("title");//标题
//                        System.out.println("标题 "+ title);
//
//    String author=jsonObject1.getJSONObject("target").getJSONObject("author").getString("name");//作者
//                        System.out.println("作者 "+ author);
//
//    //String search_key="";//关键字
//    //System.out.println("关键字 "+ search_key);
//    //excerpt
//    String summary=jsonObject1.getJSONObject("target").getString("excerpt");//简介
//    summary=Jsoup.parse(summary).text();
//                        System.out.println("简介 "+ sum金色财经mary);
//    //image_url
//    String pic_url=jsonObject1.getJSONObject("target").getOrDefault("image_url","").toString();//图片
//                        System.out.println("图片 "+ pic_url);
//
//    String href_addr=jsonObject1.getJSONObject("target").getString("url");//新闻地址
//                        System.out.println("新闻地址 "+ href_addr);
//
//    String PublishTime=jsonObject1.getJSONObject("target").getString("updated");//更新时间
//    PublishTime=CheckUtil.stampToDate(PublishTime,true);
//                        System.out.println("更新时间 "+ PublishTime);
//    String from_site=author;
//    String zhihu_kind="article";
//    String from_interface="ZHIHU_NEWS";
//    String news_type_id="16";

    @Test //链得得 types 应用
    public  void LiandedeDownloadPage(){
        for(int i=1;i<173;i++){

        Document document=JsoupUtilPor.get("https://www.chaindd.com/column/3041170/"+i,RequestCount);
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

            String types="应用";
            String from_site=author;
            String from_interface="LIANDEDE_NEWS";
            String news_type_id="12";

            dbUtil.insertAndQuery( "insert into news_info " +
                    "(`status`,`types`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                    " values (?,?,?,?,?,?,?,?,?,?,?,?,?)",title,href_addr,new Object[] {"up",types,news_type_id,title,search_key,author,summary,pic_url,href_addr,PublishTime,from_site,from_interface,PublishTime});

        }

        }

    }

    //核财经  观点 //未
    @Test
    public  void HecaijingDownload(){
        int i=1;
        while (true){
        Document document=JsoupUtilPor.get("http://www.hecaijing.com/shendu/",RequestCount);
        String typeid= document.select("a.active").attr("type");
        String jsons=HttpClientUtilPro.httpGetRequest("http://www.hecaijing.com/index/loadmore?type="+typeid+"&pn="+i,RequestCount);
        JSONObject jsonObject=JSONObject.fromObject(jsons);
        if(jsonObject.getString("msg").equals("success")){
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            if(jsonArray.size()==0){
                return;
            }
            for(Object o:jsonArray){
                JSONObject jsonObject1=JSONObject.fromObject(o);
                String title=jsonObject1.getString("title");
                System.out.println("标题 : "+title);

                String summary=jsonObject1.getString("describe");
                System.out.println("简介 : "+summary);

                String  author=jsonObject1.getString("source_site");
                System.out.println("作者 : "+author);

                String search_key="";
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("tag");
                    for(Object o1:jsonArray1){
                        search_key+=o1.toString()+",";
                    }
                    if(!search_key.equals("")){
                        search_key=search_key.substring(0,search_key.length()-1);
                    }
                System.out.println("关键字 : "+search_key);
                String pic_url=jsonObject1.getString("img");
                System.out.println("图片地址 : "+pic_url);
                String href_addr=jsonObject1.getString("app_url");//新闻地址
                System.out.println("新闻地址 : "+href_addr);

                String PublishTime=jsonObject1.getString("publish_time");//创建时间
                System.out.println("更新时间 "+PublishTime);
                System.out.println("-------------------------------------------");
                String types="观点";
                String from_site=author;
                String from_interface="HECAIJING_NEWS";
                String news_type_id="12";
                dbUtil.insertAndQuery( "insert into news_info " +
                    "(`status`,`types`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                    " values (?,?,?,?,?,?,?,?,?,?,?,?,?)",title,href_addr,new Object[] {"up",types,news_type_id,title,search_key,author,summary,pic_url,href_addr,PublishTime,from_site,from_interface,PublishTime});

            }

        }
            i++;
        }
    }

//    @Test
//    //链向财经 精英视点
//    public void getLianxiangDownload(){
//        for(int i=1;i<770;i++){
//            CheckUtil.sleep(1000);
//            String  json=HttpClientUtilPro.httpGetRequest("https://www.chainfor.com/home/list/news/data.do?device_type=0&categoryId=11&pageNo="+i,RequestCount);
//            JSONObject jsonObject=JSONObject.fromObject(json);
//            System.out.println(jsonObject);
//            JSONArray jsonArray=jsonObject.getJSONArray("list");
//            for(Object o:jsonArray){
//                JSONObject jsonObject1=JSONObject.fromObject(o);
//                String title=jsonObject1.getString("title");
//                System.out.println("标题 : "+title);
//
//                String summary=jsonObject1.getString("introduction");
//                System.out.println("简介 : "+summary);
//
//                String author=jsonObject1.getString("nickName");
//                System.out.println("作者 : "+author);
//
//                String search_key=jsonObject1.getString("lable").replace("，",",");
//                System.out.println("关键字 : "+search_key);
//
//                String pic_url="https://lianxiangfiles.oss-cn-beijing.aliyuncs.com/"+jsonObject1.getString("imgUrl")+"?x-oss-process=style/news";
//                System.out.println("图片地址 : "+pic_url);
//
//                String href_addr="https://www.chainfor.com/news/show/"+jsonObject1.getString("newDetailId")+".html";
//                System.out.println("新闻地址 : "+href_addr);
//
//                String PublishTime=jsonObject1.getJSONObject("createDate").getString("time");
//                PublishTime=DateUtil.stampToDate(PublishTime);
//                System.out.println("更新时间 "+PublishTime);
//                String types="深度";
//                String from_site=jsonObject1.getString("source");
//                System.out.println("来源 "+from_site);
//
//                String from_interface="LIANXIANG_NEWS";
//                String news_type_id="12";
//                System.out.println("--------------------------------");
//                dbUtil.insertAndQuery( "insert into news_info " +
//                        "(`status`,`types`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
//                        " values (?,?,?,?,?,?,?,?,?,?,?,?,?)",title,href_addr,new Object[] {"up",types,news_type_id,title,search_key,author,summary,pic_url,href_addr,PublishTime,from_site,from_interface,PublishTime});
//
//            }
//
//        }
//
//
//    }




    @Test
    //挖链网 //未
    public  void WalianDownloadPage(){
        Map map=new HashMap<>();
        map.put("channelId","26");
        map.put("rows","20");
        for(int i=1;i<=583;i++){
            map.put("page",i);
            String  document= HttpClientUtilPro.httpPostRequest("https://api.walian.cn/api/article-lists",map,RequestCount);
            JSONObject jsonObject=JSONObject.fromObject(document);
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
                        "(`status`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                        " values (?,?,?,?,?,?,?,?,?,?,?,?)",title,href_addr,new Object[] {"up",news_type_id,title,search_key,author,summary,pic_url,href_addr,PublishTime,from_site,from_interface,PublishTime});

            }


        }

    }
    @Test
    //陀螺财经 //未
    public void Tuoluocaijing(){
        String tuo= HttpClientUtilPro.httpGetRequest("https://www.tuoluocaijing.cn/api/article/get_list?page_num=1&limit=5000&toutiao=1",RequestCount);
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
            System.out.println("图片 "+pic_url);

            String href_addr="https://www.tuoluocaijing.cn/article/detail-"+jsonObject2.getString("id")+".html";
            System.out.println("新闻地址 "+href_addr);

            String PublishTime=jsonObject2.getString("audit_time");
            System.out.println("更新时间 "+PublishTime);

            String from_site="陀螺财经";
            String from_interface="陀螺财经";
            String news_type_id="12";
            dbUtil.insertAndQuery( "insert into news_info " +
                    "(`status`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                    " values (?,?,?,?,?,?,?,?,?,?,?,?)",title,href_addr,new Object[] {"up",news_type_id,title,search_key,author,summary,pic_url,href_addr,PublishTime,from_site,from_interface,PublishTime});

        }
    }

    //深链财经 推荐 all
    @Test
    public void shenliancaijing(){
        int i=1;
        while (true){
            Map map=new HashMap();
            map.put("id",8);
            map.put("limit",50);
            map.put("page",i);
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

                String pic_url="https://www.shenliancaijing.com/"+jsonObject2.getString("pic");
                System.out.println("图片 "+pic_url);

                String href_addr=jsonObject2.getString("url");
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


            i++;
            System.out.println("---"+i);
        }

    }

    //https://www.chainnews.com/ 链闻
    @Test
    public void lianwen(){
        for(int i=1;i<110;i++){
            // https://www.chainnews.com/api/articles/?t=%E9%A1%B9%E7%9B%AE&ts=1547005264 酷项目
            String kujson= HttpClientUtilPro.httpGetRequest("https://www.chainnews.com/api/articles/?t=%E9%A1%B9%E7%9B%AE&page="+i,RequestCount);
            JSONObject jsonObject=JSONObject.fromObject(kujson);
            JSONArray jsonArray=jsonObject.getJSONArray("results");
            for(Object o:jsonArray){
                JSONObject jsonObject1=JSONObject.fromObject(o);
                String title=jsonObject1.getString("title");
                System.out.println("标题 "+title);
                String author=jsonObject1.getString("author_name");
                System.out.println("作者 "+author);

                List search_keys=new ArrayList();
                JSONArray jsonArray1=jsonObject1.getJSONArray("tag_list");
                for(Object o1:jsonArray1){
                    search_keys.add(JSONObject.fromObject(o1).getString("name"));
                }
                String search_key=CheckUtil.getSearchKey(search_keys);
                System.out.println("关键字 "+search_key);

                String summary=jsonObject1.getString("digest");
                System.out.println("简介 "+summary);

                String pic_url=jsonObject1.getString("cover_url").split("\\?")[0];
                System.out.println("图片 "+pic_url);

                String href_addr="https://www.chainnews.com"+jsonObject1.getString("absolute_url");
                System.out.println("新闻地址 "+href_addr);

                String PublishTime=DateUtil.getHecaijingLater(jsonObject1.getString("pp_time"));
                System.out.println("更新时间 "+PublishTime);
                String from_site="链闻";
                String from_interface="链闻";
                String news_type_id="12";
                String types="公司";
                dbUtil.insertAndQuery( "insert into news_info " +
                        "(`status`,`types`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                        " values (?,?,?,?,?,?,?,?,?,?,?,?,?)",title,href_addr,new Object[] {"up",types,news_type_id,title,search_key,author,summary,pic_url,href_addr,PublishTime,from_site,from_interface,PublishTime});

            }
        }
    }

    //https://www.hellobtc.com/kp/kc/index_1.html 白话区块链 入门课 --->  新手必读
    @Test
    public void baihuaqukuailiang(){
        Integer i = 2;
        a:
        while (true) {
            Document document = JsoupUtilPor.get("https://www.hellobtc.com/kp/kc/index_"+i+".html", RequestCount);
            Elements elements = document.select("ul.contentlist").select("li");
            for (Element element : elements) {
                String title = element.select("a.caption").get(0).text();
                System.out.println("标题 " + title);

                String surl = element.select("a.caption").get(0).absUrl("href");
                Document document1 = JsoupUtilPor.get(surl, RequestCount);
//            System.out.println(document1);
                String html = document1.select("hgroup").get(0).select("h5").get(0).html();

                System.out.println("html " + html);

//          System.out.println("标题 "+title);
                String author = html.substring(html.indexOf("作者：") + 3, html.indexOf("来源：")).replace("</span>", "").replace("<span>", "");

                System.out.println("作者 " + author);
                String search_key = "";
                System.out.println("关键字 " + search_key);
                String summary = element.select("a.intro").get(0).text();
                String pic_url = element.select("img").get(0).absUrl("src");
                String href_addr = element.select("a.caption").get(0).attr("href");
                String PublishTime = html.substring(0, html.indexOf("<span>"));
                System.out.println("简介 " + summary);
                System.out.println("图片 " + pic_url);
                System.out.println("新闻地址 " + href_addr);
                System.out.println("更新时间 " + PublishTime);
//              System.exit(0);

                String from_site = "白话区块链";
                String from_interface = "白话区块链";
                String news_type_id = "12";
                String types = "新手必读";
                dbUtil.insertAndQuery("insert into news_info " +
                                "(`status`,`types`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                                " values (?,?,?,?,?,?,?,?,?,?,?,?,?)", title, href_addr,
                        new Object[]{"up", types, news_type_id, title, search_key, author, summary, pic_url, href_addr, PublishTime, from_site, from_interface, PublishTime});


                System.out.println("===============================");
            }
            i++;
            if (i>6){
                break a;
            }
        }
    }


    // https://www.odaily.com/api/pp/api/user/2147487207/posts?b_id=&per_page=50
    @Test
    public void shitaishuo(){       // 师太说
        String x = HttpClientUtilPro.httpGetRequest("https://www.odaily.com/api/pp/api/user/2147487207/posts?b_id=&per_page=50", RequestCount);
        JSONObject jsonObject = JSONObject.fromObject(x);
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("items");
//        JSONObject jsonObject2 = JSONObject.fromObject(jsonArray);
//        JSONArray jsonArray1 = jsonObject2.getJSONArray("items");

        for (Object o : jsonArray){
            JSONObject jsonObject1 = JSONObject.fromObject(o);
            String title = jsonObject1.getString("title");
            System.out.println("标题 " + title);
            String author = "师太说区块链";
            System.out.println("作者 " + author);
            String search_key = "";
            System.out.println("关键字 " + search_key);
            String summary = jsonObject1.getString("summary");
            System.out.println("简介 " + summary);
            String pic_url = jsonObject1.getString("cover").substring(0,jsonObject1.getString("cover").length()-8);
            System.out.println("图片 " + pic_url);
            String href_addr = "https://www.odaily.com/post/"+jsonObject1.getString("id");
            System.out.println("新闻地址 " + href_addr);
            String PublishTime = jsonObject1.getString("published_at");
            System.out.println("更新时间 " + PublishTime);
            String from_site = "师太说区块链";
            String from_interface = "师太说区块链";
            String news_type_id = "12";
            String types = "新手必读";

            dbUtil.insertAndQuery("insert into news_info " +
                            "(`status`,`types`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                            " values (?,?,?,?,?,?,?,?,?,?,?,?,?)", title, href_addr,
                    new Object[]{"up", types, news_type_id, title, search_key, author, summary, pic_url, href_addr, PublishTime, from_site, from_interface, PublishTime});


            System.out.println("===============================");
        }

    }
    // https://www.cybtc.com/forum.php?mod=forumdisplay&fid=120&filter=typeid&typeid=56
    @Test
    public void wakuangji() {     // 彩云比特 （挖矿  上线前跑全部的  然后只跑一次就可以了）
        Integer t = 1;
        a:
        while (true) {
//            Document document = JsoupUtilPor.get("http://www.wabi.com/news/mining", RequestCount);  // 第一页数据
            Document document = JsoupUtilPor.get("http://www.wabi.com/news/mining/page_"+t+".html", RequestCount);  // 第二页以后的数据
//            System.out.println(document);
            Elements elements = document.select("div.news-list").select("ul#newslist-all").select("li");
            for (int i = 1; i < elements.size() - 1; i++) {
                Element element = elements.get(i);
                System.out.println(element);
//                String id = element.attr("id");
//                if (id == null || id.equals("")) {
//                    continue;
//                }
                String title = element.select("h3").get(0).select("a").get(0).text();
                System.out.println("标题 " + title);
                String href_addr = element.select("div.img").get(0).select("a").get(0).attr("href");
                System.out.println("新闻地址 " + href_addr);
                Document document1 = JsoupUtilPor.get(href_addr, RequestCount);
//                System.out.println(document1);
                String author = document1.select("div.box-l").select("span.date").text();
                String PublishTime = author.substring(0,19);
                System.out.println("更新时间 " + PublishTime);
                author = author.substring(19,author.length());
                if (author.length() == 0){
                    author = "挖币网";
                }else {
                    author = author.substring(2,author.length());
                }
                System.out.println("作者 " + author);
                String search_key = "";
                System.out.println("关键字 " + search_key);
                String summary = "";
                System.out.println("简介 " + summary);
                String pic_url = element.select("div.img").get(0).select("img").get(0).absUrl("src");
                System.out.println("图片 " + pic_url);
                String from_site = "挖币网";
                String from_interface = "挖币网";
                String news_type_id = "12";
                String types = "挖矿";
                dbUtil.insertAndQuery("insert into news_info " +
                                "(`status`,`types`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                                " values (?,?,?,?,?,?,?,?,?,?,?,?,?)", title, href_addr,
                        new Object[]{"up", types, news_type_id, title, search_key, author, summary, pic_url, href_addr, PublishTime, from_site, from_interface, PublishTime});
                System.out.println("====================================================================================");
            }

            t ++;
            if (t>1000){
                break a;
            }
        }
    }


    @Test
    public void biyuan() {     // 币源 （挖矿  上线前跑全部的  然后只跑一次就可以了）
        Integer t = 1;
        a:
        while (true) {
            Document document = JsoupUtilPor.get("https://www.coingogo.com/news/index/"+t+"?parent=9", RequestCount);
            Elements elements = document.select("div.news-comment").select("ul.clearfix").select("li");
            for (int i = 0; i < elements.size(); i++) {
                Element element = elements.get(i);
//                System.out.println(element);
                String title = element.select("div.news-content-text").get(0).select("a").get(0).text();
                System.out.println("标题 " + title);
                String href_addr = element.select("a").get(0).absUrl("href");
                System.out.println("新闻地址 " + href_addr);
                String PublishTime = element.select("p.news-content-time").select("span.news-content-time-span1").text()+DateUtil.ForDate(new Date(), " hh:mm:ss");
                System.out.println("更新时间 " + PublishTime);
                String author = element.select("div.news-content-text1").select("div.news-content-text-right-name").text();
                System.out.println("作者 " + author);
                String search_key = "";
                System.out.println("关键字 " + search_key);
                String summary = "";
                System.out.println("简介 " + summary);
                String pic_url = element.select("div.news-content-text1").get(0).select("div.news-content-text-right").get(0).select("img").attr("src");
                System.out.println("图片 " + pic_url);
                String from_site = "币源";
                String from_interface = "币源";
                String news_type_id = "12";
                String types = "挖矿";
                dbUtil.insertAndQuery("insert into news_info " +
                                "(`status`,`types`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                                " values (?,?,?,?,?,?,?,?,?,?,?,?,?)", title, href_addr,
                        new Object[]{"up", types, news_type_id, title, search_key, author, summary, pic_url, href_addr, PublishTime, from_site, from_interface, PublishTime});
                System.out.println("====================================================================================");
            }
            t++;
            if (t>100){
                break a;
            }
        }
    }
}










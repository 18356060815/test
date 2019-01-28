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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.jnk.test.util.CheckUtil.getTrace;
import static com.jnk.test.util.DateUtil.getHecaijingLater;
import static com.jnk.test.util.DateUtil.getLiandedeLater;


/**
 * 分类信息
 * **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Service
public class DowloadPage_task {
    private static final Logger logger = LoggerFactory.getLogger(DowloadPage_task.class);
    final static  int  RequestCount=1;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    DBUtil dbUtil;
//    //火球  1
//    //22
//    public void HuoqiuDownloadPage() {
//        Document document = JsoupUtilPor.get("http://www.huoqiucaijing.com/Home/information",RequestCount);
//        Elements elements = document.select("div.content_article_tab").select("label");
//        for (Element element : elements) {
//            String key = element.text();
//            if (key.equals("最新") || key.equals("推荐") || key.equals("头条")) {
//                continue;
//            }
//                String url="http://www.huoqiucaijing.com"+element.attr("data-requestData");
//                String v= HttpClientUtilPro.httpPostRequest(url,RequestCount);
//                JSONObject jsonObject=JSONObject.fromObject(v);
//                String  jsonArray=jsonObject.getString("msg");
//                JSONArray jsonArray1=JSONArray.fromObject(jsonArray);
//
//                if(jsonArray1.size()==0){
//                    break;
//                }
//
//                for(Object obj:jsonArray1) {
//                    try {
//                        JSONObject jsonObject1 = JSONObject.fromObject(obj);
//                        System.out.println(jsonObject1);
//
//                        JSONObject jsonObject2 = jsonObject1.getJSONObject("ArticleInfo");
//                        String types = key;//标签词
//                        String title = jsonObject2.getString("Title");//标题
//                        String author = jsonObject2.getString("Author");//作者
//
//                        String search_key = jsonObject2.getString("Tag");//标签 xx,xx,xx
//
//                        String summary = jsonObject2.getString("ShortDescription");//简介
//
//                        String pic_url = jsonObject2.getString("ImgUrl");//图片链接
//                        String href_addr = "http://www.huoqiucaijing.com/Content/information?data=" + jsonObject1.getString("data1");//新闻内容地址
//
//                        String PublishTime = jsonObject2.getString("UpdateTime");
//                        if (PublishTime == null || PublishTime.equals("")) {
//                            PublishTime = CheckUtil.dateToString(new Date());//publishtime
//
//                        }
//                        if(title!=null&&summary!=null&&pic_url!=null&&href_addr!=null&&author!=null) {
//                            String from_site=author;
//                            dbUtil.insertAndQuery("insert into news_info " +
//                                    "(`status`,`news_type_id`,`types`,`title`,`author`,`search_key`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`)" +
//                                    " values " +
//                                    "('up','" + 12 + "','" + types + "','" + title + "','" + author + "','" + search_key + "','" + summary + "','" + pic_url + "','" + href_addr + "','" + PublishTime + "','" + from_site + "','HUOQIU_NEWS');", title,href_addr);
//
//                        }
//                    } catch (Throwable e) {
//                        logger.error(getTrace(e));
//                        e.printStackTrace();
//                    }
//
//                }
//
//
//        }
//    }
    @Test
    public void BabiteDownloadPage(){
        ///巴比特 带标签数据 1
        //19
        Document document=null;
        try {
            document= Jsoup.connect("https://www.8btc.com").header("Referer","https://www.baidu.com/link?url=SpSgaS4MWk768w7_yN7yq2SyBAG_7MkeJGQpmFq2QLi&wd=&eqid=b6a850830001a7a7000000045bd4041d").get();
        }catch (IOException e){
            e.printStackTrace();
        }
        if (document==null)
            return;
        Elements elements=document.select("script");
        Element element=elements.get(elements.size()-4);
        String  item=element.html();

        JSONObject json_b= JSONObject.fromObject(item.substring(25,item.indexOf(";(function(){var s")));
        JSONArray jsonArray=json_b.getJSONObject("menu").getJSONArray("news");
        for(int i=1;i<jsonArray.size();i++){
            JSONObject jsonObject=JSONObject.fromObject(jsonArray.getJSONObject(i));
            String key=jsonObject.getString("name");
            if (key.equals("最新") || key.equals("推荐") || key.equals("头条")) {
                continue;
            }
            String id=jsonObject.getString("id");
            System.out.println(key);
            String rs=HttpClientUtilPro.httpGetRequest("https://app.blockmeta.com/w1/news/list?post_type=post&num=20&cat_id="+id,RequestCount);
            JSONObject jsonObject1=JSONObject.fromObject(rs);
            JSONArray jsonArray1=jsonObject1.getJSONArray("list");
            for(Object  obj:jsonArray1){
                try {
                    JSONObject jsonObject2=JSONObject.fromObject(obj);
                    String types=key;//标签词
                    String title=jsonObject2.getString("title");//标题
                    String author=jsonObject2.getJSONObject("author_info").getString("display_name");//作者

                    String search_key="";//标签 xx,xx,xx

                    String summary=jsonObject2.getString("desc");//简介

                    String pic_url=jsonObject2.getString("image");//图片链接
                    String href_addr="https://www.8btc.com/article/"+jsonObject2.getString("id");//新闻内容地址
                    String PublishTime=jsonObject2.getString("post_date_format");//publishtime
                    if(PublishTime==null||PublishTime.equals("")){
                        PublishTime=CheckUtil.dateToString(new Date());//publishtime

                    }
                    if(title!=null&&summary!=null&&pic_url!=null&&href_addr!=null&&author!=null) {
                        String from_site=author;
                        dbUtil.insertAndQuery( "insert into news_info " +
                                "(`status`,`news_type_id`,`types`,`title`,`author`,`search_key`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`)" +
                                " values (?,?,?,?,?,?,?,?,?,?,?,?);",title,href_addr,new Object[]{"up","12",types,title,author,search_key,summary,pic_url,href_addr,PublishTime,from_site,"BABITE_NEWS"});

                        //"('up','" + 12 + "','" + types + "','" + title + "','" + author + "','" + search_key + "','" + summary + "','" + pic_url + "','" + href_addr + "','" + PublishTime + "','" + from_site + "','BABITE_NEWS');", title,href_addr);
                    }
                    }catch (Throwable e) {
                    logger.error(getTrace(e));
                    e.printStackTrace();
                }



            }


        }

    }
    @Test
    public  void JinseDownloadPage(){
        //金色财经 带标签数据  倒序 优先有标签数据 最后推荐 避免其他标签无数据
        //20
        Document document=JsoupUtilPor.get("https://www.jinse.com/",RequestCount);
        Elements elements=document.select("ul.index-main-nav");
        Elements elements1=elements.select("a");


        for(int l=elements1.size()-1;l>=0;l--){
            String key=elements1.get(l).attr("title");
            String url_end=elements1.get(l).attr("@click");
            System.out.println("标签 : "+key);
            if (url_end==null||"".equals(url_end)){
                continue;
            }
//            if (key.equals("最新") || key.equals("推荐") || key.equals("头条")) {
//                key=null;
//            }
            url_end=url_end.substring(url_end.indexOf(", '")+3,url_end.indexOf("',"));
            String  information_id="0";

                String url="https://api.jinse.com/v6/information/list?catelogue_key="+url_end+"&limit=23&information_id="+information_id+"&flag=down&version=9.9.9";
                String s=HttpClientUtilPro.httpGetRequest(url,RequestCount);
                JSONObject jsonObject=JSONObject.fromObject(s);
                information_id=jsonObject.getString("bottom_id");
                JSONArray jsonArray=jsonObject.getJSONArray("list");

            System.err.println("------"+jsonArray);
                if(jsonArray.size()!=0){
                    for(int i=0;i<jsonArray.size();i++){
                        try {
                            JSONObject jsonObject2=jsonArray.getJSONObject(i);
                            if("1".equals(jsonObject2.getString("type"))) {

                                String types = key;//标签词
                                String title = jsonObject2.getString("title");//标题
                                String author = jsonObject2.getJSONObject("extra").getOrDefault("author", "").toString();//作者

                                String search_key = "";//标签 xx,xx,xx
                                System.err.println(jsonObject2.getJSONObject("extra"));
                                String summary = jsonObject2.getJSONObject("extra").getString("summary");//简介


                                String pic_url = jsonObject2.getJSONObject("extra").getString("thumbnail_pic");//图片链接
                                String href_addr = jsonObject2.getJSONObject("extra").getString("topic_url");//新闻内容地址
                                String PublishTime = jsonObject2.getJSONObject("extra").getString("published_at");//publishtime
                                System.out.println(PublishTime);

                                if (PublishTime.equals("") || PublishTime == null) {
                                    PublishTime = CheckUtil.dateToString(new Date());//publishtime
                                } else {
                                    PublishTime = CheckUtil.stampToDate(PublishTime, true);
                                }
                                System.out.println(PublishTime);
                                if (title != null && summary != null && pic_url != null && href_addr != null && author != null) {
                                    String from_site = author;
                                    dbUtil.insertAndQuery("insert into news_info " +
                                            "(`status`,`news_type_id`,`types`,`title`,`author`,`search_key`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`)" +
                                            " values (?,?,?,?,?,?,?,?,?,?,?,?);", title, href_addr, new Object[]{"up", "12", types, title, author, search_key, summary, pic_url, href_addr, PublishTime, from_site, "JINSE_NEWS"});

                                    //"('up','" + 12 + "','" + types + "','" + title + "','" + author + "','" + search_key + "','" + summary + "','" + pic_url + "','" + href_addr + "','" + PublishTime + "','" + from_site + "','JINSE_NEWS');", title,href_addr);

                                }


                            } else {
                                continue;
                            }

                        }catch (Throwable e) {
                            logger.error(getTrace(e));
                            e.printStackTrace();
                        }

                    }




                }


        }
    }

    @Test
    public   void HuoXingDownloadPage(){

        //火星
        //21
        Document document = JsoupUtilPor.get("http://www.huoxing24.com/",RequestCount);
        Elements elements = document.select("ul#newsTabs").select("li");



        for(int l=elements.size()-1;l>=0;l--){

            String key = elements.get(l).text();
            System.err.println(key);
//            if (key.equals("最新") || key.equals("推荐") || key.equals("头条")|| key.equals("火星号")) {
//                continue;
//            }
            String id =  elements.get(l).attr("data-id");

            String x=HttpClientUtilPro.httpGetRequest("http://www.huoxing24.com/info/news/shownews?currentPage=1&pageSize=20&channelId="+id,RequestCount);
            JSONObject jsonObject1=JSONObject.fromObject(x);

            JSONObject jsonObject3 = jsonObject1.getJSONObject("obj");
            JSONArray jsonArray = jsonObject3.getJSONArray("inforList");
            for (int j = 0; j < jsonArray.size(); j++) {
                String types = key;//标签词
                JSONObject jsonObject2 = jsonArray.getJSONObject(j);

                String title = jsonObject2.getString("title");//标题
                System.out.println("标题 : " + title);

                String author = jsonObject2.getString("author");//作者
                System.out.println("作者 : " + author);

                String search_key = jsonObject2.getString("tags");//标签 xx,xx,xx
                System.out.println("标签 : " + search_key);

                String summary = jsonObject2.getString("synopsis");//简介
                System.out.println("简介 : " + summary);

                String pic_url = "";
                pic_url = jsonObject2.getJSONObject("coverPic").getString("pc");//图片链接
                System.out.println("图片 : " + pic_url);

                String href_addr = "http://www.huoxing24.com/newsdetail/" + jsonObject2.getString("id") + ".html";//新闻内容地址
                System.out.println("新闻地址 : " + href_addr);

                String PublishTime = jsonObject2.getString("publishTime");//publishtime
                if(PublishTime.equals("")||PublishTime==null){
                    PublishTime=CheckUtil.dateToString(new Date());//publishtime
                }else {
                    PublishTime=CheckUtil.stampToDate(PublishTime,false);
                }
                System.out.println("时间戳 : " + PublishTime);
                if(title!=null&&summary!=null&&pic_url!=null&&href_addr!=null&&author!=null) {
                    String from_site=author;
                    dbUtil.insertAndQuery( "insert into news_info " +
                            "(`status`,`news_type_id`,`types`,`title`,`author`,`search_key`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`)" +
                            " values (?,?,?,?,?,?,?,?,?,?,?,?);",title,href_addr,new Object[]{"up","12",types,title,author,search_key,summary,pic_url,href_addr,PublishTime,from_site,"HUOXING_NEWS"});

                    //"('up','" + 12 + "','" + types + "','" + title + "','" + author + "','" + search_key + "','" + summary + "','" + pic_url + "','" + href_addr + "','" + PublishTime + "','" + from_site + "','HUOXING_NEWS');", title,href_addr);

                }
            }
        }
    }


    @Test
    public  void JinNiuDownloadPage(){
        String x = HttpClientUtilPro.httpGetRequest("http://www.jinniu.cn/prefix/info/medias/categories",RequestCount);
        JSONObject jsonObject=JSONObject.fromObject(x);
        JSONArray jsonArray=jsonObject.getJSONArray("data");//提取栏目列



        for(int i=jsonArray.size()-1;i>=0;i--){
            JSONObject jsonObject1=JSONObject.fromObject(jsonArray.get(i));
            String key=jsonObject1.getString("nameWeb");//获取电脑端的栏目名称 app端：nameApp
            String id=jsonObject1.getString("id");//获取id 用于访问栏目下新闻url拼接
//            if (key.equals("最新") || key.equals("推荐") || key.equals("头条")) {
//                continue;
//            }
            if(key.equals("深度")){
                key="观点";
                }
            if(key.equals("项目")){
                key="应用";
            }
                String a= HttpClientUtilPro.httpGetRequest("http://www.jinniu.cn/prefix/info/medias/index?id="+id+"&page="+1,RequestCount);
                JSONObject jsonObject2=JSONObject.fromObject(a);

                JSONArray jsonArray1=jsonObject2.getJSONObject("data").getJSONObject("medias").getJSONArray("list");
                for(Object  object:jsonArray1){
                    JSONObject jsonObject3=JSONObject.fromObject(object);
                    System.out.println(jsonObject3);

                    String types=key;//标签词
                    System.out.println("标签词 : "+types);

                    String title=jsonObject3.getJSONObject("media").getString("title");//标题
                    System.out.println("标题 : "+title);

                    String author=jsonObject3.getJSONObject("media").getString("editor");//作者
                    System.out.println("作者 : "+author);

                    JSONArray search_key_arr=jsonObject3.getJSONObject("media").getJSONArray("tags");//标签 xx,xx,xx
                    String search_key="";
                    if(search_key_arr.size()==0){
                        search_key="";
                    }else {
                        for(Object sk:search_key_arr){
                            search_key+=sk+",";
                        }
                        search_key=search_key.substring(0,search_key.length()-1);
                    }
                    System.out.println("标签 : "+search_key);

                    String summary=jsonObject3.getJSONObject("media").getString("articleSummary");//简介
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
                     if(title!=null&&summary!=null&&pic_url!=null&&href_addr!=null&&author!=null){
                         String from_site=author;
                         dbUtil.insertAndQuery( "insert into news_info " +
                                 "(`status`,`news_type_id`,`types`,`title`,`author`,`search_key`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`)" +
                                 " values (?,?,?,?,?,?,?,?,?,?,?,?);",title,href_addr,new Object[]{"up","12",types,title,author,search_key,summary,pic_url,href_addr,PublishTime,from_site,"JINNIU_NEWS"});

                         //"('up','" + 12 + "','" + types + "','" + title + "','" + author + "','" + search_key + "','" + summary + "','" + pic_url + "','" + href_addr + "','" + PublishTime + "','" + from_site + "','JINNIU_NEWS');", title,href_addr);

                }
            }
        }
    }

//    @Test //only最新 币莱财经
//    public  void  BilaiDownloadPage(){
//            Document html=JsoupUtilPor.get("https://www.niubilai.com/index/index/ajaxpage?page=1&cateid=71",RequestCount);
//            Elements elements=html.select("div.story");
//
//            for(Element element:elements){
//                String title=element.select("div.thumbnail").select("img").attr("alt");//标题
//                System.out.println("标题 "+ title);
//
//                String author=element.select("div.info-item-cr").select("p.bb").select("a").text().trim();//作者
//                System.out.println("作者 "+ author);
//
//                String search_key=element.select("div.info-item-cr").select("p.bb").select("i").get(0).text().replace(" ",",");//关键字
//                System.out.println("关键字 "+ search_key);
//
//                String summary=element.select("div.info-item-cr").select("p.tt").text();//简介
//                System.out.println("简介 "+ summary);
//
//                String pic_url=" https://www.niubilai.com"+element.select("div.thumbnail").select("img").attr("src");//图片地址
//                pic_url=pic_url.trim();
//                System.out.println("图片地址 "+ pic_url);
//
//                String href_addr="https://www.niubilai.com"+element.select("div.thumbnail").select("a").attr("href");//新闻地址
//                System.out.println("新闻地址 "+ href_addr);
//
//                String PublishTime=element.select("div.info-item-cr").select("p.bb").select("i").get(1).text();//更新时间
//                if(PublishTime.indexOf("分钟前")!=-1){
//                    PublishTime=PublishTime.replace("• ","").replace("分钟前","").trim();
//                    PublishTime=CheckUtil.addTime(new Date(),-Integer.parseInt(PublishTime)).toString();
//                }
//                else if(PublishTime.indexOf("小时前")!=-1){
//                    PublishTime=PublishTime.replace("• ","").replace("小时前","").trim();
//                    PublishTime=CheckUtil.addTime(new Date(),-(Integer.parseInt(PublishTime)*60)).toString();
//                }
//                else {
//                    PublishTime=CheckUtil.dateToString(PublishTime.replace("• ",""));
//                }
//
//                System.out.println("更新时间 "+ PublishTime);
//                System.out.println("--------");
//
//                String from_site=author;
//                dbUtil.insertAndQuery( "insert into news_info " +
//                        "(`status`,`news_type_id`,`title`,`author`,`search_key`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`)" +
//                        " values (?,?,?,?,?,?,?,?,?,?,?);",title,href_addr,new Object[]{"up","12",title,author,search_key,summary,pic_url,href_addr,PublishTime,from_site,"BILAI_NEWS"});
//
//               // "('up','" + 12 + "','" + title + "','" + author + "','" + search_key + "','" + summary + "','" + pic_url + "','" + href_addr + "','" + PublishTime + "','" + from_site + "','BILAI_NEWS');", title,href_addr);
//
//            }
//
//
//    }









    //嘻哈财经  all
    @Test
    public  void XihaDownloadPage(){
        Document document=JsoupUtilPor.get("http://www.xiha.top/",RequestCount);
        Elements elements=document.select("ul.news-nav").select("li");
        for(int i=elements.size()-1;i>=0;i--){
            //if(dbUtil.queryIsexists(element.text().trim())||CheckUtil.Sign(element.text().trim())){//判断是否在标签库  或者 是否是推荐最新头条
                Map map=new HashMap();
                String cid=elements.get(i).attr("data-cid");
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

                        //"('up','" + 12 + "','" + types + "','" + title + "','" + author + "','" + search_key + "','" + summary + "','" + pic_url + "','" + href_addr + "','" + PublishTime + "','" + from_site + "','XIHA_NEWS');", title,href_addr);

                    }

                }

            //}

        }

    }


    @Test
    public void ZhihuDownloadPage() {
        String qukuailiang = "https://www.zhihu.com/topic/19901773/hot";
        String bitebi = "https://www.zhihu.com/topic/19600228/hot";
        String[] arr = {bitebi,qukuailiang};
        for (int i = 0; i < arr.length; i++) {
        Document firstHtml = JsoupUtilPor.get(arr[i], RequestCount);
        Elements elements = firstHtml.select("div.TopicFeedItem");
        for (Element element : elements) {
            //if(jsonObject1.getJSONObject("target").getString("type").equals("article")){//判断是文章还是问答
            Elements elements1 = element.select("div.ArticleItem");
            if (elements1.size() == 1) {
                String title = elements1.get(0).select("h2.ContentItem-title").text();//标题
                System.out.println("标题 " + title);

                String author = elements1.get(0).select("a.UserLink-link").select("img").get(0).attr("alt");//作者
                System.out.println("作者 " + author);

                String summary = elements1.get(0).select("span.CopyrightRichText-richText").text();//简介
                summary = Jsoup.parse(summary).text();
                System.out.println("简介 " + summary);

                String pic_url = "";
                if (elements1.get(0).select("div.VagueImage").size() != 0) {
                    pic_url = elements1.get(0).select("div.VagueImage").attr("data-src");//图片
                }
                System.out.println("图片 " + pic_url);

                String href_addr = elements1.get(0).select("h2.ContentItem-title").select("a").first().attr("abs:href");//新闻地址
                System.out.println("新闻地址 " + href_addr);

                String PublishTime = elements1.get(0).select("meta[itemProp=dateModified]").attr("content");//更新时间
                try {
                    PublishTime = PublishTime.replace("Z", " UTC");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
                    Date d = format.parse(PublishTime);
                    PublishTime = CheckUtil.dateToString(d);
                } catch (Throwable e) {
                    CheckUtil.getTrace(e);
                    continue;
                }
                System.out.println("更新时间 " + PublishTime);
                String from_site = author;
                String zhihu_kind = "article";
                String from_interface = "ZHIHU_NEWS";
                String news_type_id = "16";

                dbUtil.insertAndQuery( "insert into news_info " +
                        "(`status`,`zhihu_kind`,`news_type_id`,`title`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`)" +
                        " values (?,?,?,?,?,?,?,?,?,?,?)",title,href_addr,new Object[] {"up",zhihu_kind,news_type_id,title,author,summary,pic_url,href_addr,PublishTime,from_site,from_interface});
                // "('up','" + zhihu_kind + "','" + news_type_id + "','" + title + "','" + author + "','" + summary + "','" + pic_url + "','" + href_addr + "','" + PublishTime + "','" + from_site + "','"+from_interface+"','"+PublishTime+"');", title,href_addr);

            }

        }
    }
    }

    @Test
    //链得得应用---->
    public  void LiandedeDownloadPage(){

            Document document=JsoupUtilPor.get("https://www.chaindd.com/column/3041170/1",RequestCount);
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
                        "(`status`,`types`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`)" +
                        " values (?,?,?,?,?,?,?,?,?,?,?,?)",title,href_addr,new Object[] {"up",types,news_type_id,title,search_key,author,summary,pic_url,href_addr,PublishTime,from_site,from_interface});

            }

        }



    //核财经  观点+推荐最新
    @Test
    public  void HecaijingDownload(){
        //http://www.hecaijing.com/index/loadmore?type=recommend&pn=2
        Document document=JsoupUtilPor.get("http://www.hecaijing.com/shendu/",RequestCount);
        Document documents=JsoupUtilPor.get("http://www.hecaijing.com/",RequestCount);
        String typeid_guandian= document.select("a.active").attr("type");
        String typeid_tuijian= documents.select("a.active").attr("type");
        String arr[]={typeid_guandian,typeid_tuijian};
        for(int i=0;i<arr.length;i++){
        String jsons=HttpClientUtilPro.httpGetRequest("http://www.hecaijing.com/index/loadmore?type="+arr[i]+"&pn=1",RequestCount);
        System.out.println(jsons);
        JSONObject jsonObject=JSONObject.fromObject(jsons);
        if(jsonObject.getString("msg").equals("success")) {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            if (jsonArray.size() == 0) {
                return;
            }

                for (Object o : jsonArray) {
                    JSONObject jsonObject1 = JSONObject.fromObject(o);
                    String title = jsonObject1.getString("title");
                    System.out.println("标题 : " + title);

                    String summary = jsonObject1.getString("describe");
                    System.out.println("简介 : " + summary);

                    String author = jsonObject1.getString("source_site");
                    System.out.println("作者 : " + author);

                    String search_key = "";
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("tag");
                    for (Object o1 : jsonArray1) {
                        search_key += o1.toString() + ",";
                    }
                    if (!search_key.equals("")) {
                        search_key = search_key.substring(0, search_key.length() - 1);
                    }
                    System.out.println("关键字 : " + search_key);
                    String pic_url = jsonObject1.getString("img");
                    System.out.println("图片地址 : " + pic_url);
                    String href_addr = jsonObject1.getString("app_url");//新闻地址
                    System.out.println("新闻地址 : " + href_addr);

                    String PublishTime = jsonObject1.getString("publish_time");//创建时间
                    System.out.println("更新时间 " + PublishTime);
                    System.out.println("-------------------------------------------");
                    String types = "观点";
                    String from_site = author;
                    String from_interface = "HECAIJING_NEWS";
                    String news_type_id = "12";
                    dbUtil.insertAndQuery("insert into news_info " +
                            "(`status`,`types`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`)" +
                            " values (?,?,?,?,?,?,?,?,?,?,?,?)", title, href_addr, new Object[]{"up", types, news_type_id, title, search_key, author, summary, pic_url, href_addr, PublishTime, from_site, from_interface});

                }
            }

        }
    }

    //https://www.chainnews.com/ 链闻
    @Test
    public void lianwen(){

            // https://www.chainnews.com/api/articles/?t=%E9%A1%B9%E7%9B%AE&ts=1547005264 酷项目
            String kujson= HttpClientUtilPro.httpGetRequest("https://www.chainnews.com/api/articles/?t=%E9%A1%B9%E7%9B%AE",RequestCount);
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
                        "(`status`,`types`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`)" +
                        " values (?,?,?,?,?,?,?,?,?,?,?,?)",title,href_addr,new Object[] {"up",types,news_type_id,title,search_key,author,summary,pic_url,href_addr,PublishTime,from_site,from_interface});

            }
    }


//    @Test
//    //链向财经 精英视点--->深度
//    public void getLianxiangDownload(){
//        String arr[]={"https://www.chainfor.com/home/list/news/data.do","https://www.chainfor.com/home/list/news/data.do?device_type=0&categoryId=11&pageNo=1"};
//        for(int i=0;i<arr.length;i++){
//            //https://www.chainfor.com/home/list/news/data.do
//            String  json=HttpClientUtilPro.httpGetRequest(arr[i],RequestCount);
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
//                        "(`status`,`types`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`)" +
//                        " values (?,?,?,?,?,?,?,?,?,?,?,?)",title,href_addr,new Object[] {"up",types,news_type_id,title,search_key,author,summary,pic_url,href_addr,PublishTime,from_site,from_interface});
//
//            }
//        }
//
//        }

}


















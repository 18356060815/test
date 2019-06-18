package com.jnk.test.crawl;

import com.alibaba.fastjson.JSON;
import com.jnk.test.Service.DBUtil;
import com.jnk.test.util.*;
import net.minidev.json.JSONUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringEscapeUtils;
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
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Service
public class DowloadPage_task {

    private static final Logger logger = LoggerFactory.getLogger(DowloadPage_task.class);
    final static int RequestCount = 1;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    DBUtil dbUtil;

    //火球  数据  观点  报告
    //22
    @Test
    public void HuoqiuDownloadPage() {
        String[] arr = {"10", "11", "6","1"};
        for (String type : arr) {
            Map map = new HashMap();
            map.put("Type", type);
            map.put("PageIndex", "1");
            map.put("PageSize", "20");
            String data = HttpClientUtilPro.httpPostRequest("https://www.ihuoqiu.com/MAPI/GetArticleListData", map, RequestCount);
            data = JSON.parseObject(data, String.class);
            com.alibaba.fastjson.JSONObject jsonObjects = JSON.parseObject(data);
            com.alibaba.fastjson.JSONArray data1 = jsonObjects.getJSONArray("data");
            System.out.println(data1);
            for (Object obj : data1) {
                try {
                    JSONObject jsonObject1 = JSONObject.fromObject(obj);
                    System.out.println(jsonObject1);

                    JSONObject jsonObject2 = jsonObject1.getJSONObject("ArticleInfo");
                    String types = null;
                    switch (map.get("Type").toString()) {
                        case "10":
                            types = "行情";//实际是-->数据
                            break;
                        case "11":
                            types = "投研";//实际是-->报告
                            break;
                        case "6":
                            types = "观点";//实际是-->观点
                            break;
                        case "1":
                            types = null;//
                            break;
                    }
                    System.out.println("类型 : " + types);

                    String title = jsonObject2.getString("Title");//标题
                    System.out.println("标题 : " + title);

                    String author = jsonObject2.getString("Author");//作者
                    System.out.println("作者 : " + author);

                    String search_key = jsonObject2.getString("Tag");//标签 xx,xx,xx

                    search_key = search_key.replace(" ", ",");
                    if (search_key.endsWith(",")) {
                        search_key = search_key.substring(0, search_key.length() - 1);
                    }
                    System.out.println("标签 : " + search_key);

                    String summary = jsonObject2.getString("ShortDescription");//简介
                    System.out.println("简介 : " + summary);

                    String pic_url = jsonObject2.getString("ImgUrl");//图片链接
                    pic_url = pic_url.replace("!webslt", "");
                    System.out.println("图片链接 : " + pic_url);

                    String href_addr = "https://m.ihuoqiu.com/article?id=" + jsonObject1.getString("data1");//新闻内容地址
                    System.out.println("新闻内容地址 : " + href_addr);

                    String PublishTime = jsonObject2.getString("UpdateTime");
                    if (PublishTime == null || PublishTime.equals("")) {
                        PublishTime = CheckUtil.dateToString(new Date());//publishtime

                    }
                    System.out.println("publishtime : " + PublishTime);
                    System.out.println("类型 : " + types);

                    if (title != null && summary != null && pic_url != null && href_addr != null && author != null) {
                        String from_site = author;
                        String from_interface = "火球财经";

                        dbUtil.insertAndQuery("insert into news_info " +
                                "(`status`,`news_type_id`,`types`,`title`,`author`,`search_key`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                                " values (?,?,?,?,?,?,?,?,?,?,?,?,?);", title, href_addr, new Object[]{"up", "12", types, title, author, search_key, summary, pic_url, href_addr, PublishTime, from_site,from_interface,PublishTime});

                    }
                } catch (Throwable e) {
                    logger.error(getTrace(e));
                    e.printStackTrace();
                }

            }


        }
    }

    @Test
    public void BabiteDownloadPage() {
        ///巴比特 带标签挖矿 数据 1
        //19
        Document document = null;
        String data = HttpClientUtilPro.httpGetRequest("https://webapi.8btc.com/bbt_api/news/list?num=20&page=1&cat_id=6", RequestCount);
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
                logger.error(getTrace(e));
                e.printStackTrace();
            }


        }

        if (document == null)
            return;
//        Elements elements = document.select("script");
//        Element element = elements.get(elements.size() - 4);
//        String item = element.html();

//        JSONObject json_b = JSONObject.fromObject(item.substring(25, item.indexOf(";(function(){var s")));
//        JSONArray jsonArray = json_b.getJSONObject("menu").getJSONArray("news");

//        for (int i = 1; i < jsonArray.size(); i++) {
//            JSONObject jsonObject = JSONObject.fromObject(jsonArray.getJSONObject(i));
//            String key = jsonObject.getString("name");
//            if (key.equals("最新") || key.equals("推荐") || key.equals("头条")) {
//                continue;
//            }
//            String id = jsonObject.getString("id");
//            System.out.println(key);
//            String rs = HttpClientUtilPro.httpGetRequest("https://app.blockmeta.com/w1/news/list?post_type=post&num=20&cat_id=" + id, RequestCount);
//            JSONObject jsonObject1 = JSONObject.fromObject(rs);
//            JSONArray jsonArray1 = jsonObject1.getJSONArray("list");
//
//
//
//        }

    }

    @Test
    public void JinseDownloadPage() {
        //金色财经 带标签数据  倒序 优先有标签数据 最后推荐 避免其他标签无数据
        //20
        Document document = JsoupUtilPor.get("https://www.jinse.com/", RequestCount);
        Elements elements = document.select("ul.index-main-nav");
        Elements elements1 = elements.select("a");


        for (int l = elements1.size() - 1; l >= 0; l--) {
            String key = elements1.get(l).attr("title");
            String url_end = elements1.get(l).attr("@click");
            System.out.println("标签 : " + key);
            if (url_end == null || "".equals(url_end)) {
                continue;
            }
//            if (key.equals("最新") || key.equals("推荐") || key.equals("头条")) {
//                key=null;
//            }
            url_end = url_end.substring(url_end.indexOf(", '") + 3, url_end.indexOf("',"));
            String information_id = "0";

            String url = "https://api.jinse.com/v6/information/list?catelogue_key=" + url_end + "&limit=23&information_id=" + information_id + "&flag=down&version=9.9.9";
            String s = HttpClientUtilPro.httpGetRequest(url, RequestCount);
            JSONObject jsonObject = JSONObject.fromObject(s);
            information_id = jsonObject.getString("bottom_id");
            JSONArray jsonArray = jsonObject.getJSONArray("list");

            System.err.println("------" + jsonArray);
            if (jsonArray.size() != 0) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    try {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        if ("1".equals(jsonObject2.getString("type"))) {

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
                                String from_interface = "JINSE_NEWS";

                                dbUtil.insertAndQuery("insert into news_info " +
                                        "(`status`,`news_type_id`,`types`,`title`,`author`,`search_key`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                                        " values (?,?,?,?,?,?,?,?,?,?,?,?,?);", title, href_addr, new Object[]{"up", "12", types, title, author, search_key, summary, pic_url, href_addr, PublishTime, from_site, from_interface,PublishTime});

                                //"('up','" + 12 + "','" + types + "','" + title + "','" + author + "','" + search_key + "','" + summary + "','" + pic_url + "','" + href_addr + "','" + PublishTime + "','" + from_site + "','JINSE_NEWS');", title,href_addr);

                            }


                        } else {
                            continue;
                        }

                    } catch (Throwable e) {
                        logger.error(getTrace(e));
                        e.printStackTrace();
                    }

                }


            }


        }
    }

    @Test
    public void HuoXingDownloadPage() {

        //火星 币市转-->行情
        //21
        Document document = JsoupUtilPor.get("http://www.huoxing24.com/", RequestCount);
        Elements elements = document.select("ul#newsTabs").select("li");


        for (int l = elements.size() - 1; l >= 0; l--) {

            String key = elements.get(l).text();
            System.err.println(key);
//            if (key.equals("最新") || key.equals("推荐") || key.equals("头条")|| key.equals("火星号")) {
//                continue;
//            }
            if (key.equals("币市")) {
                key = "行情";
            }
            String id = elements.get(l).attr("data-id");

            String x = HttpClientUtilPro.httpGetRequest("http://www.huoxing24.com/info/news/shownews?currentPage=1&pageSize=20&channelId=" + id, RequestCount);
            JSONObject jsonObject1 = JSONObject.fromObject(x);

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
                if (PublishTime.equals("") || PublishTime == null) {
                    PublishTime = CheckUtil.dateToString(new Date());//publishtime
                } else {
                    PublishTime = CheckUtil.stampToDate(PublishTime, false);
                }
                System.out.println("时间戳 : " + PublishTime);
                if (title != null && summary != null && pic_url != null && href_addr != null && author != null) {
                    String from_site = author;
                    String from_interface = "HUOXING_NEWS";

                    dbUtil.insertAndQuery("insert into news_info " +
                            "(`status`,`news_type_id`,`types`,`title`,`author`,`search_key`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                            " values (?,?,?,?,?,?,?,?,?,?,?,?,?);", title, href_addr, new Object[]{"up", "12", types, title, author, search_key, summary, pic_url, href_addr, PublishTime, from_site, from_interface,PublishTime});

                    //"('up','" + 12 + "','" + types + "','" + title + "','" + author + "','" + search_key + "','" + summary + "','" + pic_url + "','" + href_addr + "','" + PublishTime + "','" + from_site + "','HUOXING_NEWS');", title,href_addr);

                }
            }
        }
    }


    @Test
    public void JinNiuDownloadPage() {
        String x = HttpClientUtilPro.httpGetRequest("http://www.jinniu.cn/prefix/info/medias/categories", RequestCount);
        JSONObject jsonObject = JSONObject.fromObject(x);
        JSONArray jsonArray = jsonObject.getJSONArray("data");//提取栏目列


        for (int i = jsonArray.size() - 1; i >= 0; i--) {
            JSONObject jsonObject1 = JSONObject.fromObject(jsonArray.get(i));
            String key = jsonObject1.getString("nameWeb");//获取电脑端的栏目名称 app端：nameApp
            String id = jsonObject1.getString("id");//获取id 用于访问栏目下新闻url拼接
//            if (key.equals("最新") || key.equals("推荐") || key.equals("头条")) {
//                continue;
//            }
            if (key.equals("深度")) {
                key = "观点";
            }
            if (key.equals("项目")) {
                key = "应用";
            }
            String a = HttpClientUtilPro.httpGetRequest("http://www.jinniu.cn/prefix/info/medias/index?id=" + id + "&page=" + 1, RequestCount);
            JSONObject jsonObject2 = JSONObject.fromObject(a);

            JSONArray jsonArray1 = jsonObject2.getJSONObject("data").getJSONObject("medias").getJSONArray("list");
            for (Object object : jsonArray1) {
                JSONObject jsonObject3 = JSONObject.fromObject(object);
                System.out.println(jsonObject3);

                String types = key;//标签词
                System.out.println("标签词 : " + types);

                String title = jsonObject3.getJSONObject("media").getString("title");//标题
                System.out.println("标题 : " + title);

                String author = jsonObject3.getJSONObject("media").getString("editor");//作者
                System.out.println("作者 : " + author);

                JSONArray search_key_arr = jsonObject3.getJSONObject("media").getJSONArray("tags");//标签 xx,xx,xx
                String search_key = "";
                if (search_key_arr.size() == 0) {
                    search_key = "";
                } else {
                    for (Object sk : search_key_arr) {
                        search_key += sk + ",";
                    }
                    search_key = search_key.substring(0, search_key.length() - 1);
                }
                System.out.println("标签 : " + search_key);

                String summary = jsonObject3.getJSONObject("media").getString("articleSummary");//简介
                System.out.println("简介 : " + summary);

                String pic_url = jsonObject3.getJSONObject("media").getString("imageUrl");//图片链接
                System.out.println("图片链接 : " + pic_url);

                String href_addr = "http://www.jinniu.cn/news/" + jsonObject3.getJSONObject("media").getString("id");//新闻内容地址
                System.out.println("新闻内容地址 : " + href_addr);

                String PublishTime = jsonObject3.getJSONObject("media").getString("createdAt");//publishtime

                if (PublishTime.equals("") || PublishTime == null) {
                    PublishTime = CheckUtil.dateToString(new Date());//publishtime
                } else {
                    PublishTime = CheckUtil.stampToDate(PublishTime, false);
                }
                System.out.println("时间戳 : " + PublishTime);
                if (title != null && summary != null && pic_url != null && href_addr != null && author != null) {
                    String from_site = author;
                    String from_interface = "JINNIU_NEWS";

                    dbUtil.insertAndQuery("insert into news_info " +
                            "(`status`,`news_type_id`,`types`,`title`,`author`,`search_key`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                            " values (?,?,?,?,?,?,?,?,?,?,?,?,?);", title, href_addr, new Object[]{"up", "12", types, title, author, search_key, summary, pic_url, href_addr, PublishTime, from_site, from_interface,PublishTime});

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
    public void XihaDownloadPage() {
        Document document = JsoupUtilPor.get("http://www.xiha.top/", RequestCount);
        Elements elements = document.select("ul.news-nav").select("li");
        for (int i = elements.size() - 1; i >= 0; i--) {
            //if(dbUtil.queryIsexists(element.text().trim())||CheckUtil.Sign(element.text().trim())){//判断是否在标签库  或者 是否是推荐最新头条
            Map map = new HashMap();
            String cid = elements.get(i).attr("data-cid");
            System.out.println(cid);
            map.put("cid", cid);
            map.put("stale_ids", "252,706,853,26495");
            map.put("page", 1);
            Map heardMap = new HashMap();
            heardMap.put("X-Requested-With", "XMLHttpRequest");

            String jsondata = HttpClientUtilPro.httpPostRequest("http://www.xiha.top/portal/index/ajaxgetarticle.html", heardMap, map, 1);
            JSONObject jsonObject = JSONObject.fromObject(jsondata);
            System.out.println(jsonObject);
            if ("1".equals(jsonObject.getString("code"))) {
                JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");
                for (Object obj : jsonArray) {
                    JSONObject jsonObject1 = JSONObject.fromObject(obj);
                    String types = jsonObject1.getString("c_name");//类型 types
                    System.out.println("类型 " + types);

                    String title = jsonObject1.getString("post_title");//标题
                    System.out.println("标题 " + title);

                    String author = jsonObject1.getString("user_nickname");//作者
                    System.out.println("作者 " + author);

                    String search_key = "";//关键字
                    System.out.println("关键字 " + search_key);

                    String summary = jsonObject1.getString("post_excerpt");//简介
                    System.out.println("简介 " + summary);

                    String pic_url = jsonObject1.getString("thumbnail");//图片
                    System.out.println("图片 " + pic_url);

                    String href_addr = "http://www.xiha.top" + jsonObject1.getString("url");//新闻地址
                    System.out.println("新闻地址 " + href_addr);

                    String PublishTime = jsonObject1.getString("published_time");//更新时间
                    System.out.println("更新时间 " + PublishTime);
                    String from_site = author;
                    String from_interface = "XIHA_NEWS";

                    dbUtil.insertAndQuery("insert into news_info " +
                            "(`status`,`news_type_id`,`types`,`title`,`author`,`search_key`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                            " values (?,?,?,?,?,?,?,?,?,?,?,?,?);", title, href_addr, new Object[]{"up", "12", types, title, author, search_key, summary, pic_url, href_addr, PublishTime, from_site,from_interface, PublishTime});

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
        String[] arr = {bitebi, qukuailiang};
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

                    dbUtil.insertAndQuery("insert into news_info " +
                            "(`status`,`zhihu_kind`,`news_type_id`,`title`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                            " values (?,?,?,?,?,?,?,?,?,?,?,?)", title, href_addr, new Object[]{"up", zhihu_kind, news_type_id, title, author, summary, pic_url, href_addr, PublishTime, from_site, from_interface,PublishTime});
                    // "('up','" + zhihu_kind + "','" + news_type_id + "','" + title + "','" + author + "','" + summary + "','" + pic_url + "','" + href_addr + "','" + PublishTime + "','" + from_site + "','"+from_interface+"','"+PublishTime+"');", title,href_addr);

                }

            }
        }
    }

    @Test
    //链得得应用 + 政策---->
    public void LiandedeDownloadPage() {
        String url = "https://www.chaindd.com/column/3041170/1";
        String urlzc = "https://www.chaindd.com/column/3029165/1";
        List<String> l = new ArrayList();
        l.add(url);
        l.add(urlzc);
        Map<String, String> map = new HashMap();
        map.put(url, "应用");
        map.put(urlzc, "政策");

        for (String urls : l) {
            Document document = JsoupUtilPor.get(urls, RequestCount);
            Elements elements = document.select("div.mod-article-list").select("li");
            for (Element element : elements) {
                String title = element.select("a.title").text();
                System.out.println("标题 " + title);

                String author = element.select("a.name").text().trim();
                System.out.println("作者 " + author);
                Elements elements1 = element.select("div.tag").select("a");
                String search_key = "";
                for (Element element1 : elements1) {
                    search_key += (element1.text() + ",");
                }
                search_key = !search_key.equals("") ? search_key.substring(0, search_key.length() - 1) : "";
                System.out.println("关键字 " + search_key);

                String summary = element.select("p.intro").text();
                System.out.println("简介 " + summary);

                String pic_url = element.select("div.pic").select("img").attr("src");
                System.out.println("图片 " + pic_url);

                String href_addr = element.select("a.title").attr("abs:href");
                System.out.println("新闻地址 " + href_addr);

                String PublishTime = element.select("span.author").text();
                PublishTime = PublishTime.split("•")[1];
                PublishTime = getLiandedeLater(PublishTime.trim());
                System.out.println("更新时间 " + PublishTime);
                System.out.println("-------------------------------------------");

                String types = map.get(urls);
                String from_site = author;
                String from_interface = "LIANDEDE_NEWS";
                String news_type_id = "12";

                dbUtil.insertAndQuery("insert into news_info " +
                        "(`status`,`types`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                        " values (?,?,?,?,?,?,?,?,?,?,?,?,?)", title, href_addr, new Object[]{"up", types, news_type_id, title, search_key, author, summary, pic_url, href_addr, PublishTime, from_site, from_interface,PublishTime});

            }
        }
    }


    //核财经  观点+推荐最新
    @Test
    public void HecaijingDownload() {
        //http://www.hecaijing.com/index/loadmore?type=recommend&pn=2
        Document document = JsoupUtilPor.get("http://www.hecaijing.com/shendu/", RequestCount);
        Document documents = JsoupUtilPor.get("http://www.hecaijing.com/", RequestCount);
        String typeid_guandian = document.select("a.active").attr("type");
        String typeid_tuijian = documents.select("a.active").attr("type");
        String arr[] = {typeid_guandian, typeid_tuijian};
        for (int i = 0; i < arr.length; i++) {
            String jsons = HttpClientUtilPro.httpGetRequest("http://www.hecaijing.com/index/loadmore?type=" + arr[i] + "&pn=1", RequestCount);
            System.out.println(jsons);
            JSONObject jsonObject = JSONObject.fromObject(jsons);
            if (jsonObject.getString("msg").equals("success")) {
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
                            "(`status`,`types`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                            " values (?,?,?,?,?,?,?,?,?,?,?,?,?)", title, href_addr, new Object[]{"up", types, news_type_id, title, search_key, author, summary, pic_url, href_addr, PublishTime, from_site, from_interface,PublishTime});

                }
            }
        }
    }

    //https://www.chainnews.com/ 链闻
    @Test
    public void lianwen() {

        // https://www.chainnews.com/api/articles/?t=%E9%A1%B9%E7%9B%AE&ts=1547005264 酷项目
        String kujson = HttpClientUtilPro.httpGetRequest("https://www.chainnews.com/api/articles/?t=%E9%A1%B9%E7%9B%AE", RequestCount);
        JSONObject jsonObject = JSONObject.fromObject(kujson);
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        for (Object o : jsonArray) {
            JSONObject jsonObject1 = JSONObject.fromObject(o);
            String title = jsonObject1.getString("title");
            System.out.println("标题 " + title);
            String author = jsonObject1.getString("author_name");
            System.out.println("作者 " + author);

            List search_keys = new ArrayList();
            JSONArray jsonArray1 = jsonObject1.getJSONArray("tag_list");
            for (Object o1 : jsonArray1) {
                search_keys.add(JSONObject.fromObject(o1).getString("name"));
            }
            String search_key = CheckUtil.getSearchKey(search_keys);
            System.out.println("关键字 " + search_key);

            String summary = jsonObject1.getString("digest");
            System.out.println("简介 " + summary);

            String pic_url = jsonObject1.getString("cover_url").split("\\?")[0];
            System.out.println("图片 " + pic_url);

            String href_addr = "https://www.chainnews.com" + jsonObject1.getString("absolute_url");
            System.out.println("新闻地址 " + href_addr);

            String PublishTime = DateUtil.getHecaijingLater(jsonObject1.getString("pp_time"));
            System.out.println("更新时间 " + PublishTime);
            String from_site = "链闻";
            String from_interface = "链闻";
            String news_type_id = "12";
            String types = "公司";
            dbUtil.insertAndQuery("insert into news_info " +
                    "(`status`,`types`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                    " values (?,?,?,?,?,?,?,?,?,?,?,?,?)", title, href_addr, new Object[]{"up", types, news_type_id, title, search_key, author, summary, pic_url, href_addr, PublishTime, from_site, from_interface,PublishTime});

        }
    }


    @Test
    //链向财经 精英视点--->深度  链向专访--->人物
    public void getLianxiangDownload() {
        //https://www.chainfor.com/home/list/news/data.do?device_type=0&categoryId=10
        //https://www.chainfor.com/home/list/news/data.do?device_type=0&categoryId=9
        String arr[] = {"https://www.chainfor.com/home/list/news/data.do"
                ,"https://www.chainfor.com/home/list/news/data.do?device_type=0&categoryId=11&pageNo=1"
                ,"https://www.chainfor.com/home/list/news/data.do?device_type=0&categoryId=9"//链向专访--> 人物
                ,"https://www.chainfor.com/home/list/news/data.do?device_type=0&categoryId=10"//项目报告-->应用
                ,"https://www.chainfor.com/home/list/news/data.do?device_type=0&categoryId=17"//项目评测-->应用
        };
        for (int i = 0; i < arr.length; i++) {
            //https://www.chainfor.com/home/list/news/data.do
            String json = HttpClientUtilPro.httpGetRequest(arr[i], RequestCount);
            JSONObject jsonObject = JSONObject.fromObject(json);
            System.out.println(jsonObject);
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (Object o : jsonArray) {
                JSONObject jsonObject1 = JSONObject.fromObject(o);
                String title = jsonObject1.getString("title");
                System.out.println("标题 : " + title);

                String summary = jsonObject1.getString("introduction");
                System.out.println("简介 : " + summary);

                String author = jsonObject1.getString("nickName");
                System.out.println("作者 : " + author);

                String search_key = jsonObject1.getString("lable").replace("，", ",");
                System.out.println("关键字 : " + search_key);

                String pic_url = "https://lianxiangfiles.oss-cn-beijing.aliyuncs.com/" + jsonObject1.getString("imgUrl") + "?x-oss-process=style/news";
                System.out.println("图片地址 : " + pic_url);
                System.out.println(jsonObject1);
                String href_addr = "https://www.chainfor.com/news/show/" + jsonObject1.getString("id") + ".html";
                System.out.println("新闻地址 : " + href_addr);

                String PublishTime = jsonObject1.getJSONObject("createDate").getString("time");
                PublishTime = DateUtil.stampToDate(PublishTime);
                System.out.println("更新时间 " + PublishTime);
                String types = null;

                switch (i) {
                    case 0:
                        types = null;
                        break;
                    case 1:
                        types = "深度";
                        break;
                    case 2:
                        types = "人物";
                        break;
                    case 3:
                        types = "应用";
                        break;
                    case 4:
                        types = "应用";
                        break;
                }

                String from_site = jsonObject1.getString("source");
                System.out.println("来源 " + from_site);
                System.out.println("类型 " + types);

                String from_interface = "LIANXIANG_NEWS";
                String news_type_id = "12";
                System.out.println("--------------------------------");
                dbUtil.insertAndQuery("insert into news_info " +
                        "(`status`,`types`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                        " values (?,?,?,?,?,?,?,?,?,?,?,?,?)", title, href_addr, new Object[]{"up", types, news_type_id, title, search_key, author, summary, pic_url, href_addr, PublishTime, from_site, from_interface,PublishTime});

            }
        }

    }


    @Test
    public   void  getweilaicaijingDownLoad(){
        //723 专访-->人物   1 金融科技-->技术   9 数字货币-->观点   8 区块链-->观点
        String [] arr={"8","723","1","9"};
        for(String term_taxonomy_id:arr){
            Map map=new HashMap();
            map.put("limit","10");
            map.put("page","1");
            map.put("term_taxonomy_id",term_taxonomy_id);
            String data=HttpClientUtilPro.httpPostRequest("http://m.weilaicaijing.com/api_app/Article/h5_article_lists",map,RequestCount);
            JSONObject jsonObject = JSONObject.fromObject(data);
            System.out.println(jsonObject);
            if(jsonObject.getString("code").equals("2000")){
                JSONArray jsonArray=jsonObject.getJSONObject("data").getJSONArray("article_lists");
                for(Object object:jsonArray){
                    JSONObject jsonObject1 = JSONObject.fromObject(object);

                    String title = jsonObject1.getString("post_title");
                    System.out.println("标题 : " + title);

                    String summary = "";
                    System.out.println("简介 : " + summary);

                    String author = jsonObject1.getString("display_name");
                    System.out.println("作者 : " + author);

                    String search_key = "";
                    System.out.println("关键字 : " + search_key);

                    String pic_url = jsonObject1.getString("image");
                    System.out.println("图片地址 : " + pic_url);

                    String href_addr = jsonObject1.getString("article_url");
                    System.out.println("新闻地址 : " + href_addr);

                    String PublishTime = jsonObject1.getString("post_date");
                    String types=null;
                    switch (map.get("term_taxonomy_id").toString()) {
                        case "723":
                            types = "人物";
                            break;
                        case "1":
                            types = "技术";
                            break;
                        case "9":
                            types = "观点";
                            break;
                        case "8":
                            types = "观点";
                            break;
                    }
                    System.out.println("类型 " + types);

                    String from_interface = "未来财经";
                    String from_site=from_interface;
                    String news_type_id = "12";
                    System.out.println("--------------------------------");
                    dbUtil.insertAndQuery("insert into news_info " +
                            "(`status`,`types`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                            " values (?,?,?,?,?,?,?,?,?,?,?,?,?)", title, href_addr, new Object[]{"up", types, news_type_id, title, search_key, author, summary, pic_url, href_addr, PublishTime, from_site, from_interface,PublishTime});


                }

            }

        }

    }

    //bitcoin86 最新
    @Test
    public void getbitcoin86DownLoad(){
        Document document=JsoupUtilPor.get("http://www.bitcoin86.com/bitcoin/list_17_1.html",RequestCount);

        Elements elements=document.select("article.excerpt");
        for(Element elemente:elements){
            String title = elemente.select("h2").text();
            System.out.println("标题 : " + title);

            String summary = elemente.select("p.note").text();
            System.out.println("简介 : " + summary);

            String author = "bitcoin86";
            System.out.println("作者 : " + author);

            String search_key = "";
            System.out.println("关键字 : " + search_key);

            String pic_url =  elemente.select("img").get(0).absUrl("data-original");

            System.out.println("图片地址 : " + pic_url);

            String href_addr = elemente.select("a.focus").get(0).absUrl("href");
            System.out.println("新闻地址 : " + href_addr);

            String PublishTime = elemente.select("p.meta").select("font").text();
            System.out.println("时间 : " + PublishTime);

            String from_interface = "bitcoin86";
            String from_site=from_interface;
            String news_type_id = "12";
            System.out.println("--------------------------------");
            dbUtil.insertAndQuery("insert into news_info " +
                    "(`status`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                    " values (?,?,?,?,?,?,?,?,?,?,?,?)", title, href_addr, new Object[]{"up", news_type_id, title, search_key, author, summary, pic_url, href_addr, PublishTime, from_site, from_interface,PublishTime});

        }
    }
    //bitcoin86 挖矿
    @Test
    public void getbitcoin86DownLoadwk(){
        Document document=JsoupUtilPor.get("http://www.bitcoin86.com/wk/",RequestCount);

        Elements elements=document.select("article.excerpt");
        for(Element elemente:elements){
            String title = elemente.select("h2").text();
            System.out.println("标题 : " + title);

            String summary = elemente.select("p.note").text();
            System.out.println("简介 : " + summary);

            String author = "bitcoin86";
            System.out.println("作者 : " + author);

            String search_key = "";
            System.out.println("关键字 : " + search_key);

            String pic_url =  elemente.select("img").get(0).absUrl("data-original");

            System.out.println("图片地址 : " + pic_url);

            String href_addr = elemente.select("a.focus").get(0).absUrl("href");
            System.out.println("新闻地址 : " + href_addr);


            String PublishTime = elemente.select("p.meta").select("time").text()+" "+DateUtil.getNowhhmmss();;
            System.out.println("时间 : " + PublishTime);

            String from_interface = "bitcoin86";
            String from_site=from_interface;
            String news_type_id = "12";
            String types="挖矿";
            System.out.println("--------------------------------");
            dbUtil.insertAndQuery("insert into news_info " +
                    "(`status`,`news_type_id`,`types`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                    " values (?,?,?,?,?,?,?,?,?,?,?,?,?)", title, href_addr, new Object[]{"up", news_type_id,types, title, search_key, author, summary, pic_url, href_addr, PublishTime, from_site, from_interface,PublishTime});

        }
    }

    //FN Fintech News
    @Test
    public  void  getFNDownLoad(){
          Document document=JsoupUtilPor.get("https://www.fn.com/news/",RequestCount);
          System.out.println(document);
          Elements elements=document.select("div.Pc-Article-box").get(0).select("div.Pc-Article");
          for(Element element:elements){
              String title = element.select("div.Pc-Article-Title").text();
              System.out.println("标题 : " + title);

              String summary = element.select("div.Pc-Article-describe").text();
              System.out.println("简介 : " + summary);

              String author = element.select("div.Pc-Article-bottom").select("div.Pc-Article-bottom-l").select("span").get(0).text();
              System.out.println("作者 : " + author);

              Elements elements1=element.select("div.Pc-Article-bottom-r").select("a");
              List<String> list=new ArrayList<>();
              for(Element element1:elements1){
                  list.add(element1.text());
              }
              String search_key =CheckUtil.getSearchKey(list);
              System.out.println("关键字 : " + search_key);


              String pic_url =  element.select("div.Pc-RichContent-cover-inner").select("img").attr("src").split("\\?")[0];
              System.out.println("图片地址 : " + pic_url);

              String href_addr = element.select("div.Pc-RichContent-cover-inner").select("a").get(0).absUrl("href");
              System.out.println("新闻地址 : " + href_addr);

              String PublishTime =  element.select("div.Pc-Article-bottom").select("div.Pc-Article-bottom-l").select("span").get(2).text();
              System.out.println("更新时间 : " + PublishTime);

              String from_interface = "Fintech News";
              String from_site=from_interface;
              String news_type_id = "12";
              System.out.println("--------------------------------");
              dbUtil.insertAndQuery("insert into news_info " +
                      "(`status`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                      " values (?,?,?,?,?,?,?,?,?,?,?,?)", title, href_addr, new Object[]{"up", news_type_id, title, search_key, author, summary, pic_url, href_addr, PublishTime, from_site, from_interface,PublishTime});


          }
     }


     //odaily 星球日报
     @Test
     public void getodailyDownLoad(){
         String data=HttpClientUtilPro.httpGetRequest("https://www.odaily.com/pp/api/app-front/feed-stream?feed_id=280&b_id=&per_page=20",RequestCount);
         JSONObject jsonObject = JSONObject.fromObject(data);
         System.out.println(jsonObject);
         if(jsonObject.getString("code").equals("0")){
             JSONArray jsonArray=jsonObject.getJSONObject("data").getJSONArray("items");
             for(Object object:jsonArray){
                 JSONObject jsonObject1 = JSONObject.fromObject(object);
                 if(jsonObject1.getString("entity_type").equals("post")){

                     String title = jsonObject1.getString("title");
                     System.out.println("标题 : " + title);

                     String summary = jsonObject1.getString("summary");
                     System.out.println("简介 : " + summary);

                     String author = jsonObject1.getJSONObject("extra").getJSONObject("author_info").getString("nickname");
                     System.out.println("作者 : " + author);
                     //introduction
                    //jsonObject1.getJSONObject("extra").getJSONObject("author_info").getString("introduction").replace("，",",");
                     String search_key ="";
                     System.out.println("关键字 : " + search_key);


                     String pic_url =  jsonObject1.getString("cover").split("!")[0];
                     System.out.println("图片地址 : " + pic_url);

                     String href_addr = "https://m.odaily.com/"+jsonObject1.getString("entity_type")+"/"+jsonObject1.getString("entity_id");
                     System.out.println("新闻地址 : " + href_addr);

                     String PublishTime =  jsonObject1.getString("updated_at");
                     System.out.println("更新时间 : " + PublishTime);

                     String from_interface = "星球日报";
                     String from_site=from_interface;
                     String news_type_id = "12";
                     dbUtil.insertAndQuery("insert into news_info " +
                             "(`status`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                             " values (?,?,?,?,?,?,?,?,?,?,?,?)", title, href_addr, new Object[]{"up", news_type_id, title, search_key, author, summary, pic_url, href_addr, PublishTime, from_site, from_interface,PublishTime});

                     System.out.println("--------------------------------");



                 }

             }

         }

     }

     //链氪财经
     @Test
     public void getchainkrDownLoad(){
        Document document=JsoupUtilPor.get("http://chainkr.pro/",RequestCount);
        Elements select = document.select("ul.active").get(0).select("li.item");
        for(Element element:select){

            String title = element.select("h2.item-title").select("a").text();
            System.out.println("标题 : " + title);

            //item-excerpt
            String summary = element.select("div.item-excerpt").text();
            System.out.println("简介 : " + summary);

            String author = "";
            System.out.println("作者 : " + author);
            //introduction
            //jsonObject1.getJSONObject("extra").getJSONObject("author_info").getString("introduction").replace("，",",");
            String search_key ="";
            System.out.println("关键字 : " + search_key);


            String pic_url =  element.select("img.wp-post-image").attr("src");
            System.out.println("图片地址 : " + pic_url);

            String href_addr = element.select("h2.item-title").select("a").attr("href");
            System.out.println("新闻地址 : " + href_addr);

            String PublishTime =  element.select("span.date").text();
            PublishTime=DateUtil.getHecaijingLater(PublishTime);
            System.out.println("更新时间 : " + PublishTime);

            String from_interface = "链氪财经";
            String from_site=from_interface;
            author=from_interface;
            String news_type_id = "12";
            dbUtil.insertAndQuery("insert into news_info " +
                    "(`status`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                    " values (?,?,?,?,?,?,?,?,?,?,?,?)", title, href_addr, new Object[]{"up", news_type_id, title, search_key, author, summary, pic_url, href_addr, PublishTime, from_site, from_interface,PublishTime});

            System.out.println("--------------------------------");




        }
     }

    //火讯财经
    @Test
    public void HuoxunDownloadPage(){
        String data=HttpClientUtilPro.httpGetRequest("https://huoxun.com/api/getlist/1?page=1",RequestCount);
        System.out.println(data);
        JSONObject jsonObject = JSONObject.fromObject(data);
        JSONArray data1 = jsonObject.getJSONArray("data");
        for(Object object:data1){
            JSONObject jsonObject1 = JSONObject.fromObject(object);

            String title = jsonObject1.getString("title");
            System.out.println("标题 : " + title);

            //item-excerpt
            String summary = jsonObject1.getString("des");
            System.out.println("简介 : " + summary);

            String author = jsonObject1.getJSONObject("user").getString("nickname");
            System.out.println("作者 : " + author);
            //introduction
            //jsonObject1.getJSONObject("extra").getJSONObject("author_info").getString("introduction").replace("，",",");
            String search_key ="";
            System.out.println("关键字 : " + search_key);


            String pic_url = "https://huoxun.com"+ jsonObject1.getString("img_path");
            System.out.println("图片地址 : " + pic_url);

            String href_addr ="https://huoxun.com/news/"+jsonObject1.getString("id")+".html";
            System.out.println("新闻地址 : " + href_addr);

            String PublishTime =  DateUtil.stampToDate(jsonObject1.getString("update_time"),true);
            System.out.println("更新时间 : " + PublishTime);

            String from_interface = "火讯财经";
            String from_site=from_interface;
            String news_type_id = "12";
            dbUtil.insertAndQuery("insert into news_info " +
                    "(`status`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                    " values (?,?,?,?,?,?,?,?,?,?,?,?)", title, href_addr, new Object[]{"up", news_type_id, title, search_key, author, summary, pic_url, href_addr, PublishTime, from_site, from_interface,PublishTime});

            System.out.println("--------------------------------");




        }

    }

    //小葱
    @Test
    public void getxiaochongDownloadPage(){
        String data=HttpClientUtilPro.httpGetRequest("https://cong-api.xcong.com/apiv1/dashboard/chosen_page?limit=20",RequestCount);
        System.out.println(data);
        JSONObject jsonObject = JSONObject.fromObject(data);
        JSONArray data1 = jsonObject.getJSONObject("data").getJSONArray("items");
        for(Object object:data1){
            JSONObject jsonObject1 = JSONObject.fromObject(object);
            if(jsonObject1.getString("resource_type").equals("article")){

                String title = jsonObject1.getJSONObject("resource").getString("title");
                System.out.println("标题 : " + title);

                //item-excerpt
                String summary = jsonObject1.getJSONObject("resource").getString("content_short");
                System.out.println("简介 : " + summary);

                String author = jsonObject1.getJSONObject("resource").getJSONObject("author").getString("display_name");
                System.out.println("作者 : " + author);
                //introduction
                //jsonObject1.getJSONObject("extra").getJSONObject("author_info").getString("introduction").replace("，",",");
                String search_key ="";
                System.out.println("关键字 : " + search_key);


                String pic_url = jsonObject1.getJSONObject("resource").getString("image_uri");
                System.out.println("图片地址 : " + pic_url);

                String href_addr =jsonObject1.getJSONObject("resource").getString("uri");
                System.out.println("新闻地址 : " + href_addr);

                String PublishTime =  DateUtil.stampToDate(jsonObject1.getJSONObject("resource").getString("display_time"),true);
                System.out.println("更新时间 : " + PublishTime);

                String from_interface = "小葱";
                String from_site=from_interface;
                String news_type_id = "12";
                dbUtil.insertAndQuery("insert into news_info " +
                        "(`status`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                        " values (?,?,?,?,?,?,?,?,?,?,?,?)", title, href_addr, new Object[]{"up", news_type_id, title, search_key, author, summary, pic_url, href_addr, PublishTime, from_site, from_interface,PublishTime});

                System.out.println("--------------------------------");

            }

        }

    }

    @Test//布洛克科技
    public  void getblock360Download(){
        //https://www.jgy.com/
        Document data = JsoupUtilPor.get("http://block360.pro/", RequestCount);
        Elements elements=data.select("ul#view").select("li.news_list");
        for(Element element:elements){
                String title = element.select("h3.ellips_line3").text();
                System.out.println("标题 : " + title);

                //item-excerpt
                String summary = element.select("div.ellips_line2").text();
                System.out.println("简介 : " + summary);

                String author = element.select("div.clearfix").get(0).select("div.box").get(0).select("span").text();
                System.out.println("作者 : " + author);
                //introduction
                //jsonObject1.getJSONObject("extra").getJSONObject("author_info").getString("introduction").replace("，",",");
                String search_key =element.select("div.news_label").text().replace("，",",");
                System.out.println("关键字 : " + search_key);


                String pic_url = element.select("div.img_box").select("img").get(0).absUrl("src");
                System.out.println("图片地址 : " + pic_url);

                String href_addr =   "http://block360.pro/wechat"+element.select("a").get(0).attr("href");
                System.out.println("新闻地址 : " + href_addr);

                String PublishTime =  DateUtil.getHecaijingLater(element.select("div.clearfix").get(0).select("div.box").get(1).select("span").text());
                System.out.println("更新时间 : " + PublishTime);

                String from_interface = "布洛克科技";
                String from_site=from_interface;
                String news_type_id = "12";
                dbUtil.insertAndQuery("insert into news_info " +
                        "(`status`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                        " values (?,?,?,?,?,?,?,?,?,?,?,?)", title, href_addr, new Object[]{"up", news_type_id, title, search_key, author, summary, pic_url, href_addr, PublishTime, from_site, from_interface,PublishTime});

                System.out.println("--------------------------------");


        }

    }

    @Test
    public  void  getbitecoinDownload(){
        Document document = JsoupUtilPor.get("http://www.bitecoin.com/page/1", RequestCount);
        Elements elements = document.select("div.newslist").select("article.excerpt");
        for(Element element:elements){
            String title = element.select("header").select("a").attr("title");
            System.out.println("标题 : " + title);

            //item-excerpt
            String summary = element.select("div.notess").select("p.note").next("p").get(0).text();
            System.out.println("简介 : " + summary);

            String author = element.select("div.pv").select("a").text();
            System.out.println("作者 : " + author);
            //introduction
            //jsonObject1.getJSONObject("extra").getJSONObject("author_info").getString("introduction").replace("，",",");
            String search_key =element.select("a.column-link").text();
            System.out.println("关键字 : " + search_key);


            String pic_url = element.select("img.thumb").get(0).absUrl("src");
            System.out.println("图片地址 : " + pic_url);

            String href_addr =   element.select("header").select("a").attr("title");
            System.out.println("新闻地址 : " + href_addr);

            String PublishTime =  DateUtil.getHecaijingLater(element.select("time").text());
            System.out.println("更新时间 : " + PublishTime);

            String from_interface = "比特财经";
            String from_site=from_interface;
            String news_type_id = "12";
            dbUtil.insertAndQuery("insert into news_info " +
                    "(`status`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                    " values (?,?,?,?,?,?,?,?,?,?,?,?)", title, href_addr, new Object[]{"up", news_type_id, title, search_key, author, summary, pic_url, href_addr, PublishTime, from_site, from_interface,PublishTime});

            System.out.println("--------------------------------");


        }
    }



    //https://www.hellobtc.com/kp/kc/index_1.html 白话区块链 入门课 --->  新手必读
    @Test
    public void baihuaqukuailiang(){
        Document document = JsoupUtilPor.get("https://www.hellobtc.com/kp/kc/index.html", RequestCount);
        Elements elements = document.select("ul.contentlist").select("li");
        for(Element element:elements){
            String title = element.select("a.caption").get(0).text();
            System.out.println("标题 "+title);

            String surl = element.select("a.caption").get(0).absUrl("href");
            Document document1 = JsoupUtilPor.get(surl, RequestCount);
//            System.out.println(document1);
            String html = document1.select("hgroup").get(0).select("h5").get(0).html();

            System.out.println("html "+html);

//          System.out.println("标题 "+title);
            String author=html.substring(html.indexOf("作者：")+3,html.indexOf("来源：")).replace("</span>","").replace("<span>","");

            System.out.println("作者 "+author);
            String search_key = "";
            System.out.println("关键字 "+search_key);
            String summary = element.select("a.intro").get(0).text();
            String pic_url = element.select("img").get(0).absUrl("src");
            String href_addr =  element.select("a.caption").get(0).attr("href");
            String PublishTime=html.substring(0,html.indexOf("<span>"));
            System.out.println("简介 "+summary);
            System.out.println("图片 "+pic_url);
            System.out.println("新闻地址 "+href_addr);
            System.out.println("更新时间 "+PublishTime);
//              System.exit(0);

            String from_site="白话区块链";
            String from_interface="白话区块链";
            String news_type_id="12";
            String types="新手必读";
            dbUtil.insertAndQuery( "insert into news_info " +
                    "(`status`,`types`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
                    " values (?,?,?,?,?,?,?,?,?,?,?,?,?)",title,href_addr,
                    new Object[] {"up",types,news_type_id,title,search_key,author,summary,pic_url,href_addr,PublishTime,from_site,from_interface,PublishTime});


            System.out.println("===============================");



        }
    }


    @Test
    public void shitaishuo() {      // 师太说  上线前跑一次全的  然后智跑第一页
        String x = HttpClientUtilPro.httpGetRequest("https://www.odaily.com/api/pp/api/user/2147487207/posts?b_id=&per_page=50", RequestCount);
        JSONObject jsonObject = JSONObject.fromObject(x);
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("items");
//        JSONObject jsonObject2 = JSONObject.fromObject(jsonArray);
//        JSONArray jsonArray1 = jsonObject2.getJSONArray("items");

        for (Object o : jsonArray) {
            JSONObject jsonObject1 = JSONObject.fromObject(o);
            String title = jsonObject1.getString("title");
            System.out.println("标题 " + title);
            String author = "师太说区块链";
            System.out.println("作者 " + author);
            String search_key = "";
            System.out.println("关键字 " + search_key);
            String summary = jsonObject1.getString("summary");
            System.out.println("简介 " + summary);
            String pic_url = jsonObject1.getString("cover").substring(0, jsonObject1.getString("cover").length() - 8);
            System.out.println("图片 " + pic_url);
            String href_addr = "https://www.odaily.com/post/" + jsonObject1.getString("id");
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

//    @Test
//    public void chaiyunbite() {     // 彩云比特 （挖矿  上线前跑全部的  然后只跑一次就可以了）
//        Integer t = 1;
//        a:
//        while (true) {
//            Document document = JsoupUtilPor.get("https://www.cybtc.com/forum.php?mod=forumdisplay&fid=120&typeid=56&typeid=56&filter=typeid&page=" + t + "&t=", RequestCount);
////        System.out.println(document);
//            Elements elements = document.select("form#moderate").select("table").select("tbody");
//            for (int i = 1; i < elements.size() - 1; i++) {
//                Element element = elements.get(i);
//                System.out.println(element);
////            System.out.println(element);
////            System.out.println("标题 " + title);
////            System.out.println("作者 " + author);
////            System.out.println("关键字 " + search_key);
////            System.out.println("简介 " + summary);
////            System.out.println("图片 " + pic_url);
////            System.out.println("新闻地址 " + href_addr);
////            System.out.println("更新时间 " + PublishTime);
////            String from_site = "师太说区块链";
////            String from_interface = "师太说区块链";
////            String news_type_id = "12";
////            String types = "新手必读";
//                String id = element.attr("id");
//                if (id == null || id.equals("")) {
//                    continue;
//                }
//                String title = element.select("tr.first-tr").get(0).select("a.xst").get(0).text();
//                System.out.println("标题 " + title);
//                String author = element.select("tr.third-tr").get(0).select("div.third-tr-second-td-name").get(0).select("a").get(0).text();
//                System.out.println("作者 " + author);
//                String search_key = "";
//                System.out.println("关键字 " + search_key);
//                String summary = element.select("tr.second-tr").get(0).select("td").get(0).text();
//                System.out.println("简介 " + summary);
//                String pic_url = "";  // todo ... 图片不存在
//                System.out.println("图片 " + pic_url);
//                String href_addr = "https://www.cybtc.com/" + element.select("tr.first-tr").get(0).select("a").get(0).attr("href");
//                System.out.println("新闻地址 " + href_addr);
//                String PublishTime = element.select("tr.third-tr").get(0).select("td").get(0).select("div").get(0).select("span").attr("title") + DateUtil.ForDate(new Date(), " hh:mm:ss");
//                if (PublishTime.length() != 18) {
//                    PublishTime = element.select("tr.third-tr").get(0).select("td").get(0).select("div").get(0).text() + DateUtil.ForDate(new Date(), " hh:mm:ss");
//                }
//                System.out.println("更新时间 " + PublishTime);
//                String from_site = "彩云比特";
//                String from_interface = "彩云比特";
//                String news_type_id = "12";
//                String types = "挖矿";
//                dbUtil.insertAndQuery("insert into news_info " +
//                                "(`status`,`types`,`news_type_id`,`title`,`search_key`,`author`,`summary`,`pic_url`,`href_addr`,`publish_time`,`from_site`,`from_interface`,`create_time`)" +
//                                " values (?,?,?,?,?,?,?,?,?,?,?,?,?)", title, href_addr,
//                        new Object[]{"up", types, news_type_id, title, search_key, author, summary, pic_url, href_addr, PublishTime, from_site, from_interface, PublishTime});
//                System.out.println("====================================================================================");
//            }
//
//            t++;
//            if (t > 1000) {
//                break a;
//            }
//        }
//    }





//    @Test
//    public void getbishijie(){
//        Map headerMap =new HashMap();
//        headerMap.put("Host","www.bishijie.com");
//        headerMap.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
//        headerMap.put("Upgrade-Insecure-Requests","1");
//        headerMap.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//        headerMap.put("Accept-Encoding","gzip, deflate, br");
//        headerMap.put("Accept-Language","zh-CN,zh;q=0.9");
//        headerMap.put("Cache-Control","max-age=0");
//        headerMap.put("Connection","keep-alive");
//        headerMap.put("Referer","https://www.bishijie.com/shendu.html");
//        headerMap.put("Cookie","acw_tc=241bda9c15515061795673042e00fc47c9ed04987d93203f86198cce18;acw_sc__v2=5c7a1ea9d23002c9cc7f2f019c6d3b4");
//
//       // Cookie: acw_tc=241bda9c15515061795673042e00fc47c9ed04987d93203f86198cce18; acw_sc__v2=5c7a1b039c1f6bea9d23002c9cc7f2f019c6d3b4
//        String data = HttpClientUtilPro.httpGetRequestHead("https://www.bishijie.com/api/Information/index?page=1&size=20",headerMap, RequestCount);
//        System.out.println(data);
//
//
//     }


    @Test
    public void wakuangji() {     // 挖矿机 （挖矿  ）
//            Document document = JsoupUtilPor.get("http://www.wabi.com/news/mining", RequestCount);  // 第一页数据
            Document document = JsoupUtilPor.get("http://www.wabi.com/news/mining/page_"+1+".html", RequestCount);  // 第二页以后的数据
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

    }



    @Test
    public void biyuan() {     // 币源 （挖矿  上线前跑全部的  然后只跑一次就可以了）

            Document document = JsoupUtilPor.get("https://www.coingogo.com/news/index/"+1+"?parent=9", RequestCount);
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

    }

}



















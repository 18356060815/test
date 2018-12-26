package com.jnk.test.crawl;

import com.jnk.test.Service.DBUtil;
import com.jnk.test.util.DateUtil;
import com.jnk.test.util.HttpClientUtilPro;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.jnk.test.crawl.DowloadPage.RequestCount;
@RunWith(SpringRunner.class)
@SpringBootTest
@Service
public class HotNodePro {

    @Autowired
    DBUtil dbUtil;
        //hotNode App 项目
    @Test
    public  void   getHotNode() {

        int i=478;
            a:while (true){
            String x = HttpClientUtilPro.httpGetRequest("https://api.personal.hotnode.cn/v1/coins/public" +
                    "?progress=&industry_id=&tag_id=&region_id=&purpose_id=&is_reachable=0&has_weekly=0&has_rating=0&has_white_paper=0&is_renowned_industry=0" +
                    "&currentPage="+i+"&pageSize=20&page="+i+"&per-page=20", RequestCount);
            JSONArray jsonArray = JSONArray.fromObject(x);
                for (Object o : jsonArray) {
                    int sort_num=0;
                    JSONObject jsonObject = JSONObject.fromObject(o);
                    String name = jsonObject.getString("name");
                    String symbol = jsonObject.getString("symbol");
                    String pic_url = jsonObject.getString("icon");
                    String id = jsonObject.getString("id");
                    String home_page= jsonObject.getString("homepage").equals("null")?null:jsonObject.getString("homepage");
                    JSONArray json_regions=jsonObject.getJSONArray("regions");

                    //标签
                    JSONArray tagArr = jsonObject.getJSONArray("tags");//一个项目 概念标签 集合

                    //是否 有无评级(has_rating) 有无白皮书(has_white_paper)  是否可联系(is_reachable)  有无周报(has_weekly)   是否知名机构所投(is_renowned_industry)
                    //String rating=jsonObject.getString("has_rating");//是否有评级
                    String has_white_paper=jsonObject.getString("has_white_paper").equals("true")?"y":"n";//是否有白皮书
                    //String reachable=jsonObject.getString("is_reachable");//是否可联系
                    //String weekly=jsonObject.getString("has_weekly");//是否有周报
                    String is_renowned_industry=jsonObject.getString("is_renowned_industry").equals("true")?"y":"n";//是否知名机构所投


                    String m = HttpClientUtilPro.httpGetRequest("https://api.personal.hotnode.cn/v1/coins/" + id, RequestCount);
                    String news_infodata = HttpClientUtilPro.httpGetRequest("https://api.personal.hotnode.cn/v1/coins/" + id+"/news-info", RequestCount);
                    JSONArray news_Array=JSONArray.fromObject(news_infodata);
                    for(Object newso:news_Array){
                        JSONObject jsonObject2=JSONObject.fromObject(newso);
                        String title=jsonObject2.getString("title");//标题
                        String href_addr=jsonObject2.getString("original_link");//地址
                        String from_site=jsonObject2.getString("source");//来源
                        String author=jsonObject2.getString("source");//来源
                        String from_interface=jsonObject2.getString("source");//来源
                        String news_type_id="20";
                        String search_key=name;
                        String publish_time= DateUtil.stampToDate(jsonObject2.getString("created_at"),true);
                        String create_time= publish_time;

                        dbUtil.insertAndQuery_pro("insert into news_info (status,title,href_addr,from_site,author,from_interface,news_type_id,search_key,publish_time,create_time) " +
                                "values " +
                                "(?,?,?,?,?,?,?,?,?,?)",title,href_addr,new Object[]{"up",title,href_addr,from_site,author,from_interface,news_type_id,search_key,publish_time,create_time});

                    }
                    System.out.println("详情---> : "+m);
                    JSONObject jsonObject1=JSONObject.fromObject(m);
                    String description = jsonObject1.getString("description");//详情

                    JSONArray team=jsonObject1.getJSONArray("members");//团队成员
                    JSONArray white_papers_arr=jsonObject1.getJSONArray("white_papers");//白皮书
                    JSONArray jsonArray1=jsonObject1.getJSONArray("industry_investments");

                    System.out.println("项目名 : " + name);
                    System.out.println("简称 : "+symbol);
                    System.out.println("图标 : " + pic_url);
                    if(pic_url.contains("data:image")){
                        continue ;
                    }
                    System.out.println("简介 : " + description);
                    System.out.println("详情 : " + description);
                    System.out.println("概念标签 : " + tagArr);


                    System.out.println("官网 : " + home_page);
                    String white_papers=null;
                    if(white_papers_arr.size()>0){
                        white_papers=JSONObject.fromObject(white_papers_arr.get(0)).getString("path_url");
                    }
                    System.out.println("白皮书 : " + white_papers);
                    System.out.println("国家 : " + json_regions);

                    System.out.println("团队人物 : " + team);
                    System.out.println("投资机构 : " + jsonArray1);

                    List <Long>lll=new ArrayList<>();

                    List<Long> guannian=new ArrayList();//概念标签 list
                    for(Object o1:tagArr){
                        String tagname=JSONObject.fromObject(o1).getString("name");
                        Long gainianid=dbUtil.BixiaobaiinsertAndQuery("insert into project_tag (`first_tag`,`second_tag`) values (?,?)",new Object[]{"概念",tagname},"概念",tagname);
                        guannian.add(gainianid);
                    }

                    //国家
                    List <Long>guojia=new ArrayList();//国家标签 list
                    for(Object o1:json_regions){
                        String regions=JSONObject.fromObject(o1).getString("name");
                        Long guojiaid=dbUtil.BixiaobaiinsertAndQuery("insert into project_tag (`first_tag`,`second_tag`) values (?,?)",new Object[]{"国家",regions},"国家",regions);
                        guojia.add(guojiaid);
                    }



                    List <Long>touzijigou=new ArrayList();//投机机构标签 list
                    for(Object o1:jsonArray1){
                        String touzijigouname=JSONObject.fromObject(o1).getString("name");
                        Long touzijigouid=dbUtil.BixiaobaiinsertAndQuery("insert into project_tag (`first_tag`,`second_tag`) values (?,?)",new Object[]{"投资机构",touzijigouname},"投资机构",touzijigouname);
                        touzijigou.add(touzijigouid);
                    }
                    lll.addAll(guannian);
                    lll.addAll(guojia);
                    lll.addAll(touzijigou);


                    String json_str=null;
                    if(jsonArray1.size()==0){
                        json_str=null;
                    }else {
                        json_str=jsonArray1.toString();
                    }


                    String team_str=null;
                    if(team.size()==0){
                        team_str=null;
                    }else {
                        team_str=team.toString();
                    }
                    if(white_papers!=null&&!"".equals(white_papers)&&!"null".equals(white_papers)){
                        sort_num+=1;
                    }
                    sort_num=touzijigou.size()+sort_num;

                    Long pro_id=dbUtil.insertAndQuery("insert into project_info " +
                            "(`name`,`symbol`,`pic_url`,`summary`,`description`,`home_page`,`white_paper`,`team`,`pimc`,`has_white_paper`,`is_renowned_industry`,`sort_num_by_ld`) " +
                            "values " +
                            "(?,?,?,?,?,?,?,?,?,?,?,?) "
                    ,new Object[]{name,symbol,pic_url,description,description,home_page,white_papers,team_str,json_str,has_white_paper,is_renowned_industry,sort_num},name,symbol);


                    for(Long os:lll){
                        dbUtil.execute("insert into project_info_tag (`pro_info_id`,`pro_tag_id`) values ('"+pro_id+"','"+os+"')");
                    }
                    System.out.println("-------------------------------------");


                }

                String next = HttpClientUtilPro.httpGetRequest("https://api.personal.hotnode.cn/v1/coins/public" +
                        "?progress=&industry_id=&tag_id=&region_id=&purpose_id=&is_reachable=0&has_weekly=0&has_rating=0&has_white_paper=0&is_renowned_industry=0" +
                        "&currentPage="+(i+1)+"&pageSize=20&page="+(i+1)+"&per-page=20", RequestCount);
                if (next.equals(x)) {
                    break a;
                }
                i++;


            }


    }




}

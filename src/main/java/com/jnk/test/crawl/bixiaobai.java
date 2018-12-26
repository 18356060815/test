package com.jnk.test.crawl;

import com.jnk.test.Service.DBUtil;
import com.jnk.test.util.HttpClientUtilPro;
import com.jnk.test.util.JsoupUtilPor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static com.jnk.test.crawl.DowloadPage.RequestCount;

@RunWith(SpringRunner.class)
@SpringBootTest
public class bixiaobai {
    static String url="http://www.bixiaobai.com/index/projectLibrary/detail?id=";
    @Autowired
    DBUtil dbUtil;
    @Test
    public void getbixiaobai(){
        Set <String>gainianset=new HashSet();//概念标签
        int k=0;
        int i=1;
        while (true){
            String s = HttpClientUtilPro.httpGetRequest
                    ("http://www.bixiaobai.com/api/projectLibrary?client=2&concept=&stage=&Grade=&order=pl.pl_is_hot&keyWords=&pageNo="+i,RequestCount);
            JSONObject jsonObject= JSONObject.fromObject(s);
            if(jsonObject.getString("status").equals("1002")){
                break;
            }
            JSONArray jsonArray=jsonObject.getJSONObject("data").getJSONArray("data");
            for(int l=0;l<jsonArray.size();l++){
                JSONObject jsonObject1=JSONObject.fromObject(jsonArray.get(l));
                String pl_id=jsonObject1.getString("pl_id");//币小白项目id
                System.out.println("币小白项目id : "+pl_id);

                String pl_chinese_name=jsonObject1.getString("pl_chinese_name");//项目中文名
                System.out.println("项目中文名 : "+pl_chinese_name);

                String name=jsonObject1.getString("pl_english_name");//项目英文名
                System.out.println("项目英文名 : "+name);

                String symbol=jsonObject1.getString("pl_abb_name");//项目简称
                System.out.println("项目简称 : "+symbol);

                String pic_url=jsonObject1.getString("pl_logo");//项目logo
                System.out.println("项目logo : "+pic_url);

                String summary=jsonObject1.getString("pl_one_intro");//简介
                System.out.println("简介 : "+summary);

                String description=jsonObject1.getString("pl_intro");//详情
                System.out.println("详情 : "+description);


//                String is_up=jsonObject1.getString("stage_name");//是否上线
//                System.out.println("是否上线 : "+is_up);
//
//                String ps_score=jsonObject1.getString("ps_score");//分数
//                System.out.println("分数 : "+ps_score);

                JSONArray tag=jsonObject1.getJSONArray("concept_name");//标签
                System.out.println("标签 : "+tag);

                Document document=JsoupUtilPor.get(url+pl_id,RequestCount);
                //System.out.println(document);
                Elements elements=document.select("table.lianXi");
                String home_page=elements.select("tr").get(0).select("a").attr("href");//官网
                System.out.println("官网 : "+home_page);

                String white_paper=elements.select("tr").get(2).select("a").attr("href");//白皮书
                System.out.println("白皮书 : "+white_paper);

                //项目资料
                JSONObject project_content=new JSONObject();
                String token_content=document.select("div.nr2").get(0).select("tr").get(0).select("td").get(1).text();//token总量
                String daibifenpei=document.select("div.nr2").get(0).select("tr").get(1).select("td").get(1).text();//代币分配
                String conceptual_plate=document.select("div.nr2").get(0).select("tr").get(2).select("td").get(1).text();//概念板块
                String project_stage=document.select("div.nr2").get(0).select("tr").get(3).select("td").get(1).select("span.act").text();//项目阶段
                String consensus_mec=document.select("div.nr2").get(0).select("tr").get(4).select("td").get(1).text();//共识机制
                Elements elements1=document.select("div.nr2").get(0).select("tr").get(5).select("td").get(1).select("span");//已上线交易所
                JSONArray jys=new JSONArray();//已上线交易所
                for(Element element:elements1){
                    String jy_name=element.text();
                    String jy_url=element.attr("onclick").replace("window.open('","").replace("')","");
                    JSONObject jsonObject2=new JSONObject();
                    jsonObject2.put("jy_name",jy_name);
                    jsonObject2.put("jy_url",jy_url);
                    jys.add(jsonObject2);
                }
                project_content.put("token_content",token_content);
                project_content.put("daibifenpei",daibifenpei);
                project_content.put("conceptual_plate",conceptual_plate);
                project_content.put("project_stage",project_stage);
                project_content.put("consensus_mec",consensus_mec);
                project_content.put("jys",jys);
                System.out.println("项目资料 : "+project_content);
//                List<Long> guannian=new ArrayList();//概念标签 list
//                for(Object o1:tag){
//                    String tagname= o1.toString();
//                    Long gainianid=dbUtil.BixiaobaiinsertAndQuery("insert into project_tag (`first_tag`,`second_tag`) values (?,?)",new Object[]{"概念",tagname},"概念",tagname);
//                    guannian.add(gainianid);
//                }
                List <Map<String,Object>>list=dbUtil.find("select id from project_info where name='"+name.replace("'","\\'")+"' and symbol='"+symbol.replace("'","\\'")+"'");
                System.out.println("hotnode是否有数据 : "+list);
                if(list.size()!=0){
                    k++;
                }
//                System.exit(0);
//                if(list.size()!=0){
//                    Object o=list.get(0).get("id");
//                    dbUtil.execute("update project_info set finances='"+project_content+"' where id="+o);
//                }

//                //核心人物
//                Elements elements2=document.select( 项目资料
//
//                        "ul.renWu").select("li");
//                JSONArray renwu_arr=new JSONArray();
//                for(Element element: elements2){
//                    JSONObject jsonObject2=new JSONObject();
//                    String renwu_img= element.select("img").get(0).attr("src");//人物图片
//                    //String [] arr=element.select("div.renWu_mz").get(0).ownText().split(" ");
//                    String renwu_name= element.select("div.renWu_mz").get(0).ownText().split(" ")[0];//人物名
//                    //String renwu_zhiwei= arr[arr.length-1];//人物职位
//                    String renwu_jieshao= element.select("div.renWu_jj").get(0).text();//人物介绍
//                    jsonObject2.put("renwu_img",renwu_img);
//                    jsonObject2.put("renwu_name",renwu_name);
//                    //jsonObject2.put("renwu_zhiwei",renwu_zhiwei);
//                    jsonObject2.put("renwu_jieshao",renwu_jieshao);
//                    renwu_arr.add(jsonObject2);
//                }
//                System.out.println("核心人物 : "+renwu_arr);


//                //合作伙伴
//                String huoban=document.select("div.huoBan").text();
//                System.out.println("合作伙伴 : "+huoban);
//
//                //项目路径
//                String pro_lujing=document.select("div.luJing").text();
//                System.out.println("项目路径 : "+pro_lujing);

                //elements.select("div.nr2").get(2)

//========
//                //String consensus_mec
//                Long pro_id=dbUtil.BixiaobaiinsertAndQuery
//                        ("insert into project_info " +
//                                "(`name`,`symbol`,`pic_url`,`summary`,`description`,`home_page`,`white_paper`,`team`,`symbol`,`symbol`) " +
//                                "values " +
//                                "(?,?,?,?,?,?,?,?,?,?,?,?)",new Object[]{});
//                for(Object o:tag){
//                    List<Map<String ,Object >> list=dbUtil.find("select id from project_tag where second_tag='"+o+"'");
//                    if(list.size()>0){
//                        for(Map<String ,Object > map:list){
//                            String tag_id=map.get("id").toString();
//
//
//                        }
//                    }
//                }


            }
            i++;
        }
//        for(String s:gainianset){
//            dbUtil.execute("insert into project_tag (`first_tag`,`second_tag`) values ('概念','"+s+"') ");
//        }

        System.out.println("hotnode 币小白重复数据 : "+k);
    }

}

















package com.jnk.test.crawl;

import com.jnk.test.Service.DBUtil;
import com.jnk.test.util.Arith;
import com.jnk.test.util.HttpClientUtilPro;
import com.jnk.test.util.JsoupUtilPor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.jnk.test.crawl.DowloadPage.RequestCount;

@RunWith(SpringRunner.class)
@SpringBootTest
@Service
public class DownLoadDapp {
    final  static  String rootpath="";

    @Autowired
    DBUtil dbUtil;

    @Test
    public void  getDapp() throws  Exception{
        int i=1;  // todo 28  34 81 86 117
        a:
        while (true){
            String x = HttpClientUtilPro.httpGetRequest("https://dapp.review/api/dapp/dapps/?search=&page="+i+"&page_size=24&ordering=-dau_last_day&new=false", RequestCount);
            JSONObject jsonObject = JSONObject.fromObject(x);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (Object o : jsonArray){
                JSONObject jsonObject1 = JSONObject.fromObject(o);
                String id = jsonObject1.getString("id");  //    id
                String title = jsonObject1.getString("title");  // title
                String dau_last_day = jsonObject1.getString("dau_last_day");  // 24小时用户
                String description = jsonObject1.getString("description");  // 简介
                String description_short = jsonObject1.getString("description_short");  // 短简介
                String logo_url = jsonObject1.getString("logo_url");  // 下载下来，放到服务器中
                String tx_last_day = jsonObject1.getString("tx_last_day");  // 24小时交易数
                String tx_last_week = jsonObject1.getString("tx_last_week");  // 7D交易数
                String volume_last_day = jsonObject1.getString("volume_last_day");  // 这个字段时干什么啊
                String volume_last_day_CNY = jsonObject1.getString("volume_last_day_CNY");  // 24小时交易额  RMB
                String volume_last_day_USD = jsonObject1.getString("volume_last_day_USD");  // 24小时交易额  $$$
                String volume_last_week = jsonObject1.getString("volume_last_week");  // 这个字段时干什么啊
                String volume_last_week_CNY = jsonObject1.getString("volume_last_week_CNY");  // 一周交易总数  RMB
                String volume_last_week_USD = jsonObject1.getString("volume_last_week_USD");  // 一周交易总数  $$$

                JSONArray json_categories=jsonObject1.getJSONArray("categories");


                System.err.println(logo_url);

//                Document documen=JsoupUtilPor.get("https://dapp.review/dapp/"+id+"/",RequestCount);
////                System.out.println(documen);
//                Elements script =  documen.select("script");
//                String x1 ;
//                String url = "";
//
//                x1 = script.get(0).html().replace("window.App=", "");
//                System.out.println(i+" ..... "+x1);
//                JSONObject jsonObject2 = JSONObject.fromObject(x1);
//                url = jsonObject2.getJSONObject("state").getJSONObject("dapp").getJSONObject("detail").getString("url");

                String categories_id ;
                if (json_categories.toString().equals("null")){
                    categories_id = null;
                }else {
                    if (json_categories.size()>0) {
                    String test1 = json_categories == null ? null : json_categories.get(0).toString();
                        JSONObject json_star1 = JSONObject.fromObject(test1);
                        categories_id = json_star1.get("id").toString();
                        System.out.println(categories_id);
                    }else {
                        categories_id = null;
                    }
                }
                JSONArray json_block_chains=jsonObject1.getJSONArray("block_chains");

                String chains_id;
                if (json_block_chains.toString().equals("null") ||json_block_chains.size() == 0 ){
                    chains_id = null;
                }else {
                    if (json_block_chains.size()>0) {
                        String test1 = json_block_chains.get(0).toString();
                        JSONObject json_star2 = JSONObject.fromObject(test1);
                        chains_id = json_star2.get("id").toString();
                    }else {
                        chains_id = null;
                    }
                }
                System.err.println(chains_id+".........chains_id");
//                Object json_stars = jsonObject1.getOrDefault("star",null);


//                String star = null;
//                if (!json_stars.toString().equals("null")) {
//                    JSONObject json_star = JSONObject.fromObject(json_stars);
////                for (Object o1 : json_star){
////                    JSONObject jsonObject2 = JSONObject.fromObject(json_star);
////                    String star_fiv = json_star.getString("star_fiv") == null ? "0" : json_star.getString("star_fiv");
//                    String star_fiv = json_star.get("star_fiv").toString();
//                    String star_fou = json_star.getString("star_fou") == null ? "0" : json_star.getString("star_fou");
//                    String star_one = json_star.getString("star_one") == null ? "0" : json_star.getString("star_one");
//                    String star_thr = json_star.getString("star_thr") == null ? "0" : json_star.getString("star_thr");
//                    String star_two = json_star.getString("star_two") == null ? "0" : json_star.getString("star_two");
//                    // 计算百分比 存入数据库
//                    String total1 = Arith.mul(star_fiv, "5");
//                    String total2 = Arith.mul(star_fou, "4");
//                    String total3 = Arith.mul(star_one, "1");
//                    String total4 = Arith.mul(star_thr, "3");
//                    String total5 = Arith.mul(star_two, "2");
//                    String total6 = Arith.add(total1, total2);
//                    String total7 = Arith.add(total3, total4);
//                    String total8 = Arith.add(total5, total6);
//                    String total9 = Arith.add(total7, total8);
//                    String sum11 = Arith.add(star_fiv, star_fou);
//                    String sum12 = Arith.add(star_one, star_thr);
//                    String sum13 = Arith.add(star_two, sum11);
//                    String sum14 = Arith.add(sum12, sum13);
//                    if (StringUtils.isNotBlank(star)) {
//                        star = Arith.div(sum14, total9, 3);
//                    } else {
//                        star = "0";
//                    }
////                }
//                }else{
//                    star = "0";
//                }

//                String querysql="select id from dapp_info where  dapp_id=? ";
//                List list=jdbcTemplate.queryForList(querysql,new Object[]{id});
//                System.out.println(list);
//                String ids =  list.get(0).toString();
//                String idss = JSONObject.fromObject(ids).getString("dapp_id");

//                String updatesql = "update dapp_info set dau_last_day ='"+dau_last_day+"',tx_last_day = '"+tx_last_day+"'" +
//                        ", tx_last_week = '"+tx_last_week+"',volume_last_day ='"+volume_last_day+"',volume_last_day_CNY = '"+volume_last_day_CNY+"',volume_last_day_USD ='"+volume_last_day_USD+"',volume_last_week = '"+volume_last_week+"'" +
//                        ",volume_last_week_CNY = '"+volume_last_week_CNY+"',volume_last_week_USD = '"+volume_last_week_USD+"',chains_id = '"+chains_id+"' where dapp_id = '"+id+"' and categories_id = '"+categories_id+"'";

                String updatesql = "update dapp_info set  pic_url = '"+logo_url+"' " +
                        " where dapp_id = '"+id+"' ";


                String insertsql = "insert into dapp_info (`dapp_id`,`title`,`dau_last_day`,`description`,`description_short`,`tx_last_day`,`tx_last_week`,`volume_last_day`,`volume_last_day_CNY`," +
                        "`volume_last_day_USD`,`volume_last_week`,`volume_last_week_CNY`,`volume_last_week_USD`,`categories_id`,`chains_id`)" +
                        " values " +
                        " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
//                if (StringUtils.isNotBlank(logo_url)) {
                    dbUtil.insertOrUpdateDapp(updatesql, insertsql, id, new Object[]{id, title, dau_last_day, description, description_short, tx_last_day, tx_last_week, volume_last_day, volume_last_day_CNY, volume_last_day_USD, volume_last_week, volume_last_week_CNY, volume_last_week_USD, categories_id, chains_id});
                    System.out.println("----------------------------------------");
//                }
            }
            i++;
            System.out.println("当前执行到第："+i+"页...");
            if (i>120){
                break a;
            }
        }

    }

    @Resource
    JdbcTemplate jdbcTemplate;

    @Test
    public void  getDappUrl() throws  Exception{
        int i=3960;  //
        a:
        while (true){
            System.out.println("开始睡觉....");
            Thread.sleep(1000);
            System.out.println("起床干活....");
            String querysql="select dapp_id from dapp_info where  id=? ";
            List list=jdbcTemplate.queryForList(querysql,new Object[]{i});
            System.out.println(list);
            String ids =  list.get(0).toString();
            String id = JSONObject.fromObject(ids).getString("dapp_id");
            System.err.println(id);
            System.err.println("https://dapp.review/dapp/"+id+"/");
            Document documen=JsoupUtilPor.get("https://dapp.review/dapp/"+id+"/",RequestCount);
            System.out.println(documen);
            Elements script =  documen.select("script");
            String x1 ;
            String url = "";

            x1 = script.get(0).html().replace("window.App=", "");
            System.out.println(x1);
            JSONObject jsonObject2 = JSONObject.fromObject(x1);
            url = jsonObject2.getJSONObject("state").getJSONObject("dapp").getJSONObject("detail").getString("url");
            System.out.println(url);
            String updatesql = "update dapp_info set url = '"+url+"' where id = '"+i+"'";
            jdbcTemplate.update(updatesql);

            i++;
            if (i>6100){
                break a;
            }
        }
    }

    public static void main(String[] args) {
        Boolean flag = true;
        Boolean flag1 = true;
        Object object1 = (Object)flag;
        Object object2 = (Object)flag1;

        System.err.println(object1.equals(object2));
    }

}

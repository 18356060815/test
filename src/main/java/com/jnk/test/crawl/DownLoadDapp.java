package com.jnk.test.crawl;

import com.jnk.test.Service.DBUtil;
import com.jnk.test.util.Arith;
import com.jnk.test.util.HttpClientUtilPro;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

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
        int i=1;
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
                String categories_id = JSONObject.fromObject(json_categories).getString("id");


                JSONArray json_block_chains=jsonObject1.getJSONArray("block_chains");
                String chains_id = JSONObject.fromObject(json_block_chains).getString("id");

                JSONArray json_star = jsonObject1.getJSONArray("star");

                String star = "";
                for (Object o1 : json_star){
                    JSONObject jsonObject2 = JSONObject.fromObject(o1);
                    String star_fiv = jsonObject2.getString("star_fiv");
                    String star_fou = jsonObject2.getString("star_fou");
                    String star_one = jsonObject2.getString("star_one");
                    String star_thr = jsonObject2.getString("star_thr");
                    String star_two = jsonObject2.getString("star_two");
                    // 计算百分比 存入数据库
                    String total1 = Arith.mul(star_fiv,"5");
                    String total2 = Arith.mul(star_fou,"4");
                    String total3 = Arith.mul(star_one,"1");
                    String total4 = Arith.mul(star_thr,"3");
                    String total5 = Arith.mul(star_two,"2");
                    String total6 = Arith.add(total1,total2);
                    String total7 = Arith.add(total3,total4);
                    String total8 = Arith.add(total5,total6);
                    String total9 = Arith.add(total7,total8);
                    String sum11 =  Arith.add(star_fiv,star_fou);
                    String sum12 =  Arith.add(star_one,star_thr);
                    String sum13 =  Arith.add(star_two,sum11);
                    String sum14 =  Arith.add(sum12,sum13);
                    if (StringUtils.isNotBlank(star)){
                        star = Arith.div(sum14,total9,3);
                    }else {
                        star = "0";
                    }
                }
                dbUtil.execute("");

            }
            i++;
        }

    }

}

package com.jnk.test.crawl;

import com.jnk.test.Service.DBUtil;
import com.jnk.test.util.CheckUtil;
import com.jnk.test.util.HttpClientUtilPro;
import com.jnk.test.util.JsoupUtilPor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
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
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Service
public class CoinDownload {
    @Autowired
    DBUtil dbUtil;

    /**
     * 获取币的最新数据， 更新或插入virtual_currency_info表， 插入virtual_currency_history_price表
     */
    @Test
    public void cralVirtualCcurrencyMarket() {
        //https://coinmarketcap.com/all/views/all/
        for (int i = 1; i < 23; i++) {
            String x = HttpClientUtilPro.httpGetRequestCoin("https://coinmarketcap.com/" + i, 1);
            Document document = Jsoup.parse(x);
            Elements elements = document.select("table#currencies").select("tbody").get(0).select("tr");
            for (Element element : elements) {
                String id = element.select("td.text-center").text().trim();//ID
                String name = element.select("td.currency-name").attr("data-sort");//全名
                String symbol = element.select("span.currency-symbol").text();//简称
                // String imgUrl=element.select("td.currency-name").select("img").attr("src");
                String price = element.select("td").get(3).select("a.price").text().replace("$", "");//当前价格
                String volume_24h = element.select("td").get(4).attr("data-sort");//24h交易量
                if ("?".equals(volume_24h)) {
                    volume_24h = "0";
                } else {
                    volume_24h = element.select("td").get(4).attr("data-sort");//24h交易量
                }
                String market_cap = element.select("td.market-cap").attr("data-sort");//市值


                //String percent_change_1h = element.select("td[data-timespan=1h]").attr("data-percentusd");//1h涨幅
                String percent_change_24h = element.select("td.percent-change").text().replace("%", "");//24小时涨幅
                //String percent_change_7d = element.select("td[data-timespan=7d]").attr("data-percentusd");//7天涨幅

                market_cap = market_cap == null ? "0" : market_cap;
                Long market_caps = Double.valueOf(market_cap).longValue();
                //Double percent_change_1hs=0.00;
                Double percent_change_24hs = 0.00;
                //  Double percent_change_7ds=0.00;
//                if(StringUtils.isNotBlank(percent_change_1h)) {
//                    percent_change_1hs=Double.valueOf(percent_change_1h);
//
//                }
                if (StringUtils.isNotBlank(percent_change_24h)) {
                    percent_change_24hs = Double.valueOf(percent_change_24h);

                }
//                if(StringUtils.isNotBlank(percent_change_7d)) {
//                    percent_change_7ds=Double.valueOf(percent_change_7d);
//
//                }

                String volume_24hs = "0.0";
                if (StringUtils.isNotBlank(volume_24h)) {
                    volume_24hs = volume_24h;
                }
                String now_price = "0.0";
                if (StringUtils.isNotBlank(price)) {
                    now_price = price;
                }

                //imgUrl="https://s2.coinmarketcap.com/static/img/coins/32x32/"+imgUrl.split(" ")[0].split("-")[2]+".png";
                //System.out.println("imgUrl : "+imgUrl);//名称
                System.out.println("name : " + name);//名称
                System.out.println("symbol : " + symbol);//简称
                System.out.println("now_price : " + now_price);//当前交易额
                //System.out.println("percent_change_1h : "+percent_change_1hs);//名称
                System.out.println("percent_change_24h : " + percent_change_24hs);//名称
                //System.out.println("percent_change_7d : "+percent_change_7ds);//名称
                System.out.println("market_value : " + market_caps);//名称
                System.out.println("volume_24h : " + volume_24hs);//名称
                name = name.replace("'", "\\'");
                //    public  synchronized  void  insertAndQueryCoin(String updatesql,String name,String symbol,String imgurl,Object[] objects) {
                String updatesql = "update virtual_currency_info set now_price='" + now_price + "'," +
                        "percent_change_24h='" + percent_change_24hs + "'," +
                        "market_value='" + market_caps + "',volume_24h='" + volume_24hs + "' where name='" + name + "' and symbol='" + symbol + "' ";


                String insertsql = "insert into virtual_currency_info " +
                        "(`name`,`symbol`,`now_price`,`percent_change_24h`,`market_value`,`volume_24h`,`update_time`) " +
                        " values " +
                        "(?,?,?,?,?,?,?)";

                dbUtil.insertAndQueryCoin(updatesql, insertsql, name, symbol, new Object[]{name, symbol, now_price, percent_change_24hs, market_caps, volume_24hs, new Date()});

                System.out.println("----------------------------------------");

            }
        }

    }

    //搜索结果币的交易 hash地址 币的合约类型== source
    @Test
    public void getCoin() {

        List<Map<String, Object>> list = dbUtil.find("SELECT * FROM virtual_currency_info WHERE latestBlock LIKE '{\"code\":1,\"msg\":\"成功\"%'");
        System.out.println(list.size());
        a:for (Map<String, Object> map : list) {
            //https://tokenview.com/api/search/btc
            //https://tokenview.com/api/coin/latestBlockOrTx/btc
            String id = map.get("id").toString();

            String symbol = map.get("symbol").toString();
            String name = map.get("name").toString();
            name = name.replace(" ", "%20");

            String url1 = "https://tokenview.com/api/search/" + name;
            System.out.println(url1);
            System.out.println(name);
            if (name.contains("]")) {
                continue;
            }
            System.out.println(symbol);
            String data1 = null;

            try {
                data1 = HttpClientUtilPro.send(url1, 1, "https://tokenview.com");
            } catch (Exception e) {
                continue;
            }
            System.out.println(data1);
            if (data1 == null) {
                continue;
            }

            try {
            JSONObject jsonObject = JSONObject.fromObject(data1);
                if (jsonObject.getString("code").equals("1")) {
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (Object obj : data) {
                        JSONObject jsonObject1 = JSONObject.fromObject(obj);
                        String type = jsonObject1.getString("type");
                        if ("detail".equals(type)) {
                            String uniqueId = jsonObject1.getString("uniqueId");
                            String loweruniqueId = uniqueId.toLowerCase();
                            if (loweruniqueId.equals(symbol.toLowerCase()) || loweruniqueId.equals(name.replace(" ", "_").toLowerCase())) {
                                JSONObject jsonObject3 = new JSONObject();
                                String issueDate = jsonObject1.getOrDefault("issueDate", "").toString();//发行时间
                                String algorithm = jsonObject1.getOrDefault("algorithm", "").toString();//加密算法
                                String timeForBlock = jsonObject1.getOrDefault("timeForBlock", "").toString();//模块周期
                                String supply = jsonObject1.getOrDefault("supply", "").toString();//发行总量
                                String consensusMethod = jsonObject1.getOrDefault("consensusMethod", "").toString();//公识机制
                                String coinShortName = jsonObject1.getOrDefault("coinShortName", "").toString();//货币单位
                                String mineable = jsonObject1.getOrDefault("mineable", "").toString();//是否可挖
                                jsonObject3.put("issueDate", issueDate);
                                jsonObject3.put("algorithm", algorithm);
                                jsonObject3.put("timeForBlock", timeForBlock);
                                jsonObject3.put("supply", supply);
                                jsonObject3.put("consensusMethod", consensusMethod);
                                jsonObject3.put("coinShortName", coinShortName);
                                jsonObject3.put("mineable", mineable);

                                dbUtil.execute("update virtual_currency_info set finances ='" + jsonObject3.toString() + "'  where id=" + id);
                                System.out.println("update virtual_currency_info set finances ='" + jsonObject3.toString() + "'  where id=" + id);
                                uniqueId = URLEncoder.encode(uniqueId);
                                String data2 = null;
                                try {
                                    data2 = HttpClientUtilPro.send("https://tokenview.com/api/coin/latestBlockOrTx/" + uniqueId, 1, url1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if(data2==null){
                                    continue a;
                                }
                                    JSONObject jsonObject2 = JSONObject.fromObject(data2);
                                    dbUtil.execute("update virtual_currency_info set latestBlock ='" + jsonObject2.toString() + "' where id=" + id);
                                    System.out.println("update virtual_currency_info set latestBlock ='" + jsonObject2.toString() + "' where id=" + id);


                                break;
                            } else {
                                break;
                            }
                        }
                    }
                    System.out.println("------------------");
                }
            } catch (Exception e) {
                System.out.println("=====");
                e.printStackTrace();
               continue;
            }
        }
    }
}

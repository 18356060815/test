package com.jnk.test.crawl;

import com.alibaba.fastjson.JSONObject;
import com.jnk.test.Service.DBUtil;
import com.jnk.test.util.HttpClientUtilPro;
import com.jnk.test.util.JsoupUtilPor;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Service
public class vixCrawl {

    @Autowired
    DBUtil DBUtil;

    @Test
    public  void  getVix () {
       Document html= JsoupUtilPor.get("https://alternative.me/crypto/fear-and-greed-index/",1);
       Elements elements=html.select("div.box-row").select("div.fng-circle");
       String vix= elements.get(0).text();
       System.out.println(vix);
       DBUtil.execute("update vix_data set vix='"+vix+"' where id=1");
    }


    public static void main(String[] args) {
//        //wx735e0203cd994b90
//        //73a1ee6f106c999973df184c80350cb3
//
//        String data= HttpClientUtilPro.httpGetRequest("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx735e0203cd994b90&secret=73a1ee6f106c999973df184c80350cb3",1);
//        JSONObject jsonObject=JSONObject.fromObject(data);
//        String access_token=jsonObject.getString("access_token");
//        //ACCESS_TOKEN
//        System.out.println(access_token);
//
//        String data1= HttpClientUtilPro.httpGetRequest("https://api.weixin.qq.com/cgi-bin/get_current_autoreply_info?access_token="+access_token,1);
//
//        System.out.println(data1);
        //PAY SUCCESS 成功
//        Map<String ,Object > map =new HashMap();
//        map.put("amount","100");
//        map.put("appid","wx9dda0127a53865e8");
//        map.put("desc","bee360");
//        map.put("openid","oQmEa0nMeGrU4riukkzZsx1yL-9A");
//        map.put("orderno","2019030216131163462");
//        String plainText=map.get("amount").toString()+map.get("appid").toString()+map.get("desc").toString()+"OMSRN0Pl32rzIRkrOsIg4ADAevfF4Rv0J"+map.get("openid").toString()+map.get("orderno").toString();
//        map.put("sign",md5(plainText));
//
//        String data=HttpClientUtilPro.httpPostRequest("https://dayou.liangxinloan.com/common/dayou/promotiontrans",map,1);
//        System.out.println(data);

        Map<String ,Object > querymap =new HashMap();
        querymap.put("orderno","2019030418290254585");
        querymap.put("appid","wx9dda0127a53865e8");
        String plainTexts=querymap.get("appid").toString()+"OMSRN0Pl32rzIRkrOsIg4ADAevfF4Rv0J"+querymap.get("orderno").toString();
        querymap.put("sign",md5(plainTexts));
        String querydata=HttpClientUtilPro.httpPostRequest("https://dayou.liangxinloan.com/common/dayou/querypromotiontrans",querymap,1);

        JSONObject jsonObject = JSONObject.parseObject(querydata);
        System.out.println(jsonObject);

        //https://dayou.liangxinloan.com/common/dayou/querypromotiontrans
    }


    //写一个md5加密的方法
    public static String md5(String plainText) {
        //定义一个字节数组
        byte[] secretBytes = null;
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            //对字符串进行加密
            md.update(plainText.getBytes());
            //获得加密后的数据
            secretBytes = md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有md5这个算法！");
        }
        //将加密后的数据转换为16进制数字
        String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

}

package com.jnk.test.ScheduledTask;

import com.alibaba.fastjson.JSONObject;
import com.jnk.test.TestApplication;
import com.jnk.test.crawl.DowloadPage;
import com.jnk.test.crawl.DowloadPage_task;
import com.jnk.test.util.CheckUtil;
import com.jnk.test.Service.DBUtil;
import com.jnk.test.util.HttpClientUtilPro;
import com.jnk.test.util.MD5;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jnk.test.crawl.vixCrawl.md5;

/**
 *
 *
 * **/

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TestApplication.class,DowloadPage.class,DowloadPage_task.class})
@Configuration
public class one_task {
     @Autowired
     DBUtil dbUtil;
     //todo
     @Test//一次性冻结快豆
     public void oneScore(){
       String querysql= "select id from user_info where user_tel in(17026698471,17381545043,13543513367,18745890384,13207151251,13060115237,18771640753,15900172471,13346663539,15837386267,15717951235,13871980132,15700931403,18770853839,15546174327,18123049656,17642027208,18748828049,18482374324,18121597203,17637244946,18483170106,15279474934,15181912674,18216495372,13934554201,13285785540,13006994734,13599579903,18052634839,18115873204,15689708725,15039953259,18502197062,18514339815,17586095845,13920081933,17014412252,15917179442,15342614387,15979486341,15828187545,18972816273,13131786370,15589709594);";
       List<Map<String, Object>> list = dbUtil.find(querysql);
       System.out.println(list.size());
       if(list.size()==45){
           for(Map map:list){
               String id=map.get("id").toString();
               dbUtil.execute("insert into  user_operate (`user_id`,`operate`,`sketch`,`operate_value`,`unit`) values ('"+id+"','year_award','年终活动奖励','1000','score');");
               dbUtil.execute("update user_info set score=score+1000 where id ='"+id+"'");

           }
       }

     }

     //---------->需要运行上面冻结快豆  再运行所有转零钱  再运行下面冻结零钱
//    //todo
//    @Test//一次性冻结零钱
//    public void oneMoney(){
//        List<Map<String, Object>> list = dbUtil.find("select SUM(operate_value) operate_value,user_id from user_operate where sketch='邀请送BRT' GROUP BY user_id;");
//        for(Map map:list){
//            String user_id=map.get("user_id").toString();
//            System.out.println(user_id);
//            String operate_value=map.get("operate_value").toString();
//            dbUtil.execute("update user_info set frozen_score='"+operate_value+"' where id='"+user_id+"'");
//
//        }
//    }

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
//        map.put("openid","oQmEa0tNqx71UdiyRpo2Jahl5xcw");
//        map.put("orderno","20190309061500105603");
//        String plainText=map.get("amount").toString()+map.get("appid").toString()+map.get("desc").toString()+"OMSRN0Pl32rzIRkrOsIg4ADAevfF4Rv0J"+map.get("openid").toString()+map.get("orderno").toString();
//        map.put("sign",MD5.GetMD5Code(plainText));
//
//        String data=HttpClientUtilPro.httpPostRequest("https://dayou.liangxinloan.com/common/dayou/promotiontrans",map,1);
//        System.out.println(data);

        Map<String ,Object > querymap =new HashMap();
        querymap.put("orderno","2019030905054095173");
        querymap.put("appid","wx9dda0127a53865e8");
        String plainTexts=querymap.get("appid").toString()+"OMSRN0Pl32rzIRkrOsIg4ADAevfF4Rv0J"+querymap.get("orderno").toString();
        System.out.println("前 "+plainTexts);
        querymap.put("sign", MD5.GetMD5Code(plainTexts));
        System.out.println("后 "+querymap.get("sign"));

        String querydata= HttpClientUtilPro.httpPostRequest("https://dayou.liangxinloan.com/common/dayou/querypromotiontrans",querymap,1);

        JSONObject jsonObject = JSONObject.parseObject(querydata);
        System.out.println(jsonObject);

        //https://dayou.liangxinloan.com/common/dayou/querypromotiontrans
    }

}

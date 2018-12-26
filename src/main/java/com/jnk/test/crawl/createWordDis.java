//package com.jnk.test.crawl;
//
//import com.jnk.test.Service.DBUtil;
//import org.apache.commons.lang.StringEscapeUtils;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class createWordDis {
//
//    @Autowired
//    DBUtil dbUtil;
//    @Test
//    public  void  createWord(){
//        List <Map<String ,Object>> list1=dbUtil.find("select symbol as name  from virtual_currency_info");
//        List <Map<String ,Object>> list2=dbUtil.find("select name  from virtual_currency_info");
//        List <Map<String ,Object>> list3=dbUtil.find("select name  from app_info");
//        List <Map<String ,Object>> list4=dbUtil.find("select name  from virtual_currency_exchange_info");
//
//        list1.addAll(list2);
//        list1.addAll(list3);
//        list1.addAll(list4);
//
//        for(Map map:list1){
//           String name = map.get("name").toString();
//           System.out.println(name);
//           name=StringEscapeUtils.escapeSql(name);
//           dbUtil.executes(name);
//        }
////        List <Map<String ,Object>> list5=dbUtil.find("select  search_key  from news_info");
////        Set<String> l=new HashSet();
////        for(Map map:list5){
////            Object search_key=map.get("search_key");
////            if(search_key!=null){
////                String [] arr=search_key.toString().split(",");
////                for(String a:arr ){
////                    if(a==null){
////                        continue;
////                    }
////                    if(a.equals("")){
////                        continue;
////                    }
////                    if(a.length()<=1){
////                        continue;
////                    }
////                    l.add(a);
////                }
////            }
////        }
////        for(String name:l){
////            name=StringEscapeUtils.escapeSql(name);
////            System.out.println(name);
////            dbUtil.executes(name);
////        }
//    }
//}

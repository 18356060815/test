package com.jnk.test.crawl;

import com.jnk.test.Service.DBUtil;
import com.jnk.test.util.JsoupUtilPor;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class vixCrawl {

    @Autowired
    DBUtil DBUtil;
    public  void  getVix () {
       Document html= JsoupUtilPor.get("https://alternative.me/crypto/fear-and-greed-index/",1);
       Elements elements=html.select("div.fng-secondary").select("div.fng-circle");
       String vix= elements.get(0).text();
       System.out.println(vix);
        DBUtil.execute("update vix_data set vix='"+vix+"' where id=1");
    }

    public static void main(String[] args) {
       System.out.println(JsoupUtilPor.get("https://www.qisuu.la/du/29/29517/9598375.html",1));
    }
}

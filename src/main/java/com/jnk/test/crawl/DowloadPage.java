package com.jnk.test.crawl;

import com.jnk.test.CrawlInterface.Download;
import com.jnk.test.util.HttpClientUtilPro;
import com.jnk.test.util.JsoupUtilPor;
import com.jnk.test.util.StaticFile;
import net.sf.json.JSONObject;


public class DowloadPage implements Download {


    @Override
    public  void downloadPage() {
        String news=HttpClientUtilPro.httpGetRequest(StaticFile.LXCJ_NEWS_URL);
        System.out.print(news);
        JSONObject jsonObject=JSONObject.fromObject(news);

    }

    public static void main(String[] args) {
        DowloadPage dowloadPage=new DowloadPage();
        dowloadPage.downloadPage();

    }


}

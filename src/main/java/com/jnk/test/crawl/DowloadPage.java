package com.jnk.test.crawl;

import com.jnk.test.CrawlInterface.Download;
import com.jnk.test.util.HttpClientUtilPro;
import com.jnk.test.util.JsoupUtilPor;
import com.jnk.test.util.StaticFile;


public class DowloadPage implements Download {


    @Override
    public  void downloadPage() {
        String news=HttpClientUtilPro.httpGetRequest(StaticFile.LXCJ_NEWS_URL);
        System.out.print(news);

    }

    public static void main(String[] args) {
        DowloadPage dowloadPage=new DowloadPage();
        for(int i=0;i<50;i++){
            dowloadPage.downloadPage();

        }
    }
}

package com.jnk.test.ScheduledTask;

import com.jnk.test.crawl.DowloadPage;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 备用
 *
 *
 * **/
@Configuration
public class ScheduledTest_Reserve {
    @Scheduled(cron = "0/5 * * * * ?")
    public void rsrun14_9() {
        System.out.print("go");
        DowloadPage dowloadPage=new DowloadPage();
        dowloadPage.downloadPage();

    }
}

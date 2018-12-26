//package com.jnk.test.ScheduledTask;
//
//import com.jnk.test.TestApplication;
//import com.jnk.test.crawl.DowloadPage;
//import com.jnk.test.crawl.DowloadPage_task;
//import com.jnk.test.util.CheckUtil;
//import com.jnk.test.Service.DBUtil;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.sql.DataSource;
//
///**
// *
// *
// * **/
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = {TestApplication.class,DowloadPage.class,DowloadPage_task.class})
//@Configuration
//public class one_task {
//    private static final Logger logger = LoggerFactory.getLogger(DowloadPage.class);
//
//    @Autowired
//    DataSource dataSource;
//    @Autowired
//    DowloadPage dowloadPage;
//    @Autowired
//    DowloadPage_task dowloadPage_task;
//
//    //"0/10 * * * * ?"
//    @Test
//    @Scheduled(cron = "0/10 * * * * ?")
//    public void babite_task() {//巴比特
//        System.out.println("巴比特------");
//        dowloadPage.BabiteDownloadPage(dataSource);
//
//
//    }
//    //    @Scheduled(cron = "0 0/40 * * * ?")
////    public void huoqiu_task() {// 火球财经
////        System.out.println("------");
////        dowloadPage_task.HuoqiuDownloadPage(dataSource);
////
////    }
////
//
//    @Test
//    @Scheduled(cron = "0/10 * * * * ?")
//    public void jinse_task() {//金色财经
//        System.out.println("金色财经------");
//        dowloadPage.JinseDownloadPage(dataSource);
//
//    }
//
//
//    @Test
//    @Scheduled(cron = "0/10 * * * * ?")
//    public void Huoxing_task() {//火星财经
//        System.out.println("火星财经------");
//        dowloadPage.HuoXingDownloadPage(dataSource);
//
//    }
//
//    @Test
//    @Scheduled(cron = "0/10 * * * * ?")
//    public void JinNiu_task() {//金牛财经
//        System.out.println("金牛财经------");
//        dowloadPage.JinNiuDownloadPage(dataSource);
//
//    }
//
//}

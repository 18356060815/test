package com.jnk.test.ScheduledTask;

import com.jnk.test.crawl.DowloadPage_task;
import com.jnk.test.crawl.HotNodePro;
import com.jnk.test.crawl.vixCrawl;
import com.jnk.test.util.CheckUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
/**
 *分类信息采集
 **/


@Configuration
@ComponentScan(basePackages = "com.jnk.*")
public class ScheduledTest_Reserve  {
//    @Autowired
//    HotNodePro hotNodePro;
//        @Scheduled(fixedDelay = 240000000)//巴比特 40分钟一次
//    public void babite_task() {
//            hotNodePro.getHotNode();
//
//    }
//    private static final Logger logger = LoggerFactory.getLogger(ScheduledTest_Reserve.class);
//    @Autowired
//    DowloadPage_task dowloadPage_task;
//    @Autowired
//    vixCrawl vixCrawl;
//
//    //"0/10 * * * * ?"
//    @Scheduled(fixedDelay = 2400000)//巴比特 40分钟一次
//    public void babite_task() {
//        logger.error("巴比特新闻开始---------------------------------");
//        dowloadPage_task.BabiteDownloadPage();
//
//    }
//
//    @Scheduled(fixedDelay = 2400000)//金色财经 40分钟一次
//    public void jinse_task() {
//        logger.error("金色财经新闻开始---------------------------------");
//        dowloadPage_task.JinseDownloadPage();
//    }
//
//
//    @Scheduled(fixedDelay = 2400000)//火星财经 40分钟一次
//    public void Huoxing_task() {
//        logger.error("火星财经新闻开始---------------------------------");
//        dowloadPage_task.HuoXingDownloadPage();
//
//    }
//
//    @Scheduled(fixedDelay = 2400000)//金牛财经 40分钟一次
//    public void JinNiu_task() {
//        logger.error("金牛财经新闻开始---------------------------------");
//        dowloadPage_task.JinNiuDownloadPage();
//
//    }
//
//    @Scheduled(fixedDelay = 2400000)//币莱财经 40分钟一次
//    public void Bilai_task() {
//        logger.error("币莱财经新闻开始---------------------------------");
//        dowloadPage_task.BilaiDownloadPage();
//
//    }
//
//    @Scheduled(fixedDelay = 2400000)//嘻哈财经 40分钟一次
//    public void Xiha_task() {
//        logger.error("嘻哈财经新闻开始---------------------------------");
//        dowloadPage_task.XihaDownloadPage();
//
//    }
//
//    @Scheduled(fixedDelay = 3600000)//知乎 区块链 比特币资讯 一小时一次
//    public void zhihu_task() {//知乎资讯
//        logger.error("知乎资讯开始---------------------------------");
//        dowloadPage_task.ZhihuDownloadPage();
//    }
//
//
//    @Scheduled(fixedDelay = 7200000)//恐慌指数 两小时一次
//    public void vix_task() {
//        logger.error("恐慌指数开始---------------------------------");
//        vixCrawl.getVix();
//    }
//
//    @Scheduled(fixedDelay = 3600000)//链得得应用 两小时一次
//    public void Liandede_task() {
//        logger.error("链得得应用---------------------------------");
//        dowloadPage_task.LiandedeDownloadPage();
//    }
//
//    @Scheduled(fixedDelay = 3600000)//核财经 深度----> 观点 两小时一次
//    public void Hecaijing_task() {
//        logger.error("核财经 深度----观点---------------------------------");
//        dowloadPage_task.HecaijingDownload();
//    }
//
////    @Scheduled(cron = "0 0 0 * * ?")//词库更新 一天一次
////    public void word_task() {
////        logger.error("词库更新开始---------------------------------");
////        createWordDis.createWord();
////    }
//
////    @Scheduled(cron = "0 0/40 * * * ?")
////    public void huoqiu_task() {// 火球财经
////        System.out.println("------");
////        dowloadPage_task.HuoqiuDownloadPage(dataSource);
////
////    }

}

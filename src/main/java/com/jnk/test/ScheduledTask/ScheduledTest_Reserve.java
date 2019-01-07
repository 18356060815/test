package com.jnk.test.ScheduledTask;

import com.jnk.test.crawl.AllNewNewsInfo;
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
      @Autowired
      AllNewNewsInfo allNewNewsInfo;

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTest_Reserve.class);
    @Autowired
    DowloadPage_task dowloadPage_task;
    @Autowired
    vixCrawl vixCrawl;

    //"0/10 * * * * ?"
    @Scheduled(fixedDelay = 2000000)//巴比特 40分钟一次
    public void babite_task() {
        logger.error("巴比特新闻开始---------------------------------");
        dowloadPage_task.BabiteDownloadPage();//标签   (标签优先)
        allNewNewsInfo.BabiteDownloadPage();//推荐最后

    }

    @Scheduled(fixedDelay = 2100000)//金色财经 40分钟一次
    public void jinse_task() {
        logger.error("金色财经新闻开始---------------------------------");
        dowloadPage_task.JinseDownloadPage();// (标签优先)-->推荐最后
    }


    @Scheduled(fixedDelay = 2200000)//火星财经 40分钟一次
    public void Huoxing_task() {
        logger.error("火星财经新闻开始---------------------------------");
        dowloadPage_task.HuoXingDownloadPage();// (标签优先)-->推荐最后

    }

    @Scheduled(fixedDelay = 2300000)//金牛财经 40分钟一次
    public void JinNiu_task() {
        logger.error("金牛财经新闻开始---------------------------------");
        dowloadPage_task.JinNiuDownloadPage();

    }

//    @Scheduled(fixedDelay = 2400000)//币莱财经 40分钟一次
//    public void Bilai_task() {
//        logger.error("币莱财经新闻开始---------------------------------");
//        dowloadPage_task.BilaiDownloadPage();
//
//    }

    @Scheduled(fixedDelay = 2400000)//嘻哈财经 40分钟一次
    public void Xiha_task() {
        logger.error("嘻哈财经新闻开始---------------------------------");
        dowloadPage_task.XihaDownloadPage();

    }

    @Scheduled(fixedDelay = 2500000)//知乎 区块链 比特币资讯 一小时一次
    public void zhihu_task() {//知乎资讯
        logger.error("知乎资讯开始---------------------------------");
        dowloadPage_task.ZhihuDownloadPage();
    }


    @Scheduled(fixedDelay = 7200000)//恐慌指数 两小时一次
    public void vix_task() {
        logger.error("恐慌指数开始---------------------------------");
        vixCrawl.getVix();
    }

    @Scheduled(fixedDelay = 2400000)//链得得应用 两小时一次
    public void Liandede_task() {
        logger.error("链得得应用---------------------------------");
        dowloadPage_task.LiandedeDownloadPage();

        logger.error("链得得推荐---------------------------------");
        allNewNewsInfo.LiandedeDownloadPage();
    }

    @Scheduled(fixedDelay = 1900000)//核财经 深度----> 观点 两小时一次
    public void Hecaijing_task() {
        logger.error("核财经 深度----观点---------------------------------");
        dowloadPage_task.HecaijingDownload();
    }



//    @Scheduled(cron = "0 0/40 * * * ?")
//    public void huoqiu_task() {// 火球财经
//        System.out.println("------");
//        dowloadPage_task.HuoqiuDownloadPage(dataSource);
//
//    }


////--------------------------------------------------------------------------------------无耻的分割线-------------------------------------------------------------------------


    @Scheduled(fixedDelay = 1800000)//挖链网推荐 40分钟一次
    public void walianwang_new_task() {
        logger.error("挖链网推荐---------------------------------");
        allNewNewsInfo.WalianDownloadPage();
    }
    @Scheduled(fixedDelay = 1600000)//陀螺财经推荐 40分钟一次
    public void tuoluocaijing_new_task() {
        logger.error("陀螺财经---------------------------------");
        allNewNewsInfo.Tuoluocaijing();
    }
    @Scheduled(fixedDelay = 1700000)//深链财经推荐 40分钟一次
    public void shenliancaijing_new_task() {
        logger.error("深链财经---------------------------------");
        allNewNewsInfo.shenliancaijing();
    }

}

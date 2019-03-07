package com.jnk.test.ScheduledTask;

import com.jnk.test.crawl.*;
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
//    AllNewNewsInfo allNewNewsInfo;
//    private static final Logger logger = LoggerFactory.getLogger(ScheduledTest_Reserve.class);
//    @Autowired
//    DowloadPage_task dowloadPage_task;
//    @Autowired
//    vixCrawl vixCrawl;
//    @Autowired
//    CoinDownload coinDownload;
//
//    @Scheduled(fixedDelay = 30000)//币行情 30s一次
//    public void coinMarketcup_task() {
//        logger.error("币行情---------------------------------");
//        coinDownload.cralVirtualCcurrencyMarket();
//    }
//
//    //"0/10 * * * * ?"
//    @Scheduled(fixedDelay = 2000000)//巴比特
//    public void babite_task() {
//        logger.error("巴比特新闻开始---------------------------------");
//        dowloadPage_task.BabiteDownloadPage();//标签   (标签优先)
//        allNewNewsInfo.BabiteDownloadPage();//推荐最后
//
//    }
//
//    @Scheduled(fixedDelay = 2100000)//金色财经
//    public void jinse_task() {
//        logger.error("金色财经新闻开始---------------------------------");
//        dowloadPage_task.JinseDownloadPage();// (标签优先)-->推荐最后
//    }
//
//
//    @Scheduled(fixedDelay = 2200000)//火星财经
//    public void Huoxing_task() {
//        logger.error("火星财经新闻开始---------------------------------");
//        dowloadPage_task.HuoXingDownloadPage();// (标签优先)-->推荐最后
//
//    }
//
//    @Scheduled(fixedDelay = 2300000)//金牛财经
//    public void JinNiu_task() {
//        logger.error("金牛财经新闻开始---------------------------------");
//        dowloadPage_task.JinNiuDownloadPage();
//
//    }
//
////    @Scheduled(fixedDelay = 2400000)//币莱财经
////    public void Bilai_task() {
////        logger.error("币莱财经新闻开始---------------------------------");
////        dowloadPage_task.BilaiDownloadPage();
////
////    }
//
////    @Scheduled(fixedDelay = 2400000)//嘻哈财经
////    public void Xiha_task() {
////        logger.error("嘻哈财经新闻开始---------------------------------");
////        dowloadPage_task.XihaDownloadPage();
////    }
//
//    @Scheduled(fixedDelay = 2500000)//知乎 区块链 比特币资讯
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
//    @Scheduled(fixedDelay = 2400000)//链得得 推荐 + 应用 + 政策
//    public void Liandede_task() {
//        logger.error("链得得应用 + 政策---------------------------------");
//        dowloadPage_task.LiandedeDownloadPage();
//
//        logger.error("链得得推荐---------------------------------");
//        allNewNewsInfo.LiandedeDownloadPage();
//    }
//
//    @Scheduled(fixedDelay = 1900000)//核财经 深度----> 观点
//    public void Hecaijing_task() {
//        logger.error("核财经 深度----观点---------------------------------");
//        dowloadPage_task.HecaijingDownload();
//    }
//
//
//
//
//    @Scheduled(fixedDelay = 1950000)//链向财经
//    public void lianxiang_task() {
//        logger.error("链向财经---------------------------------");
//        dowloadPage_task.getLianxiangDownload();
//    }
//
//    @Scheduled(fixedDelay = 2055000)//未来财经
//    public void weilai_task() {
//        logger.error("未来财经---------------------------------");
//        dowloadPage_task.getweilaicaijingDownLoad();
//    }
//
//
//    @Scheduled(fixedDelay = 1860000)//火球财经
//    public void huoqiu_task() {
//        System.out.println("火球财经---------------------------------");
//        dowloadPage_task.HuoqiuDownloadPage();
//
//    }
//
//
//////--------------------------------------------------------------------------------------无耻的分割线-------------------------------------------------------------------------
//
//    @Scheduled(fixedDelay = 1800000)//挖链网
//    public void walianwang_new_task() {
//        logger.error("挖链网推荐---------------------------------");
//        allNewNewsInfo.WalianDownloadPage();
//    }
//
//    @Scheduled(fixedDelay = 1600000)//陀螺财经
//    public void tuoluocaijing_new_task() {
//        logger.error("陀螺财经---------------------------------");
//        allNewNewsInfo.Tuoluocaijing();
//    }
//    @Scheduled(fixedDelay = 1700000)//深链财经
//    public void shenliancaijing_new_task() {
//        logger.error("深链财经---------------------------------");
//        allNewNewsInfo.shenliancaijing();
//    }
//    @Scheduled(fixedDelay = 2700000)//链闻公司
//    public void lianwen_new_task() {
//        logger.error("链闻公司---------------------------------");
//        dowloadPage_task.lianwen();
//    }
//
//    @Scheduled(fixedDelay = 2800000)//bitcoin86
//    public void bitcoin86_new_task() {
//        logger.error("bitcoin86---------------------------------");
//        dowloadPage_task.getbitcoin86DownLoad();
//    }
//
//    @Scheduled(fixedDelay = 2900000)//Fintech News
//    public void Fintech_News_new_task() {
//        logger.error("Fintech News---------------------------------");
//        dowloadPage_task.getFNDownLoad();
//    }
//
//    @Scheduled(fixedDelay = 2300000)//星球日报
//    public void odaily_new_task() {
//        logger.error("星球日报---------------------------------");
//        dowloadPage_task.getodailyDownLoad();
//    }
//
//    @Scheduled(fixedDelay = 21200000)//链氪财经
//    public void chainkr_new_task() {
//        logger.error("链氪财经---------------------------------");
//        dowloadPage_task.getchainkrDownLoad();
//    }
//    @Scheduled(fixedDelay = 16900000)//鸵鸟区块链
//    public void tuoniao_new_task() {
//        logger.error("鸵鸟区块链---------------------------------");
//        allNewNewsInfo.tuoniaoqukuailiang();
//    }
//
//    @Scheduled(fixedDelay = 21100000)//火讯财经
//    public void Huoxun_new_task() {
//        logger.error("火讯财经---------------------------------");
//        dowloadPage_task.HuoxunDownloadPage();
//    }
//
//   @Scheduled(fixedDelay = 21300000)//小葱
//    public void xiaocong_new_task() {
//        logger.error("小葱---------------------------------");
//        dowloadPage_task.getxiaochongDownloadPage();
//    }
//
//
//   @Scheduled(fixedDelay = 20000000)//布洛克科技
//    public void block_new_task() {
//        logger.error("布洛克科技---------------------------------");
//        dowloadPage_task.getblock360Download();
//    }
//
////    @Scheduled(fixedDelay = 600000)//公链信息
////    public void gonglian() {
////        logger.error("公链信息---------------------------------");
////        coinDownload.getCoin();
////    }

}

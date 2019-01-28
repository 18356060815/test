package com.jnk.test.crawl;

import com.jnk.test.Service.DBUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

public class createWordDis {

    public static void main(String[] args) throws  Exception{
        String firefoxdriver = "E:\\selenium\\geckodriver.exe";
        String firefox = "D:\\firefox\\firefox.exe";
        System.setProperty("webdriver.gecko.driver",firefoxdriver);
        System.setProperty("webdriver.firefox.bin",firefox);
        WebDriver driver = new FirefoxDriver();
        driver.get("https://www.alipay.com/");
        driver.findElement(By.className("personal-login")).click();
        new Thread().sleep(2000);
        driver.findElement(By.cssSelector("a[seed=clickMe-amButton]")).click();
        new Thread().sleep(2000);
        String url=driver.switchTo().frame("J_loginIframe").getPageSource();
        System.out.println(url);
        driver.findElement(By.className("J-input-user")).sendKeys("18356060815");
        driver.findElement(By.id("password_rsainput")).sendKeys("a86508650");
        new Thread().sleep(1000);

        driver.findElement(By.id("J-login-btn")).click();

//        WebElement source=driver.findElement(By.id("nc_1_n1z"));
//        Actions action = new Actions(driver);
//
//        action.clickAndHold(source).moveByOffset((int)(Math.random()*200)+80, 0);
//        new Thread().sleep(1000);
//        action.clickAndHold(source).moveByOffset((int)(Math.random()*200)+80, 0);
//        new Thread().sleep(1000);
//
//
//        //拖动完释放鼠标
//        action.moveToElement(source).release();
//        //组织完这些一系列的步骤，然后开始真实执行操作
//        Action actions = action.build();
//        actions.perform();
//        driver.findElement(By.id("J_SubmitStatic")).click();

    }
}

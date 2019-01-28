package com.jnk.test.util;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * 业务概述:
 * curl测试
 *
 * @author xx
 * @create 201x-0x-1x 下午x:xx
 */
public class CurlDemo {


    public static void main(String[] args) {
       System.out.println(curl("https://coinmarketcap.com/all/views/all/"));
    }
    public static String curl(String addr){
        String line = "";
        String m="";
        try {
            URL url = new URL(addr);
            FileOutputStream fo = new FileOutputStream(new File("D:/Baidu.html"));
            BufferedOutputStream oBuffer = new BufferedOutputStream(fo);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
            while ((line = reader.readLine()) != null){
                line += "\n";
                oBuffer.write(line.getBytes());
            }
            oBuffer.flush();
            oBuffer.close();
        }catch (Exception e){
            e.printStackTrace();

        }
        return  m;
    }


}


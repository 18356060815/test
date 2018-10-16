package com.jnk.test.util;


import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

import static com.jnk.test.util.CheckUtil.getTrace;


public class HttpClientUtilPro {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtilPro.class);

    private static PoolingHttpClientConnectionManager cm;
    private static String UTF_8 = "UTF-8";
    private static int TIME_OUT =  6000;

    private static void init() {
        if (cm == null) {
            cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(50);// 整个连接池最大连接数
            cm.setDefaultMaxPerRoute(5);// 每路由最大连接数，默认值是2
        }
    }

    /**
     * 通过连接池获取HttpClient
     *
     * @return
     */
    private static CloseableHttpClient getHttpClient() {
        init();
        return HttpClients.custom().setConnectionManager(cm).setRetryHandler(new DefaultHttpRequestRetryHandler(3,true)) .build();
    }


//    static String IPURL="http://webapi.http.zhimacangku.com/getip?num=1&type=2&pro=&city=0&yys=0&port=1&pack=20822&ts=1&ys=1&cs=0&lb=1&sb=0&pb=4&mr=1&regions=";
//    public static JSONObject getProxyIp() {
//        HttpGet httpGet = new HttpGet(IPURL);
//        RequestConfig config = RequestConfig.custom()
//                .setConnectTimeout(TIME_OUT).setSocketTimeout(TIME_OUT)
//                .build();
//        httpGet.setConfig(config);
//        String ipport=getResult(httpGet);
//
//        JSONObject json=JSONObject.fromObject(ipport);
//
//        HttpGet httpTest = new HttpGet("https://www.baidu.com");
//
//        CloseableHttpClient httpClient = getHttpClient();
//        try {
//            CloseableHttpResponse response = httpClient.execute(httpTest);
//            int Status=response.getStatusLine().getStatusCode();
//            System.out.println("Status : "+Status);
//            if(Status==200){
//                return json;
//            }else {
//                CheckUtil.sleep(4000);
//                json=getProxyIp();
//            }
//        }catch (Throwable e){
//            e.printStackTrace();
//            logger.error(getTrace(e)+"IP不可用");
//            CheckUtil.sleep(4000);
//            json=getProxyIp();
//        }finally {
//            System.out.println("=========");
//        }
//        return json;
//    }




    // 代理隧道验证信息
    final static String ProxyUser = "H6X17QB4LC84KZOD";
    final static String ProxyPass = "E5B0CD39481D0579";

    // 代理服务器
    final static String ProxyHost = "http-dyn.abuyun.com";
    final static Integer ProxyPort = 9020;



    /**
     *
     *
     *all
     *
     * **/
    public static String httpGetRequest(String url) {
        HttpGet httpGet = new HttpGet(url);
        RequestConfig config = RequestConfig.custom()
               .setConnectTimeout(TIME_OUT).setSocketTimeout(TIME_OUT)
               .build();
        httpGet.setConfig(config);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
        return getResult(httpGet);
    }


    /**
     *
     *
     *all
     *
     * **/
    public static JSONObject JshttpGetRequest(String url, String start) {
        HttpGet httpGet = new HttpGet(url);
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(TIME_OUT).setSocketTimeout(TIME_OUT)
                .build();
        httpGet.setConfig(config);
        String str=getResult(httpGet);
        str=str.replace(start,"");
        str=str.substring(0,str.length()-2);
        return JSONObject.fromObject(str);

    }



    /**
     *
     *
     *
     *
     * **/
    public static String httpGetRequest(String url, Map<String, Object> params) throws URISyntaxException {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);

        HttpGet httpGet = new HttpGet(ub.build());
        return getResult(httpGet);
    }



    /**
     *
     *
     *ssq
     *
     * **/
    public static String httpGetRequestHead(String url, Map<String, Object> headers) {
        HttpGet httpGet = new HttpGet(url);
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(TIME_OUT).setSocketTimeout(TIME_OUT)
                .build();
        httpGet.setConfig(config);
        for(String key:headers.keySet()){
            httpGet.setHeader(key, headers.get(key).toString());

        }
        return getResult(httpGet);
    }


    /**
     *
     *
     *14_9结果
     *
     * **/
    public static String httpPostRequest(String url) {
        HttpPost httpPost = new HttpPost(url);
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(TIME_OUT).setSocketTimeout(TIME_OUT)
                .build();
        httpPost.setConfig(config);
        return getResult(httpPost);
    }


    /**
     *
     *
     *
     *
     * **/
    public static String httpPostRequest(String url, Map<String, Object> params) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);

        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(TIME_OUT).setSocketTimeout(TIME_OUT)
                .build();
        httpPost.setConfig(config);
        return getResult(httpPost);
    }

    /**
     *
     *
     *
     *
     * **/
    public static String httpPostRequest(String url, Map<String, Object> headers, Map<String, Object> params)
            throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);

        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));

        return getResult(httpPost);
    }

    private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, Object> params) {
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
        }

        return pairs;
    }

    /**
     * 处理默认编码Http请求
     *
     * @param request
     * @return
     */
    private static String getResult(HttpRequestBase request) {
        String EMPTY_STR = "";
        // CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = getHttpClient();
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            // response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // long len = entity.getContentLength();// -1 表示长度未知
                String result = EntityUtils.toString(entity);
                response.close();
                // httpClient.close();
                return result;
            }
        } catch (Throwable e) {
            logger.error(getTrace(e));
            CheckUtil.sleep(6000);
            EMPTY_STR=getResult(request);
        }  finally {

        }

        return EMPTY_STR;
    }











    //对okooo编码 单独设置  可整体设置为动态编码精简代码 。


    public static String httpGetRequestokooo(String url) {
        HttpGet httpGet = new HttpGet(url);
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(TIME_OUT).setSocketTimeout(TIME_OUT)
                .build();
        httpGet.setConfig(config);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
        return getResultokooo(httpGet);
    }



    private static String getResultokooo(HttpRequestBase request) {
        String EMPTY_STR = "";
        // CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = getHttpClient();
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            // response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // long len = entity.getContentLength();// -1 表示长度未知
                String result = EntityUtils.toString(entity,"gb2312");
                response.close();
                // httpClient.close();
                return result;
            }
        } catch (Throwable e) {
            logger.error(getTrace(e));
            CheckUtil.sleep(6000);
            EMPTY_STR=getResultokooo(request);
        }  finally {

        }

        return EMPTY_STR;
    }



    public static void main(String[]a){
        System.out.println(httpGetRequest("https://www.baidu.com"));
    }
}












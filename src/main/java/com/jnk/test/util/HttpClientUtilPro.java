package com.jnk.test.util;

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
            cm.setMaxTotal(100);// 整个连接池最大连接数
            cm.setDefaultMaxPerRoute(10);// 每路由最大连接数，默认值是2
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




    /**
     *
     *
     *all
     *
     * **/
    public static String httpGetRequest(String url,int requestCount) {
        HttpGet httpGet = new HttpGet(url);
        RequestConfig config = RequestConfig.custom()
               .setConnectTimeout(TIME_OUT).setSocketTimeout(TIME_OUT)
               .build();
        httpGet.setConfig(config);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
        return getResult(httpGet,requestCount);
    }
    /**
     *
     *
     *all
     *
     * **/
    public static String httpGetRequestCoin(String url,int requestCount) {
        HttpGet httpGet = new HttpGet(url);
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(TIME_OUT).setSocketTimeout(TIME_OUT)
                .build();
        httpGet.setConfig(config);
        httpGet.setHeader("Host", "coinmarketcap.com");
        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate, br");
        httpGet.setHeader("Upgrade-Insecure-Requests", "1");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        httpGet.setHeader("Cache-Control", "max-age=0");

        return getResult(httpGet,requestCount);
    }





    /**
     *
     *
     *
     *
     * **/
    public static String httpGetRequest(String url, Map<String, Object> params,int requestCount) throws URISyntaxException {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);

        HttpGet httpGet = new HttpGet(ub.build());
        return getResult(httpGet,requestCount);
    }



    /**
     *
     *
     *ssq
     *
     * **/
    public static String httpGetRequestHead(String url, Map<String, Object> headers,int requestCount) {
        HttpGet httpGet = new HttpGet(url);
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(TIME_OUT).setSocketTimeout(TIME_OUT)
                .build();
        httpGet.setConfig(config);
        for(String key:headers.keySet()){
            httpGet.setHeader(key, headers.get(key).toString());

        }
        return getResult(httpGet,requestCount);
    }


    /**
     *
     *
     *14_9结果
     *
     * **/
    public static String httpPostRequest(String url,int requestCount) {
        HttpPost httpPost = new HttpPost(url);
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(TIME_OUT).setSocketTimeout(TIME_OUT)
                .build();
        httpPost.setConfig(config);
        return getResult(httpPost,requestCount);
    }


    /**
     *
     *
     *
     *
     * **/
    public static String httpPostRequest(String url, Map<String, Object> params,int requestCount){
        HttpPost httpPost = new HttpPost(url);
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
        }catch (UnsupportedEncodingException e){
            CheckUtil.getTrace(e);
        }
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(TIME_OUT).setSocketTimeout(TIME_OUT)
                .build();
        httpPost.setConfig(config);
        return getResult(httpPost,requestCount);
    }

    /**
     *
     *
     *
     *
     * **/
    public static String httpPostRequest(String url, Map<String, Object> headers, Map<String, Object> params,int requestCount)
        {
        HttpPost httpPost = new HttpPost(url);

        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));

        }catch (UnsupportedEncodingException e){
            CheckUtil.getTrace(e);
        }

        return getResult(httpPost,requestCount);
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
    private static String getResult(HttpRequestBase request,int requestCount) {
        if(requestCount==3){
            return null;
        }
        String EMPTY_STR = null;
        // CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse response=null;
        try {
            requestCount++;
            response = httpClient.execute(request);
            int statusLine=response.getStatusLine().getStatusCode();
            System.out.println(statusLine);
            if(statusLine!=200){
                    CheckUtil.sleep(2000);
                    response.close();
                    logger.info("返回不正确!重试中......");
                    EMPTY_STR=getResult(request,requestCount);
            }else {
                // response.getStatusLine().getStatusCode();
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // long len = entity.getContentLength();// -1 表示长度未知
                    EMPTY_STR = EntityUtils.toString(entity);
                    response.close();
                    //httpClient.close();
                    return EMPTY_STR;
                }
            }

        } catch (Throwable e) {
            logger.error(getTrace(e));
            CheckUtil.sleep(2000);
            response.close();
            logger.info("异常!重试中......");
            EMPTY_STR=getResult(request,requestCount);
        }  finally {
            return EMPTY_STR;

        }

    }






    public static void main(String[]a){
        for(int i=0;i<100;i++){
            System.out.println("-----------------------------------------------start"+i);

            System.out.println(httpGetRequest
            ("https://api.jinse.co",1));
            System.out.println("-----------------------------------------------end");

        }
    }
}












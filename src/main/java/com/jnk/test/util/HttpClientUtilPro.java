package com.jnk.test.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
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
     *all  币的合约== tokenview
     *
     * **/
    public static String httpGetRequest(String url,int requestCount,String ref) {
        HttpGet httpGet = new HttpGet(url);
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(TIME_OUT).setSocketTimeout(TIME_OUT)
                .build();
        httpGet.setConfig(config);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
        httpGet.setHeader("Referer", ref);

        return getResults(httpGet,requestCount);
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

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("异常!重试中......");
            CheckUtil.sleep(2000);
            if(response!=null){
                response.close();
            }
            EMPTY_STR=getResult(request,requestCount);

//            e.printStackTrace();
//            CheckUtil.sleep(2000);
//            response.close();
//            logger.info("异常!重试中......");
//            EMPTY_STR=getResult(request,requestCount);
        }  finally {
            return EMPTY_STR;

        }

    }



    /**
     *
     * 币的hash地址util
     * @param request
     * @return
     */
    private static String getResults(HttpRequestBase request,int requestCount) {
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
                return  null;
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

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("异常!重试中......");
            CheckUtil.sleep(2000);
            if(response!=null){
                response.close();
            }
            EMPTY_STR=getResult(request,requestCount);

//            e.printStackTrace();
//            CheckUtil.sleep(2000);
//            response.close();
//            logger.info("异常!重试中......");
//            EMPTY_STR=getResult(request,requestCount);
        }  finally {
            return EMPTY_STR;

        }

    }


    /**
     * 绕过验证
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[] { trustManager }, null);
        return sc;
    }

    /**
     * 模拟请求
     *
     * @param url		资源地址

     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static String send(String url,int requestCount,String ref) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException {
        String body = "";

        //采用绕过验证的方式处理https请求
        SSLContext sslcontext = createIgnoreVerifySSL();

        //设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        HttpClients.custom().setConnectionManager(connManager);


        //创建自定义的httpclient对象
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();
        //CloseableHttpClient client = HttpClients.createDefault();

        try{
            //创建get方式请求对象
            HttpGet get = new HttpGet(url);

            //指定报文头Content-type、User-Agent
            get.setHeader("Content-type", "application/x-www-form-urlencoded");
            get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
            get.setHeader("Referer", ref);

            //执行请求操作，并拿到结果（同步阻塞）
            CloseableHttpResponse response = client.execute(get);

            //获取结果实体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                //按指定编码转换结果实体为String类型
                body = EntityUtils.toString(entity, "UTF-8");
            }

            EntityUtils.consume(entity);
            //释放链接
            response.close();
            System.out.println("body:" + body);
        } finally{
            client.close();
        }
        return  body;
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












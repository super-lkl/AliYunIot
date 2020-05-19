package com.example.demo.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

/**
 * Content-Type application/json; charset=UTF-8
 * @author admin
 *
 */
public class HttpClientUtils {

    private static PoolingHttpClientConnectionManager cm;

    public static String UTF_8 = "UTF-8";
    public static String CONTENT_TYPE_TEXT_JSON = "text/json";

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
        return HttpClients.custom().setConnectionManager(cm).build();
    }


    public static JSONObject doGet(String url, Map<String, Object> headers, Map<String, Object> pMap){
        CloseableHttpClient httpclient = getHttpClient();
        HttpGet httpGet;
        if(pMap == null || pMap.size() == 0) {
            httpGet = new HttpGet(url);
        }else {
            httpGet = new HttpGet(url+"?"+createLinkString(pMap));
        }
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        CloseableHttpResponse response2 = null;
        try {
            response2 = httpclient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            HttpEntity entity2 = response2.getEntity();
            return JSON.parseObject(EntityUtils.toString(entity2));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String doGetStr(String url, Map<String, Object> headers, Map<String, Object> params){
        CloseableHttpClient httpclient = getHttpClient();
        HttpGet httpGet;
        if(params == null || params.size() == 0) {
            httpGet = new HttpGet(url);
        }else {
            httpGet = new HttpGet(url+"?"+createLinkString(params));
        }
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        CloseableHttpResponse response2 = null;
        try {
            response2 = httpclient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            HttpEntity entity2 = response2.getEntity();
            return EntityUtils.toString(entity2);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static JSONObject doPost(String url, Map<String, Object> headers, Map<String, Object> params){
        CloseableHttpClient httpclient = getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        CloseableHttpResponse response2 = null;
        try {
            ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
            response2 = httpclient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            HttpEntity entity2 = response2.getEntity();
            return JSON.parseObject(EntityUtils.toString(entity2));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static JSONObject doPostUrl(String url, Map<String, Object> urlParams, Map<String, Object> params){
        CloseableHttpClient httpclient = getHttpClient();
        HttpPost httpPost;
        if(urlParams == null || urlParams.size() == 0) {
            httpPost = new HttpPost(url);
        }else {
            httpPost = new HttpPost(url+"?"+createLinkString(urlParams));
        }
        CloseableHttpResponse response2 = null;
        try {
            ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
            response2 = httpclient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            HttpEntity entity2 = response2.getEntity();
            return JSON.parseObject(EntityUtils.toString(entity2));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String doPostStr(String url, Map<String, Object> headers, Map<String, Object> params){
        CloseableHttpClient httpclient = getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        CloseableHttpResponse response2 = null;
        try {
            ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
            response2 = httpclient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            HttpEntity entity2 = response2.getEntity();
            return EntityUtils.toString(entity2);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static JSONObject doPost(String url, Map<String, Object> headers, Object valObj){
        CloseableHttpClient httpclient = getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        CloseableHttpResponse response2 = null;
        try {
            StringEntity se = new StringEntity(JSON.toJSONString(valObj), "UTF-8");
            httpPost.setEntity(se);
            response2 = httpclient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            HttpEntity entity2 = response2.getEntity();
            return JSON.parseObject(EntityUtils.toString(entity2));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static String doPostStr(String url, Map<String, Object> headers, Object valObj){
        CloseableHttpClient httpclient = getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        CloseableHttpResponse response2 = null;
        try {
            StringEntity se = new StringEntity(JSON.toJSONString(valObj), "UTF-8");
            httpPost.setEntity(se);
            response2 = httpclient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            HttpEntity entity2 = response2.getEntity();
            return EntityUtils.toString(entity2);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param pMap 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, Object> pMap) {
        List<String> keys = new ArrayList<String>(pMap.keySet());
        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = pMap.get(key).toString();

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }

    public static Map<String, Object> getJsonMapHeaders(){
        Map<String, Object> headers = Maps.newLinkedHashMap();
        headers.put("Content-Type", "application/json; charset=UTF-8");
        return headers;
    }

    public static Map<String, Object> getFormMapHeaders(){
        Map<String, Object> headers = Maps.newLinkedHashMap();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        return headers;
    }

    private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, Object> params) {
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
        }

        return pairs;
    }

    public static String doPostJSON(String url, Map<String, Object> urlParams, String jsonString){
        CloseableHttpClient httpclient = getHttpClient();
        HttpPost httpPost;
        if(urlParams == null || urlParams.size() == 0) {
            httpPost = new HttpPost(url);
        }else {
            httpPost = new HttpPost(url+"?"+createLinkString(urlParams));
        }
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        String result = null;
        try {
            StringEntity se = new StringEntity(jsonString);
            se.setContentType(CONTENT_TYPE_TEXT_JSON);
            httpPost.setEntity(se);
            CloseableHttpResponse response2 = null;
            response2 = httpclient.execute(httpPost);
            HttpEntity entity2 = null;
            entity2 = response2.getEntity();
            result = EntityUtils.toString(entity2, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String doPostJSON(String url, String accessToken, String jsonString){
        CloseableHttpClient httpclient = getHttpClient();
        HttpPost httpPost;
        httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("X-Access-Token", accessToken);
        String result = null;
        try {
            StringEntity se = new StringEntity(jsonString);
            se.setContentType(CONTENT_TYPE_TEXT_JSON);
            httpPost.setEntity(se);
            CloseableHttpResponse response2 = null;
            response2 = httpclient.execute(httpPost);
            HttpEntity entity2 = null;
            entity2 = response2.getEntity();
            result = EntityUtils.toString(entity2, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject doPut(String url, Map<String, Object> headers, Map<String, Object> pMap) {
        CloseableHttpClient httpclient = getHttpClient();
        HttpPut httpPut;
        if(pMap == null || pMap.size() == 0) {
            httpPut = new HttpPut(url);
        }else {
            httpPut = new HttpPut(url+"?"+createLinkString(pMap));
        }
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPut.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        CloseableHttpResponse response2 = null;
        try {
            response2 = httpclient.execute(httpPut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            HttpEntity entity2 = response2.getEntity();
            return JSON.parseObject(EntityUtils.toString(entity2));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static JSONObject doDelete(String url, Map<String, Object> headers, Map<String, Object> pMap) {
        CloseableHttpClient httpclient = getHttpClient();
        HttpDelete httpDelete;
        if(pMap == null || pMap.size() == 0) {
            httpDelete = new HttpDelete(url);
        }else {
            httpDelete = new HttpDelete(url+"?"+createLinkString(pMap));
        }
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpDelete.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        CloseableHttpResponse response2 = null;
        try {
            response2 = httpclient.execute(httpDelete);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            HttpEntity entity2 = response2.getEntity();
            return JSON.parseObject(EntityUtils.toString(entity2));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

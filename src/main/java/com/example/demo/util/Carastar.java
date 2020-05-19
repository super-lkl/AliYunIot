package com.example.demo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 李康龙
 */
public class Carastar{

    /**接口请求地址*/
    private static String url = "http://api.xy-aiot.com/rest/api/v1";
    /**授权码*/
    private static String accessToken = "111111";

    /**
     * 查询该授权码关联绑定的设备列表
     */
    public static void getDeviceList() {
        Map<String,Object> map = Maps.newHashMap();
        map.put("apiCode", "GetDeviceList");
        String jsonString = HttpClientUtils.doPostJSON(url, accessToken, JSON.toJSONString(map));
        JSONObject jsonObject = JSON.parseObject(jsonString);
        System.out.println(jsonObject);
    }

    /**
     * 查询设备采集最新或指定时点的传感数据
     */
    public static JSONObject getLastSingleData() {
        Map<String,Object> map = Maps.newHashMap();
        List<Map<String, Object>> queries = Lists.newArrayList();
        Map<String,Object> querie1 = Maps.newHashMap();
        Map<String,Object> tags = Maps.newHashMap();
        tags.put("SN", "0018DE743E31");
        querie1.put("metric", "atmosphericTemperature");
        querie1.put("tags", tags);
        queries.add(querie1);
        Map<String,Object> querie2 = Maps.newHashMap();
        Map<String,Object> tagss = Maps.newHashMap();
        tagss.put("SN", "0018DE743E31");
        querie2.put("metric", "latitude");
        querie2.put("tags", tagss);
        queries.add(querie2);
        map.put("apiCode", "GetLastSingleData");
        //
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -2);
        long timestamp = calendar.getTime().getTime();
        //
        map.put("timestamp", timestamp);
        map.put("queries", queries);
        String jsonString = HttpClientUtils.doPostJSON(url, accessToken, JSON.toJSONString(map));
        JSONObject jsonObject = JSON.parseObject(jsonString);
        System.out.println(jsonObject);
        Object object = jsonObject.get("data");
        System.out.println(object);
        if(object instanceof JSONArray) {
            Object object2 = ((JSONArray)object).get(0);
            if(object2 instanceof JSONObject) {
                Object object3 = ((JSONObject)object2).get("value");
                System.out.println(object3);
            }
        }
        return jsonObject;
    }

    /**
     * 	查询设备传感器指标历史范围数据
     * @return
     */
    public static JSONObject GetHisSingleData() {
        Map<String,Object> map = Maps.newHashMap();
        List<Map<String, Object>> queries = Lists.newArrayList();
        Map<String,Object> querie1 = Maps.newHashMap();
        Map<String,Object> tags = Maps.newHashMap();
        tags.put("SN", "0018DE743E31");
        querie1.put("metric", "atmosphericTemperature");
        querie1.put("tags", tags);
        queries.add(querie1);
        Map<String,Object> querie2 = Maps.newHashMap();
        Map<String,Object> tagss = Maps.newHashMap();
        tagss.put("SN", "0018DE743E31");
        querie2.put("metric", "latitude");
        querie2.put("tags", tagss);
        queries.add(querie2);
        map.put("apiCode", "GetHisSingleData");
        //
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -3);
        Date start = calendar.getTime();
        //
        map.put("start", start.getTime());
        map.put("queries", queries);
        String jsonString = HttpClientUtils.doPostJSON(url, accessToken, JSON.toJSONString(map));
        JSONObject jsonObject = JSON.parseObject(jsonString);
        System.out.println(jsonObject);
        Object object = jsonObject.get("data");
        System.out.println(object);
        if(object instanceof JSONArray) {
            Object object2 = ((JSONArray)object).get(0);
            if(object2 instanceof JSONObject) {
                Object object3 = ((JSONObject)object2).get("value");
                System.out.println(object3);
            }
        }
        return jsonObject;
    }

    /**
     * 查询已绑定的设备运行日志信息
     */
    public static void getDeviceLog() {
        Map<String,Object> map = Maps.newHashMap();
        List<Map<String, Object>> queries = Lists.newArrayList();
        Map<String,Object> querie = Maps.newHashMap();
        Map<String,Object> tags = Maps.newHashMap();
        tags.put("SN", "0018DE743E32");
        querie.put("metric", "code");
        querie.put("tags", tags);
        queries.add(querie);
        map.put("apiCode", "GetDeviceLog");
//		map.put("start", DateUtils.string2Date("2020-01-10 11:00:01", DateUtils.dateFormat1).getTime());
        map.put("end", System.currentTimeMillis());
        map.put("queries", queries);
        String jsonString = HttpClientUtils.doPostJSON(url, accessToken, JSON.toJSONString(map));
        JSONObject jsonObject = JSON.parseObject(jsonString);
        System.out.println(jsonObject);
    }

    /**
     * 对已绑定的设备功能远程下达开关指令进行控制，以及开关状态查询
     */
    public static void deviceCommand() {
        Map<String,Object> map = Maps.newHashMap();
        map.put("apiCode", "DeviceCommand");
        map.put("SN", "0018DE743E31");
        map.put("flag", 13);
        map.put("action", 2);
        String jsonString = HttpClientUtils.doPostJSON(url, accessToken, JSON.toJSONString(map));
        JSONObject jsonObject = JSON.parseObject(jsonString);
        System.out.println(jsonObject);
    }

    /**
     * 查询已绑定带视频直播功能设备的视频直播播放地址和直播封面图片地址
     */
    public static void getLiveInfo() {
        Map<String,Object> map = Maps.newHashMap();
        map.put("apiCode", "GetLiveInfo");
        map.put("SN", "0018DE743E31");
        String jsonString = HttpClientUtils.doPostJSON(url, accessToken, JSON.toJSONString(map));
        JSONObject jsonObject = JSON.parseObject(jsonString);
        System.out.println(jsonObject);
    }

    /**
     * 用户通过此接口查询该授权码 AccessToken 对应设置的设备数据推送接口 URL 地址。
     */
    public static void getNotifyUrl() {
        Map<String,Object> map = Maps.newHashMap();
        map.put("apiCode", "GetNotifyUrl");
        String jsonString = HttpClientUtils.doPostJSON(url, accessToken, JSON.toJSONString(map));
        JSONObject jsonObject = JSON.parseObject(jsonString);
        System.out.println(jsonObject);
    }

    public static void main(String[] args) {
//		getDeviceList();
		getLastSingleData();
//		getDeviceLog();
//		deviceCommand();
//		getLiveInfo();
//		getNotifyUrl();
//      GetHisSingleData();
        /*Date date = new Date(1586302482000L);
        System.out.println(date);*/
    }
}


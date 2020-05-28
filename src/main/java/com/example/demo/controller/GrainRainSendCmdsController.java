package com.example.demo.controller;

import cmcc.iot.onenet.javasdk.api.datastreams.FindDatastreamListApi;
import cmcc.iot.onenet.javasdk.api.datastreams.GetDatastreamApi;
import cmcc.iot.onenet.javasdk.response.BasicResponse;
import cmcc.iot.onenet.javasdk.response.datastreams.DatastreamsResponse;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.bridge.BridgeBasicManager;
import com.example.demo.onenet.cmd.SendCmd;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 李康龙
 */
@Controller
@Slf4j
public class GrainRainSendCmdsController {

    @Autowired
    private SendCmd sendCmd;

    @Autowired
    private BridgeBasicManager bridgeBasicManager;

    /**
     * 谷雨下发命令
     * @param params
     * @return
     */
    @RequestMapping(value = "/grainRainSendCmds",method = RequestMethod.POST)
    @ResponseBody
    public boolean grainRainSendCmds(@RequestBody String params){
        JSONObject jsonObject = JSONObject.parseObject(params);
        String devId = jsonObject.getString("devId");
        String key = jsonObject.getString("key");
        boolean flag = jsonObject.getBoolean("flag");
        String succ = "succ";
        String errorMessage = sendCmd.grainRainSendCmd(devId, key, flag);
        if(!succ.equals(errorMessage)){
            return false;
        }
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                GetDatastreamApi getDatastreamApi = new GetDatastreamApi(devId, "switch", key);
                BasicResponse<DatastreamsResponse> basicResponse = getDatastreamApi.executeApi();
                Object currentValue = basicResponse.getData().getCurrentValue();
                String content = "{\"id\":\"1\",\"version\":\"1.0\",\"params\":{\"switch1\":"+currentValue+"}}";
                bridgeBasicManager.doOnline(devId);
                bridgeBasicManager.doPublish(devId,content);
            }
        },2, TimeUnit.SECONDS);
        return true;
    }

    /**
     * 春分下发命令
     * @param params
     * @return
     */
    @RequestMapping(value = "/vernalEquinoxSendCmds",method = RequestMethod.POST)
    @ResponseBody
    public boolean vernalEquinoxSendCmds(@RequestBody String params){
        JSONObject jsonObject = JSONObject.parseObject(params);
        String devId = jsonObject.getString("devId");
        String key = jsonObject.getString("key");
        int switching = Integer.parseInt(jsonObject.getString("switch"), 2);
        log.info("switch : "+jsonObject.getString("switch"));
        log.info("switching : "+switching);
        String succ = "succ";
        String errorMessage = sendCmd.vernalEquinoxSendCmd(devId, key, switching);
        if(!succ.equals(errorMessage)){
            return false;
        }
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                FindDatastreamListApi api = new FindDatastreamListApi("switch1,switch2,switch3,switch4", devId, key);
                BasicResponse<List<DatastreamsResponse>> basicResponse = api.executeApi();
                Object switch1 = basicResponse.getData().get(0).getCurrentValue();
                Object switch2 = basicResponse.getData().get(1).getCurrentValue();
                Object switch3 = basicResponse.getData().get(2).getCurrentValue();
                Object switch4 = basicResponse.getData().get(3).getCurrentValue();
                String content = "{\"id\":\"1\",\"version\":\"1.0\",\"params\":{\"switch1\":"+switch1+",\"switch2\":"+switch2+",\"switch3\":"+switch3+",\"switch4\":"+switch4+"}}";
                log.info("content ："+content);
                bridgeBasicManager.doOnline(devId);
                bridgeBasicManager.doPublish(devId,content);
            }
        },2, TimeUnit.SECONDS);
        return true;
    }

    /**
     * 惊蛰下发命令
     * @param params
     * @return
     */
    @RequestMapping(value = "/insectsAwakenSendCmds",method = RequestMethod.POST)
    @ResponseBody
    public boolean insectsAwakenSendCmds(@RequestBody String params){
        JSONObject jsonObject = JSONObject.parseObject(params);
        String devId = jsonObject.getString("devId");
        String key = jsonObject.getString("key");
        int switchNum = Integer.parseInt(jsonObject.getString("switchNum"));
        int switching = Integer.parseInt(jsonObject.getString("switching"));
        log.info("switching : "+switching);
        String succ = "succ";
        String errorMessage = sendCmd.insectsAwakenSendCmd(devId, key,switchNum, switching);
        if(!succ.equals(errorMessage)){
            return false;
        }
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                FindDatastreamListApi findDatastreamListApiapi = new FindDatastreamListApi("switch1,switch2,switch3,switch4,switch5,switch6,switch7,switch8", devId, key);
                BasicResponse<List<DatastreamsResponse>> basicResponse = findDatastreamListApiapi.executeApi();
                Object switch1 = basicResponse.getData().get(0).getCurrentValue();
                Object switch2 = basicResponse.getData().get(1).getCurrentValue();
                Object switch3 = basicResponse.getData().get(2).getCurrentValue();
                Object switch4 = basicResponse.getData().get(3).getCurrentValue();
                Object switch5 = basicResponse.getData().get(4).getCurrentValue();
                Object switch6 = basicResponse.getData().get(5).getCurrentValue();
                Object switch7 = basicResponse.getData().get(6).getCurrentValue();
                Object switch8 = basicResponse.getData().get(7).getCurrentValue();
                String content = "{\"id\":\"1\",\"version\":\"1.0\",\"params\":{\"switch1\":"+switch1+",\"switch2\":"+switch2+",\"switch3\":"+switch3+",\"switch4\":"+switch4+",\"switch5\":"+switch5+",\"switch6\":"+switch6+",\"switch7\":"+switch7+",\"switch8\":"+switch8+"}}";
                log.info("content ："+content);
                log.info("----------");
                bridgeBasicManager.doOnline(devId);
                bridgeBasicManager.doPublish(devId,content);
            }
        },2, TimeUnit.SECONDS);
        return true;
    }

    @RequestMapping(value = "/grainRainTimer",method = RequestMethod.POST)
    @ResponseBody
    public boolean grainRainTimer(@RequestBody String params){
        JSONObject jsonObject = JSONObject.parseObject(params);
        String devId = jsonObject.getString("devId");
        String key = jsonObject.getString("key");
        String succ = "succ";
        String errorMessage = sendCmd.grainRainTimingSendCmd(devId, key, jsonObject);
        if(!succ.equals(errorMessage)){
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/timerMessage",method = RequestMethod.GET)
    @ResponseBody
    public Object timerMessage(@RequestBody String params){

        return null;
    }

}

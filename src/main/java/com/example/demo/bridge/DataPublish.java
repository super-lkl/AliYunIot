package com.example.demo.bridge;

import com.alibaba.fastjson.JSON;
import com.aliyun.iot.as.bridge.core.config.ConfigFactory;
import com.aliyun.iot.as.bridge.core.model.DeviceIdentity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author 李康龙
 */
@Slf4j
@EnableScheduling
@Component
public class DataPublish {

    @Autowired
    private DeviceListManager deviceListManager;

    @Autowired
    private BridgeBasicManager bridgeBasicManager;

    @Scheduled(fixedDelay = 60000L)
    public synchronized void updateDeviceProperty(){
//        log.info("============= start update =============");
        Map<String, Object> devices = deviceListManager.getDevices();
        for (Map.Entry<String,Object> entry : devices.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
//            log.info("++++++++++ value +++++++++++"+value.toString());
            String jsonString = JSON.toJSONString(value);
            DeviceIdentity deviceIdentity = ConfigFactory.getDeviceConfigManager().getDeviceIdentity(key);
            if(deviceIdentity==null){
//                log.info("can`t find this deviceId( "+key+" ,please check your configuration )");
                return;
            }
            String content = "{\"id\":\"1\",\"version\":\"1.0\",\"params\":"+jsonString+"}";
//            log.info(content);
            bridgeBasicManager.doOnline(key);
            bridgeBasicManager.doPublish(key,content);
        }
//        log.info("============= end update =============");
    }

}


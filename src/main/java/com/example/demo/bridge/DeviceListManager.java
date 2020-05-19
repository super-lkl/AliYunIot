package com.example.demo.bridge;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.pojo.VernalEquinox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 李康龙
 */
@Component
@Slf4j
public class DeviceListManager {

    private Map<String,Object> devices = new HashMap<>();

    public synchronized Map<String,Object> getDevices(){
        return devices;
    }

    public synchronized void dataResolve(Object object) throws IllegalAccessException {
//        log.info("============= start Resolve =============");
        JSONObject jsonObject = JSON.parseObject(String.valueOf(object));
        Object devId = jsonObject.get("dev_id");
        Object dsId = jsonObject.get("ds_id");
        Object value = jsonObject.get("value");
        if("switch".equals(dsId)){
            dsId = "switch1";
        }
//        log.info("devId: "+devId+"   dsId: "+dsId+"  value: "+value);
        Object obj = devices.get(devId);
        if(obj == null){
            obj = new VernalEquinox();
            devices.put(String.valueOf(devId),obj);
        }
        Class<VernalEquinox> vernalEquinoxClass = VernalEquinox.class;
        Field[] fields = vernalEquinoxClass.getDeclaredFields();
        for (Field field : fields) {
            if(dsId.equals(field.getName())){
                field.setAccessible(true);
                field.set(obj,value);
                break;
            }
        }
//        log.info("============= end Resolve =============");
    }
}






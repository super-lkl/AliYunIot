package com.example.demo.bridge;

import com.aliyun.iot.as.bridge.core.BridgeBootstrap;
import com.aliyun.iot.as.bridge.core.config.ConfigFactory;
import com.aliyun.iot.as.bridge.core.handler.DownlinkChannelHandler;
import com.aliyun.iot.as.bridge.core.handler.UplinkChannelHandler;
import com.aliyun.iot.as.bridge.core.model.DeviceIdentity;
import com.aliyun.iot.as.bridge.core.model.ProtocolMessage;
import com.aliyun.iot.as.bridge.core.model.Session;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 李康龙
 */
@Component
public class BridgeBasicManager {

    private static Logger log = LoggerFactory.getLogger(BridgeBasicManager.class);

    private static BridgeBootstrap bridgeBootstrap;

    private UplinkChannelHandler upLinkHandler;

    private final static String TOPIC_TING_PROPERTY_POST = "/sys/%s/%s/thing/event/property/post";

    private static ExecutorService executorService  = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2,
            60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            new ThreadFactoryBuilder().setDaemon(true).setNameFormat("bridge-downlink-handle-%d").build(),
            new ThreadPoolExecutor.AbortPolicy());

    public static void init(){
        bridgeBootstrap = new BridgeBootstrap();
        bridgeBootstrap.bootstrap(new DownlinkChannelHandler() {
            @Override
            public boolean pushToDevice(Session session, String topic, byte[] payload) {
                //get downlink message from cloud
                executorService.submit(() -> handleDownLinkMessage(session, topic, payload));
                return true;
            }

            @Override
            public boolean broadcast(String topic, byte[] payload) {
                return false;
            }
        });
        log.info("======== Bridge bootstrap success =========");
    }

    public boolean doOnline(String originalIdentity){
        upLinkHandler = new UplinkChannelHandler();
        //创建Session
        Object channel = new Object();
        Session session = Session.newInstance(originalIdentity, channel);
        //设备上线
        boolean success = upLinkHandler.doOnline(session, originalIdentity);
        return success;
    }

    public void doPublish(String originalIdentity,String content){
        DeviceIdentity deviceIdentity = ConfigFactory.getDeviceConfigManager().getDeviceIdentity(originalIdentity);
        ProtocolMessage protocolMessage = new ProtocolMessage();
        protocolMessage.setPayload(content.getBytes());
        protocolMessage.setQos(0);
        protocolMessage.setTopic(String.format(TOPIC_TING_PROPERTY_POST, deviceIdentity.getProductKey(), deviceIdentity.getDeviceName()));
        upLinkHandler.doPublishAsync(originalIdentity, protocolMessage);
    }

    /**
     * 向云端上报某个设备下线。
     * @param originalIdentity 设备原始标识符。
     * @return 是否成功上报。
     */
    public boolean doOffline(String originalIdentity){
        return upLinkHandler.doOffline(originalIdentity);
    }

    private static void handleDownLinkMessage(Session session, String topic, byte[] payload) {
        String content = new String(payload);
        log.info("Get DownLink message, session:{}, topic:{}, content:{}", session, topic, content);
        Object channel = session.getChannel();
        String originalIdentity = session.getOriginalIdentity();
        //for example, you can send the message to device via channel, it depends on you specific server implementation
    }

    public static void main(String[] args) {
        //private static String originalIdentity = "519705904";
        //private static String originalIdentity = "519705905";
        //private static String originalIdentity = "514901472";
//        new BridgeBasicManager().doOnline("519705904");
//        doOnline("519705905");
//        UplinkChannelHandler uplinkChannelHandler = doOnline("514901472");
    }
}

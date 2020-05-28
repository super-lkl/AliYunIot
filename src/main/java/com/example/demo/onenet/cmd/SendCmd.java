package com.example.demo.onenet.cmd;

import cmcc.iot.onenet.javasdk.api.cmds.SendCmdsApi;
import cmcc.iot.onenet.javasdk.response.BasicResponse;
import cmcc.iot.onenet.javasdk.response.cmds.NewCmdsResponse;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.util.XorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author 李康龙
 */
@Component
@Slf4j
public class SendCmd{

    /**
     * 谷雨下发命令
     * @param devId
     * @param key
     * @param flag
     * @return
     */
    public String grainRainSendCmd(String devId,String key,boolean flag){
        if(StringUtils.isEmpty(devId)||StringUtils.isEmpty(key)){
            return "error";
        }
        int id = Integer.parseInt(devId);
        int length = 0;
        byte[] buffer = new byte[21];
        //帧头
        buffer[length++] = (byte) (0x95);
        //协议版本
        buffer[length++] = (byte) (0x01);
        //设备编号
        buffer[length++] = (byte) (id>>24);
        buffer[length++] = (byte) (id>>16);
        buffer[length++] = (byte) (id>>8);
        buffer[length++] = (byte) (id>>0);
        //设备类型
        buffer[length++]  = (byte) (0x00);
        buffer[length++]  = (byte) (0x02);
        //物理通道
        buffer[length++]  = (byte) (0x83);
        //帧序列号
        buffer[length++]  = (byte) (0x00);
        buffer[length++] = (byte) (0x00);
        //连接周期
        buffer[length++] = (byte) (0x00);
        buffer[length++] = (byte) (0x3C);
        //功能码
        buffer[length++] = (byte) (0x40);
        buffer[length++] = (byte) (0x03);
        //长度
        buffer[length++] = (byte) (0x00);
        buffer[length++] = (byte) (0x03);
        //静荷：类型type
        buffer[length++] = (byte) (0x02);
        //静荷：长度Len
        buffer[length++] = (byte) (0x01);
        if(flag){
            //静荷 0：关闭，1：开启
            buffer[length++] = (byte) (0x01);
            //静荷 校验码：用于整个数据帧的校验，占用1个字节，计算从协议版本（包含本身）到静荷所有数据的异或值。
            buffer[length++] = (byte) (XorUtil.getXor(buffer));
        }else {
            //静荷 0：关闭，1：开启
            buffer[length++] = (byte) (0x00);
            //静荷 校验码：用于整个数据帧的校验，占用1个字节，计算从协议版本（包含本身）到静荷所有数据的异或值。
            buffer[length++] = (byte) (XorUtil.getXor(buffer));
        }
        return sendCmdCommon(devId,key,buffer);
    }

    /**
     * 春分下发命令
     * @param devId
     * @param key
     * @return
     */
    public String vernalEquinoxSendCmd(String devId,String key,int switching){
        if(StringUtils.isEmpty(devId)||StringUtils.isEmpty(key)){
            return "error";
        }
        int id = Integer.parseInt(devId);
        int length = 0;
        byte[] buffer = new byte[22];
        //帧头
        buffer[length++] = (byte) (0x95);
        //协议版本
        buffer[length++] = (byte) (0x01);
        //设备编号
        buffer[length++] = (byte) (id>>24);
        buffer[length++] = (byte) (id>>16);
        buffer[length++] = (byte) (id>>8);
        buffer[length++] = (byte) (id>>0);
        //设备类型
        buffer[length++]  = (byte) (0x00);
        buffer[length++]  = (byte) (0x02);
        //物理通道
        buffer[length++]  = (byte) (0x83);
        //帧序列号
        buffer[length++]  = (byte) (0x00);
        buffer[length++] = (byte) (0x03);
        //连接周期
        buffer[length++] = (byte) (0x00);
        buffer[length++] = (byte) (0x3C);
        //功能码
        buffer[length++] = (byte) (0x40);
        buffer[length++] = (byte) (0x03);
        //长度
        buffer[length++] = (byte) (0x00);
        buffer[length++] = (byte) (0x04);
        //静荷:Type类型
        buffer[length++] = (byte) (0x0D);
        //静荷:Len长度
        buffer[length++] = (byte) (0x02);
        //静荷：屏蔽码
        buffer[length++] = (byte) (0xFF);
        //静荷：开关值 1:打开 0:关闭
        buffer[length++] = (byte) (switching);
        //校验码
        buffer[length++] = (byte) (XorUtil.getXor(buffer));
        return sendCmdCommon(devId,key,buffer);
    }

    /**
     * 下发命令公共方法
     * @param devId
     * @param key
     * @param buffer
     * @return
     */
    public String sendCmdCommon(String devId,String key,byte[] buffer){
        /**
         * 发送命令
         * @param devId：接收该数据的设备ID（必选），String
         * @param qos:是否需要响应，默认为0,Integer
         * 0：不需要响应，即最多发送一次，不关心设备是否响应；
         * 1：需要响应，如果设备收到命令后没有响应，则会在下一次设备登陆时若命令在有效期内(有效期定义参见timeout参数）则会继续发送。
         * 对响应时间无限制，多次响应以最后一次为准。
         * 本参数仅当type=0时有效；
         * @param timeOut:命令有效时间，默认0,Integer
         * 0：在线命令，若设备在线,下发给设备，若设备离线，直接丢弃；
         *  >0： 离线命令，若设备在线，下发给设备，若设备离线，在当前时间加timeout时间内为有效期，有效期内，若设备上线，则下发给设备。单位：秒，有效围：0~2678400。
         *  本参数仅当type=0时有效；
         * @param type://默认0。0：发送CMD_REQ包，1：发送PUSH_DATA包
         * @param contents:用户自定义数据：json、string、二进制数据（小于64K）
         * @param key:masterkey或者设备apikey
         */
        SendCmdsApi api = new SendCmdsApi(devId, null, null, null, buffer, key);
        BasicResponse<NewCmdsResponse> response = api.executeApi();
        return response.getError();
    }

    /**
     * 谷雨定时
     * @param devId
     * @param key
     * @return
     */
    public String grainRainTimingSendCmd(String devId, String key, JSONObject jsonObject){
        if(StringUtils.isEmpty(devId)||StringUtils.isEmpty(key)){
            return "error";
        }
        int id = Integer.parseInt(devId);
        int length = 0;
        //20+10*5
        byte[] buffer = new byte[70];
        //帧头
        buffer[length++] = (byte) (0x95);
        //协议版本
        buffer[length++] = (byte) (0x01);
        //设备编号
        buffer[length++] = (byte) (id>>24);
        buffer[length++] = (byte) (id>>16);
        buffer[length++] = (byte) (id>>8);
        buffer[length++] = (byte) (id>>0);
        //设备类型
        buffer[length++]  = (byte) (0x00);
        buffer[length++]  = (byte) (0x02);
        //物理通道
        buffer[length++]  = (byte) (0x83);
        //帧序列号
        buffer[length++]  = (byte) (0x00);
        buffer[length++] = (byte) (0x16);
        //连接周期
        buffer[length++] = (byte) (0x00);
        buffer[length++] = (byte) (0x3C);
        //功能码
        buffer[length++] = (byte) (0x40);
        buffer[length++] = (byte) (0x04);
        //长度 52 0x00 0x34
        buffer[length++] = (byte) (0x00);
        buffer[length++] = (byte) (0x34);
        //静荷:Type类型
        buffer[length++] = (byte) (0x20);
        //静荷:Len长度 50 0x32
        buffer[length++] = (byte) (0x32);
        for(int i = 0;i < 10;i++){
            //静荷：小时
            buffer[length++] = (byte) (jsonObject.getInteger("h"+(i + 1)).intValue());
            //静荷：分钟
            buffer[length++] = (byte) (jsonObject.getInteger("m"+(i + 1)).intValue());
            //静荷：打开时间
            int timer = jsonObject.getInteger("d" + (i + 1)).intValue();
            buffer[length++] = (byte) (timer>>8);
            buffer[length++] = (byte) (timer>>0);
            //静荷：星期
            buffer[length++] = (byte) (Integer.parseInt(jsonObject.getString("week"),2));
        }
        //校验码
        buffer[length++] = (byte) (XorUtil.getXor(buffer));
        return sendCmdCommon(devId,key,buffer);
    }

    /**
     * 春分定时
     * @param devId
     * @param key
     * @param jsonObject
     * @return
     */
    public String vernalEquinoxTimingSendCmd(String devId, String key, JSONObject jsonObject){
        if(StringUtils.isEmpty(devId)||StringUtils.isEmpty(key)){
            return "error";
        }
        int id = Integer.parseInt(devId);
        int length = 0;
        //20+10*6
        byte[] buffer = new byte[80];
        //帧头
        buffer[length++] = (byte) (0x95);
        //协议版本
        buffer[length++] = (byte) (0x01);
        //设备编号
        buffer[length++] = (byte) (id>>24);
        buffer[length++] = (byte) (id>>16);
        buffer[length++] = (byte) (id>>8);
        buffer[length++] = (byte) (id>>0);
        //设备类型
        buffer[length++]  = (byte) (0x00);
        buffer[length++]  = (byte) (0x02);
        //物理通道
        buffer[length++]  = (byte) (0x83);
        //帧序列号
        buffer[length++]  = (byte) (0x00);
        buffer[length++] = (byte) (0x08);
        //连接周期
        buffer[length++] = (byte) (0x00);
        buffer[length++] = (byte) (0x3C);
        //功能码
        buffer[length++] = (byte) (0x40);
        buffer[length++] = (byte) (0x04);
        //长度 62 0x00 0x3E N*6+2
        buffer[length++] = (byte) (0x00);
        buffer[length++] = (byte) (0x3E);
        //静荷:Type类型
        buffer[length++] = (byte) (0x22);
        //静荷:Len长度 60 0x3C N*6
        buffer[length++] = (byte) (0x3C);
        for(int i = 0;i < 10;i++){
            //静荷：小时
            buffer[length++] = (byte) (jsonObject.getInteger("h"+(i + 1)).intValue());
            //静荷：分钟
            buffer[length++] = (byte) (jsonObject.getInteger("m"+(i + 1)).intValue());
            //静荷：打开时间
            int timer = jsonObject.getInteger("d" + (i + 1));
            buffer[length++] = (byte) (timer >> 8);
            buffer[length++] = (byte) (timer);
            //静荷：星期
            buffer[length++] = (byte) (Integer.parseInt(jsonObject.getString("week"),2));
            //开关
            buffer[length++] = (byte) (1);
        }
        //校验码
        buffer[length++] = (byte) (XorUtil.getXor(buffer));
        return sendCmdCommon(devId,key,buffer);
    }

    /**
     * 惊蛰发送命令
     * @param devId
     * @param key
     * @return
     */
    public String insectsAwakenSendCmd(String devId,String key,int switchNum,int switching){
        if(StringUtils.isEmpty(devId)||StringUtils.isEmpty(key)){
            return "error";
        }
        int id = Integer.parseInt(devId);
        int length = 0;
        byte[] buffer = new byte[22];
        //帧头
        buffer[length++] = (byte) (0x95);
        //协议版本
        buffer[length++] = (byte) (0x01);
        //设备编号
        buffer[length++] = (byte) (id>>24);
        buffer[length++] = (byte) (id>>16);
        buffer[length++] = (byte) (id>>8);
        buffer[length++] = (byte) (id>>0);
        //设备类型
        buffer[length++]  = (byte) (0x00);
        buffer[length++]  = (byte) (0x02);
        //物理通道
        buffer[length++]  = (byte) (0x83);
        //帧序列号
        buffer[length++]  = (byte) (0x00);
        buffer[length++] = (byte) (0x02);
        //连接周期
        buffer[length++] = (byte) (0x00);
        buffer[length++] = (byte) (0x3C);
        //功能码
        buffer[length++] = (byte) (0x40);
        buffer[length++] = (byte) (0x03);
        //长度
        buffer[length++] = (byte) (0x00);
        buffer[length++] = (byte) (0x04);
        //静荷:Type类型
        buffer[length++] = (byte) (0x0D);
        //静荷:Len长度
        buffer[length++] = (byte) (0x02);
        //静荷：屏蔽码
        buffer[length++] = (byte) (switchNum);
        //静荷：开关值 1:打开 0:关闭
        buffer[length++] = (byte) (switching);
        //校验码
        buffer[length++] = (byte) (XorUtil.getXor(buffer));
        return sendCmdCommon(devId,key,buffer);
    }

    /**
     * 立春发送命令
     * @param devId
     * @param key
     * @param switchValue
     * @return
     */
    public String springBeginsSendCmd(String devId,String key,int switchValue){
        if(StringUtils.isEmpty(devId)||StringUtils.isEmpty(key)){
            return "error";
        }
        int id = Integer.parseInt(devId);
        int length = 0;
        byte[] buffer = new byte[21];
        //帧头
        buffer[length++] = (byte) (0x95);
        //协议版本
        buffer[length++] = (byte) (0x01);
        //设备编号
        buffer[length++] = (byte) (id>>24);
        buffer[length++] = (byte) (id>>16);
        buffer[length++] = (byte) (id>>8);
        buffer[length++] = (byte) (id);
        //设备类型
        buffer[length++]  = (byte) (0x00);
        buffer[length++]  = (byte) (0x03);
        //物理通道
        buffer[length++]  = (byte) (0x83);
        //帧序列号
        buffer[length++]  = (byte) (0x00);
        buffer[length++] = (byte) (0x03);
        //连接周期
        buffer[length++] = (byte) (0x00);
        buffer[length++] = (byte) (0x3C);
        //功能码
        buffer[length++] = (byte) (0x40);
        buffer[length++] = (byte) (0x03);
        //长度
        buffer[length++] = (byte) (0x00);
        buffer[length++] = (byte) (0x03);
        //静荷:Type类型
        buffer[length++] = (byte) (0x17);
        //静荷:Len长度
        buffer[length++] = (byte) (0x01);
        //静荷：值 Value    0:停止 1：正转 2：反转 【正转和反转必须先停止在进行切换】
        buffer[length++] = (byte) (switchValue);
        //校验码
        buffer[length++] = (byte) (XorUtil.getXor(buffer));
        return sendCmdCommon(devId,key,buffer);
    }

    /**
     * 立春定时
     * @param devId
     * @param key
     * @return
     */
    public String springBeginsTimingSendCmd(String devId,String key){
        if(StringUtils.isEmpty(devId)||StringUtils.isEmpty(key)){
            return "error";
        }
        int id = Integer.parseInt(devId);
        int length = 0;
        byte[] buffer = new byte[25];
        //帧头
        buffer[length++] = (byte) (0x95);
        //协议版本
        buffer[length++] = (byte) (0x01);
        //设备编号
        buffer[length++] = (byte) (id>>24);
        buffer[length++] = (byte) (id>>16);
        buffer[length++] = (byte) (id>>8);
        buffer[length++] = (byte) (id);
        //设备类型
        buffer[length++]  = (byte) (0x00);
        buffer[length++]  = (byte) (0x03);
        //物理通道
        buffer[length++]  = (byte) (0x83);
        //帧序列号
        buffer[length++]  = (byte) (0x00);
        buffer[length++] = (byte) (0x0B);
        //连接周期
        buffer[length++] = (byte) (0x00);
        buffer[length++] = (byte) (0x3C);
        //功能码
        buffer[length++] = (byte) (0x40);
        buffer[length++] = (byte) (0x04);
        //长度 N*6+2 / N*5+2
        buffer[length++] = (byte) (0x00);
        buffer[length++] = (byte) (0x07);
        //静荷:Type类型
        buffer[length++] = (byte) (0x1B);
        //静荷:Len长度 N*5
        buffer[length++] = (byte) (0x05);
        //静荷：小时
        buffer[length++] = (byte) (14);
        //静荷：分钟
        buffer[length++] = (byte) (12);
        //静荷：打开时间+卷帘状态
        buffer[length++] = (byte) (0x80);
        buffer[length++] = (byte) (0x01);
        //静荷：循环周期
        buffer[length++] = (byte) (0x80);
        //校验码
        buffer[length++] = (byte) (XorUtil.getXor(buffer));
        return sendCmdCommon(devId,key,buffer);
    }

    public static void main(String[] args) {
        //惊蛰 514901637
        SendCmd sendCmd = new SendCmd();
//        String jsonStr = "{\"h1\":11,\"h2\":0,\"h3\":0,\"h4\":0,\"h5\":0,\"h6\":0,\"h7\":0,\"h8\":0,\"h9\":0,\"h10\":0," +
//                "\"m1\":16,\"m2\":0,\"m3\":0,\"m4\":0,\"m5\":0,\"m6\":0,\"m7\":0,\"m8\":0,\"m9\":0,\"m10\":0," +
//                "\"d1\":2,\"d2\":0,\"d3\":0,\"d4\":0,\"d5\":0,\"d6\":0,\"d7\":0,\"d8\":0,\"d9\":0,\"d10\":0," +
//                "\"week\":\"10000000\"}";
//        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
//        log.info("json:"+jsonObject);
//        String s = sendCmd.vernalEquinoxTimingSendCmd("514901472", "2LCG3DHHS69eH7RK1Q8R=70k7oI=", jsonObject);
//        System.out.println(s);

//        String s = sendCmd.testVETiming("514901472", "2LCG3DHHS69eH7RK1Q8R=70k7oI=");
//        System.out.println(s);
        //立春
//        sendCmd.springBeginsSendCmd("539246614","g7oROzGyKmsaxC5zBtLjp8yoz5M=",2);
        sendCmd.springBeginsTimingSendCmd("539246614", "g7oROzGyKmsaxC5zBtLjp8yoz5M=");
    }

    /**
     * 春分测试一个定时
     * @param devId
     * @param key
     * @return
     */
    public String testVETiming(String devId,String key){
        int id = Integer.parseInt(devId);
        int length = 0;
        //20+10*6
        byte[] buffer = new byte[26];
        //帧头
        buffer[length++] = (byte) (0x95);
        //协议版本
        buffer[length++] = (byte) (0x01);
        //设备编号
        buffer[length++] = (byte) (id>>24);
        buffer[length++] = (byte) (id>>16);
        buffer[length++] = (byte) (id>>8);
        buffer[length++] = (byte) (id>>0);
        //设备类型
        buffer[length++]  = (byte) (0x00);
        buffer[length++]  = (byte) (0x02);
        //物理通道
        buffer[length++]  = (byte) (0x83);
        //帧序列号
        buffer[length++]  = (byte) (0x00);
        buffer[length++] = (byte) (0x08);
        //连接周期
        buffer[length++] = (byte) (0x00);
        buffer[length++] = (byte) (0x3C);
        //功能码
        buffer[length++] = (byte) (0x40);
        buffer[length++] = (byte) (0x04);
        //长度 62 0x00 0x3E
        buffer[length++] = (byte) (0x00);
        buffer[length++] = (byte) (0x08);
        //静荷:Type类型
        buffer[length++] = (byte) (0x22);
        //静荷:Len长度 50 0x32 N*6
        buffer[length++] = (byte) (0x06);
        //静荷：小时
        buffer[length++] = (byte) (0x0B);
        //静荷：分钟
        buffer[length++] = (byte) (0x0B);
        //静荷：打开时间
        buffer[length++] = (byte) (0x00);
        buffer[length++] = (byte) (0x01);
        //静荷：星期
        buffer[length++] = (byte) (0x80);
        //开关
        buffer[length++] = (byte) (1);
        //校验码
        buffer[length++] = (byte) (XorUtil.getXor(buffer));
        return sendCmdCommon(devId,key,buffer);
    }
}
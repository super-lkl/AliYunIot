package com.example.demo.pojo;

import java.math.BigDecimal;

/**
 * 春分
 * @author 李康龙
 */
public class VernalEquinox {

    /**开关1*/
    private Object switch1;

    /**开关2*/
    private Object switch2;

    /**开关3*/
    private Object switch3;

    /**开关4*/
    private Object switch4;

    /**空气温度*/
    private Object air_temp1;

    /**空气湿度*/
    private Object air_moi1;

    /**风速*/
    private Object wind_speed1;

    /**风向*/
    private Object wind_dir1;

    /**大气压力*/
    private Object air_pressure1;

    /**水表：瞬时流量*/
    private Object flow_rate1;

    /**水表：总流量和*/
    private Object total_flow1;

    /**土壤温度*/
    private Object soil_temp1;

    /**土壤湿度*/
    private Object soil_moi1;

    @Override
    public String toString() {
        return "VernalEquinox{" +
                "switch1='" + switch1 + '\'' +
                ", switch2='" + switch2 + '\'' +
                ", switch3='" + switch3 + '\'' +
                ", switch4='" + switch4 + '\'' +
                ", air_temp1=" + air_temp1 +
                ", air_moi1=" + air_moi1 +
                ", wind_speed1=" + wind_speed1 +
                ", wind_dir1='" + wind_dir1 + '\'' +
                ", air_pressure1=" + air_pressure1 +
                ", flow_rate1=" + flow_rate1 +
                ", total_flow1=" + total_flow1 +
                ", soil_temp1=" + soil_temp1 +
                ", soil_moi1=" + soil_moi1 +
                '}';
    }

    /**开关1*/
    public Object getSwitch1() {
        return switch1;
    }

    /**开关1*/
    public void setSwitch1(Object switch1) {
        this.switch1 = switch1;
    }

    /**开关2*/
    public Object getSwitch2() {
        return switch2;
    }

    /**开关2*/
    public void setSwitch2(Object switch2) {
        this.switch2 = switch2;
    }

    /**开关3*/
    public Object getSwitch3() {
        return switch3;
    }

    /**开关3*/
    public void setSwitch3(Object switch3) {
        this.switch3 = switch3;
    }

    /**开关4*/
    public Object getSwitch4() {
        return switch4;
    }

    /**开关4*/
    public void setSwitch4(Object switch4) {
        this.switch4 = switch4;
    }

    /**空气温度*/
    public Object getAir_temp1() {
        return air_temp1;
    }

    /**空气温度*/
    public void setAir_temp1(Object air_temp1) {
        this.air_temp1 = air_temp1;
    }

    /**空气湿度*/
    public Object getAir_moi1() {
        return air_moi1;
    }

    /**空气湿度*/
    public void setAir_moi1(Object air_moi1) {
        this.air_moi1 = air_moi1;
    }

    /**风速*/
    public Object getWind_speed1() {
        return wind_speed1;
    }

    /**风速*/
    public void setWind_speed1(Object wind_speed1) {
        this.wind_speed1 = wind_speed1;
    }

    /**风向*/
    public Object getWind_dir1() {
        return wind_dir1;
    }

    /**风向*/
    public void setWind_dir1(Object wind_dir1) {
        this.wind_dir1 = wind_dir1;
    }

    /**大气压力*/
    public Object getAir_pressure1() {
        return air_pressure1;
    }

    /**大气压力*/
    public void setAir_pressure1(Object air_pressure1) {
        this.air_pressure1 = air_pressure1;
    }

    /**水表：瞬时流量*/
    public Object getFlow_rate1() {
        return flow_rate1;
    }

    /**水表：瞬时流量*/
    public void setFlow_rate1(Object flow_rate1) {
        this.flow_rate1 = flow_rate1;
    }

    /**水表：总流量和*/
    public Object getTotal_flow1() {
        return total_flow1;
    }

    /**水表：总流量和*/
    public void setTotal_flow1(Object total_flow1) {
        this.total_flow1 = total_flow1;
    }

    /**土壤温度*/
    public Object getSoil_temp1() {
        return soil_temp1;
    }

    /**土壤温度*/
    public void setSoil_temp1(Object soil_temp1) {
        this.soil_temp1 = soil_temp1;
    }

    /**土壤湿度*/
    public Object getSoil_moi1() {
        return soil_moi1;
    }

    /**土壤湿度*/
    public void setSoil_moi1(Object soil_moi1) {
        this.soil_moi1 = soil_moi1;
    }
}

package com.rc.darren.smartbutler.entity;

/**
 * 项目名：    SmartButler
 * 包名：      com.rc.darren.smartbutler.entity
 * 文件名：    CourierData
 * 创建者：    Darren
 * 创建时间：  2017/2/28 13:47
 * 描述：      快递实体类
 */

public class CourierData {
    private String datetime;
    private String remark;
    private String zone;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "CourierData{" +
                "datetime='" + datetime + '\'' +
                ", remark='" + remark + '\'' +
                ", zone='" + zone + '\'' +
                '}';
    }
}

package com.example.traffic_light_with_arduino;

import java.util.Date;

public class TrafficIntersection {
    private String itstId;
    private String itstNm;
    private double mapCtptIntLat;
    private double mapCtptIntLot;
    private Double laneWidth; // null일 수 있으므로 래퍼 클래스로 선언
    private String limitSpedTypeNm;
    private Integer limitSped; // null일 수 있으므로 래퍼 클래스로 선언
    private String itstEngNm;
    private String rgtrId;
    private Date regDt;

    public TrafficIntersection() {
    }

    public TrafficIntersection(String itstId, String itstNm, double mapCtptIntLat, double mapCtptIntLot,
                               Double laneWidth, String limitSpedTypeNm, Integer limitSped,
                               String itstEngNm, String rgtrId, Date regDt) {
        this.itstId = itstId;
        this.itstNm = itstNm;
        this.mapCtptIntLat = mapCtptIntLat;
        this.mapCtptIntLot = mapCtptIntLot;
        this.laneWidth = laneWidth;
        this.limitSpedTypeNm = limitSpedTypeNm;
        this.limitSped = limitSped;
        this.itstEngNm = itstEngNm;
        this.rgtrId = rgtrId;
        this.regDt = regDt;
    }

    public String getItstId() {
        return itstId;
    }

    public String getItstNm() {
        return itstNm;
    }

    public double getMapCtptIntLat() {
        return mapCtptIntLat;
    }

    public double getMapCtptIntLot() {
        return mapCtptIntLot;
    }

    public Double getLaneWidth() {
        return laneWidth;
    }

    public String getLimitSpedTypeNm() {
        return limitSpedTypeNm;
    }

    public Integer getLimitSped() {
        return limitSped;
    }

    public String getItstEngNm() {
        return itstEngNm;
    }

    public String getRgtrId() {
        return rgtrId;
    }

    public Date getRegDt() {
        return regDt;
    }

    public void setItstId(String itstId) {
        this.itstId = itstId;
    }

    public void setItstNm(String itstNm) {
        this.itstNm = itstNm;
    }

    public void setMapCtptIntLat(double mapCtptIntLat) {
        this.mapCtptIntLat = mapCtptIntLat;
    }

    public void setMapCtptIntLot(double mapCtptIntLot) {
        this.mapCtptIntLot = mapCtptIntLot;
    }

    public void setLaneWidth(Double laneWidth) {
        this.laneWidth = laneWidth;
    }

    public void setLimitSpedTypeNm(String limitSpedTypeNm) {
        this.limitSpedTypeNm = limitSpedTypeNm;
    }

    public void setLimitSped(Integer limitSped) {
        this.limitSped = limitSped;
    }

    public void setItstEngNm(String itstEngNm) {
        this.itstEngNm = itstEngNm;
    }

    public void setRgtrId(String rgtrId) {
        this.rgtrId = rgtrId;
    }

    public void setRegDt(Date regDt) {
        this.regDt = regDt;
    }
}

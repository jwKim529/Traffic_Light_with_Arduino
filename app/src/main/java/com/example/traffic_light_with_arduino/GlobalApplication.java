package com.example.traffic_light_with_arduino;

import android.annotation.SuppressLint;
import android.app.Application;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import kotlin.collections.ArrayDeque;


public class GlobalApplication extends Application {

    private final String SERVER_IP = "10.10.108.148";
    private final int SERVER_PORT = 9999;
    private final String SEOUL_API_KEY = BuildConfig.SEOUL_API_KEY;
    private final String SEOUL_TI_API_URL = "http://t-data.seoul.go.kr/apig/apiman-gateway/tapi/v2xSignalPhaseTimingInformation/1.0?apikey=";
    private final String SEOUL_MAP_API_URL = "http://t-data.seoul.go.kr/apig/apiman-gateway/tapi/v2xCrossroadMapInformation/1.0?apikey=";

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    private List<TrafficIntersection> intersectionList = new ArrayList<>();

    private static GlobalApplication instance;

    public GlobalApplication() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        new TrafficIntersectionTask().execute();
    }

    public static GlobalApplication getInstance() {
        return instance;
    }

    public String getSERVER_IP() {
        return SERVER_IP;
    }

    public int getSERVER_PORT() { return SERVER_PORT; }

    public String getSEOUL_TI_API_URL() {
        return SEOUL_TI_API_URL+SEOUL_API_KEY;
    }

    public String getSEOUL_MAP_API_URL() { return SEOUL_MAP_API_URL+SEOUL_API_KEY; }

    public List<TrafficIntersection> getIntersectionList() {
        return intersectionList;
    }

    public void setIntersectionList(List<TrafficIntersection> intersectionList) {
        instance.intersectionList = intersectionList;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }
}

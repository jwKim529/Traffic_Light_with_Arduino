package com.example.traffic_light_with_arduino;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TrafficIntersectionTask extends AsyncTask<Void, Void, List<TrafficIntersection>> {

    private final GlobalApplication myApp = GlobalApplication.getInstance();

    private final String API_URL = myApp.getSEOUL_MAP_API_URL();

    SimpleDateFormat dateFormat = myApp.getDateFormat();

    @Override
    protected List<TrafficIntersection> doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(API_URL)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String jsonData = response.body().string();
            return parseJson(jsonData);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<TrafficIntersection> parseJson(String jsonData) throws JSONException {
        List<TrafficIntersection> intersections = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(jsonData);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            TrafficIntersection intersection = new TrafficIntersection();
            intersection.setItstId(jsonObject.getString("itstId"));
            intersection.setItstNm(jsonObject.getString("itstNm"));
            intersection.setMapCtptIntLat(jsonObject.getDouble("mapCtptIntLat"));
            intersection.setMapCtptIntLot(jsonObject.getDouble("mapCtptIntLot"));
            intersection.setLaneWidth(jsonObject.getDouble("laneWidth"));
            intersection.setLimitSpedTypeNm(jsonObject.getString("limitSpedTypeNm"));
            intersection.setLimitSped(jsonObject.getInt("limitSped"));
            intersection.setItstEngNm(jsonObject.getString("itstEngNm"));
            intersection.setRgtrId(jsonObject.getString("rgtrId"));
            try {
                intersection.setRegDt(dateFormat.parse(jsonObject.getString("regDt")));
            } catch (ParseException e){
                e.printStackTrace();
            }
            intersections.add(intersection);
        }
        myApp.setIntersectionList(intersections);
        return intersections;
    }
}


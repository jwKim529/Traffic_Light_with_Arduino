package com.example.traffic_light_with_arduino;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.view.View;
import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class TrafficLightDialog extends Dialog {

    private static final String BASE_URL = "http://t-data.seoul.go.kr/apig/apiman-gateway/tapi/v2xSignalPhaseTimingInformation/1.0";
    private static final String API_KEY = "YOUR_API_KEY"; // 여기에 API 키 값을 설정하세요
    private int remainingTime;
    private Handler handler;
    private Runnable updateTimeTask;
    private String itstId;

    public TrafficLightDialog(@NonNull Context context, String itstId) {
        super(context);
        this.itstId = itstId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traffic_light_dialog);

        setCanceledOnTouchOutside(true);  // 다이얼로그 외부 터치 시 종료 설정

        TextView remainingTimeTextView = findViewById(R.id.remaining_time);
        View northStraightSignal = findViewById(R.id.north_straight_signal);
        TextView northStraightTime = findViewById(R.id.north_straight_time);
        View northLeftSignal = findViewById(R.id.north_left_signal);

        View eastStraightSignal = findViewById(R.id.east_straight_signal);
        TextView eastStraightTime = findViewById(R.id.east_straight_time);
        View eastLeftSignal = findViewById(R.id.east_left_signal);

        View southStraightSignal = findViewById(R.id.south_straight_signal);
        TextView southStraightTime = findViewById(R.id.south_straight_time);
        View southLeftSignal = findViewById(R.id.south_left_signal);

        View westStraightSignal = findViewById(R.id.west_straight_signal);
        TextView westStraightTime = findViewById(R.id.west_straight_time);
        View westLeftSignal = findViewById(R.id.west_left_signal);

        // 초기값 설정
        handler = new Handler();
        updateTimeTask = new Runnable() {
            @Override
            public void run() {
                remainingTimeTextView.setText("Remaining Time: " + remainingTime + " seconds");
                if (remainingTime > 0) {
                    remainingTime--;
                    handler.postDelayed(this, 1000);
                }
            }
        };

        sendRequest(itstId, new TrafficLightCallback() {
            @Override
            public void onSuccess(JSONObject data) {
                try {
                    remainingTime = data.optInt("wtPdsgRmdrCs", 0) / 10; // 예시로 남은 시간을 설정
                    handler.post(updateTimeTask);

                    // 신호등 상태 업데이트
                    updateSignal(northStraightSignal, northStraightTime, data.optString("ntStsgRmdrCs"));
                    updateSignal(northLeftSignal, null, data.optString("ntLtsgRmdrCs"));

                    updateSignal(eastStraightSignal, eastStraightTime, data.optString("etStsgRmdrCs"));
                    updateSignal(eastLeftSignal, null, data.optString("etLtsgRmdrCs"));

                    updateSignal(southStraightSignal, southStraightTime, data.optString("stStsgRmdrCs"));
                    updateSignal(southLeftSignal, null, data.optString("stLtsgRmdrCs"));

                    updateSignal(westStraightSignal, westStraightTime, data.optString("wtStsgRmdrCs"));
                    updateSignal(westLeftSignal, null, data.optString("wtLtsgRmdrCs"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        handler.removeCallbacks(updateTimeTask);
    }

    private void sendRequest(String itstId, TrafficLightCallback callback) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("itstId", itstId);
        urlBuilder.addQueryParameter("apiKey", API_KEY);

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(responseData);
                        if (jsonArray.length() > 0) {
                            JSONObject data = jsonArray.getJSONObject(0);
                            callback.onSuccess(data);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    callback.onFailure(new IOException("Unexpected code " + response));
                }
            }
        });
    }

    private void updateSignal(View signalView, TextView timeView, String remainingSeconds) {
        if (remainingSeconds != null && !remainingSeconds.equals("null")) {
            int remaining = Integer.parseInt(remainingSeconds) / 10;
            signalView.setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_green_light));
            if (timeView != null) {
                timeView.setText(String.valueOf(remaining) + "s");
            }
        } else {
            signalView.setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_red_light));
            if (timeView != null) {
                timeView.setText("");
            }
        }
    }

    // 콜백 인터페이스 정의
    private interface TrafficLightCallback {
        void onSuccess(JSONObject data);
        void onFailure(IOException e);
    }
}

package com.example.traffic_light_with_arduino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Build;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener {
    private MapView mapView;
    private ViewGroup mapViewContainer;
    private TextView locationTextView;
    private Socket socket;
    private final String SERVER_IP = "10.10.108.148";
    private final int SERVER_PORT = 9999;
    private OutputStream outputStream;

    private double currentLatitude;
    private double currentLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 권한 확인 및 요청
        checkPermissions();

        // 지도 설정
        mapView = new MapView(this);
        mapViewContainer = findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        mapView.setMapViewEventListener(this);
        mapView.setCurrentLocationEventListener(this);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

        // 위치 텍스트뷰 설정
        locationTextView = findViewById(R.id.location_text_view);

        // 서버 소켓 연결
        connectToServer();

        // 송신 버튼 설정
        Button sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(view -> sendMessage("hello_world"));
    }

    private void checkPermissions() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 1000);
        }
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
                outputStream = socket.getOutputStream(); // OutputStream 초기화
                // 서버로부터 데이터를 받는 메서드 호출
                receivedDataFromServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void receivedDataFromServer() {
        // 서버로부터 데이터를 받는 로직 추가
        // 예시: InputStream을 통해 데이터 읽기
    }

    private void sendMessage(String message) {
        new Thread(() -> {
            try {
                if (outputStream != null) {
                    outputStream.write(message.getBytes());
                    outputStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    finish();
                    return;
                }
            }
        }
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        currentLatitude = mapPointGeo.latitude;
        currentLongitude = mapPointGeo.longitude;
        updateLocationText();
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float headingAngle) {
        // 현재 위치 방향 업데이트
    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {
        // 현재 위치 업데이트 실패 처리
    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {
        // 현재 위치 업데이트 취소 처리
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        // 지도 초기화 완료 처리
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
        // 지도 중심점 이동 처리
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int zoomLevel) {
        // 지도 줌 레벨 변경 처리
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();

        // 마커 생성 및 위치 설정
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("Clicked Location");
        marker.setTag(0);
        marker.setMapPoint(mapPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

        mapView.removeAllPOIItems(); // 기존 마커 제거
        mapView.addPOIItem(marker); // 새로운 마커 추가

        // 클릭한 위치의 위도와 경도를 텍스트 뷰에 설정
        locationTextView.setText(String.format("Location: %f, %f\nMark Location: %f, %f", currentLatitude, currentLongitude, mapPointGeo.latitude, mapPointGeo.longitude));
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
        // 지도 더블 탭 처리
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
        // 지도 롱 프레스 처리
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
        // 지도 드래그 시작 처리
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
        // 지도 드래그 종료 처리
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        // 지도 이동 완료 처리
    }

    private void updateLocationText() {
        locationTextView.setText(String.format("Location: %f, %f", currentLatitude, currentLongitude));
    }
}
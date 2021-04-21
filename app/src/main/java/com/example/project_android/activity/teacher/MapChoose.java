package com.example.project_android.activity.teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.example.project_android.R;
import com.example.project_android.util.MyApplication;
import com.example.project_android.util.ProjectStatic;
import com.example.project_android.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class MapChoose extends AppCompatActivity {
    //存储选择坐标的信息
    private String location;
    private Double latitude,longitude;
    private LatLng currentLocation;

    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.action_bar_title)
    TextView title;

    private LocationClient locationClient;
    private BaiduMap baiduMap;
    private GeoCoder search = GeoCoder.newInstance();

    private List<Overlay> overlays = new ArrayList<>();

    private Boolean isFirst = true;

    public void setInfo(Double latitude,Double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map_choose);
        ButterKnife.bind(this);
        title.setText("选择坐标");

        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new MyLocationListener());
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        baiduMap.setOnMapClickListener(onMapClickListener);
        search.setOnGetGeoCodeResultListener(onGetGeoCoderResultListener);
        requestLocation();
    }

    @OnClick({R.id.map_my_location,R.id.map_choose_location,R.id.action_bar_back})
    public void onClicked(View view){
        switch (view.getId()){
            case R.id.map_my_location:
                Toast.makeText(this, "我的位置", Toast.LENGTH_SHORT).show();
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(currentLocation,19f);
                baiduMap.animateMapStatus(update);
//                执行点击地图操作
                onMapClickListener.onMapClick(currentLocation);
                break;
            case R.id.action_bar_back:
            case R.id.map_choose_location:
                Intent intent = new Intent();
                intent.putExtra("location",location);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                setResult(RESULT_OK,intent);
                finish();
                break;
            default:break;
        }
    }

    public void requestLocation(){
        initLocation();
        locationClient.start();
    }

    public void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationPurpose(LocationClientOption.BDLocationPurpose.SignIn);
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        locationClient.setLocOption(option);
    }

    public void navigateTo(BDLocation location){
        if (isFirst){
            isFirst = false;
            LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
//            setInfo(ll.latitude,ll.longitude);
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(ll,19f);
            baiduMap.animateMapStatus(update);
        }
        MyLocationData.Builder builder = new MyLocationData.Builder();
        builder.latitude(location.getLatitude());
        builder.longitude(location.getLongitude());
        MyLocationData locationData = builder.build();
        baiduMap.setMyLocationData(locationData);
    }

    private void drawCircle(LatLng point){
        CircleOptions circle = new CircleOptions().fillColor(0x384d73b3).
                center(point).stroke(new Stroke(3,0x784d73b3))
                .radius(ProjectStatic.DISTANCE).visible(true);
        overlays.add(baiduMap.addOverlay(circle));
    }

    private void setInfoWindow(String message){
        Button button = new Button(getApplicationContext());
        button.setBackgroundResource(R.drawable.style_ellipse_recycler_white);
        button.setPadding(10,0,10,0);
        button.setText(message);
        button.setTextSize(13f);
        baiduMap.showInfoWindow(new InfoWindow(button,new LatLng(latitude,longitude),-130));
        location = message;
    }

//    定义地图点击事件
    public BaiduMap.OnMapClickListener onMapClickListener = new BaiduMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            baiduMap.removeOverLays(overlays);
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(ImageUtils.getBitmap(R.drawable.ic_location_on));
            OverlayOptions option = new MarkerOptions().position(latLng)
                    .icon(bitmap).animateType(MarkerOptions.MarkerAnimateType.grow)
                    .alpha(1f).visible(true);
            overlays.add(baiduMap.addOverlay(option));
            setInfo(latLng.latitude,latLng.longitude);
            drawCircle(latLng);

            search.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
        }

        @Override
        public void onMapPoiClick(MapPoi mapPoi) {
            LatLng latLng = mapPoi.getPosition();
            baiduMap.removeOverLays(overlays);
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(ImageUtils.getBitmap(R.drawable.ic_location_on));
            OverlayOptions option = new MarkerOptions().position(latLng)
                    .icon(bitmap).animateType(MarkerOptions.MarkerAnimateType.grow)
                    .alpha(1f).visible(true);
            overlays.add(baiduMap.addOverlay(option));
            setInfo(latLng.latitude,latLng.longitude);
            drawCircle(latLng);

            setInfoWindow(mapPoi.getName());
        }
    };

//    获取当前位置的名称
    public OnGetGeoCoderResultListener onGetGeoCoderResultListener = new OnGetGeoCoderResultListener() {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            List<PoiInfo> poiList = reverseGeoCodeResult.getPoiList();
            if (!poiList.isEmpty()){
                PoiInfo poiInfo = poiList.get(0);
                String text = poiInfo.name + poiInfo.direction + poiInfo.distance + "米";
                setInfoWindow(text);
            }
        }
    };

//    定位我的位置
    public class MyLocationListener extends BDAbstractLocationListener{
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
                currentLocation = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                navigateTo(bdLocation);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        locationClient.stop();
        search.destroy();
        baiduMap.setMyLocationEnabled(false);
    }

}
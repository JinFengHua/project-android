package com.example.project_android.activity.student;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
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
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.example.project_android.R;
import com.example.project_android.dialog.LoadingDialog;
import com.example.project_android.dialog.SetGestureDialog;
import com.example.project_android.dialog.ShowFaceDialog;
import com.example.project_android.entity.AttendList;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ProjectStatic;
import com.example.project_android.util.ViewUtils;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class StudentDoRecord extends AppCompatActivity {
    @BindView(R.id.mapView)
    MapView mapView;

    private AttendList attend;
    private LatLng attendLocation;
    private LatLng currentLocation;
    private Boolean isFirst = true;

    private String pictureDir;
    private String picturePath;

    private LocationClient locationClient;
    private BaiduMap baiduMap;
    private GeoCoder search = GeoCoder.newInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_student_do_record);
        ButterKnife.bind(this);
        ViewUtils.initActionBar(this,"开始签到");

        Intent intent = getIntent();
        attend = (AttendList)intent.getExtras().getSerializable("attend");
        attendLocation = new LatLng(attend.getLatitude(),attend.getLongitude());

        pictureDir = PathUtils.getExternalAppPicturesPath();
        FileUtils.createOrExistsDir(pictureDir);
        picturePath = pictureDir + "/temp.png";

        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new MyLocationListener());
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        search.setOnGetGeoCodeResultListener(onGetGeoCoderResultListener);
        requestLocation();
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
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(ll,19f);
            baiduMap.animateMapStatus(update);
        }
        MyLocationData.Builder builder = new MyLocationData.Builder();
        builder.latitude(location.getLatitude());
        builder.longitude(location.getLongitude());
        MyLocationData locationData = builder.build();
        baiduMap.setMyLocationData(locationData);

        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(ImageUtils.getBitmap(R.drawable.ic_location_on));
        OverlayOptions option = new MarkerOptions().position(attendLocation)
                .icon(bitmap).animateType(MarkerOptions.MarkerAnimateType.grow)
                .alpha(1f).visible(true);
        baiduMap.addOverlay(option);
        drawCircle(attendLocation);
    }

    private void drawCircle(LatLng point){
        CircleOptions circle = new CircleOptions().fillColor(0x384d73b3).
                center(point).stroke(new Stroke(3,0x784d73b3))
                .radius(ProjectStatic.DISTANCE).visible(true);
        baiduMap.addOverlay(circle);
    }

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

                if (attend.getType() == 1) {
                    String name = getSharedPreferences("localRecord", MODE_PRIVATE).getString("id", "") + "_" + attend.getAttendId();
                    ShowFaceDialog dialog = new ShowFaceDialog(StudentDoRecord.this, picturePath, name, text);
                    dialog.setRecordSuccess(() -> finish());
                    dialog.show();
                } else {
                    LoadingDialog dialog = new LoadingDialog(StudentDoRecord.this);
                    dialog.setTitle("正在签到");
                    dialog.show();
                    Map<String, String> map = new HashMap<>();
                    map.put("attendId",String.valueOf(attend.getAttendId()));
                    map.put("studentId",getSharedPreferences("localRecord",MODE_PRIVATE).getString("id",""));
                    map.put("result","2");
                    map.put("time",new Timestamp(System.currentTimeMillis()).toString());
                    map.put("location",text);
                    NetUtil.getNetData("record/modifyRecord",map,new Handler(msg -> {
                        dialog.showSingleButton();
                        if (msg.what == 1){
                            dialog.setMessage("签到成功");
                            dialog.setOnDismissListener(dialog1 -> {
                                finish();
                            });
                        } else {{
                            dialog.setMessage(msg.getData().getString("message"));
                        }}
                        return false;
                    }));
                }
            }
        }
    };

    //    定位我的位置
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
                currentLocation = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                Log.d("location-->",bdLocation.getLatitude() + "," +bdLocation.getLongitude());
                navigateTo(bdLocation);
            } else {
                LoadingDialog loadingDialog = new LoadingDialog(getApplicationContext());
                loadingDialog.setTitle("定位错误");
                loadingDialog.setMessage("无法定位，请打开GPS或网络重试");
                loadingDialog.showSingleButton();
                loadingDialog.show();
                loadingDialog.setOnDismissListener(dialog -> finish());
            }
        }
    }


    @OnClick({R.id.map_start_record,R.id.map_my_location})
    public void onClicked(View v){
        LoadingDialog dialog = new LoadingDialog(this);
        dialog.setTitle("提示");
        switch (v.getId()){
            case R.id.map_start_record:
                //获取当前位置并与签到位置比对
                if (DistanceUtil.getDistance(currentLocation,attendLocation) > ProjectStatic.DISTANCE){
                    dialog.setMessage("不在考勤范围内，请在考勤范围内签到");
                    dialog.showSingleButton();
                    dialog.show();
                } else {
//                    打开相机
                    if (attend.getType() == 1) {
                        openCamera();
                    } else if (attend.getType() == 2){
                        showGestureDialog();
                    } else {
                        search.reverseGeoCode(new ReverseGeoCodeOption().location(currentLocation));
                    }
                }
                break;
            case R.id.map_my_location:
                Toast.makeText(this, "我的位置", Toast.LENGTH_SHORT).show();
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(currentLocation,19f);
                baiduMap.animateMapStatus(update);
                break;
        }

    }

    private void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(picturePath)));
        startActivityForResult(intent, ProjectStatic.OPEN_CAMERA);
    }

    public void showGestureDialog(){
        SetGestureDialog gestureDialog = new SetGestureDialog(this);
        gestureDialog.setRightGesture(attend.getGesture());
        gestureDialog.setCancelable(true);
        gestureDialog.setCanceledOnTouchOutside(true);
        gestureDialog.setYesClickedListener(new SetGestureDialog.onYesClickedListener() {
            @Override
            public void yesClicked(String list) {

            }

            @Override
            public void yesClicked() {
                search.reverseGeoCode(new ReverseGeoCodeOption().location(currentLocation));
                gestureDialog.dismiss();
            }
        });
        gestureDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File tempFile = new File(picturePath);
        switch (requestCode){
            case ProjectStatic.OPEN_CAMERA:
                if (resultCode == RESULT_OK) {
                    Crop.of(Uri.fromFile(tempFile),Uri.fromFile(tempFile)).asSquare().withAspect(500,500).start(this);
                }
                break;
            case Crop.REQUEST_CROP:
                if (resultCode == RESULT_OK) {
//                    获得当前位置的中文描述
                    search.reverseGeoCode(new ReverseGeoCodeOption().location(currentLocation));
                }
                break;

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
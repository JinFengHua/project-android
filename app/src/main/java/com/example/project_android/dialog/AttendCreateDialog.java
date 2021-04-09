package com.example.project_android.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;
import com.codbking.widget.DatePickDialog;

import androidx.annotation.NonNull;

import com.codbking.widget.bean.DateType;
import com.example.project_android.R;
import com.example.project_android.util.NetUtil;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

@SuppressLint("NonConstantResourceId")
public class AttendCreateDialog extends Dialog {
    private TextView yes,no,chooseLocation;
    private TextView timeStart,timeEnd;

    private Integer courseId;
    private Double longitude;
    private Double latitude;
    private Timestamp startTime,endTime;

    private int attendType = 0;

    private LoadingDialog loadingDialog;

    private onChooseClickListener chooseClickListener;

    public interface onChooseClickListener{
        void onChooseClick();
    }

    public void setChooseClickListener(onChooseClickListener chooseClickListener) {
        this.chooseClickListener = chooseClickListener;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public AttendCreateDialog(@NonNull Context context,Integer courseId) {
        super(context, R.style.BottomDialog);
        this.courseId = courseId;
    }

    Handler createAttendHandler = new Handler(msg -> {
        if (msg.what == 1){
            dismiss();
        }
        loadingDialog.setMessage(msg.getData().getString("message"));
        loadingDialog.setVisibility(View.VISIBLE);
        return false;
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_attend_create, null);
        setContentView(inflate);
        ButterKnife.bind(this,inflate);

        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.setTitle("创建考勤任务");

        initView();

        initEvent();
    }

    private void initEvent(){
        yes.setOnClickListener(v -> {
            if (attendType < 1){
                Toast.makeText(v.getContext(), "考勤方式未填写", Toast.LENGTH_SHORT).show();
                return;
            }
            if (startTime == null || endTime == null){
                Toast.makeText(v.getContext(), "时间信息未填写", Toast.LENGTH_SHORT).show();
                return;
            }
            if (longitude == null || latitude == null){
                Toast.makeText(v.getContext(), "坐标未填写", Toast.LENGTH_SHORT).show();
                return;
            }
            if (startTime.after(endTime)){
                Toast.makeText(v.getContext(), "开始时间不能晚于截止时间", Toast.LENGTH_SHORT).show();
                return;
            }
            if (endTime.before(new Timestamp(System.currentTimeMillis()))){
                Toast.makeText(v.getContext(), "截止时间已过", Toast.LENGTH_SHORT).show();
                return;
            }
            if (endTime.getTime() - startTime.getTime() < 300000){
                Toast.makeText(v.getContext(), "持续时间不得低于五分钟", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, String> map = new HashMap<>();
            map.put("courseId",courseId.toString());
            map.put("start",startTime.toString());
            map.put("end",endTime.toString());
            map.put("longitude",longitude.toString());
            map.put("latitude",latitude.toString());
            map.put("location",chooseLocation.getText().toString());
//            添加考勤方式
//            map.put("attendMethod", String.valueOf(attendType));
            NetUtil.getNetData("attend/addAttend",map,createAttendHandler);
            loadingDialog.setMessage(StringUtils.getString(R.string.wait_message));
            loadingDialog.show();
        });

        no.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "取消创建", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        chooseLocation.setOnClickListener(v -> {
            if (chooseClickListener != null){
                chooseClickListener.onChooseClick();
            }
        });

        timeEnd.setOnClickListener(v -> {
            DatePickDialog dateTimeDialog = createDateTimeDialog();
            dateTimeDialog.setOnSureLisener(date -> {
                endTime = new Timestamp(date.getTime());
                timeEnd.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(endTime));
            });
            dateTimeDialog.show();
        });

        timeStart.setOnClickListener(v -> {
            DatePickDialog dateTimeDialog = createDateTimeDialog();
            dateTimeDialog.setOnSureLisener(date -> {
                startTime = new Timestamp(date.getTime());
                timeStart.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(startTime));
            });
            dateTimeDialog.show();
        });
    }

    @OnCheckedChanged({R.id.attend_register_method_face,R.id.attend_register_method_gesture,R.id.attend_register_method_GPS,R.id.attend_register_method_QR})
    public void onRadioCheck(CompoundButton view, boolean ischanged){
        switch (view.getId()){
            case R.id.attend_register_method_face:
                if (ischanged){
                    attendType = 1;
                }
                break;
            case R.id.attend_register_method_gesture:
                if (ischanged){
                    attendType = 2;
                }
                break;
            case R.id.attend_register_method_QR:
                if (ischanged){
                    attendType = 3;
                }
                break;
            case R.id.attend_register_method_GPS:
                if (ischanged){
                    attendType = 4;
                }
                break;
            default:
                attendType = 0;
                break;
        }
    }

    private void initView(){
        yes = findViewById(R.id.attend_register_yes);
        no = findViewById(R.id.attend_register_no);
        timeEnd = findViewById(R.id.attend_register_time_end);
        timeStart = findViewById(R.id.attend_register_time_start);
        chooseLocation = findViewById(R.id.attend_register_location_choose);
    }

    public DatePickDialog createDateTimeDialog(){
        DatePickDialog dialog = new DatePickDialog(getContext());
        dialog.setYearLimt(5);
        dialog.setTitle("选择时间");
        dialog.setType(DateType.TYPE_YMDHM);
        dialog.setMessageFormat("yyyy-MM-dd HH:mm");
        return dialog;
    }

    public void updateLocation(String s){
        chooseLocation.setText(s);
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width= ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height= ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }
}

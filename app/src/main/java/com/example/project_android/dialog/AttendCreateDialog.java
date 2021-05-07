package com.example.project_android.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;
import com.codbking.widget.DatePickDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.PopupMenu;

import com.codbking.widget.bean.DateType;
import com.example.project_android.R;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ProjectStatic;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

@SuppressLint("NonConstantResourceId")
public class AttendCreateDialog extends Dialog {
    private TextView yes,no,chooseLocation;
    private TextView timeStart,timeEnd;
    private NiceSpinner spinner;

    private Integer courseId;
    private Double longitude;
    private Double latitude;
    private Timestamp startTime,endTime;
    private Integer type;

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
        loadingDialog.showSingleButton();
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
            type = spinner.getSelectedIndex();
            map.put("type",String.valueOf(type));
//            当是2时说明此时选择考勤种类为手势
            if (type == 2){
                SetGestureDialog gestureDialog = new SetGestureDialog(v.getContext());
                gestureDialog.setYesClickedListener(new SetGestureDialog.onYesClickedListener() {
                    @Override
                    public void yesClicked(String list) {
                        gestureDialog.setOnDismissListener(dialog -> {
                            map.put("gesture",list);
                            NetUtil.getNetData("attend/addAttend",map,createAttendHandler);
                            loadingDialog.setMessage(StringUtils.getString(R.string.wait_message));
                            loadingDialog.show();
                        });
                        gestureDialog.dismiss();
                    }

                    @Override
                    public void yesClicked() {

                    }
                });
                gestureDialog.show();
            } else {
                NetUtil.getNetData("attend/addAttend",map,createAttendHandler);
                loadingDialog.setMessage(StringUtils.getString(R.string.wait_message));
                loadingDialog.show();
            }
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
                timeEnd.setText(new SimpleDateFormat(ProjectStatic.DATE_FORMAT_MINUTE, Locale.CHINA).format(endTime));
            });
            dateTimeDialog.show();
        });

        timeStart.setOnClickListener(v -> {
            DatePickDialog dateTimeDialog = createDateTimeDialog();
            dateTimeDialog.setOnSureLisener(date -> {
                startTime = new Timestamp(date.getTime());
                timeStart.setText(new SimpleDateFormat(ProjectStatic.DATE_FORMAT_MINUTE,Locale.CHINA).format(startTime));
            });
            dateTimeDialog.show();
        });

    }

    private void initView(){
        yes = findViewById(R.id.attend_register_yes);
        no = findViewById(R.id.attend_register_no);
        timeEnd = findViewById(R.id.attend_register_time_end);
        timeStart = findViewById(R.id.attend_register_time_start);
        chooseLocation = findViewById(R.id.attend_register_location_choose);
        spinner = findViewById(R.id.attend_register_type);
        spinner.setSelectedIndex(0);
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

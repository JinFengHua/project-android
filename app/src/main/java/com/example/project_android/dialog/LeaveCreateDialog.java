package com.example.project_android.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.bean.DateType;
import com.example.project_android.R;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ProjectStatic;
import com.example.project_android.util.ViewUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class LeaveCreateDialog extends Dialog {
    @BindView(R.id.no)
    TextView no;
    @BindView(R.id.yes)
    TextView yes;
    @BindView(R.id.leave_create_date_start)
    TextView start;
    @BindView(R.id.leave_create_num)
    EditText num;
    @BindView(R.id.leave_create_reason)
    EditText reasonEdit;

    private Integer courseId;

    private Timestamp startTime;
    private Timestamp endTime;

    public LeaveCreateDialog(@NonNull Context context, Integer courseId) {
        super(context, R.style.BottomDialog);
        this.courseId = courseId;
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_leave_create, null);
        ButterKnife.bind(this,view);
        setContentView(view);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        initEvent();
    }

    private void initEvent() {
        yes.setOnClickListener(v -> {
            String reason = reasonEdit.getText().toString();
            if (reason.length() < 1 || startTime == null || num.getText().toString().length() < 1){
                Toast.makeText(v.getContext(), "请将信息填写完整", Toast.LENGTH_SHORT).show();
                return;
            }
            Integer integer = Integer.valueOf(num.getText().toString());
            if (integer < 1){
                Toast.makeText(v.getContext(), "请假时长最少为1", Toast.LENGTH_SHORT).show();
                return;
            }

            String s = start.getText().toString();
            s = s + " 00:00:00";
            startTime = Timestamp.valueOf(s);

            endTime = new Timestamp(startTime.getTime() + 1000*3600*24*integer);
            Log.d("NET-->",startTime + "," +endTime);
            LoadingDialog dialog = new LoadingDialog(v.getContext());
            dialog.setTitle("请假申请");
            dialog.show();
            Map<String, String> map = new HashMap<>();
            map.put("leaveReason",reason);
            map.put("leaveTime",startTime.toString());
            map.put("backTime",endTime.toString());
            map.put("courseId",String.valueOf(courseId));
            map.put("studentId",v.getContext().getSharedPreferences("localRecord",Context.MODE_PRIVATE).getString("id",""));
            NetUtil.getNetData("leave/addLeave",map,new Handler(msg -> {
                dialog.showSingleButton();
                dialog.setMessage(msg.getData().getString("message"));
                if (msg.what == 1){
                    dialog.setOnDismissListener(dialog1 -> dismiss());
                }
                return false;
            }));
        });

        no.setOnClickListener(v -> dismiss());
    }

    @OnClick(R.id.leave_create_date_start)
    public void onClicked(){
        DatePickDialog dateTimeDialog = createDateTimeDialog();
        dateTimeDialog.setOnSureLisener(date -> {
            startTime = new Timestamp(date.getTime());
            start.setText(new SimpleDateFormat(ProjectStatic.DATE_FORMAT_DAY, Locale.CHINA).format(startTime));
        });
        dateTimeDialog.show();
    }

    private DatePickDialog createDateTimeDialog(){
        DatePickDialog dialog = new DatePickDialog(getContext());
        dialog.setYearLimt(5);
        dialog.setTitle("选择时间");
        dialog.setType(DateType.TYPE_YMD);
        dialog.setMessageFormat("yyyy-MM-dd");
        return dialog;
    }

    @Override
    public void show() {
        super.show();
        ViewUtils.show(getWindow());
    }
}

package com.example.project_android.activity.student;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.blankj.utilcode.util.ColorUtils;
import com.example.project_android.R;
import com.example.project_android.dialog.LoadingDialog;
import com.example.project_android.entity.Leave;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ProjectStatic;
import com.example.project_android.util.ViewUtils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class StudentLeaveDetail extends AppCompatActivity {
    @BindView(R.id.leave_detail_name)
    TextView nameText;
    @BindView(R.id.leave_detail_account)
    TextView accountText;
    @BindView(R.id.leave_detail_phone)
    TextView phoneText;
    @BindView(R.id.leave_detail_date_start)
    TextView startText;
    @BindView(R.id.leave_detail_date_end)
    TextView endText;
    @BindView(R.id.leave_detail_reason)
    TextView reasonText;
    @BindView(R.id.leave_detail_result)
    TextView resultText;
    @BindView(R.id.leave_detail_remark)
    EditText remarkText;
    @BindView(R.id.leave_detail_remark_layout)
    LinearLayout layout;
    @BindView(R.id.leave_detail_delete)
    Button deleteButton;

    private Leave leave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_leave_detail);
        ButterKnife.bind(this);
        ViewUtils.initActionBar(this,"请假详情");

        Intent intent = getIntent();
        leave = (Leave) intent.getExtras().getSerializable("leave");

        initView();
    }

    private void initView() {
        nameText.setText(leave.getStudentName());
        accountText.setText(leave.getStudentAccount());
        phoneText.setText(leave.getStudentPhone());
        SimpleDateFormat format = new SimpleDateFormat(ProjectStatic.DATE_FORMAT_MINUTE, Locale.CHINA);
        startText.setText(format.format(leave.getLeaveTime()));
        endText.setText(format.format(leave.getBackTime()));
        reasonText.setText(leave.getLeaveReason());

        String remark = leave.getApprovalRemark() == null ? "无备注信息" : leave.getApprovalRemark();
        switch (leave.getApprovalResult()){
            case 0:
                resultText.setText("审批中");
                resultText.setTextColor(ColorUtils.getColor(R.color.soft_blue));
                layout.setVisibility(View.GONE);
                deleteButton.setVisibility(View.VISIBLE);
                deleteButton.setOnClickListener(v -> {
                    //删除申请
                    LoadingDialog dialog = new LoadingDialog(v.getContext());
                    dialog.setTitle("撤销申请");
                    dialog.show();
                    Map<String, String> map = new HashMap<>();
                    map.put("leaveId",String.valueOf(leave.getLeaveId()));
                    NetUtil.getNetData("leave/deleteLeave",map,new Handler(msg -> {
                        dialog.setMessage(msg.getData().getString("message"));
                        dialog.showSingleButton();
                        return false;
                    }));
                });
                break;
            case 1:
                resultText.setText("不批准");
                resultText.setTextColor(ColorUtils.getColor(R.color.cancel_red));
                remarkText.setText(remark);
                break;
            case 2:
                resultText.setText("批准");
                resultText.setTextColor(ColorUtils.getColor(R.color.green));
                remarkText.setText(remark);
                break;
        }

    }
}
package com.example.project_android.activity.student;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ColorUtils;
import com.example.project_android.R;
import com.example.project_android.entity.AttendList;
import com.example.project_android.entity.Record;
import com.example.project_android.util.ProjectStatic;
import com.example.project_android.util.ViewUtils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class StudentRecord extends AppCompatActivity {
    @BindView(R.id.student_record_name)
    TextView nameText;
    @BindView(R.id.student_record_account)
    TextView accountText;
    @BindView(R.id.student_record_location)
    TextView locationText;
    @BindView(R.id.student_record_start_time)
    TextView startText;
    @BindView(R.id.student_record_end_time)
    TextView endText;
    @BindView(R.id.student_record_my_time)
    TextView myTimeText;
    @BindView(R.id.student_record_my_location)
    TextView myLocationText;
    @BindView(R.id.student_record_detail_layout)
    LinearLayout detailLayout;
    @BindView(R.id.student_record_result)
    TextView resultText;
    @BindView(R.id.student_record_face)
    ImageView faceImage;

    private AttendList attend;
    private Record record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_record);
        ButterKnife.bind(this);
        ViewUtils.initActionBar(this,"考勤记录");

        Intent intent = getIntent();
        attend = (AttendList)intent.getExtras().getSerializable("attend");
        record = getRecord(intent.getExtras().getString("record"));

        initView();

    }

    private void initView() {
        nameText.setText(record.getRecordName());
        accountText.setText(record.getRecordAccount());

        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd hh:mm:ss", Locale.CHINA);
        locationText.setText(attend.getLocation());
        startText.setText(format.format(attend.getStartTime()));
        endText.setText(format.format(attend.getEndTime()));

        if (record.getRecordResult().equals("2") || record.getRecordResult().equals("1")){
            if (record.getRecordResult().equals("2")) {
                resultText.setText("签到成功");
                resultText.setTextColor(ColorUtils.getColor(R.color.green));
            } else {
                resultText.setText("签到失败");
                resultText.setTextColor(ColorUtils.getColor(R.color.cancel_red));
            }

            myLocationText.setText(record.getRecordLocation());
            myTimeText.setText(format.format(record.getRecordTime()));
            Picasso.with(this)
                    .load(ProjectStatic.SERVICE_PATH + record.getRecordPhoto())
                    .fit()
                    .error(R.drawable.ic_net_error)
                    .into(faceImage);
        } else {
            if (record.getRecordResult().equals("3")){
                resultText.setText("已请假");
                resultText.setTextColor(ColorUtils.getColor(R.color.button_blue));
            } else {
                resultText.setText("缺勤");
                resultText.setTextColor(ColorUtils.getColor(R.color.purple_700));
            }
            detailLayout.setVisibility(View.GONE);
        }
    }


    private Record getRecord(String s){
        SharedPreferences preferences = getSharedPreferences("localRecord", MODE_PRIVATE);
        JSONObject object = JSONObject.parseObject(s);
        Record record = new Record(preferences.getString("avatar",""),object.getTimestamp("recordTime"),
                preferences.getString("name",""), preferences.getString("account",""),
                object.getString("recordResult"),object.getString("recordLocation"));
        record.setRecordPhoto(object.getString("recordPhoto"));
        return record;
    }
}
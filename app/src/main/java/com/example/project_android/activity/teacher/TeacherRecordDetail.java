package com.example.project_android.activity.teacher;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.ResourceUtils;
import com.example.project_android.R;
import com.example.project_android.adapter.AttendDetailAdapter;
import com.example.project_android.util.ViewUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class TeacherRecordDetail extends AppCompatActivity {
    private String data = "[{\"attendId\":12,\"studentId\":1,\"recordTime\":null,\"recordLongitude\":null,\"recordLatitude\":null,\"recordResult\":3,\"recordRemark\":null,\"recordPhoto\":null,\"student\":{\"studentId\":1,\"studentAccount\":\"000001\",\"studentPassword\":\"000000\",\"studentName\":\"江道宽\",\"studentSex\":true,\"studentAvatar\":\"/image/avatars/user-default.jpg\",\"studentClass\":\"0814171\",\"studentFace\":\"\",\"studentPhone\":\"13137749525\",\"studentEmail\":\"2116161338@qq.com\",\"records\":null}},{\"attendId\":12,\"studentId\":6,\"recordTime\":null,\"recordLongitude\":null,\"recordLatitude\":null,\"recordResult\":3,\"recordRemark\":null,\"recordPhoto\":null,\"student\":{\"studentId\":6,\"studentAccount\":\"081417158\",\"studentPassword\":\"123456\",\"studentName\":\"江道宽\",\"studentSex\":true,\"studentAvatar\":\"/image/avatars/user-default.jpg\",\"studentClass\":\"计科一班\",\"studentFace\":null,\"studentPhone\":\"13137749525\",\"studentEmail\":\"2116161338@qq.com\",\"records\":null}},{\"attendId\":12,\"studentId\":7,\"recordTime\":null,\"recordLongitude\":null,\"recordLatitude\":null,\"recordResult\":0,\"recordRemark\":null,\"recordPhoto\":null,\"student\":{\"studentId\":7,\"studentAccount\":\"081417152\",\"studentPassword\":\"jdk136924\",\"studentName\":\"陈庆旭\",\"studentSex\":false,\"studentAvatar\":\"/image/avatars/user-default.jpg\",\"studentClass\":\"计科一班\",\"studentFace\":null,\"studentPhone\":\"13137749525\",\"studentEmail\":\"2116161338@qq.com\",\"records\":null}}]";

    private TeacherRecordDetailViewModel viewModel;

    private int recordType = 0;

    Handler getListHandler = new Handler(msg -> {
        if (msg.what == 1){
            viewModel.setRecordList(msg.getData().getString("data"));
        }
        Toast.makeText(this, msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
        return false;
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        ButterKnife.bind(this);
        ViewUtils.initActionBar(this,"考勤详情");

        viewModel = new ViewModelProvider(this).get(TeacherRecordDetailViewModel.class);
        viewModel.getAttendDetailList().observe(this,attendDetailLists -> {
            ViewUtils.setRecycler(this,R.id.recycler_record_list,new AttendDetailAdapter(attendDetailLists));
        });

        //do Net Method
        viewModel.setRecordList(data);
//        Map<String, String> map = new HashMap<>();
//        NetUtil.getNetData("record/findAllRecord",map,getListHandler);
    }



    @OnClick({R.id.record_type_all,R.id.record_type_success,
            R.id.record_type_failure,R.id.record_type_absent,R.id.record_type_leave})
    public void onClicked(View view){
        refreshButton();
        switch (view.getId()){
            case R.id.record_type_all:
                viewModel.setRecordList(data);
                break;
            case R.id.record_type_success:
                viewModel.updateRecordList(data,2);
                break;
            case R.id.record_type_failure:
                viewModel.updateRecordList(data,1);
                break;
            case R.id.record_type_absent:
                viewModel.updateRecordList(data,0);
                break;
            case R.id.record_type_leave:
                viewModel.updateRecordList(data,3);
                break;
            default:break;
        }
        view.setBackground(ResourceUtils.getDrawable(R.color.gray));
    }

    public void refreshButton(){
        findViewById(R.id.record_type_all).setBackground(ResourceUtils.getDrawable(R.color.smssdk_transparent));
        findViewById(R.id.record_type_success).setBackground(ResourceUtils.getDrawable(R.color.smssdk_transparent));
        findViewById(R.id.record_type_failure).setBackground(ResourceUtils.getDrawable(R.color.smssdk_transparent));
        findViewById(R.id.record_type_absent).setBackground(ResourceUtils.getDrawable(R.color.smssdk_transparent));
        findViewById(R.id.record_type_leave).setBackground(ResourceUtils.getDrawable(R.color.smssdk_transparent));
    }
}

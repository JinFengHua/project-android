package com.example.project_android.activity.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.project_android.R;
import com.example.project_android.dialog.LoadingDialog;
import com.example.project_android.entity.CourseList;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ProjectStatic;
import com.example.project_android.util.ViewUtils;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.ui.companent.CircleImageView;

@SuppressLint("NonConstantResourceId")
public class StudentCourseConfirm extends AppCompatActivity {
    @BindView(R.id.course_confirm_avatar)
    CircleImageView courseAvatar;
    @BindView(R.id.course_confirm_name)
    TextView courseName;
    @BindView(R.id.course_confirm_code)
    TextView courseCode;
    @BindView(R.id.course_confirm_teacher_name)
    TextView teacherName;
    @BindView(R.id.course_confirm_teacher_phone)
    TextView teacherPhone;
    @BindView(R.id.course_confirm_teacher_email)
    TextView teacherEmail;
    @BindView(R.id.course_confirm_introduce)
    TextView courseIntroduce;

    private StudentCourseConfirmViewModel viewModel;
    private CourseList course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course_confirm);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");

        viewModel = new ViewModelProvider(this).get(StudentCourseConfirmViewModel.class);
        viewModel.getCourse().observe(this, courseList -> {
            course = courseList;
            initView();
        });
        assert data != null;
        viewModel.updateCourse(data);
        ViewUtils.initActionBar(this,viewModel.getCourse().getValue().getCourseName());
    }

    private void initView(){
        Picasso.with(this)
                .load(ProjectStatic.SERVICE_PATH + course.getCourseAvatar())
                .fit()
                .error(R.drawable.ic_net_error)
                .into(courseAvatar);

        courseName.setText(course.getCourseName());
        courseCode.setText(course.getCourseCode());
        courseIntroduce.setText(course.getCourseIntroduce());
        teacherEmail.setText(course.getUserEmail());
        teacherName.setText(course.getUesrName());
        teacherPhone.setText(course.getUserPhone());
    }

    @OnClick(R.id.course_confirm_add)
    public void onClicked(View view){
        Map<String, String> map = new HashMap<>();
        map.put("courseCode",course.getCourseCode());
        map.put("studentId",getSharedPreferences("localRecord",MODE_PRIVATE).getString("id",""));
        LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.setTitle("加入课程");
        loadingDialog.show();
        NetUtil.getNetData("courseStudent/addCourseStudent", map, new Handler(msg -> {
            if (msg.what == 1){
                String s = msg.getData().getString("data");
                loadingDialog.setOnYesClickedListener(view1 -> {
                    JSONObject data = JSON.parseObject(s);
                    Intent intent = new Intent(ProjectStatic.STUDENT_COURSE_DETAIL);
                    Bundle bundle = new Bundle();
                    course.setJoinTime(data.getTimestamp("joinTime"));
                    bundle.putSerializable("course",course);
                    intent.putExtras(bundle);
                    view1.getContext().startActivity(intent);
                    loadingDialog.dismiss();
                    finish();
                });
                loadingDialog.no.setVisibility(View.INVISIBLE);
                loadingDialog.setMessage(msg.getData().getString("message"));
            } else {
                loadingDialog.showSingleButton();
                loadingDialog.setMessage(msg.getData().getString("message"));
            }
            return false;
        }));
    }

}
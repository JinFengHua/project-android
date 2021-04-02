package com.example.project_android.activity.teacher;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_android.R;
import com.example.project_android.util.ViewUtils;

public class TeacherCourseDetail extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_course_detail);

        ViewUtils.initActionBar(this,"课程详情");
    }
}

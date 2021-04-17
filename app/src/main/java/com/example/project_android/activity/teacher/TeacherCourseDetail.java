package com.example.project_android.activity.teacher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.project_android.R;
import com.example.project_android.activity.CourseViewModel;
import com.example.project_android.entity.CourseList;
import com.example.project_android.util.ViewUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TeacherCourseDetail extends AppCompatActivity {
//    String data = "{\"courseId\":2,\"teacherId\":1,\"courseName\":\"云计算\",\"courseAvatar\":\"/image/avatars/course-default.png\",\"courseIntroduce\":\"云计算云计算云计算云计算云计算云计算\",\"courseCode\":\"647904\",\"teacher\":{\"teacherId\":1,\"adminId\":1,\"teacherAccount\":\"000001\",\"teacherPassword\":\"000000\",\"teacherName\":\"张老师\",\"teacherSex\":false,\"teacherPhone\":\"13137749525\",\"teacherEmail\":\"2116161338@qq.com\",\"teacherAvatar\":\"/image/avatars/user-default.jpg\",\"courses\":null}}";

    private CourseViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_course_detail);

        Intent intent = getIntent();
//        执行网络上传
        CourseList course = (CourseList) intent.getExtras().getSerializable("course");

//        模拟数据
        /*JSONObject o = JSONObject.parseObject(data);
        JSONObject teacher = JSONObject.parseObject(o.getString("teacher"));
        CourseList course = new CourseList(o.getInteger("courseId"),o.getString("teacherId"),
                teacher.getString("teacherName"),o.getString("courseName"),
                o.getString("courseIntroduce"),o.getString("courseCode"),
                o.getString("courseAvatar"));
        course.setTeacherEmail(teacher.getString("teacherEmail"));
        course.setTeacherPhone(teacher.getString("teacherPhone"));*/


        ViewUtils.initActionBar(this,course.getCourseName());

        viewModel = new ViewModelProvider(this).get(CourseViewModel.class);

//        先初始化course
        viewModel.getCourse();

        viewModel.setCourse(course);

        initBottomNavigation();

    }

    //实现fragment之间的跳转
    @SuppressLint("ResourceType")
    private void initBottomNavigation(){
        BottomNavigationView navigationView = findViewById(R.id.teacher_course_bottom);
        NavController controller = Navigation.findNavController(this,R.id.teacher_course_fragment);
        NavigationUI.setupWithNavController(navigationView,controller);

    }

}

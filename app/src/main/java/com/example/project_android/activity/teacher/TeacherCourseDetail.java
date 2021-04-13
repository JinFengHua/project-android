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
import com.example.project_android.entity.CourseList;
import com.example.project_android.util.ViewUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TeacherCourseDetail extends AppCompatActivity {

    private TeacherCourseDetailViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_course_detail);

        Intent intent = getIntent();
        CourseList course = (CourseList) intent.getExtras().getSerializable("course");

        ViewUtils.initActionBar(this,course.getCourseName());

        viewModel = new ViewModelProvider(this).get(TeacherCourseDetailViewModel.class);

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

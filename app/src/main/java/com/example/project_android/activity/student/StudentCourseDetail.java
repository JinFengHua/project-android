package com.example.project_android.activity.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.project_android.R;
import com.example.project_android.activity.CourseViewModel;
import com.example.project_android.entity.CourseList;
import com.example.project_android.util.ViewUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentCourseDetail extends AppCompatActivity {

    private CourseViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course_detail);

        Intent intent = getIntent();
        CourseList course = (CourseList) intent.getExtras().getSerializable("course");
        ViewUtils.initActionBar(this,course.getCourseName());
        viewModel = new ViewModelProvider(this).get(CourseViewModel.class);
//        先初始化course
        viewModel.getCourse();
        viewModel.setCourse(course);
        initBottomNavigation();
    }

    @SuppressLint("ResourceType")
    private void initBottomNavigation(){
        BottomNavigationView navigationView = findViewById(R.id.student_course_bottom);
        NavController controller = Navigation.findNavController(this,R.id.student_course_fragment);
        NavigationUI.setupWithNavController(navigationView,controller);

    }
}
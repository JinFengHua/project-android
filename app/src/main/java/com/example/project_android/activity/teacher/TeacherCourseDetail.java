package com.example.project_android.activity.teacher;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.project_android.R;
import com.example.project_android.util.ViewUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TeacherCourseDetail extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_course_detail);

        ViewUtils.initActionBar(this,"课程详情");

        initBottomNavigation();
    }

    //实现fragment之间的跳转
    private void initBottomNavigation(){
        BottomNavigationView navigationView = findViewById(R.id.teacher_course_bottom);
        NavController controller = Navigation.findNavController(this,R.id.teacher_course_fragment);
        NavigationUI.setupWithNavController(navigationView,controller);
    }
}

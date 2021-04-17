package com.example.project_android.activity.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.project_android.R;
import com.example.project_android.entity.AttendList;

public class StudentDoRecord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_do_record);

        Intent intent = getIntent();
        AttendList attend = (AttendList)intent.getExtras().getSerializable("attend");
    }
}
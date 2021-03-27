package com.example.project_android.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.project_android.R;
import com.example.project_android.util.ActionBarUtil;
import com.example.project_android.util.CommenUtil;

import butterknife.ButterKnife;

public class ModifyPassword extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        ButterKnife.bind(this);
        ActionBarUtil.initActionBar(this,"密码找回");

    }
}
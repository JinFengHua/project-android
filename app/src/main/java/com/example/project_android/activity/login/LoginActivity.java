package com.example.project_android.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.project_android.R;
import com.example.project_android.util.ActionBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.login_register})
    public void onClicked(View view){
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.login_register:
                intent.setAction("com.example.project_android.activity.login.LoginRegisterActivity");
                break;
            default:
                break;
        }
        startActivity(intent);
    }
}
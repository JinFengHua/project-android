package com.example.project_android.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.project_android.R;
import com.example.project_android.util.ActionName;
import com.example.project_android.util.NetUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.login_account)
    EditText accountEdit;
    @BindView(R.id.login_password)
    EditText passwordEdit;

    private int userType;

//    监听执行登录操作的子线程
    Handler loginHandler = new Handler(msg -> {
        if (msg.what == 1){
            String data = msg.getData().getString("data");
            Intent intent = new Intent(userType == 1 ? ActionName.TEACHER_MAIN : ActionName.STUDENT_MAIN);
            intent.putExtra("data",data);
            intent.putExtra("type",userType);
            Toast.makeText(this, msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
            startActivity(intent);
        } else{
            Toast.makeText(this, msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
        }
        return false;
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.login_register,R.id.login_forget_password,R.id.login_button})
    public void onClicked(View view){
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.login_register:
                intent.setAction("com.example.project_android.activity.login.LoginRegisterActivity");
                break;
            case R.id.login_forget_password:
                intent.setAction("com.example.project_android.activity.login.ModifyPassword");
                break;
            case R.id.login_button:
//                进行登陆操作
                if (userType == 0 || accountEdit.getText().toString().isEmpty() || passwordEdit.getText().toString().isEmpty()) {
                    Toast.makeText(this, "请补全登录信息", Toast.LENGTH_SHORT).show();
                    break;
                }
                Map<String,String> map = new HashMap<>();
                map.put("type", String.valueOf(userType));
                map.put("account",accountEdit.getText().toString());
                map.put("password",passwordEdit.getText().toString());
                NetUtil.getNetData("account/login",map,loginHandler);
                break;
            default:
                break;
        }
        startActivity(intent);
    }

    /**
     * 监控单选结果
     **/
    @OnCheckedChanged({R.id.login_radio_teacher,R.id.login_radio_student})
    public void onRadioCheck(CompoundButton view, boolean ischanged) {
        switch (view.getId()) {
            case R.id.login_radio_teacher:
                if (ischanged) {
                    userType = 1;
                }
                break;
            case R.id.login_radio_student:
                if (ischanged) {
                    userType = 2;
                }
                break;
            default:
                userType = 0;
                break;
        }
    }
}
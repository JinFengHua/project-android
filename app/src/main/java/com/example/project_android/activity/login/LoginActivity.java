package com.example.project_android.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;
import com.example.project_android.R;
import com.example.project_android.dialog.LoadingDialog;
import com.example.project_android.util.ProjectStatic;
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
                startActivity(intent);
                break;
            case R.id.login_forget_password:
                intent.setAction("com.example.project_android.activity.login.ModifyPassword");
                startActivity(intent);
                break;
            case R.id.login_button:
//                进行登陆操作
                if (userType == 0 || accountEdit.getText().toString().isEmpty() || passwordEdit.getText().toString().isEmpty()) {
                    Toast.makeText(this, "请补全登录信息", Toast.LENGTH_SHORT).show();
                    break;
                }
                LoadingDialog dialog = new LoadingDialog(view.getContext());
                dialog.setTitle("登录操作");
                dialog.setMessage(StringUtils.getString(R.string.wait_message));
                dialog.show();
                Map<String,String> map = new HashMap<>();
                map.put("type", String.valueOf(userType));
                map.put("account",accountEdit.getText().toString());
                map.put("password",passwordEdit.getText().toString());
                NetUtil.getNetData("account/login",map,new Handler(msg -> {
                    if (msg.what == 1){
                        dialog.dismiss();

                        String data = msg.getData().getString("data");
                        Intent loginIntent = new Intent(userType == 1 ? ProjectStatic.TEACHER_MAIN : ProjectStatic.STUDENT_MAIN);
                        updateLoginInfo(data,String.valueOf(userType));
                        Toast.makeText(this, msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(loginIntent);
                        finish();
                    } else{
                        dialog.setVisibility(View.VISIBLE);
                        dialog.setMessage(msg.getData().getString("message"));
                    }
                    return false;
                }));

                break;
        }
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

    //    将此次的登录信息记录到SharedPreferences中
    private void updateLoginInfo(String data,String type){
        JSONObject account = JSONObject.parseObject(data);

        SharedPreferences.Editor editor = getSharedPreferences("localRecord",MODE_PRIVATE).edit();
        editor.putString("userType",type);
        if (type.equals("1")){
            editor.putString("id",account.getString("teacherId"));
            editor.putString("account",account.getString("teacherAccount"));
            editor.putString("password",account.getString("teacherPassword"));
            editor.putString("name",account.getString("teacherName"));
            editor.putBoolean("sex",account.getBoolean("teacherSex"));
            editor.putString("phone",account.getString("teacherPhone"));
            editor.putString("email",account.getString("teacherEmail"));
            editor.putString("avatar",account.getString("teacherAvatar"));
        } else {
            editor.putString("id",account.getString("studentId"));
            editor.putString("account",account.getString("studentAccount"));
            editor.putString("password",account.getString("studentPassword"));
            editor.putString("name",account.getString("studentName"));
            editor.putBoolean("sex",account.getBoolean("studentSex"));
            editor.putString("phone",account.getString("studentPhone"));
            editor.putString("email",account.getString("studentEmail"));
            editor.putString("avatar",account.getString("studentAvatar"));
            editor.putString("class",account.getString("studentClass"));
            editor.putString("face",account.getString("studentFace"));
        }
        editor.apply();
    }
}
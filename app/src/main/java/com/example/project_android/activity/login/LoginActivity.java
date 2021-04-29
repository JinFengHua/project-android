package com.example.project_android.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
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
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.StringUtils;
import com.example.project_android.R;
import com.example.project_android.dialog.CourseAddDialog;
import com.example.project_android.dialog.LoadingDialog;
import com.example.project_android.util.ProjectStatic;
import com.example.project_android.util.NetUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        preferences = getSharedPreferences("localRecord",MODE_PRIVATE);
    }

    @OnClick({R.id.login_register,R.id.login_forget_password,R.id.login_button})
    public void onClicked(View view){
        if (!initPermission()){
            return;
        }
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.login_register:
                intent.setAction(ProjectStatic.LOGIN_REGISTER);
                startActivity(intent);
                break;
            case R.id.login_forget_password:
                intent.setAction(ProjectStatic.MODIFY_PASSWORD);
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
                        Intent loginIntent = new Intent(ProjectStatic.MAIN);
                        updateLoginInfo(preferences, data,String.valueOf(userType));
                        Toast.makeText(this, msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(loginIntent);
                        finish();
                    } else{
                        dialog.showSingleButton();
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
    public static void updateLoginInfo(SharedPreferences preferences, String data, String type){
        JSONObject account = JSONObject.parseObject(data);

        SharedPreferences.Editor editor = preferences.edit();
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

    private boolean initPermission(){
        List<String> permissionList = new ArrayList<>();
        if (!PermissionUtils.isGranted(Manifest.permission.READ_PHONE_STATE)){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (!PermissionUtils.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!PermissionUtils.isGranted(Manifest.permission.ACCESS_FINE_LOCATION)){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            PermissionUtils.permission(permissions).request();
            return false;
        }
        return true;
    }
}
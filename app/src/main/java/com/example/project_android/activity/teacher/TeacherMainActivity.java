package com.example.project_android.activity.teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.project_android.R;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class TeacherMainActivity extends AppCompatActivity {
    @BindView(R.id.action_bar_title)
    TextView titleText;
    @BindView(R.id.drawer_layout_teacher)
    DrawerLayout drawerLayout;

    private int currentFragmentId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);

        ButterKnife.bind(this);
        titleText.setText("课程列表");

//        Intent intent = getIntent();
//        updateLoginInfo(intent.getExtras().getString("data"),intent.getExtras().getString("type"));

        initNavigation();
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController controller = Navigation.findNavController(this,R.id.fragment_main);
        return controller.navigateUp();
    }

    @OnClick({R.id.action_bar_avatar,R.id.course_list_create_course})
    public void onClicked(View view){
        switch (view.getId()){
            case R.id.action_bar_avatar:
//                设置标题栏左部小头像的点击事件
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.course_list_create_course:

                break;
            default:
                break;
        }
    }

    private void initNavigation(){
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.nav_1);
        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawers();
            currentFragmentId = item.getItemId();
            switch (currentFragmentId){
                case R.id.nav_0:
                    Toast.makeText(this, "0", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_1:
                    Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_2:
                    Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_3:
                    Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            return true;
        });
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

    }
}
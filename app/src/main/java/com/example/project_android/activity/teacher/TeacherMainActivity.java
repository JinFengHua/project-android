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
import com.example.project_android.activity.login.LoginActivity;
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

        initNavigation();
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController controller = Navigation.findNavController(this,R.id.fragment_main);
        return controller.navigateUp();
    }

    @OnClick({R.id.action_bar_avatar})
    public void onClicked(View view){
        switch (view.getId()){
            case R.id.action_bar_avatar:
//                设置标题栏左部小头像的点击事件
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
    }

    private void initNavigation(){
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.nav_0);
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
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
            return true;
        });
    }

}
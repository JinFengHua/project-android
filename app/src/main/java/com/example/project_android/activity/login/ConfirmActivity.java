package com.example.project_android.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_android.R;
import com.example.project_android.entity.AccountStudent;
import com.example.project_android.util.ActionBarUtil;
import com.example.project_android.util.CommenUtil;
import com.example.project_android.util.NetUtil;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ConfirmActivity extends AppCompatActivity {
    @BindView(R.id.confirm_code) EditText codeEdit;
    @BindView(R.id.confirm_resend) TextView resend;
    @BindView(R.id.confirm_confirm) TextView confirm;

//    确认当前获得的msg是发送验证码时的还是验证时的
    public AccountStudent student;
    public int time = 60;
    public Timer timer;

    /**
     * 处理注册请求，成功则跳转到登陆页面，失败则返回注册界面
     */
    Handler registerHandler = new Handler(msg -> {
        if (msg.what == 0){
            Toast.makeText(this, msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
        finish();
        return true;
    });

    /**
     * 处理提交验证码的请求，验证通过发送真正的注册请求，失败则进行提示
     */
    Handler mHandler = new Handler(msg -> {
        if (msg.arg2 == SMSSDK.RESULT_COMPLETE && msg.arg1 == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
            Toast.makeText(this, "验证成功！！！", Toast.LENGTH_SHORT).show();
            Map<String, String> map = CommenUtil.object2Map(student);
            NetUtil.getNetData("account/addStudent",map,registerHandler);
        }else if (msg.arg2 == SMSSDK.RESULT_ERROR && msg.arg1 == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
            Toast.makeText(this, "验证码错误！！！", Toast.LENGTH_SHORT).show();
        }
        return true;
    });

    /**
     * 控制重新发送按钮的样式
     */
    Handler handler = new Handler(msg -> {
        if (msg.what == 1){
            resend.setText("重新发送");
            resend.setBackground(getDrawable(R.drawable.style_ellipse_blue));
            resend.setEnabled(true);
            timer.cancel();
        } else {
            resend.setText("重新发送(" + time + "s)");
            resend.setBackground(getDrawable(R.drawable.style_ellipse_gray));
            resend.setEnabled(false);
        }
        return false;
    });

    EventHandler eh=new EventHandler(){
        @Override
        public void afterEvent(int event, int result, Object data) {
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            mHandler.sendMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        ButterKnife.bind(this);
        ActionBarUtil.initActionBar(this,"手机号验证");

        student = (AccountStudent) getIntent().getSerializableExtra("data");
        SMSSDK.registerEventHandler(eh);

//        resend.performClick();

    }

    @OnClick({R.id.confirm_resend,R.id.confirm_confirm})
    public void onClicked(View view){
        switch (view.getId()){
            case R.id.confirm_resend:
                time = 60;
                timer = new Timer(true);
                timer.schedule(timerTask,0,1000);
                SMSSDK.getVerificationCode("86",student.getPhone());
                break;
            case R.id.confirm_confirm:
                if (codeEdit.getText().toString().isEmpty()){
                    Toast.makeText(this, "验证码不可为空", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    SMSSDK.submitVerificationCode("86",student.getPhone(),codeEdit.getText().toString());
                }
                break;
            default:
                break;
        }
    }

    TimerTask timerTask = new TimerTask() {
        public void run() {
            Message message = new Message();
            if (time != 0){
                time--;
                message.what = 0;
            } else {
                message.what = 1;
            }
            handler.sendMessage(message);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }
}
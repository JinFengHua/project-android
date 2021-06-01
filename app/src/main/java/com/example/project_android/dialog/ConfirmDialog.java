package com.example.project_android.dialog;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.project_android.R;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ViewUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

@SuppressLint("NonConstantResourceId")
public class ConfirmDialog extends Dialog {
    @BindView(R.id.resend)
    public TextView resend;//重新发送验证码
    @BindView(R.id.code)
    EditText codeEdit;//输入

    private String phone;

    private int time = 60;//重发计时
    private Timer timer;

    private onConfirmSuccessListener confirmSuccessListener;

    public void setConfirmSuccessListener(onConfirmSuccessListener confirmSuccessListener) {
        this.confirmSuccessListener = confirmSuccessListener;
    }

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

    Handler timerHandler = new Handler(msg -> {
        if (msg.what == 1){
            timer.cancel();
        }
        return false;
    });

    Handler mHandler = new Handler(msg -> {
        if (msg.arg2 == SMSSDK.RESULT_COMPLETE && msg.arg1 == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
            confirmSuccessListener.onConfirmSuccess();
        }else if (msg.arg2 == SMSSDK.RESULT_ERROR && msg.arg1 == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
            Toast.makeText(getContext(), "验证码错误！！！", Toast.LENGTH_SHORT).show();
        }
        return true;
    });

    public ConfirmDialog(Context context, String phone) {
        super(context, R.style.Dialog_Msg);
        this.phone = phone;
        setCanceledOnTouchOutside(false);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit,null);
        ButterKnife.bind(this,view);
        setContentView(view);

        SMSSDK.registerEventHandler(eh);
        resend.performClick();
    }

    @OnClick({R.id.yes,R.id.no,R.id.resend})
    public void onClicked(View view){
        switch (view.getId()){
            case R.id.yes:
                if (codeEdit.getText().toString().length() < 6) {
                    Toast.makeText(view.getContext(), "验证码未填写完整", Toast.LENGTH_SHORT).show();
                    break;
                }
                SMSSDK.submitVerificationCode("86",phone,codeEdit.getText().toString());
                Log.d("NET-->","执行验证");
                break;
            case R.id.no:
                dismiss();
                break;
            case R.id.resend:
                if (time != 0){
                    Toast.makeText(view.getContext(), "重新发送请再等待" + time + "s", Toast.LENGTH_SHORT).show();
                    return;
                }
                /**
                 * 重新发送验证
                 */
                time = 60;
                timer = new Timer(true);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        if (time != 0){
                            time--;
                            message.what = 0;
                        } else {
                            message.what = 1;
                        }
                        timerHandler.sendMessage(message);
                    }
                }, 0, 1000);
                SMSSDK.getVerificationCode("86",phone);
                break;
        }
    }

    public interface onConfirmSuccessListener{
        void onConfirmSuccess();
    }

    @Override
    public void show() {
        super.show();
        ViewUtils.show(getWindow());
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);
        SMSSDK.unregisterEventHandler(eh);
    }
}


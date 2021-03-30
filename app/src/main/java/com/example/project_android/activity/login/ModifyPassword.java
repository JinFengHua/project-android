package com.example.project_android.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project_android.R;
import com.example.project_android.util.CommenUtil;
import com.example.project_android.util.ConfirmDialog;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ViewUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ModifyPassword extends AppCompatActivity {
    @BindView(R.id.modify_password_account)
    EditText accountEdit;
    @BindView(R.id.modify_password_phone)
    EditText phoneEdit;
    @BindView(R.id.modify_password_old_password)
    EditText oldPasswordEdit;
    @BindView(R.id.modify_password_new_password)
    EditText newPasswordEdit;

    private String account;
    private String phone;
    private String newPassword;
    private String oldPassword;
    private int userType = 0;

    private String id;

    private ConfirmDialog confirmDialog;

    private int time = 0;//重发计时
    private Timer timer;

    Handler confirmHandler = new Handler(msg -> {
        if(msg.what == 1){
            //do something
            confirmDialog.show();
            confirmDialog.resend.performClick();
            id = msg.getData().getString("message");
            return true;
        } else {
            Toast.makeText(this, "手机号输入错误", Toast.LENGTH_SHORT).show();
            return false;
        }
    });

    Handler modifyHandler = new Handler(msg -> {
        if(msg.what == 1){
            Toast.makeText(this, msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show();
        }
        finish();
        return false;
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        ButterKnife.bind(this);
        ViewUtils.initActionBar(this,"修改密码");
        SMSSDK.registerEventHandler(eh);
        confirmDialog = createDialog();
    }

    /**
     *点击事件，先判断是否为空，再判断输入手机号是否正确
     * @param v 按钮组件
     */
    @OnClick(R.id.modify_password_submit)
    public void onClicked(View v){
        account = accountEdit.getText().toString();
        phone = phoneEdit.getText().toString();
        oldPassword = oldPasswordEdit.getText().toString();
        newPassword = newPasswordEdit.getText().toString();

        if(account.isEmpty() || !CommenUtil.isPhone(phone) || oldPassword.isEmpty() || newPassword.isEmpty() || userType == 0){
            Toast.makeText(this, "请补全信息", Toast.LENGTH_SHORT).show();
        } else if (!oldPassword.equals(newPassword)){
            Toast.makeText(this, "密码不一致", Toast.LENGTH_SHORT).show();
        } else{
            Map<String,String> map = new HashMap<>();
            map.put("type",String.valueOf(userType));
            map.put("account",account);
            map.put("phone",phone);
            NetUtil.getNetData("account/confirmAccount",map,confirmHandler);
        }
    }

    /**
     * 监控单选结果
    **/
    @OnCheckedChanged({R.id.modify_password_teacher,R.id.modify_password_student})
    public void onRadioCheck(CompoundButton view, boolean ischanged) {
        switch (view.getId()) {
            case R.id.modify_password_teacher:
                if (ischanged) {
                    userType = 1;
                }
                break;
            case R.id.modify_password_student:
                if (ischanged) {
                    userType = 2;
                }
                break;
            default:
                userType = 0;
                break;
        }
    }

    public ConfirmDialog createDialog(){
        ConfirmDialog confirmDialog = new ConfirmDialog(this);
        confirmDialog.setTitle("请输入电话号码");
        confirmDialog.setYesOnclickListener("确定", code -> {
            if (TextUtils.isEmpty(code)) {
                Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            } else {
                //让软键盘隐藏
                ViewUtils.hiddenSoftKey(confirmDialog.et_phone);
                /**
                 * 调用获取验证码方法
                 */
                SMSSDK.submitVerificationCode("86",phone,code);
            }
        });

        confirmDialog.setNoOnclickListener("取消", confirmDialog::dismiss);

        confirmDialog.setResendOnclickListener(() -> {
            if(time != 0){
                Toast.makeText(this, "重新发送请再等待" + time + "s", Toast.LENGTH_SHORT).show();
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
        });

        return confirmDialog;
    }

    /**
     * 处理提交验证码的请求，验证通过发送真正的注册请求，失败则进行提示
     */
    Handler mHandler = new Handler(msg -> {
        if (msg.arg2 == SMSSDK.RESULT_COMPLETE && msg.arg1 == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
            Toast.makeText(this, "验证成功！！！", Toast.LENGTH_SHORT).show();
            Map<String,String> map = new HashMap<>();
            map.put(userType == 1 ? "teacherId" : "studentId",id);
            map.put("password",newPassword);
            NetUtil.getNetData("account/modify"+ (userType == 1 ? "Teacher" : "Student"),map,modifyHandler);
        }else if (msg.arg2 == SMSSDK.RESULT_ERROR && msg.arg1 == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
            Toast.makeText(this, "验证码错误！！！", Toast.LENGTH_SHORT).show();
        }
        return true;
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

    Handler timerHandler = new Handler(msg -> {
        if (msg.what == 1){
            timer.cancel();
        }
        return false;
    });

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }
}
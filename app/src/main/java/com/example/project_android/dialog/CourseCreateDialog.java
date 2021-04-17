package com.example.project_android.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.StringUtils;
import com.example.project_android.R;
import com.example.project_android.util.MyApplication;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ProjectStatic;
import com.example.project_android.util.ViewUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.HashMap;
import java.util.Map;

public class CourseCreateDialog extends Dialog {
    private TextView yes,no,chooseImage,useDefault;
    private EditText nameEdit,introduceEdit;
    public ImageView previewImage;
    public TextView imageState;
    private onChooseClickListener chooseClickListener;

    private String imgPath;
    private LoadingDialog loadingDialog;

    public CourseCreateDialog(@NonNull Context context) {
        super(context, R.style.BottomDialog);
    }

    public interface onChooseClickListener{public void onChooseClick();}

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public void setChooseClickListener(onChooseClickListener chooseClickListener) {
        this.chooseClickListener = chooseClickListener;
    }

    Handler createHandler = new Handler(msg -> {
        if (msg.what == 1){
            this.dismiss();
            loadingDialog.setMessage("课程验证码为：" + msg.getData().getString("data"));
        } else {
            loadingDialog.setMessage(msg.getData().getString("message"));
        }
        loadingDialog.showSingleButton();
        return false;
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_course_create);
        setCanceledOnTouchOutside(false);
        loadingDialog = new LoadingDialog(getContext());

        initView();

        initEvent();

    }

    private void initEvent() {
        no.setOnClickListener(v -> {
            this.dismiss();
        });

        yes.setOnClickListener(v -> {
            //执行创建课程操作
            String name = nameEdit.getText().toString();
            String introduce = introduceEdit.getText().toString();
            if (name.isEmpty() || introduce.isEmpty()){
                Toast.makeText(v.getContext(), "请补全信息", Toast.LENGTH_SHORT).show();
                return;
            } else if (imgPath == null){
                Toast.makeText(v.getContext(), "请选择课程封面", Toast.LENGTH_SHORT).show();
                return;
            }
            SharedPreferences preferences = MyApplication.getContext().getSharedPreferences("localRecord",Context.MODE_PRIVATE);
            String id = preferences.getString("id", "");

            Map<String,String> map = new HashMap<>();
            map.put("teacherId",id);
            map.put("name",name);
            map.put("avatar",imgPath);
            map.put("introduce",introduce);
            NetUtil.getNetData("course/addCourse",map,createHandler);
            loadingDialog.setTitle("创建课程");
            loadingDialog.setMessage(StringUtils.getString(R.string.wait_message));
            loadingDialog.show();
        });

        useDefault.setOnClickListener(v -> {
            imgPath = "image/avatars/course-default.png";
            Toast.makeText(v.getContext(), "使用默认头像", Toast.LENGTH_SHORT).show();
            Picasso.with(v.getContext())
                    .load(ProjectStatic.SERVICE_PATH + imgPath)
                    .error(R.drawable.ic_net_error)
                    .fit().into(previewImage, new Callback() {
                @Override
                public void onSuccess() {
                    imageState.setTextColor(ColorUtils.getColor(R.color.green));
                    imageState.setText("选取成功");
                }

                @Override
                public void onError() {
                    imageState.setTextColor(ColorUtils.getColor(R.color.cancel_red));
                    imageState.setText("网络异常！图片无法加载");
                }
            });
        });

        chooseImage.setOnClickListener(v -> {
            if (chooseClickListener != null){
                chooseClickListener.onChooseClick();
            }
        });

        introduceEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus){
                KeyboardUtils.showSoftInput();
            } else {
                KeyboardUtils.hideSoftInput(v);
            }
        });

        nameEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus){
                KeyboardUtils.showSoftInput();
            } else {
                KeyboardUtils.hideSoftInput(v);
            }
        });
    }

    private void initView() {
        yes = findViewById(R.id.course_register_yes);
        no = findViewById(R.id.course_register_no);
        nameEdit = findViewById(R.id.course_register_name);
        introduceEdit = findViewById(R.id.course_register_introduce);
        chooseImage = findViewById(R.id.course_register_avatar_choose);
        useDefault = findViewById(R.id.course_register_avatar_default);
        previewImage = findViewById(R.id.course_register_avatar_preview);
        imageState = findViewById(R.id.course_register_avatar_state);
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width= ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height= ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }
}

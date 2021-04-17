package com.example.project_android.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.StringUtils;
import com.example.project_android.R;
import com.example.project_android.util.NetUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class CourseModifyDialog extends Dialog {
    @BindView(R.id.dialog_course_name)
    EditText nameEdit;
    @BindView(R.id.dialog_course_introduce)
    EditText introduceEdit;

    private String oldName;
    private String oldIntroduce;
    private Integer courseId;

    private LoadingDialog loadingDialog;

    public CourseModifyDialog(@NonNull Context context,String name,String introduce,Integer courseId) {
        super(context,R.style.Dialog_Msg);
        oldIntroduce = introduce;
        oldName = name;
        this.courseId = courseId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_course_modify, null);
        ButterKnife.bind(this,inflate);
        setContentView(inflate);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        nameEdit.setText(oldName);
        introduceEdit.setText(oldIntroduce);
    }

    @OnClick({R.id.no,R.id.yes})
    public void onClicked(View view){
        switch (view.getId()){
            case R.id.no:
                nameEdit.setText(oldName);
                introduceEdit.setText(oldIntroduce);
                dismiss();
                break;
            case R.id.yes:
                String newName = nameEdit.getText().toString();
                String newIntroduce = introduceEdit.getText().toString();
                if (newName.length() < 1 || newIntroduce.length() < 1){
                    Toast.makeText(view.getContext(), "输入内容为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                Boolean modifyFlag = false;
//                保证map里的值是需要修改的内容
                Map<String, String> map = new HashMap<>();
                map.put("courseId",String.valueOf(courseId));
                if (!newName.equals(oldName)){
                    map.put("courseName",newName);
                    modifyFlag = true;
                }
                if (!newIntroduce.equals(oldIntroduce)){
                    map.put("courseIntroduce",newIntroduce);
                    modifyFlag = true;
                }
                if (modifyFlag) {
                    NetUtil.getNetData("course/modifyCourse", map, new Handler(msg -> {
                        if (msg.what == 1) {
                            dismiss();
                        }
                        loadingDialog.setMessage(msg.getData().getString("message"));
                        loadingDialog.showSingleButton();
                        return false;
                    }));
                    loadingDialog = new LoadingDialog(view.getContext());
                    loadingDialog.setTitle("修改课程信息");
                    loadingDialog.setMessage(StringUtils.getString(R.string.wait_message));
                    loadingDialog.show();
                } else {
                    Toast.makeText(view.getContext(), "内容未修改", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                break;
            default:break;
        }
    }

    public String getName(){
        return nameEdit.getText().toString().length() < 1 ? oldName : nameEdit.getText().toString();
    }

    public String getIntroduce(){
        return introduceEdit.getText().toString().length() < 1 ? oldIntroduce : introduceEdit.getText().toString();
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width= ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height= ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }
}

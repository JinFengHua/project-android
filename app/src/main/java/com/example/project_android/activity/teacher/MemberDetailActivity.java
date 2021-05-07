package com.example.project_android.activity.teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;
import com.example.project_android.R;
import com.example.project_android.dialog.LoadingDialog;
import com.example.project_android.entity.Student;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ProjectStatic;
import com.example.project_android.util.ViewUtils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.smssdk.ui.companent.CircleImageView;

@SuppressLint("NonConstantResourceId")
public class MemberDetailActivity extends AppCompatActivity {
    @BindView(R.id.member_detail_class)
    TextView studentClass;
    @BindView(R.id.member_detail_name)
    TextView studentName;
    @BindView(R.id.member_detail_account)
    TextView studentAccount;
    @BindView(R.id.member_detail_sex)
    TextView studentSex;
    @BindView(R.id.member_detail_phone)
    TextView studentPhone;
    @BindView(R.id.member_detail_email)
    TextView studentEmail;
    @BindView(R.id.member_detail_join_time)
    TextView studentJoin;
    @BindView(R.id.member_detail_face)
    ImageView studentFace;
    @BindView(R.id.member_detail_avatar)
    CircleImageView studentAvatar;
    @BindView(R.id.member_detail_face_prompt)
    TextView prompt;

    private Student student;
    private Integer courseId;

    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail);
        unbinder = ButterKnife.bind(this);
        ViewUtils.initActionBar(this,"学生详情");

        Intent intent = getIntent();
        student = (Student) intent.getExtras().getSerializable("student");
        courseId = (Integer) intent.getIntExtra("courseId",-1);


        initView();
    }

    @OnClick(R.id.member_detail_delete)
    public void onClicked(View view){
        switch (view.getId()){
            case R.id.member_detail_delete:
                Toast.makeText(this, "即将执行删除学生操作", Toast.LENGTH_SHORT).show();
                LoadingDialog dialog = new LoadingDialog(view.getContext());
                dialog.setTitle("警告");
                dialog.setMessage("该操作不可逆，请重复确认");
                dialog.setOnYesClickedListener(new LoadingDialog.OnYesClickedListener() {
                    @Override
                    public void onYesClicked(View v) {
                        dialog.dismiss();
                        LoadingDialog dialog1 = new LoadingDialog(v.getContext());
                        dialog1.setTitle("删除学生");
                        dialog1.setMessage(StringUtils.getString(R.string.wait_message));
                        dialog1.show();
                        Map<String, String> map = new HashMap<>();
                        map.put("courseId",String.valueOf(courseId));
                        map.put("studentId",String.valueOf(student.getStudentId()));
                        NetUtil.getNetData("courseStudent/deleteCourseStudent",map,new Handler(msg -> {
                            if (msg.what == 1){
                                dialog1.setOnDismissListener(dialog2 -> finish());
                            }
                            dialog1.showSingleButton();
                            dialog1.setMessage(msg.getData().getString("message"));
                            return false;
                        }));
                    }
                });
                dialog.show();
                break;
            default:break;
        }
    }

    private void initView() {
        studentName.setText(student.getStudentName());
        studentSex.setText(student.getStudentSex() == true ? "男" : "女");
        studentAccount.setText(student.getStudentAccount());
        studentClass.setText(student.getStudentClass());
        studentPhone.setText(student.getStudentPhone());
        studentEmail.setText(student.getStudentEmail());
        studentJoin.setText(new SimpleDateFormat(ProjectStatic.DATE_FORMAT_DAY, Locale.CHINA).format(student.getJoinTime()));

        Picasso.with(this)
                .load(ProjectStatic.SERVICE_PATH + student.getStudentAvatar())
                .fit()
                .error(R.drawable.ic_net_error)
                .into(studentAvatar);

        if (student.getStudentFace() != null) {
            Picasso.with(this)
                    .load(ProjectStatic.SERVICE_PATH + student.getStudentFace())
                    .fit()
                    .error(R.drawable.ic_net_error)
                    .into(studentFace);
        } else {
            studentFace.setVisibility(View.GONE);
            prompt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
package com.example.project_android.activity.teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_android.R;
import com.example.project_android.entity.Student;
import com.example.project_android.util.ProjectStatic;
import com.example.project_android.util.ViewUtils;
import com.squareup.picasso.Picasso;

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
    @BindView(R.id.member_detail_face)
    ImageView studentFace;
    @BindView(R.id.member_detail_avatar)
    CircleImageView studentAvatar;
    @BindView(R.id.member_detail_face_prompt)
    TextView prompt;


    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail);
        unbinder = ButterKnife.bind(this);
        ViewUtils.initActionBar(this,"学生详情");

        Intent intent = getIntent();
        Student student = (Student) intent.getExtras().getSerializable("student");
        initView(student);
    }

    @OnClick(R.id.member_detail_delete)
    public void onClicked(View view){
        switch (view.getId()){
            case R.id.member_detail_delete:
                Toast.makeText(this, "即将执行删除学生操作", Toast.LENGTH_SHORT).show();
                break;
            default:break;
        }
    }

    private void initView(Student student) {
        studentName.setText(student.getStudentName());
        studentSex.setText(student.getStudentSex() == true ? "男" : "女");
        studentAccount.setText(student.getStudentAccount());
        studentClass.setText(student.getStudentClass());
        studentPhone.setText(student.getStudentPhone());
        studentEmail.setText(student.getStudentEmail());

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
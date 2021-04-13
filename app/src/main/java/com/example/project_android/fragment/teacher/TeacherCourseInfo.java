package com.example.project_android.fragment.teacher;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.project_android.R;
import com.example.project_android.activity.teacher.TeacherCourseDetailViewModel;
import com.example.project_android.dialog.CourseCreateDialog;
import com.example.project_android.dialog.CourseModifyDialog;
import com.example.project_android.entity.CourseList;
import com.example.project_android.util.ProjectStatic;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.ui.companent.CircleImageView;

@SuppressLint("NonConstantResourceId")
public class TeacherCourseInfo extends Fragment {
    @BindView(R.id.teacher_course_info_avatar)
    CircleImageView courseAvatar;
    @BindView(R.id.teacher_course_info_name)
    TextView courseName;
    @BindView(R.id.teacher_course_info_code)
    TextView courseCode;
    @BindView(R.id.teacher_course_info_teacher_name)
    TextView teacherName;
    @BindView(R.id.teacher_course_info_teacher_phone)
    TextView teacherPhone;
    @BindView(R.id.teacher_course_info_teacher_email)
    TextView teacherEmail;
    @BindView(R.id.teacher_course_info_introduce)
    TextView courseIntroduce;

    private TeacherCourseDetailViewModel viewModel;
    private CourseList course;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_info, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(TeacherCourseDetailViewModel.class);
        // TODO: Use the ViewModel
        course = viewModel.getCourse().getValue();
        initView();
    }

    private void initView(){
        Picasso.with(this.getContext())
                .load(ProjectStatic.SERVICE_PATH + course.getCourseAvatar())
                .fit()
                .error(R.drawable.ic_net_error)
                .into(courseAvatar);

        courseName.setText(course.getCourseName());
        courseCode.setText(course.getCourseCode());
        courseIntroduce.setText(course.getCourseIntroduce());
        teacherEmail.setText(course.getTeacherEmail());
        teacherName.setText(course.getTeacherName());
        teacherPhone.setText(course.getTeacherPhone());
    }

    @OnClick({R.id.teacher_course_info_modify,R.id.teacher_course_info_delete})
    public void onClicked(View view){
        switch (view.getId()){
            case R.id.teacher_course_info_modify:
                CourseModifyDialog modifyDialog = new CourseModifyDialog(view.getContext(),course.getCourseName(),course.getCourseIntroduce(),course.getCourseId());
                modifyDialog.setOnDismissListener(dialog -> {
                    courseIntroduce.setText(modifyDialog.getIntroduce());
                    courseName.setText(modifyDialog.getName());
                });
                modifyDialog.show();
                break;
            case R.id.teacher_course_info_delete:

                break;
            default:break;
        }
    }

}
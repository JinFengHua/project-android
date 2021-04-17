package com.example.project_android.fragment.teacher;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.example.project_android.R;
import com.example.project_android.activity.CourseViewModel;
import com.example.project_android.dialog.CourseModifyDialog;
import com.example.project_android.dialog.LoadingDialog;
import com.example.project_android.entity.CourseList;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ProjectStatic;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

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

    private CourseViewModel viewModel;
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
        viewModel = new ViewModelProvider(getActivity()).get(CourseViewModel.class);
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
        teacherEmail.setText(course.getUserEmail());
        teacherName.setText(course.getUesrName());
        teacherPhone.setText(course.getUserPhone());
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
                LoadingDialog dialog = new LoadingDialog(view.getContext());
                dialog.setTitle("警告");
                dialog.setMessage("该操作不可逆，请重复确认");
                dialog.setOnYesClickedListener(v -> {
                    dialog.dismiss();
                    LoadingDialog dialog1 = new LoadingDialog(v.getContext());
                    dialog1.setTitle("删除课程");
                    dialog1.setMessage(StringUtils.getString(R.string.wait_message));
                    dialog1.show();
                    Map<String, String> map = new HashMap<>();
                    map.put("id",String.valueOf(course.getCourseId()));
                    NetUtil.getNetData("course/deleteCourse",map,new Handler(msg -> {
                        if (msg.what == 1){
                            dialog1.setOnDismissListener(dialog2 -> getActivity().finish());
                        }
                        dialog1.showSingleButton();
                        dialog1.setMessage(msg.getData().getString("message"));
                        return false;
                    }));
                });
                dialog.show();
                break;
            default:break;
        }
    }

}
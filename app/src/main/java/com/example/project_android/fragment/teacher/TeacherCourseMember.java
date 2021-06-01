package com.example.project_android.fragment.teacher;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.project_android.R;
import com.example.project_android.activity.CourseViewModel;
import com.example.project_android.adapter.MemberAdapter;
import com.example.project_android.util.MyApplication;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ViewUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class TeacherCourseMember extends Fragment {
    @BindView(R.id.refresh_teacher_member)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.content_not_found_layout)
    LinearLayout notFoundLayout;

    private TeacherCourseMemberViewModel mViewModel;
    private CourseViewModel viewModel;

    Handler getStudentHandler = new Handler(msg -> {
        if (msg.what == 1){
            mViewModel.updateStudentList(msg.getData().getString("data"));
        } else {
            Toast.makeText(MyApplication.getContext(), "请求失败", Toast.LENGTH_SHORT).show();
        }
        refreshLayout.setRefreshing(false);
        return false;
    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_teacher_course_member, container, false);
        ButterKnife.bind(this,inflate);
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(TeacherCourseMemberViewModel.class);
        viewModel = new ViewModelProvider(getActivity()).get(CourseViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.getStudentList().observe(getViewLifecycleOwner(),students -> {
            if (students.size() < 1){
                notFoundLayout.setVisibility(View.VISIBLE);
            } else {
                notFoundLayout.setVisibility(View.GONE);
                ViewUtils.setRecycler(getActivity(), R.id.recycler_teacher_member_list, new MemberAdapter(students, viewModel.getCourse().getValue().getCourseId()));
            }
        });
        Map<String,String> map = new HashMap<>();
        map.put("courseId",String.valueOf(viewModel.getCourse().getValue().getCourseId()));
        NetUtil.getNetData("courseStudent/findAllByCourseId",map,getStudentHandler);
        refreshLayout.setColorSchemeColors(ViewUtils.getRefreshColor());
        refreshLayout.setOnRefreshListener(() -> NetUtil.getNetData("courseStudent/findAllByCourseId",map,getStudentHandler));
    }

}
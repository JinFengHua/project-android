package com.example.project_android.fragment.teacher;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.project_android.R;
import com.example.project_android.activity.teacher.TeacherCourseDetailViewModel;
import com.example.project_android.adapter.LeaveAdapter;
import com.example.project_android.adapter.MemberAdapter;
import com.example.project_android.entity.Student;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ViewUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherCourseLeave extends Fragment {

    private TeacherCourseLeaveViewModel mViewModel;
    private TeacherCourseDetailViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_teacher_course_leave, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TeacherCourseLeaveViewModel.class);
        viewModel = new ViewModelProvider(getActivity()).get(TeacherCourseDetailViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.getLeaveList().observe(getViewLifecycleOwner(),leaves -> ViewUtils.setRecycler(getActivity(),R.id.recycler_teacher_leave_list,new LeaveAdapter(leaves)));
        Integer courseId = viewModel.getCourse().getValue().getCourseId();

        Map<String, String> map = new HashMap<>();
        map.put("courseId",String.valueOf(courseId));
        NetUtil.getNetData("leave/findAllLeave",map,new Handler(msg -> {
            if (msg.what == 1){
                String data = msg.getData().getString("data");
                mViewModel.updateLeaveList(data);
            }
            Toast.makeText(getContext(), msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
            return false;
        }));
    }

}
package com.example.project_android.fragment.teacher;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import com.example.project_android.adapter.AttendListAdapter;
import com.example.project_android.entity.AttendList;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ViewUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TeacherCourseAttend extends Fragment {

    private TeacherCourseAttendViewModel mViewModel;

    Unbinder unbinder;

    Handler attendListHandler = new Handler(msg -> {
        if (msg.what == 1){
            mViewModel.updateAttendList(msg.getData().getString("data"));
        }
        Toast.makeText(getContext(), msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
        return false;
    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_course_attend, container, false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Map<String, String> map = new HashMap<>();
        NetUtil.getNetData("attend/findAttendByMap",map,attendListHandler);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TeacherCourseAttendViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.getAttendLists().observe(getViewLifecycleOwner(), attendLists -> ViewUtils.setRecycler(getActivity(),R.id.recycler_attend_list,new AttendListAdapter(attendLists)));
    }

}
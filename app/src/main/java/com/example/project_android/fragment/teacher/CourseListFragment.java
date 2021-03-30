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
import android.widget.Toast;

import com.example.project_android.R;
import com.example.project_android.adapter.CourseListAdapter;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ViewUtils;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class CourseListFragment extends Fragment {
    private CourseListViewModel viewModel;

    Handler courseListHandler = new Handler(msg -> {
        if (msg.what == 1){
            Toast.makeText(getActivity(), msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
            viewModel.updateCourses(msg.getData().getString("data"));
        }
        return false;
    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_list, container, false);
        ButterKnife.bind(this,view);

//        进行网络请求返回所有与用户相关的课程
        NetUtil.getNetData("course/findCourseByMap",new HashMap<>(),courseListHandler);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CourseListViewModel.class);
        // TODO: Use the ViewModel
        viewModel.getCourseLists().observe(getViewLifecycleOwner(), courseLists -> ViewUtils.setRecycler(getActivity(),R.id.recycler_course_list,new CourseListAdapter(courseLists)));
    }

    @OnClick(R.id.course_list_create_course)
    public void onClicked(View view){
        switch (view.getId()){
            case R.id.course_list_create_course:
                //点击弹出创建课程的页面
                break;
            default:break;
        }
    }

}
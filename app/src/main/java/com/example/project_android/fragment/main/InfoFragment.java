package com.example.project_android.fragment.main;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project_android.R;
import com.example.project_android.adapter.CourseListAdapter;
import com.example.project_android.entity.CourseList;
import com.example.project_android.util.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoFragment extends Fragment {
    private final static String json = "[{\"courseId\":2,\"teacherId\":1,\"courseName\":\"云计算\",\"courseAvatar\":\"faceimages/face.jpg\",\"courseIntroduce\":\"云计算云计算云计算云计算云计算云计算\",\"courseCode\":\"647904\"},{\"courseId\":4,\"teacherId\":1,\"courseName\":\"数据库\",\"courseAvatar\":\"faceimages/face.jpg\",\"courseIntroduce\":\"云计算云计算云计算云计算云计算云计算\",\"courseCode\":\"322989\"},{\"courseId\":5,\"teacherId\":1,\"courseName\":\"计算机原理\",\"courseAvatar\":\"faceimages/face.jpg\",\"courseIntroduce\":\"云计算云计算云计算云计算云计算云计算\",\"courseCode\":\"353964\"}]";

    private InfoViewModel viewModel;

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_info, container, false);
        ButterKnife.bind(this,inflate);
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(InfoViewModel.class);
        // TODO: Use the ViewModel
        }

}
package com.example.project_android.fragment.teacher;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project_android.R;
import com.example.project_android.activity.teacher.TeacherCourseDetailViewModel;

public class TeacherCourseInfo extends Fragment {

    private TeacherCourseInfoViewModel mViewModel;
    private TeacherCourseDetailViewModel viewModel;

    public static TeacherCourseInfo newInstance() {
        return new TeacherCourseInfo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_info, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TeacherCourseInfoViewModel.class);
        viewModel = new ViewModelProvider(getActivity()).get(TeacherCourseDetailViewModel.class);
        // TODO: Use the ViewModel
    }

}
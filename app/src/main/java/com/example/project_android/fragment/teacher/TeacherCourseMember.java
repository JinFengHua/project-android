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
//    private String data = "[{\"studentId\":1,\"studentAccount\":\"000001\",\"studentPassword\":\"000000\",\"studentName\":\"江道宽\",\"studentSex\":true,\"studentAvatar\":\"/image/avatars/default.jpg\",\"studentClass\":\"0814171\",\"studentFace\":null,\"studentPhone\":\"13137749525\",\"studentEmail\":\"2116161338@qq.com\",\"records\":null},{\"studentId\":2,\"studentAccount\":\"000011\",\"studentPassword\":\"000000\",\"studentName\":\"陈庆旭\",\"studentSex\":true,\"studentAvatar\":\"/image/avatars/default.jpg\",\"studentClass\":\"0814171\",\"studentFace\":null,\"studentPhone\":\"13137749525\",\"studentEmail\":\"2116161338@qq.com\",\"records\":null},{\"studentId\":3,\"studentAccount\":\"000111\",\"studentPassword\":\"000000\",\"studentName\":\"郭军甫\",\"studentSex\":true,\"studentAvatar\":\"/image/avatars/default.jpg\",\"studentClass\":\"0814171\",\"studentFace\":null,\"studentPhone\":\"13137749525\",\"studentEmail\":\"2116161338@qq.com\",\"records\":null},{\"studentId\":4,\"studentAccount\":\"001111\",\"studentPassword\":\"000000\",\"studentName\":\"闫新宇\",\"studentSex\":true,\"studentAvatar\":\"/image/avatars/default.jpg\",\"studentClass\":\"0814171\",\"studentFace\":null,\"studentPhone\":\"13137749525\",\"studentEmail\":\"2116161338@qq.com\",\"records\":null},{\"studentId\":5,\"studentAccount\":\"011111\",\"studentPassword\":\"000000\",\"studentName\":\"段浩琦\",\"studentSex\":true,\"studentAvatar\":\"/image/avatars/default.jpg\",\"studentClass\":\"0814171\",\"studentFace\":null,\"studentPhone\":\"13137749525\",\"studentEmail\":\"2116161338@qq.com\",\"records\":null},{\"studentId\":6,\"studentAccount\":\"111111\",\"studentPassword\":\"000000\",\"studentName\":\"崔露阳\",\"studentSex\":true,\"studentAvatar\":\"/image/avatars/default.jpg\",\"studentClass\":\"0814171\",\"studentFace\":null,\"studentPhone\":\"13137749525\",\"studentEmail\":\"2116161338@qq.com\",\"records\":null}]";
    @BindView(R.id.refresh_teacher_member)
    SwipeRefreshLayout refreshLayout;

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
        mViewModel.getStudentList().observe(getViewLifecycleOwner(),students -> ViewUtils.setRecycler(getActivity(),R.id.recycler_teacher_member_list,new MemberAdapter(students,viewModel.getCourse().getValue().getCourseId())));
//        mViewModel.updateStudentList(data);
        Map<String,String> map = new HashMap<>();
        map.put("courseId",String.valueOf(viewModel.getCourse().getValue().getCourseId()));
        NetUtil.getNetData("courseStudent/findAllByCourseId",map,getStudentHandler);
        refreshLayout.setColorSchemeColors(ViewUtils.getRefreshColor());
        refreshLayout.setOnRefreshListener(() -> NetUtil.getNetData("courseStudent/findAllByCourseId",map,getStudentHandler));
    }

}
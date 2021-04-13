package com.example.project_android.fragment.teacher;

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
import com.example.project_android.activity.teacher.TeacherCourseDetailViewModel;
import com.example.project_android.adapter.MemberAdapter;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ViewUtils;

import java.util.HashMap;
import java.util.Map;

public class TeacherCourseMember extends Fragment {
    private String data = "[{\"leaveId\":2,\"studentId\":1,\"courseId\":1,\"leaveTime\":\"2021-04-10T02:36:24.000+00:00\",\"backTime\":\"2021-04-21T07:36:24.000+00:00\",\"leaveReason\":\"不想上了\\\"\",\"approvalTime\":\"2021-04-11T04:36:24.000+00:00\",\"approvalResult\":2,\"approvalRemark\":\"准了\",\"student\":{\"studentId\":1,\"studentAccount\":\"000001\",\"studentPassword\":\"000000\",\"studentName\":\"江道宽\",\"studentSex\":true,\"studentAvatar\":\"/image/avatars/user-default.png\",\"studentClass\":\"0814171\",\"studentFace\":null,\"studentPhone\":\"13137749525\",\"studentEmail\":\"2116161338@qq.com\",\"records\":null,\"leaves\":null}},{\"leaveId\":3,\"studentId\":1,\"courseId\":1,\"leaveTime\":\"2021-04-10T02:36:24.000+00:00\",\"backTime\":\"2021-04-10T07:36:24.000+00:00\",\"leaveReason\":\"时间已经过了\",\"approvalTime\":null,\"approvalResult\":0,\"approvalRemark\":null,\"student\":{\"studentId\":1,\"studentAccount\":\"000001\",\"studentPassword\":\"000000\",\"studentName\":\"江道宽\",\"studentSex\":true,\"studentAvatar\":\"/image/avatars/user-default.png\",\"studentClass\":\"0814171\",\"studentFace\":null,\"studentPhone\":\"13137749525\",\"studentEmail\":\"2116161338@qq.com\",\"records\":null,\"leaves\":null}},{\"leaveId\":4,\"studentId\":2,\"courseId\":1,\"leaveTime\":\"2021-04-20T02:36:24.000+00:00\",\"backTime\":\"2021-04-30T07:36:24.000+00:00\",\"leaveReason\":\"还没开始的\",\"approvalTime\":null,\"approvalResult\":0,\"approvalRemark\":null,\"student\":{\"studentId\":2,\"studentAccount\":\"000011\",\"studentPassword\":\"000000\",\"studentName\":\"闫新宇\",\"studentSex\":true,\"studentAvatar\":\"/image/avatars/user-default.png\",\"studentClass\":\"0814171\",\"studentFace\":null,\"studentPhone\":\"13137749525\",\"studentEmail\":\"2116161338@qq.com\",\"records\":null,\"leaves\":null}}]";

    private TeacherCourseMemberViewModel mViewModel;
    private TeacherCourseDetailViewModel viewModel;

    Handler getStudentHandler = new Handler(msg -> {
        if (msg.what == 1){
            mViewModel.updateStudentList(msg.getData().getString("data"));
        } else {
            Toast.makeText(getContext(), "请求失败", Toast.LENGTH_SHORT).show();
        }
        return false;
    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_teacher_course_member, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TeacherCourseMemberViewModel.class);
        viewModel = new ViewModelProvider(getActivity()).get(TeacherCourseDetailViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.getStudentList().observe(getViewLifecycleOwner(),students -> ViewUtils.setRecycler(getActivity(),R.id.recycler_teacher_member_list,new MemberAdapter(students)));
        mViewModel.updateStudentList(data);
        Map<String,String> map = new HashMap<>();
        map.put("courseId",String.valueOf(viewModel.getCourse().getValue().getCourseId()));
//        NetUtil.getNetData("courseStudent/findAllByCourseId",map,getStudentHandler);
    }

}
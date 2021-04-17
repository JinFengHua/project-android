package com.example.project_android.fragment.teacher;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
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
import com.example.project_android.adapter.LeaveAdapter;
import com.example.project_android.entity.Leave;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ViewUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class TeacherCourseLeave extends Fragment {
//    String data = "[{\"leaveId\":2,\"studentId\":1,\"courseId\":1,\"leaveTime\":\"2021-04-10T02:36:24.000+00:00\",\"backTime\":\"2021-04-21T07:36:24.000+00:00\",\"leaveReason\":\"不想上了\\\"\",\"approvalTime\":\"2021-04-11T04:36:24.000+00:00\",\"approvalResult\":2,\"approvalRemark\":\"准了\",\"student\":{\"studentId\":1,\"studentAccount\":\"000001\",\"studentPassword\":\"000000\",\"studentName\":\"江道宽\",\"studentSex\":true,\"studentAvatar\":\"/image/avatars/user-default.png\",\"studentClass\":\"0814171\",\"studentFace\":null,\"studentPhone\":\"13137749525\",\"studentEmail\":\"2116161338@qq.com\",\"records\":null,\"leaves\":null}},{\"leaveId\":3,\"studentId\":1,\"courseId\":1,\"leaveTime\":\"2021-04-10T02:36:24.000+00:00\",\"backTime\":\"2021-04-10T07:36:24.000+00:00\",\"leaveReason\":\"时间已经过了\",\"approvalTime\":null,\"approvalResult\":0,\"approvalRemark\":null,\"student\":{\"studentId\":1,\"studentAccount\":\"000001\",\"studentPassword\":\"000000\",\"studentName\":\"江道宽\",\"studentSex\":true,\"studentAvatar\":\"/image/avatars/user-default.png\",\"studentClass\":\"0814171\",\"studentFace\":null,\"studentPhone\":\"13137749525\",\"studentEmail\":\"2116161338@qq.com\",\"records\":null,\"leaves\":null}},{\"leaveId\":4,\"studentId\":2,\"courseId\":1,\"leaveTime\":\"2021-04-20T02:36:24.000+00:00\",\"backTime\":\"2021-04-30T07:36:24.000+00:00\",\"leaveReason\":\"还没开始的\",\"approvalTime\":null,\"approvalResult\":0,\"approvalRemark\":null,\"student\":{\"studentId\":2,\"studentAccount\":\"000011\",\"studentPassword\":\"000000\",\"studentName\":\"闫新宇\",\"studentSex\":true,\"studentAvatar\":\"/image/avatars/user-default.png\",\"studentClass\":\"0814171\",\"studentFace\":null,\"studentPhone\":\"13137749525\",\"studentEmail\":\"2116161338@qq.com\",\"records\":null,\"leaves\":null}}]";
    @BindView(R.id.refresh_teacher_leave)
    SwipeRefreshLayout refreshLayout;

    private TeacherCourseLeaveViewModel mViewModel;
    private CourseViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_teacher_course_leave, container, false);
        ButterKnife.bind(this,inflate);
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TeacherCourseLeaveViewModel.class);
        viewModel = new ViewModelProvider(getActivity()).get(CourseViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.getLeaveList().observe(getViewLifecycleOwner(), new Observer<List<Leave>>() {
            @Override
            public void onChanged(List<Leave> leaves) {
                ViewUtils.setRecycler(getActivity(),R.id.recycler_teacher_leave_list,new LeaveAdapter(leaves));
            }
        });
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

        refreshLayout.setColorSchemeColors(ViewUtils.getRefreshColor());
        refreshLayout.setOnRefreshListener(() -> NetUtil.getNetData("leave/findAllLeave",map,new Handler(msg -> {
            if (msg.what == 1){
                String data = msg.getData().getString("data");
                mViewModel.updateLeaveList(data);
            }
            Toast.makeText(getContext(), msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
            refreshLayout.setRefreshing(false);
            return false;
        })));
//        mViewModel.updateLeaveList(data);
    }

}
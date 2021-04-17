package com.example.project_android.fragment.teacher;

import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.blankj.utilcode.util.PermissionUtils;
import com.example.project_android.R;
import com.example.project_android.activity.CourseViewModel;
import com.example.project_android.adapter.AttendListAdapter;
import com.example.project_android.dialog.AttendCreateDialog;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

@SuppressLint("NonConstantResourceId")
public class TeacherCourseAttend extends Fragment {
//    String data = "[{\"attendId\":4,\"courseId\":2,\"attendStart\":\"2021-03-16T07:36:24.000+00:00\",\"attendEnd\":\"2021-03-16T07:38:05.000+00:00\",\"attendLongitude\":113.187315,\"attendLatitude\":33.780052,\"attendLocation\":\"计算机数据与科学学院\"},{\"attendId\":5,\"courseId\":4,\"attendStart\":\"2021-04-07T00:36:24.000+00:00\",\"attendEnd\":\"2021-04-07T07:38:05.000+00:00\",\"attendLongitude\":113.187315,\"attendLatitude\":33.780052,\"attendLocation\":\"河南城建学院二区二楼\"},{\"attendId\":6,\"courseId\":2,\"attendStart\":\"2021-04-07T00:36:24.000+00:00\",\"attendEnd\":\"2021-04-07T07:38:05.000+00:00\",\"attendLongitude\":113.187315,\"attendLatitude\":33.780052,\"attendLocation\":\"河南城建学院二区二楼\"},{\"attendId\":7,\"courseId\":2,\"attendStart\":\"2021-04-08T00:36:24.000+00:00\",\"attendEnd\":\"2021-04-08T07:38:05.000+00:00\",\"attendLongitude\":113.187315,\"attendLatitude\":33.780052,\"attendLocation\":\"河南城建学院二区二楼\"}]";
    @BindView(R.id.refresh_teacher_attend)
    SwipeRefreshLayout refreshLayout;
    private TeacherCourseAttendViewModel mViewModel;
    private CourseViewModel viewModel;

//    创建新考勤任务的对话框
    private AttendCreateDialog attendCreateDialog;

    Unbinder unbinder;

    Handler attendListHandler = new Handler(msg -> {
        if (msg.what == 1){
            mViewModel.updateAttendList(msg.getData().getString("data"));
        }
        Toast.makeText(getContext(), msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
        refreshLayout.setRefreshing(false);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TeacherCourseAttendViewModel.class);
        viewModel = new ViewModelProvider(getActivity()).get(CourseViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.getAttendLists().observe(getViewLifecycleOwner(), attendLists -> ViewUtils.setRecycler(getActivity(),R.id.recycler_attend_list,new AttendListAdapter(attendLists)));

        Map<String, String> map = new HashMap<>();
        map.put("course_id",String.valueOf(viewModel.getCourse().getValue().getCourseId()));
        NetUtil.getNetData("attend/findAttendByMap",map,attendListHandler);
        refreshLayout.setColorSchemeColors(ViewUtils.getRefreshColor());
        refreshLayout.setOnRefreshListener(() -> {
            NetUtil.getNetData("attend/findAttendByMap",map,attendListHandler);
        });
//        mViewModel.updateAttendList(data);
    }

    @OnClick(R.id.attend_create_button)
    public void onClicked(View view){
        switch (view.getId()){
            case R.id.attend_create_button:
                attendCreateDialog = new AttendCreateDialog(getContext(),viewModel.getCourse().getValue().getCourseId());
                attendCreateDialog.setChooseClickListener(() -> {
//                    请求权限进入地图activity
                    List<String> permissionList = new ArrayList<>();
                    if (!PermissionUtils.isGranted(Manifest.permission.ACCESS_FINE_LOCATION)){
                        permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                    }
                    if (!PermissionUtils.isGranted(Manifest.permission.READ_PHONE_STATE)){
                        permissionList.add(Manifest.permission.READ_PHONE_STATE);
                    }
                    if (!PermissionUtils.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                    if (!permissionList.isEmpty()){
                        String[] permissions = permissionList.toArray(new String[permissionList.size()]);
                        PermissionUtils.permission(permissions).request();
                    } else {
                        Intent intent = new Intent("com.example.project_android.activity.teacher.MapChoose");
                        startActivityForResult(intent,1);
                    }
                });
                attendCreateDialog.show();
                break;
            default:break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK) {
                    if (data.getStringExtra("location") != null) {
                        attendCreateDialog.updateLocation(data.getStringExtra("location"));
                        attendCreateDialog.setLatitude(data.getDoubleExtra("latitude",0));
                        attendCreateDialog.setLongitude(data.getDoubleExtra("longitude",0));
                    } else {
                        attendCreateDialog.updateLocation("未选择坐标");
                    }
                }
                break;
            default:break;
        }
    }
}
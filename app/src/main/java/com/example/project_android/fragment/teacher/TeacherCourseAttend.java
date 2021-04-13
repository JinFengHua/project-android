package com.example.project_android.fragment.teacher;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
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

import com.blankj.utilcode.util.PermissionUtils;
import com.example.project_android.R;
import com.example.project_android.activity.teacher.MapChoose;
import com.example.project_android.activity.teacher.TeacherCourseDetailViewModel;
import com.example.project_android.adapter.AttendListAdapter;
import com.example.project_android.dialog.AttendCreateDialog;
import com.example.project_android.entity.AttendList;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

@SuppressLint("NonConstantResourceId")
public class TeacherCourseAttend extends Fragment {
    private TeacherCourseAttendViewModel mViewModel;
    private TeacherCourseDetailViewModel viewModel;

//    创建新考勤任务的对话框
    private AttendCreateDialog attendCreateDialog;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TeacherCourseAttendViewModel.class);
        viewModel = new ViewModelProvider(getActivity()).get(TeacherCourseDetailViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.getAttendLists().observe(getViewLifecycleOwner(), attendLists -> ViewUtils.setRecycler(getActivity(),R.id.recycler_attend_list,new AttendListAdapter(attendLists)));
//        mViewModel.updateAttendList(data);
        NetUtil.getNetData("attend/findAttendByMap",new HashMap<>(),attendListHandler);
    }

    @OnClick(R.id.attend_create_attend)
    public void onClicked(View view){
        switch (view.getId()){
            case R.id.attend_create_attend:
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
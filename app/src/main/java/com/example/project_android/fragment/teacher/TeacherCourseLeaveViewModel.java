package com.example.project_android.fragment.teacher;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.project_android.entity.Leave;

import java.util.ArrayList;
import java.util.List;

public class TeacherCourseLeaveViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<List<Leave>> leaveList;

    public MutableLiveData<List<Leave>> getLeaveList() {
        if (leaveList == null){
            leaveList = new MutableLiveData<>();
        }
        return leaveList;
    }

    public void updateLeaveList(String s){
        List<Leave> leaves = new ArrayList<>();
        Leave leave = new Leave();
        JSONArray array = JSON.parseArray(s);
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            leave.setLeaveId(object.getInteger("leaveId"));
            leave.setLeaveTime(object.getTimestamp("leaveTime"));
            leave.setBackTime(object.getTimestamp("backTime"));
            leave.setLeaveReason(object.getString("leaveReason"));

            leave.setApprovalTime(object.getTimestamp("approvalTime"));
            leave.setApprovalResult(object.getInteger("approvalResult"));
            leave.setApprovalRemark(object.getString("approvalRemark"));

            JSONObject student = object.getJSONObject("student");
            leave.setStudentId(student.getInteger("studentId"));
            leave.setStudentAccount(student.getString("studentAccount"));
            leave.setStudentAvatar(student.getString("studentAvatar"));
            leave.setStudentName(student.getString("studentName"));
            leaves.add(leave);
        }
        leaveList.setValue(leaves);
    }
}
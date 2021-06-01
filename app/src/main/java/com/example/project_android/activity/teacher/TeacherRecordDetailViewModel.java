package com.example.project_android.activity.teacher;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.project_android.entity.AttendList;
import com.example.project_android.entity.Record;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TeacherRecordDetailViewModel extends ViewModel {
    private MutableLiveData<List<Record>> recordList;

    public MutableLiveData<List<Record>> getAttendDetailList() {
        if (recordList == null){
            recordList = new MutableLiveData<>();
        }
        return recordList;
    }

    public void setRecordList(String s){
//        创建列表实例
        List<Record> lists = new ArrayList<>();
        Record record;
        JSONArray objects = JSONObject.parseArray(s);
        for (int i = 0; i < objects.size(); i++) {
            JSONObject object = (JSONObject) objects.get(i);
            JSONObject student = JSON.parseObject(object.getString("student"));
            record = new Record(student.getString("studentAvatar"),object.getTimestamp("recordTime"),
                    student.getString("studentName"),student.getString("studentAccount"),
                    object.getString("recordResult"),object.getString("recordLocation"));
            record.setRecordPhoto(object.getString("recordPhoto"));
            record.setAttendId(object.getString("attendId"));
            record.setStudentId(student.getString("studentId"));
            lists.add(record);
        }
        recordList.setValue(lists);
    }

    public void updateRecordList(String s,int type){
        List<Record> lists = new ArrayList<>();
        Record record;
        JSONArray objects = JSONObject.parseArray(s);
        for (int i = 0; i < objects.size(); i++) {
            JSONObject object = (JSONObject) objects.get(i);
            if (object.getInteger("recordResult") != type){
                continue;
            }
            JSONObject student = JSON.parseObject(object.getString("student"));
            record = new Record(student.getString("studentAvatar"),object.getTimestamp("recordTime"),
                    student.getString("studentName"),student.getString("studentAccount"),
                    object.getString("recordResult"),object.getString("recordLocation"));
            record.setAttendId(object.getString("attendId"));
            record.setStudentId(student.getString("studentId"));
            lists.add(record);
        }
         recordList.setValue(lists);
    }
}

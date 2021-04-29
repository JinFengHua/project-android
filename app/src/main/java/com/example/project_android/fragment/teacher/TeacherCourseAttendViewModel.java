package com.example.project_android.fragment.teacher;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.project_android.entity.AttendList;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TeacherCourseAttendViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<List<AttendList>> attendLists;

    public MutableLiveData<List<AttendList>> getAttendLists() {
        if (attendLists == null){
            attendLists = new MutableLiveData<>();
        }
        return attendLists;
    }

    public void updateAttendList(String s){
        List<AttendList> lists = new ArrayList<>();
        AttendList attendList;
        JSONArray objects = JSONObject.parseArray(s);
        for (int i = 0; i < objects.size(); i++) {
            JSONObject object = (JSONObject) objects.get(i);
            Timestamp start = new Timestamp(object.getTimestamp("attendStart").getTime() + 8000 * 3600);
            Timestamp end =  new Timestamp(object.getTimestamp("attendEnd").getTime() + 8000 * 3600);

            Timestamp current = new Timestamp(System.currentTimeMillis());
            String state = "进行中";
            if (current.before(start)){
                state = "未开始";
            } else if (current.after(end)){
                state = "已结束";
            }
            attendList = new AttendList(object.getInteger("attendId"),object.getString("courseId"),start,end,
                    object.getDouble("attendLongitude"),object.getDouble("attendLatitude"),
                    object.getString("attendLocation"),state);
            lists.add(attendList);
        }
        attendLists.setValue(lists);
    }
}
package com.example.project_android.fragment.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.project_android.entity.CourseList;
import com.example.project_android.util.ProjectStatic;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CourseListViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<List<CourseList>> courseLists;

    public MutableLiveData<List<CourseList>> getCourseLists() {
        if (courseLists == null){
            courseLists = new MutableLiveData<>();
        }
        return courseLists;
    }

    //    更新recyclerview列表
    public void updateCourses(String s){
        List<CourseList> lists = new ArrayList<>();
        CourseList courseList;
        JSONArray objects = JSONObject.parseArray(s);
        for (int i = 0; i < objects.size(); i++) {
            JSONObject o = (JSONObject) objects.get(i);
            JSONObject teacher = JSONObject.parseObject(o.getString("teacher"));
            courseList = new CourseList(o.getInteger("courseId"),o.getString("teacherId"),
                    teacher.getString("teacherName"),o.getString("courseName"),
                    o.getString("courseIntroduce"),o.getString("courseCode"),
                    o.getString("courseAvatar"));
            courseList.setUserEmail(teacher.getString("teacherEmail"));
            courseList.setUserPhone(teacher.getString("teacherPhone"));
            courseList.setJoinTime(o.getTimestamp("joinTime"));
            lists.add(courseList);
        }
        courseLists.setValue(lists);
    }

}
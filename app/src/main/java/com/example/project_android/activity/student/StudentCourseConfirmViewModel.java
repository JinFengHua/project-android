package com.example.project_android.activity.student;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.project_android.entity.CourseList;

public class StudentCourseConfirmViewModel extends ViewModel {
    private MutableLiveData<CourseList> course;

    public MutableLiveData<CourseList> getCourse() {
        if (course == null){
            course = new MutableLiveData<>();
        }
        return course;
    }

    public void updateCourse(String s){
        JSONObject o = JSON.parseObject(s);
        JSONObject teacher = JSONObject.parseObject(o.getString("teacher"));
        CourseList courseList = new CourseList(o.getInteger("courseId"),o.getString("teacherId"),
                teacher.getString("teacherName"),o.getString("courseName"),
                o.getString("courseIntroduce"),o.getString("courseCode"),
                o.getString("courseAvatar"));
        courseList.setUserEmail(teacher.getString("teacherEmail"));
        courseList.setUserPhone(teacher.getString("teacherPhone"));
        course.setValue(courseList);
    }
}

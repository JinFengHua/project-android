package com.example.project_android.fragment.teacher;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.project_android.entity.Student;

import java.util.ArrayList;
import java.util.List;

public class TeacherCourseMemberViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<List<Student>> studentList;

    public MutableLiveData<List<Student>> getStudentList() {
        if (studentList == null){
            studentList = new MutableLiveData<>();
        }
        return studentList;
    }

    public void updateStudentList(String s){
        List<Student> lists = new ArrayList<>();
        Student student;
        JSONArray object = JSONArray.parseArray(s);
        for (int i = 0; i < object.size(); i++) {
            JSONObject courseStudent = object.getJSONObject(i);
            JSONObject stu = courseStudent.getJSONObject("student");
            student = new Student(stu.getInteger("studentId"),stu.getString("studentAccount"),stu.getString("studentPassword"),stu.getString("studentName"),stu.getBoolean("studentSex"),
                    stu.getString("studentAvatar"),stu.getString("studentClass"),stu.getString("studentFace"),stu.getString("studentPhone"),stu.getString("studentEmail"));
            student.setJoinTime(courseStudent.getTimestamp("joinTime"));

            lists.add(student);
        }
        studentList.setValue(lists);
    }
}
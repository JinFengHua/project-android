package com.example.project_android.activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.project_android.entity.CourseList;

public class CourseViewModel extends ViewModel {
    private MutableLiveData<CourseList> course;

    public MutableLiveData<CourseList> getCourse() {
        if (course == null){
            course = new MutableLiveData<>();
        }
        return course;
    }

    public void setCourse(CourseList courseList){
        course.setValue(courseList);
    }
}

package com.example.project_android.entity;

import java.io.Serializable;

public class CourseList implements Serializable {
    private Integer courseId;
    private String courseTeacherId;
    private String courseTeacherName;
    private String courseName;
    private String courseIntroduce;
    private String courseCode;
    private String courseAvatar;

    public CourseList(Integer courseId, String courseTeacherId, String courseTeacherName, String courseName, String courseIntroduce, String courseCode, String courseAvatar) {
        this.courseId = courseId;
        this.courseTeacherId = courseTeacherId;
        this.courseTeacherName = courseTeacherName;
        this.courseName = courseName;
        this.courseIntroduce = courseIntroduce;
        this.courseCode = courseCode;
        this.courseAvatar = courseAvatar;
    }

    public String getCourseAvatar() {
        return courseAvatar;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public String getCourseTeacherId() {
        return courseTeacherId;
    }

    public String getCourseTeacherName() {
        return courseTeacherName;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseIntroduce() {
        return courseIntroduce;
    }

    public String getCourseCode() {
        return courseCode;
    }
}

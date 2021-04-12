package com.example.project_android.entity;

import java.io.Serializable;

public class CourseList implements Serializable {
    private Integer courseId;
    private String teacherId;
    private String teacherName;
    private String teacherPhone;
    private String teacherEmail;
    private String courseName;
    private String courseIntroduce;
    private String courseCode;
    private String courseAvatar;

    public CourseList(Integer courseId, String teacherId, String teacherName, String courseName, String courseIntroduce, String courseCode, String courseAvatar) {
        this.courseId = courseId;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
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

    public String getTeacherId() {
        return teacherId;
    }

    public String getTeacherPhone() {
        return teacherPhone;
    }

    public void setTeacherPhone(String teacherPhone) {
        this.teacherPhone = teacherPhone;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    public String getTeacherName() {
        return teacherName;
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

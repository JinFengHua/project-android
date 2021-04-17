package com.example.project_android.entity;

import java.io.Serializable;

public class CourseList implements Serializable {
    private Integer courseId;
    private String userId;
    private String uesrName;
    private String userPhone;
    private String userEmail;
    private String courseName;
    private String courseIntroduce;
    private String courseCode;
    private String courseAvatar;

    public CourseList(Integer courseId, String userId, String uesrName, String courseName, String courseIntroduce, String courseCode, String courseAvatar) {
        this.courseId = courseId;
        this.userId = userId;
        this.uesrName = uesrName;
        this.courseName = courseName;
        this.courseIntroduce = courseIntroduce;
        this.courseCode = courseCode;
        this.courseAvatar = courseAvatar;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUesrName() {
        return uesrName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserEmail() {
        return userEmail;
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

    public String getCourseAvatar() {
        return courseAvatar;
    }
}

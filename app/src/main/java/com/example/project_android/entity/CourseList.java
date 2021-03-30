package com.example.project_android.entity;

public class CourseList {
    private String courseId;
    private String courseTeacherId;
    private String courseTeacherName;
    private String courseName;
    private String courseIntroduce;
    private String courseCode;

    public CourseList(String courseId, String courseTeacherId, String courseTeacherName, String courseName, String courseIntroduce, String courseCode) {
        this.courseId = courseId;
        this.courseTeacherId = courseTeacherId;
        this.courseTeacherName = courseTeacherName;
        this.courseName = courseName;
        this.courseIntroduce = courseIntroduce;
        this.courseCode = courseCode;
    }

    public String getCourseId() {
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

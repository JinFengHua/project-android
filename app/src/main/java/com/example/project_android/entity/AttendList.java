package com.example.project_android.entity;

import java.sql.Timestamp;

public class AttendList {
    private Integer attendId;
    private String courseId;
    private Timestamp startTime;
    private Timestamp endTime;
    private Double longitude;
    private Double latitude;
    private String location;
    private String state;

    public AttendList(Integer attendId, String courseId, Timestamp startTime, Timestamp endTime, Double longitude, Double latitude, String location, String state) {
        this.attendId = attendId;
        this.courseId = courseId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.location = location;
        this.state = state;
    }

    public String getLocation() {
        return location;
    }

    public Integer getAttendId() {
        return attendId;
    }

    public String getCourseId() {
        return courseId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getState() {
        return state;
    }
}

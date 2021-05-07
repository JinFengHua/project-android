package com.example.project_android.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class AttendList implements Serializable {
    private Integer attendId;
    private String courseId;
    private Timestamp startTime;
    private Timestamp endTime;
    private Double longitude;
    private Double latitude;
    private String location;
    private String state;
    private Integer type;
    private String gesture;

    public AttendList(Integer attendId, String courseId, Timestamp startTime, Timestamp endTime, Double longitude, Double latitude, String location, String state, Integer type) {
        this.attendId = attendId;
        this.courseId = courseId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.location = location;
        this.state = state;
        this.type = type;
    }

    public String getGesture() {
        return gesture;
    }

    public void setGesture(String gesture) {
        this.gesture = gesture;
    }

    public Integer getType() {
        return type;
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

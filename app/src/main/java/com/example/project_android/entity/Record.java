package com.example.project_android.entity;

import java.sql.Timestamp;

public class Record {
    private String avatarUrl;
    private Timestamp recordTime;
    private String recordName;
    private String recordAccount;
    private String recordResult;
    private String recordLocation;
    private String recordPhoto;

    private String studentId;
    private String attendId;

    public Record(String avatarUrl, Timestamp recordTime, String recordName, String recordAccount, String recordResult, String recordLocation) {
        this.avatarUrl = avatarUrl;
        this.recordTime = recordTime;
        this.recordName = recordName;
        this.recordAccount = recordAccount;
        this.recordResult = recordResult;
        this.recordLocation = recordLocation;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setRecordResult(String recordResult) {
        this.recordResult = recordResult;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getAttendId() {
        return attendId;
    }

    public void setAttendId(String attendId) {
        this.attendId = attendId;
    }

    public String getRecordPhoto() {
        return recordPhoto;
    }

    public void setRecordPhoto(String recordPhoto) {
        this.recordPhoto = recordPhoto;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Timestamp getRecordTime() {
        return recordTime;
    }

    public String getRecordName() {
        return recordName;
    }

    public String getRecordAccount() {
        return recordAccount;
    }

    public String getRecordResult() {
        return recordResult;
    }

    public String getRecordLocation() {
        return recordLocation;
    }
}

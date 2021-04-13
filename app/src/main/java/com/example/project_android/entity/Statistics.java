package com.example.project_android.entity;

public class Statistics  {
    private String studentName;
    private String studentAccount;
    private Integer absentCount;
    private Integer failedCount;
    private Integer successCount;
    private Integer leaveCount;

    public Statistics(String studentName, String studentAccount, Integer absentCount, Integer failedCount, Integer successCount, Integer leaveCount) {
        this.studentName = studentName;
        this.studentAccount = studentAccount;
        this.absentCount = absentCount;
        this.failedCount = failedCount;
        this.successCount = successCount;
        this.leaveCount = leaveCount;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentAccount() {
        return studentAccount;
    }

    public Integer getAbsentCount() {
        return absentCount;
    }

    public Integer getFailedCount() {
        return failedCount;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public Integer getLeaveCount() {
        return leaveCount;
    }
}

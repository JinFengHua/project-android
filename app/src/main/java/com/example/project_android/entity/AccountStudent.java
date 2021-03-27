package com.example.project_android.entity;

import java.io.Serializable;

public class AccountStudent implements Serializable {
    private String name;
    private String account;
    private String password;
    private Boolean sex;
    private String major;
    private String phone;
    private String email;

    public AccountStudent(String name, String account, String password, Boolean sex, String major, String phone, String email) {
        this.name = name;
        this.account = account;
        this.password = password;
        this.sex = sex;
        this.major = major;
        this.phone = phone;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getSex() {
        return sex;
    }

    public String getMajor() {
        return major;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}

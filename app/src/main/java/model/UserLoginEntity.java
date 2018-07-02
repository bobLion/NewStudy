package model;

import java.io.Serializable;

/**
 * @package com.bob.android.newstudy
 * @fileName UserLoginEntity
 * @Author Bob on 2018/5/21 11:11.
 * @Describe TODO
 */

public class UserLoginEntity implements Serializable{

    private String userCode;
    private String password;

    public UserLoginEntity() {
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

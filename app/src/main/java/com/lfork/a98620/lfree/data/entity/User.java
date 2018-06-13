package com.lfork.a98620.lfree.data.entity;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 *
 * Created by 98620 on 2018/3/23.
 */

public class User extends DataSupport {
    private int id; //储存在本地数据库里面的id

    @Column(unique = true, defaultValue = "unknown") //啊啊啊
    private int userId;     //用户的ID

    private String studentId;  //userId == studentId

    private String userName;    //用户姓名

    private String userPassword;  //用户密码

    private String userEmail;     //用户邮箱地址

    private String userPhone;      //用户电话号码

    private String userAddress;    //用户的住址

    private String userImagePath;   //用户头像文件存放路径

    private String userDesc;          //用户描述

    private String userMakeDate;        //用户修改或者注册时间

    private School userSchool;

    private long timestamp;

    private boolean isLogin;

    private boolean isChatUser;

    public User() {
    }

    /**
     * 注册的时候用
     */
    public User(String userName, String userPassword, String userEmail, String userPhone, String userAddress,
                String userImagePath, String userDesc, String userMakeDate) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.userImagePath = userImagePath;
        this.userDesc = userDesc;
        this.userMakeDate = userMakeDate;
    }


    public int getUserId() {
        return userId;
    }


    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getUserPassword() {
        return userPassword;
    }


    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }


    public String getUserEmail() {
        return userEmail;
    }


    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    public String getUserPhone() {
        return userPhone;
    }


    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }


    public String getUserAddress() {
        return userAddress;
    }


    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }


    public String getUserImagePath() {
        return userImagePath;
    }


    public void setUserImagePath(String userImagePath) {
        this.userImagePath = userImagePath;
    }


    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }


    public String getUserMakeDate() {
        return userMakeDate;
    }


    public void setUserMakeDate(String userMakeDate) {
        this.userMakeDate = userMakeDate;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public boolean isChatUser() {
        return isChatUser;
    }

    public void setChatUser(boolean chatUser) {
        isChatUser = chatUser;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", userName=" + userName + ", userPassword=" + userPassword + ", userEmail="
                + userEmail + ", userPhone=" + userPhone + ", userAddress=" + userAddress + ", userImagePath="
                + userImagePath + ", userDesc=" + userDesc + ", userMakeDate=" + userMakeDate + "]";
    }

    public School getSchool() {
        return userSchool;
    }

    public void setSchool(School school) {
        this.userSchool = school;
    }
}

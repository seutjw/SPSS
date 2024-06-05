package com.mars.ecsheet.entity;

import java.util.List;

public class UserEntity {
    private int uid;
    private String UserName;
    private String pwd;


    public UserEntity(int UID ,String name,String PWD)
    {
        this.uid = UID;
        this.UserName = name;
        this.pwd = PWD;
    }

    public UserEntity()
    {
        this.uid = 0;
        this.UserName = "";
        this.pwd = "";
    }

    public int getUid() {
        return uid;
    }

    public String getUserName() {
        return UserName;
    }

    public String getPwd() {
        return pwd;
    }



    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }


}

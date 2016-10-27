package com.example.asus.myapp;

import java.util.Objects;

/**
 * Created by asus on 2016/7/9.
 */
public class Check {

    private String username = null;
    private String userpass = null;
    public Check(String username ,String userpass){
        this.username = username;
        this.userpass = userpass;
    }

    public boolean CheckResult(){
        if("admin".equals(username) && "123".equals(userpass)){
            return true;
        }
        return false;
    }
}

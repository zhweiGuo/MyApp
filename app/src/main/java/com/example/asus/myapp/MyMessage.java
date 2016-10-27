package com.example.asus.myapp;

import java.util.Date;

/**
 * Created by asus on 2016/9/17.
 */
public class MyMessage {
    public static final int RECEIVE = 0;
    public static final int SEND = 1;

    private int type;
    private String content;
    private Date msgDate;
    private String studentId_left = null;
    private String studentId_right = null;

    public MyMessage(String content, int type, Date date,String studentId_left,String studentId_right) {
        this.content = content;
        this.type = type;
        this.msgDate = date;

        this.studentId_left = studentId_left;
        this.studentId_right = studentId_right;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public String getContent() {
        return content;
    }

    public Date getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(Date msgDate) {
        this.msgDate = msgDate;
    }

    public void setStudentId_left(String studentId_left){
        this.studentId_left = studentId_left;
    }
    public String getStudentId_left(){
        return this.studentId_left;
    }

    public void setStudentId_right(String studentId_right){
        this.studentId_right = studentId_right;
    }
    public String getStudentId_right(){
        return this.studentId_left;
    }
}

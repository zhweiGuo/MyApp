package com.example.asus.pushreflash;

import com.android.internal.http.multipart.Part;
import com.example.asus.app.UI.SendDanmuActivity;

/**
 * TalkNotifacatio
 *
 * @author: Allen
 * @time: 2016/10/30 19:28
 */

public class TalkNotifacatio {
    private String content;
    private String TeacherName;
    private String hour;
    private String minute;

    public TalkNotifacatio(String content, String teacherName, String hour, String minute) {
        this.content = content;
        TeacherName = teacherName;
        this.hour = hour;
        this.minute = minute;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTeacherName() {
        return TeacherName;
    }

    public void setTeacherName(String teacherName) {
        TeacherName = teacherName;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }
}

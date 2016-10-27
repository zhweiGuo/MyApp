package com.example.asus.pushreflash;

/**
 * Created by asus on 2016/9/27.
 */
public class Course {
    private String courseName= null;
    private String courseTeacher = null;
    private String courseID = null;
    private String courseFlag = null;
    private int courseHead;

    public Course(String courseTeacher, String courseName, int courseHead , String courseID , String courseFlag) {
        this.courseTeacher = courseTeacher;
        this.courseName = courseName;
        this.courseHead = courseHead;
        this.courseID = courseID;
        this.courseFlag = courseFlag;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(String courseTeacher) {
        this.courseTeacher = courseTeacher;
    }

    public int getCourseHead() {
        return courseHead;
    }

    public void setCourseHead(int courseHead) {
        this.courseHead = courseHead;
    }

    public void setCourseID(String courseID){
        this.courseID = courseID;
    }
    public String getCourseID(){
        return this.courseID;
    }

    public void  setCourseFlag(String courseFlag){
        this.courseFlag = courseFlag;
    }
    public String getCourseFlag(){
        return this.courseFlag;
    }
}

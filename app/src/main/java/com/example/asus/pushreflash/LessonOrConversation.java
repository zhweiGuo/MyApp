package com.example.asus.pushreflash;

/**
 * LessonOrConversation
 *
 * @author: Allen
 * @time: 2016/10/26 20:47
 */

public class LessonOrConversation {
    private String name;
    private String creator;
    private int number;
    private int resourceId;
    private boolean isLesson;

    public boolean isLesson() {
        return isLesson;
    }

    public void setLesson(boolean lesson) {
        isLesson = lesson;
    }

    public LessonOrConversation(String name, String creator, int number, int resourceId, boolean isLesson) {
        this.name = name;
        this.creator = creator;
        this.number = number;
        this.resourceId = resourceId;
        this.isLesson = isLesson;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public LessonOrConversation(String name, String creator, int number, int resourceId) {
        this.name = name;
        this.creator = creator;
        this.number = number;
        this.resourceId = resourceId;
    }
}

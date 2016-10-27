package com.example.asus.myapp;

/**
 * Created by asus on 2016/9/22.
 */
public class SettingItem {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SettingItem(String key, String value) {
        this.key = key;
        this.value = value;
    }
}

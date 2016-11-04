package com.example.asus.myapp.DBOperate;

import android.content.Context;

/**
 * Created by asus on 2016/11/2.
 */
public class DBContext {
    private MyDB myDB = null;
    private String tableName = null;
    private Context context = null; //传递操作上下文
    private String flag = null;//传递显示标识符是发送方还是接收方
    private String groupId = null;//传递组ID
    private String info = null;//传递内容
    private String name = null;//传递姓名

    public DBContext(MyDB myDB,String tableName ,
                     Context context,String flag ,
                     String groupId,String info ,
                     String name){
        this.myDB = myDB;
        this.tableName = tableName;
        this.context = context;
        this.flag = flag ;
        this.groupId = groupId;
        this.name = name;
        this.info = info;
    }
    public void doStractegy(int type){
        switch(type){
            //插入表格
            case 0:
                myDB.Insert(this.tableName,context,
                        flag,groupId,info,name);
                break;

            case 1:

                break;
        }
    }

    //设置要操作表格的表名字
    public void setTableName(String tableName){
        this.tableName = tableName;
    }
    public String tableName(){
        return this.tableName;
    }
}

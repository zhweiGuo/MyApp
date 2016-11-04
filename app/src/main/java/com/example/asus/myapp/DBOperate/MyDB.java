package com.example.asus.myapp.DBOperate;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by asus on 2016/11/2.
 */
public abstract  class MyDB {
    public abstract void Insert(String tableName,
                                Context context,String flag ,
                                String groupId,String info,
                                String name);
    public abstract Cursor Query(Context context,
                                 String tableName, String[] where);
}

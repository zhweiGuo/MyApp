package com.example.asus.myapp;

/**
 * Created by asus on 2016/9/25.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDBHelp extends SQLiteOpenHelper {
    private static final String TAG = "SQLite";
    public static final int VERSION = 1;
    private static String table_name = null;

    //必须要有构造函数
    public MyDBHelp(Context context, String name, CursorFactory factory,
                       int version) {
        super(context, name, factory, version);
        this.table_name = name;
    }

    // 当第一次创建数据库的时候，调用该方法
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table "+table_name+"(id INTEGER primary key autoincrement,_name varchar(32),author varchar(10),_nameId varchar(30),flag varchar(30))";
//输出创建数据库的日志信息
        Log.i(TAG, "create Database------------->");
//execSQL函数用于执行SQL语句
        db.execSQL(sql);
    }

    //当更新数据库的时候执行该方法
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//输出更新数据库的日志信息
        Log.i(TAG, "update Database------------->");
    }
}

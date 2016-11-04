package com.example.asus.myapp.DBOperate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.asus.myapp.MyDBHelp;

/**
 * Created by asus on 2016/10/31.
 */
public class DatabanseOperate extends MyDB {

    @Override
    public void Insert(String tableName,
                       Context context ,
                       String flag ,
                       String groupId,
                       String info ,
                       String name) {
        MyDBHelp myDBHelp = new MyDBHelp(context, tableName, null, 1);
        SQLiteDatabase db = myDBHelp.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("author", flag);//flag = RECEIVE 或者是SEND
        contentValues.put("flag", info);
        contentValues.put("_nameID", groupId);
        contentValues.put("_name",name);

        db.insert(tableName, null, contentValues);
        db.close();
    }

    @Override
    public Cursor Query(Context context ,
                        String tableName,String[] where) {


        MyDBHelp myDBHelp = new MyDBHelp(context, tableName, null, 1);
        SQLiteDatabase db = myDBHelp.getReadableDatabase();
        Cursor cursor = db.query(tableName, where, null, null, null, null, null);

        //db.close();
        return cursor;
    }
}

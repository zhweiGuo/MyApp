package com.example.asus.myapp;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CommitTeacher extends AppCompatActivity {


    //获取通知内容
    private  String showMsg = null;
    private  String showHour = null;
    private  String showMin = null;
    private  String showUser = null;
    private  String showDate = null;

    private String tempInfo = null;

    //Android2.2版本以后的URL，之前的就不写了
    private static String calanderURL = "content://com.android.calendar/calendars";
    private static String calanderEventURL = "content://com.android.calendar/events";
    private static String calanderRemiderURL = "content://com.android.calendar/reminders";

    private final String url = "http://115.28.80.81/app/check.php";

    private ListView show_info = null;
    private MessageAdapter msgAdapter;
    private List<MyMessage> msgList = new ArrayList<MyMessage>();
    private UserTeacherActivity userTeacherActivity = new UserTeacherActivity();

    private Toolbar toolbar = null;

    private EditText promble = null;
    private EditText hour = null;
    private EditText min = null;
    private Button commit = null;
    //private Button showNew = null;
    //private Button insert = null;
    private SimpleDateFormat simpleDateFormat = null;
    private Date date = null;

    private static String groupId = null;



    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0 :Toast.makeText(CommitTeacher.this,"发布通知失败",Toast.LENGTH_SHORT).show();
                    break;
                case 1 :
                    Toast.makeText(CommitTeacher.this,"发布通知成功",Toast.LENGTH_SHORT).show();

                    MyMessage mg = new MyMessage(getTempInfo(), MyMessage.SEND, new Date(),getShowUser(),"");
                    msgList.add(mg);
                    msgAdapter.notifyDataSetChanged();
                    show_info.setSelection(msgList.size());

                    //插入数据库
                    MyDBHelp myDBHelp = new MyDBHelp(CommitTeacher.this,"notification",null,1);
                    SQLiteDatabase db = myDBHelp.getWritableDatabase();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("author", "SEND");
                    contentValues.put("flag", getTempInfo());
                    contentValues.put("_nameID",CommitTeacher.this.getGroupId());
                    contentValues.put("_name",userTeacherActivity.getUsername());

                    db.insert("notification",null,contentValues);
                    db.close();
                    break;
                case 2 :
                    break;
                case 3:
                    break;
            }
        }
    };

    public static final String ACTION_UPDATEUI = "action.updateUI";

    UpdateUIBroadcastReceiver broadcastReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.committeacher);

        Resources resources = getResources();
        Drawable drawable = resources.getDrawable(R.drawable.registerWindowBackground);
        this.getWindow().setBackgroundDrawable(drawable);

        this.toolbar = (Toolbar) super.findViewById(R.id.toolbar);
        this.toolbar.setTitle("");
        this.toolbar.setNavigationIcon(R.drawable.back_button);
        setSupportActionBar(toolbar);

        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommitTeacher.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        msgAdapter = new MessageAdapter(CommitTeacher.this, R.layout.message_item1, msgList);
        this.show_info = (ListView)super.findViewById(R.id.listviewshow);
        this.show_info.setAdapter(msgAdapter);

        this.commit = (Button) super.findViewById(R.id.commitMsg);
        this.promble = (EditText) super.findViewById(R.id.problem_info);
        this.hour = (EditText)super.findViewById(R.id.hour);
        this.min = (EditText) super.findViewById(R.id.min);


        this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date = new Date(System.currentTimeMillis() / 1000);


        //CommitTeacher.this.startService(new Intent(CommitTeacher.this, MyService.class));

        /*// 动态注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_UPDATEUI);
        broadcastReceiver = new UpdateUIBroadcastReceiver();
        registerReceiver(broadcastReceiver, filter);

        // 启动服务
        Intent intent = new Intent(this, MyService.class);
        startService(intent);

        /*show_info.setAdapter(new ArrayAdapter<String>(CommitTeacher.this,android.R.layout.simple_expandable_list_item_1,getData()));
        setContentView(show_info);*/


        MyDBHelp myDBHelp2 = new MyDBHelp(CommitTeacher.this, "notification", null, 1);
        SQLiteDatabase db2 = myDBHelp2.getReadableDatabase();
        Cursor cursor2 = db2.query("notification", new String[]{"author", "flag", "_nameId","_name"}, null, null, null, null, null);

        while (cursor2.moveToNext()) {
            String author = cursor2.getString(cursor2.getColumnIndex("author"));
            String _flag = cursor2.getString(cursor2.getColumnIndex("flag"));
            String _nameId = cursor2.getString(cursor2.getColumnIndex("_nameId"));
            String _name = cursor2.getString(cursor2.getColumnIndex("_name"));
            if (getGroupId().equals(_nameId)) {
                if ("SEND".equals(author)) {
                    MyMessage msg = new MyMessage(_flag, MyMessage.SEND, new Date(),"",_name);
                    msgList.add(msg);
                    msgAdapter.notifyDataSetChanged();
                    show_info.setSelection(msgList.size());
                } else if ("RECEIVE".equals(author)) {
                    MyMessage msg = new MyMessage(_flag, MyMessage.RECEIVE, new Date(),_name,"");
                    msgList.add(msg);
                    msgAdapter.notifyDataSetChanged();
                    show_info.setSelection(msgList.size());
                }
            }
        }
//关闭数据库
        //cursor.moveToLast();
        db2.close();



        this.commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    //URL url=new URL(urlPath);
                    String info_hour = hour.getText().toString();
                    String info_min = min.getText().toString();
                    String info = promble.getText().toString();
                    String time = simpleDateFormat.format(date);
                    String unix = simpleDateFormat.parse(time).getTime() + "";

                    if (info.equals("")){
                        Toast.makeText(CommitTeacher.this,"通知不能为空",Toast.LENGTH_SHORT).show();
                    }else {
                        SentProm sentProm = new SentProm(info, unix,info_hour,info_min);
                        sentProm.start();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                CommitTeacher.this.promble.setText("");
                CommitTeacher.this.hour.setText("");
                CommitTeacher.this.min.setText("");

            }
        });


        //查看通知
        /*this.showNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReciveNew reciveNew = new ReciveNew();
                reciveNew.start();
            }
        });

        //插入日历
        /*this.insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getData()!= null) {
                    Toast.makeText(CommitTeacher.this,"无事件插入",Toast.LENGTH_SHORT).show();
                }
                else {
                    String calId = "";
                    Cursor userCursor = getContentResolver().query(Uri.parse(calanderURL), null, null, null, null);

                    if (userCursor.getCount() > 0) {
                        userCursor.moveToLast();  //注意：是向最后一个账户添加，开发者可以根据需要改变添加事件 的账户
                        calId = userCursor.getString(userCursor.getColumnIndex("_id"));
                    }else{
                        initCalendars();//添加账户
                    }
                    for (userCursor.moveToFirst(); !userCursor.isAfterLast(); userCursor.moveToNext()) {
                        System.out.println("name: " + userCursor.getString(userCursor.getColumnIndex("ACCOUNT_NAME")));


                        String userName1 = userCursor.getString(userCursor.getColumnIndex("name"));
                        String userName0 = userCursor.getString(userCursor.getColumnIndex("ACCOUNT_NAME"));
                        //Toast.makeText(CommitTeacher.this, "NAME: " + userName1 + " -- ACCOUNT_NAME: " + userName0, Toast.LENGTH_LONG).show();
                    }

                    ContentValues event = new ContentValues();
                    event.put("title", "通知");//****
                    event.put("description", getShowMsg());//***
                    // 插入账户
                    event.put("calendar_id", calId);
                    System.out.println("calId: " + calId);
                    event.put("eventLocation", "地球-华夏");

                    Calendar mCalendar = Calendar.getInstance();
                    mCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(getShowHour())-1);//*****
                    mCalendar.set(Calendar.MINUTE, Integer.parseInt(getShowMin()));//*****
                    long start = mCalendar.getTime().getTime();
                    mCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(getShowHour()));//****
                    mCalendar.set(Calendar.MINUTE, Integer.parseInt(getShowMin()));
                    long end = mCalendar.getTime().getTime();

                    event.put("dtstart", start);
                    event.put("dtend", end);
                    event.put("hasAlarm", 1);

                    event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai");  //这个是时区，必须有，
                    //添加事件
                    Uri newEvent = getContentResolver().insert(Uri.parse(calanderEventURL), event);
                    //事件提醒的设定
                    long id = Long.parseLong(newEvent.getLastPathSegment());
                    ContentValues values = new ContentValues();
                    values.put("event_id", id);
                    // 提前10分钟有提醒
                    values.put("minutes", 10);
                    getContentResolver().insert(Uri.parse(calanderRemiderURL), values);

                    Toast.makeText(CommitTeacher.this, "插入日历成功", Toast.LENGTH_LONG).show();

                }

            }
        });*/
    }



    //广播接收消息
    private class UpdateUIBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            System.out.println("++-+-+-+-+-+-+"+getData());

            CommitTeacher.this.setShowMsg(String.valueOf(intent.getExtras().getString("msgBody")));
            CommitTeacher.this.setShowHour(String.valueOf(intent.getExtras().getString("hour")));
            CommitTeacher.this.setShowMin(String.valueOf(intent.getExtras().getString("min")));
            CommitTeacher.this.setShowDate(String.valueOf(intent.getExtras().getString("time")));
            CommitTeacher.this.setShowUser(String.valueOf(intent.getExtras().getString("name")));
            //show_info.setAdapter(new ArrayAdapter<String>(CommitTeacher.this,android.R.layout.simple_expandable_list_item_1,getData()));
            //setContentView(show_info);
            String info = "内容: " + getShowMsg() + " " + getShowHour() + ":" + getShowMin() + " 发件人：" + getShowUser() + " " + getShowDate();

            MyMessage msg = new MyMessage(info, MyMessage.RECEIVE, new Date(),getShowUser(),"");
            msgList.add(msg);
            msgAdapter.notifyDataSetChanged();
            show_info.setSelection(msgList.size());



            //InsertSystemDate(); //后台插入


            //插入数据库
            MyDBHelp myDBHelp = new MyDBHelp(CommitTeacher.this,"notification",null,1);
            SQLiteDatabase db = myDBHelp.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("author", "RECEIVE");
            contentValues.put("flag", info);
            contentValues.put("_nameID",CommitTeacher.this.getGroupId());
            contentValues.put("_name",getShowUser());

            db.insert("notification",null,contentValues);
            db.close();
        }

    }
    /*
    @Override
    protected void onDestroy() {
        System.out.println("onDestroy");
        super.onDestroy();
        // 注销广播
        unregisterReceiver(broadcastReceiver);
    }

    */



    /******获取通知内容*********/
    public void setShowMsg(String showMsg){
        this.showMsg = showMsg;
    }
    public String getShowMsg(){
        return this.showMsg;
    }

    public void setShowHour(String showHour){
        this.showHour = showHour;
    }
    public String getShowHour(){
        return this.showHour;
    }

    public void setShowMin(String showMin){
        this.showMin = showMin;
    }
    public String getShowMin(){
        return this.showMin;
    }

    public void setShowUser(String showUser){
        this.showUser = showUser;
    }
    public String getShowUser(){
        return this.showUser;
    }

    public void setShowDate(String showDate){
        this.showDate = showDate;
    }
    public String getShowDate(){
        return this.showDate;
    }


    public void setGroupId(String groupId){this.groupId = groupId;}
    public String getGroupId(){return this.groupId;}


    public void setTempInfo(String tempInfo){
        this.tempInfo = tempInfo;
    }
    public String getTempInfo(){
        return this.tempInfo;
    }

    public Date getDate(){
        return this.date;
    }
    /*+++++++++++++*/


    //添加账户
    private void initCalendars() {

        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, "yy");

        value.put(CalendarContract.Calendars.ACCOUNT_NAME, "mygmailaddress@gmail.com");
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, "com.android.exchange");
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "mytt");
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, -9206951);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, "mygmailaddress@gmail.com");
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = CalendarContract.Calendars.CONTENT_URI;
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "mygmailaddress@gmail.com")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, "com.android.exchange")
                .build();

        getContentResolver().insert(calendarUri, value);
    }



    //发布通知
    private class SentProm extends Thread {

        private final String url = "http://115.28.80.81/app/check.php";
        private String promble = null;
        private String info_hour = null;
        private String info_min = null;
        private String date = null;


        public SentProm(String promble, String date,String info_hour , String info_min) {
            this.promble = promble;
            this.date = date;
            this.info_hour = info_hour;
            this.info_min = info_min;

        }

        @Override
        public void run() {


            try {

                System.out.println("date----->"+date);
                System.out.println("prom----->"+promble);

                HttpPost request = new HttpPost(url);

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("action", "msgSend"));
                params.add(new BasicNameValuePair("name",userTeacherActivity.getUsername()));
                params.add(new BasicNameValuePair("msgBody",promble));
                params.add(new BasicNameValuePair("hour",info_hour));
                params.add(new BasicNameValuePair("min",info_min));
                params.add(new BasicNameValuePair("time",date));
                params.add(new BasicNameValuePair("groupId",getGroupId()));
                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                HttpResponse response = new DefaultHttpClient().execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String str = EntityUtils.toString(response.getEntity());
                    System.out.println("JSON1-------->" + str);
                    JSONObject jsonObject = new JSONObject(str);
                    System.out.println("STATUS------------------->" + jsonObject.getBoolean("status"));
                    if (jsonObject.getBoolean("status")){

                        CommitTeacher.this.setShowMsg(promble);
                        CommitTeacher.this.setShowHour(info_hour);
                        CommitTeacher.this.setShowMin(info_min);
                        CommitTeacher.this.setShowUser(userTeacherActivity.getUsername());

                        String info = "内容: " + getShowMsg() + " " + getShowHour() + ":" + getShowMin() + " 发件人：" + getShowUser() ;
                        setTempInfo(info);


                        CommitTeacher.this.handler.sendEmptyMessage(1);
                    }else{
                        CommitTeacher.this.handler.sendEmptyMessage(0);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //查看通知
    private class ReciveNew extends Thread{
        private final String url = "http://115.28.80.81/app/check.php";
        @Override
        public void run() {
            try {
                HttpPost request = new HttpPost(url);

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("action", "msgGet"));

                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                HttpResponse response = new DefaultHttpClient().execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String str = EntityUtils.toString(response.getEntity());
                    System.out.println("JSON1-------->" + str);
                    //JSONObject jsonObject = new JSONObject(str);
                    //System.out.println("RECIVE------------------->" + jsonObject.getBoolean("status"));
                    JSONArray jsonArray = new JSONArray(str);
                    for (int x = 0 ; x < jsonArray.length() ;x++){
                        JSONObject jsonObject = jsonArray.getJSONObject(x);
                        CommitTeacher.this.setShowMsg(jsonObject.getString("msgBody"));
                        CommitTeacher.this.setShowHour(jsonObject.getString("hour"));
                        CommitTeacher.this.setShowMin(jsonObject.getString("min"));
                        CommitTeacher.this.setShowUser(jsonObject.getString("name"));

                        String result = formatData("yyyy-MM-dd HH:mm:ss", jsonObject.getLong("time"));

                        CommitTeacher.this.setShowDate(result);
                        System.out.println(CommitTeacher.this.getShowDate() + CommitTeacher.this.getShowHour() + CommitTeacher.this.getShowMin() + CommitTeacher.this.getShowMsg()+CommitTeacher.this.getShowUser());
                    }


                    CommitTeacher.this.handler.sendEmptyMessage(2);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public  String formatData(String dataFormat, long timeStamp) {
            if (timeStamp == 0) {
                return "";
            }
            timeStamp = timeStamp * 1000;
            String result = "";
            SimpleDateFormat format = new SimpleDateFormat(dataFormat);
            result = format.format(new Date(timeStamp));
            return result;
        }
    }



    public List<String> getData() {
        List<String> list = new ArrayList<>();
        list.add("内容: " + getShowMsg() + " "+getShowHour() + ":" + getShowMin() + " 发件人：" + getShowUser() + " " + getShowDate());

        return list;
    }

/*
//插入日历
    public void InsertSystemDate(){
        if (msgList != null) {
            Toast.makeText(CommitTeacher.this,"无事件插入",Toast.LENGTH_SHORT).show();
        }
        else {
            String calId = "";
            Cursor userCursor = getContentResolver().query(Uri.parse(calanderURL), null, null, null, null);

            if (userCursor.getCount() > 0) {
                userCursor.moveToLast();  //注意：是向最后一个账户添加，开发者可以根据需要改变添加事件 的账户
                calId = userCursor.getString(userCursor.getColumnIndex("_id"));
            }else{
                initCalendars();//添加账户
            }
            for (userCursor.moveToFirst(); !userCursor.isAfterLast(); userCursor.moveToNext()) {
                System.out.println("name: " + userCursor.getString(userCursor.getColumnIndex("ACCOUNT_NAME")));


                String userName1 = userCursor.getString(userCursor.getColumnIndex("name"));
                String userName0 = userCursor.getString(userCursor.getColumnIndex("ACCOUNT_NAME"));
                //Toast.makeText(CommitTeacher.this, "NAME: " + userName1 + " -- ACCOUNT_NAME: " + userName0, Toast.LENGTH_LONG).show();
            }

            ContentValues event = new ContentValues();
            event.put("title", "通知");//****
            event.put("description", getShowMsg());//***
            // 插入账户
            event.put("calendar_id", calId);
            System.out.println("calId: " + calId);
            event.put("eventLocation", "地球-华夏");

            Calendar mCalendar = Calendar.getInstance();
            mCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(getShowHour()));//*****
            mCalendar.set(Calendar.MINUTE, Integer.parseInt(getShowMin()));//*****
            long start = mCalendar.getTime().getTime();
            mCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(getShowHour()+1));//****
            mCalendar.set(Calendar.MINUTE, Integer.parseInt(getShowMin()));
            long end = mCalendar.getTime().getTime();

            event.put("dtstart", start);
            event.put("dtend", end);
            event.put("allDay", 1);
            event.put("hasAlrarm",1);

            event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai");  //这个是时区，必须有，
            //添加事件
            Uri newEvent = getContentResolver().insert(Uri.parse(calanderEventURL), event);
            //事件提醒的设定
            long id = Long.parseLong(newEvent.getLastPathSegment());
            ContentValues values = new ContentValues();
            values.put("event_id", id);
            // 提前10分钟有提醒
            values.put("minutes", 10);
            getContentResolver().insert(Uri.parse(calanderRemiderURL), values);

            Toast.makeText(CommitTeacher.this, "插入日历成功", Toast.LENGTH_LONG).show();

        }

    }*/

}

package com.example.asus.myapp;


import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.example.asus.app.UI.Login1;
import com.example.asus.pushreflash.*;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String url = "http://115.28.80.81/app/check.php";
    //Android2.2版本以后的URL，之前的就不写了
    private static String calanderURL = "content://com.android.calendar/calendars";
    private static String calanderEventURL = "content://com.android.calendar/events";
    private static String calanderRemiderURL = "content://com.android.calendar/reminders";

    private boolean ifSuccess = false;

    //保存读取回来的listView
    private static String courseName = null;
    private static String teacherName = null;


    private int isTalk = 0;

    private Toolbar toolbar = null;
    private static int loginFlag = 0;
    private static String classNumberFlag = "9633";
    private static int join = 0;
    private static int selected = 1;    //判断登陆人群
    private static int hash_info = 0;
    private String saveId = null;
    private String saveFlag = null; //班课号码
    private static String groupId = null;   //会话组编号


    private static ListView datalist = null;
    private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    private SimpleAdapter simpleAdapter = null;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    MainActivity.this.datalist.setAdapter(simpleAdapter);
                    saveFlag = "（会话）";
                    testData.add(new Course(MainActivity.this.getCourseName(),
                            MainActivity.this.getTeacherName(), R.drawable.file_pic, saveId, saveFlag));
                    mCourseAdapter = new CourseAdapter(MainActivity.this, R.layout.item_course, testData);
                    courseListView.setAdapter(mCourseAdapter);
                    isTalk = 1;

                    //插入本地数据库

                    MyDBHelp myDBHelp = new MyDBHelp(MainActivity.this, getDatabaseName(), null, 1);
                    SQLiteDatabase db = myDBHelp.getWritableDatabase();
                    //生成插入数据对象
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("_name", MainActivity.this.getCourseName());
                    contentValues.put("author", MainActivity.this.getTeacherName());
                    contentValues.put("_nameId", saveId);
                    contentValues.put("flag", saveFlag);

                    db.insert(getDatabaseName(), null, contentValues);
                    db.close();

                    break;
                case 2:
                    saveFlag = "（班课）";
                    testData.add(new Course(MainActivity.this.getCourseName(),
                            MainActivity.this.getTeacherName(), R.drawable.file_pic, saveId, saveFlag));
                    mCourseAdapter = new CourseAdapter(MainActivity.this, R.layout.item_course, testData);

                    courseListView.setAdapter(mCourseAdapter);


                    isTalk = 0;


                    MyDBHelp myDBHelp1 = new MyDBHelp(MainActivity.this, getDatabaseName(), null, 1);
                    SQLiteDatabase db1 = myDBHelp1.getWritableDatabase();
                    //生成插入数据对象
                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("_name", MainActivity.this.getCourseName());
                    contentValues1.put("author", MainActivity.this.getTeacherName());
                    contentValues1.put("_nameId", saveId);
                    contentValues1.put("flag", saveFlag);

                    db1.insert(getDatabaseName(), null, contentValues1);
                    db1.close();

                    break;
                case 3:
                    Toast.makeText(MainActivity.this, "编号错误", Toast.LENGTH_SHORT).show();
                    break;

                case 4:

                    if ("0".equals(getSaveFlag())) {
                        testData.add(new Course(MainActivity.this.getCourseName(),
                                MainActivity.this.getTeacherName(), R.drawable.file_pic, saveId, "（会话）"));
                        mCourseAdapter = new CourseAdapter(MainActivity.this, R.layout.item_course, testData);

                        courseListView.setAdapter(mCourseAdapter);

                        //插入会话表
                        MyDBHelp myDBHelp3 = new MyDBHelp(MainActivity.this, getDatabaseName(), null, 1);
                        SQLiteDatabase db3 = myDBHelp3.getWritableDatabase();
                        //生成插入数据对象
                        ContentValues contentValues3 = new ContentValues();
                        contentValues3.put("_name", MainActivity.this.getCourseName());
                        contentValues3.put("author", MainActivity.this.getTeacherName());
                        contentValues3.put("_nameId", getGroupId());
                        contentValues3.put("flag", "（会话）");

                        db3.insert(getDatabaseName(), null, contentValues3);
                        db3.close();


                    } else if ("1".equals(getSaveFlag())) {
                        testData.add(new Course(MainActivity.this.getCourseName(),
                                MainActivity.this.getTeacherName(), R.drawable.file_pic, saveId, "（班课）"));
                        mCourseAdapter = new CourseAdapter(MainActivity.this, R.layout.item_course, testData);
                        courseListView.setAdapter(mCourseAdapter);

                        //插入本地数据库
                        MyDBHelp myDBHelp11 = new MyDBHelp(MainActivity.this, getDatabaseName(), null, 1);
                        SQLiteDatabase db11 = myDBHelp11.getWritableDatabase();
                        //生成插入数据对象
                        ContentValues contentValues11 = new ContentValues();
                        contentValues11.put("_name", MainActivity.this.getCourseName());
                        contentValues11.put("author", MainActivity.this.getTeacherName());
                        contentValues11.put("_nameId", getGroupId());
                        contentValues11.put("flag", "（班课）");

                        db11.insert(getDatabaseName(), null, contentValues11);
                        db11.close();

                    }
                    break;
            }
        }
    };  //  handler 结束


    //+++++++++++++++++++++++++++++++++++++

    private SwipeRefreshLayout courseListViewContainer;
    private ListView courseListView;
    private CourseAdapter mCourseAdapter;
    private boolean isRefreshing = false;
    private List<Course> testData = new ArrayList<Course>();

    //+++++++++++++++++++++++++++++++++++++

    public static final String ACTION_UPDATEUI = "action.updateUI";

    UpdateUIBroadcastReceiver broadcastReceiver;


    private UserTeacherActivity userTeacherActivity = new UserTeacherActivity();

    private static String databaseName = null;

    //通知栏提醒
    private NotificationManager nm = null;
    private PendingIntent pd = null;


    private int Notification_ID_BASE = 110;

    private Notification baseNF;


    private UserActivity userActivity = new UserActivity();

    //广播接收消息
    private class UpdateUIBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String info = "内容: " + String.valueOf(intent.getExtras().getString("msgBody")) + " " +
                    String.valueOf(intent.getExtras().getString("hour")) + " : " +
                    String.valueOf(intent.getExtras().getString("min")) + " " +
                    String.valueOf(intent.getExtras().getString("time")) + " 发件人 " +
                    String.valueOf(intent.getExtras().getString("name"));


            MainActivity.this.setGroupId(String.valueOf(intent.getExtras().getString("groupId")));

            if (getGroupId() != null) {

                for (int x = 0; x < courseListView.getCount(); x++) {
                    View view = courseListView.getChildAt(x);   //获取listview 中的内容

                    TextView show_id = (TextView) view.findViewById(R.id.course_id);
                    String tempid = show_id.getText().toString();
                    ImageView imageView = (ImageView) view.findViewById(R.id.course_head);

                    if (getGroupId().equals(tempid)) {
                        imageView.setImageResource(R.drawable.file_pic_notifi);
                        showAppNotification();//提示


                        if (String.valueOf(intent.getExtras().getString("msgBody")) != null) {
                            //插入日历
                            InsertSystemDate(String.valueOf(intent.getExtras().getString("msgBody")), String.valueOf(intent.getExtras().getString("hour")), String.valueOf(intent.getExtras().getString("min")));

                            //插入数据库
                            MyDBHelp myDBHelp = new MyDBHelp(MainActivity.this, "notification", null, 1);
                            SQLiteDatabase db = myDBHelp.getWritableDatabase();

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("author", "RECEIVE");
                            contentValues.put("flag", info);
                            contentValues.put("_nameID", MainActivity.this.getGroupId());
                            contentValues.put("_name", intent.getExtras().getString("name"));

                            db.insert("notification", null, contentValues);
                            db.close();
                        }
                    }
                }

            }
        }

    }// UpdateUIBroadcastReceiver结束

    //*********************************************************
    boolean refresh = true;

    @Override
    public void onRefresh() {

        if (loginFlag == 1) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    courseListViewContainer.setRefreshing(false);    //显示或者是隐藏刷新进度条
                    if (refresh == true) {
                        lodingInfo load = new lodingInfo();
                        load.start();
                        refresh = false;
                    }
                }
            }, 3000);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    courseListViewContainer.setRefreshing(false);    //显示或者是隐藏刷新进度条

                }
            }, 3000);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.setContentView(R.layout.activity_list_with_swipe_refresh);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.mytitle);

        this.toolbar = (Toolbar) super.findViewById(R.id.toolbar);
        this.toolbar.setTitle("");
        this.toolbar.setNavigationIcon(R.drawable.sub);
        setSupportActionBar(toolbar);


        this.datalist = (ListView) super.findViewById(R.id.datalist);

        courseListViewContainer = (SwipeRefreshLayout) findViewById(R.id.course_list_container);
        courseListView = (ListView) findViewById(R.id.course_list);
        mCourseAdapter = new CourseAdapter(this, R.layout.item_course, testData);

        courseListView.setAdapter(mCourseAdapter);
        courseListViewContainer.setOnRefreshListener(this);

        courseListViewContainer
                .setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        //注册通知
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);

        pd = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);

        //￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this,"Text",Toast.LENGTH_SHORT).show();
                Dialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("             加入会话或者班课")
                        .setItems(R.array.about, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                switch (id) {
                                    case 0:

                                        final Dialog dialog2 = new Dialog(MainActivity.this, R.style.mytalkdialog);

                                        dialog2.show();
                                        WindowManager windowManager = getWindowManager();
                                        Display display = windowManager.getDefaultDisplay();
                                        WindowManager.LayoutParams lp = dialog2.getWindow().getAttributes();
                                        lp.width = (int) (display.getWidth() * 0.8); //设置宽度
                                        dialog2.getWindow().setAttributes(lp);


                                        dialog2.setContentView(R.layout.talkstyle);
                                        Button talkcancelBut = (Button) dialog2.findViewById(R.id.talkcancel);

                                        talkcancelBut.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog2.hide();
                                            }
                                        });

                                        Button talksure = (Button) dialog2.findViewById(R.id.talksure);
                                        talksure.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                EditText inputKey = (EditText) dialog2.findViewById(R.id.inputalKey);

                                                if (loginFlag == 1) {
                                                    MainActivity.this.saveId = inputKey.getText().toString();
                                                    AddTalk addTalk = new AddTalk(inputKey.getText().toString());
                                                    addTalk.start();
                                                    dialog2.hide();


                                                } else {
                                                    Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                                                    dialog2.hide();

                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            try {
                                                                Thread.sleep(1000);
                                                                Intent intent = new Intent(MainActivity.this, Login1.class);
                                                                startActivity(intent);

                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }).start();
                                                }
                                                //dialog2.hide();
                                            }
                                        });


                                        break;
                                    case 1:
                                        //Toast.makeText(MainActivity.this, "id = 1", Toast.LENGTH_SHORT).show();
                                        final Dialog dialog1 = new Dialog(MainActivity.this, R.style.mydialog);
                                        dialog1.show();

                                        WindowManager windowManager1 = getWindowManager();
                                        Display display1 = windowManager1.getDefaultDisplay();
                                        WindowManager.LayoutParams lp1 = dialog1.getWindow().getAttributes();
                                        lp1.width = (int) (display1.getWidth() * 0.8); //设置宽度
                                        dialog1.getWindow().setAttributes(lp1);

                                        dialog1.setContentView(R.layout.dialogstyle);


                                        Button cancelBut = (Button) dialog1.findViewById(R.id.cancel);
                                        cancelBut.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog1.hide();
                                            }
                                        });

                                        Button sure = (Button) dialog1.findViewById(R.id.sure);
                                        sure.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                //Toast.makeText(MainActivity.this,"Sure", Toast.LENGTH_SHORT).show();
                                                EditText inputKey = (EditText) dialog1.findViewById(R.id.inputKey);
                                                String tempFlag = inputKey.getText().toString().trim();
                                                System.out.print(tempFlag);
                                                if (loginFlag == 1) {

                                                    MainActivity.this.saveId = inputKey.getText().toString();
                                                    AddClass addClass = new AddClass(inputKey.getText().toString());
                                                    addClass.start();

                                                    System.out.println("运行到这里");


                                                    //请求listview数据

                                                    dialog1.hide();

                                                } else {
                                                    Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                                                    dialog1.hide();

                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            try {
                                                                Thread.sleep(1000);
                                                                Intent intent = new Intent(MainActivity.this, Login1.class);
                                                                startActivity(intent);
                                                                finish();

                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }).start();
                                                }
                                            }
                                        });

                                        break;
                                }

                            }

                        })
                        .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create();

                dialog.show();
            }
        });

        System.out.println("****************************" + this.getJoin());


        if (1 == join) {
        } else if (0 == join) {
            System.out.println("**********------***************" + this.getJoin());
            list.clear();
//            this.datalist.setAdapter(null);
        }

        this.courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView show_id = (TextView) view.findViewById(R.id.course_id);
                String tempid = show_id.getText().toString();
                //Toast.makeText(MainActivity.this,tempshow,Toast.LENGTH_SHORT).show();
                TextView show_flag = (TextView) view.findViewById(R.id.course_Flag);
                String tempflag = show_flag.getText().toString();

                ImageView imageView = (ImageView) view.findViewById(R.id.course_head); //显示回原来的图标

                if (0 == selected) {

                    Commit commit = new Commit();
                    commit.setGroupId(tempid);
                    commit.setIsWho(0);
                    MyService2 myService2 = new MyService2();
                    myService2.setGroupId(tempid);
                    myService2.setName(userActivity.getUsername());
                    imageView.setImageResource(R.drawable.file_pic);
                    Intent intent = new Intent(MainActivity.this, Commit.class);
                    System.out.println("**********---Commit---***************");
                    startActivity(intent);
                } else if (1 == selected) {


                    if ("（班课）".equals(tempflag)) {
                        Commit commit = new Commit();
                        commit.setGroupId(tempid);
                        commit.setIsWho(1);
                        imageView.setImageResource(R.drawable.file_pic);
                        Intent intent = new Intent(MainActivity.this, Commit.class);
                        System.out.println("**********---Commit---***************");
                        startActivity(intent);

                    } else if ("（会话）".equals(tempflag)) {
                        MyService myService = new MyService();
                        myService.setGroupId(tempid);
                        CommitTeacher commitTeacher = new CommitTeacher();
                        commitTeacher.setGroupId(tempid);
                        imageView.setImageResource(R.drawable.file_pic);
                        Intent intent = new Intent(MainActivity.this, CommitTeacher.class);
                        System.out.println("**********---CommitTeacher---***************");
                        startActivity(intent);
                    }

                }
            }
        });
        System.out.println("loginflag ----- = " + loginFlag);

        if (loginFlag == 1) {
            // 动态注册广播
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_UPDATEUI);
            broadcastReceiver = new UpdateUIBroadcastReceiver();
            registerReceiver(broadcastReceiver, filter);


            // 启动服务
            Intent intent1 = new Intent(MainActivity.this, MyService.class);
            startService(intent1);


            //Toast.makeText(MainActivity.this,"这里是main函数的服务",Toast.LENGTH_SHORT).show();

            MyDBHelp dbHelper = new MyDBHelp(MainActivity.this, getDatabaseName(), null, 1);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(getDatabaseName(), new String[]{"_name", "author", "_nameId", "flag"},
                    null, null, null, null, null);
            while (cursor.moveToNext()) {
                String _name = cursor.getString(cursor.getColumnIndex("_name"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                String _nameId = cursor.getString(cursor.getColumnIndex("_nameId"));
                String _flag = cursor.getString(cursor.getColumnIndex("flag"));
                System.out.println("query------->" + "姓名：" + _name + " " + "年龄：" + author + " " + "性别：" + _nameId);

                testData.add(new Course(_name, author, R.drawable.file_pic, _nameId, _flag));
                mCourseAdapter.notifyDataSetChanged();
                isRefreshing = false;
            }
//关闭数据库
            cursor.moveToLast();
            db.close();


        } else {
            testData.clear();
            this.courseListView.setAdapter(null);
            //list.clear();
            //this.datalist.setAdapter(null);
        }
    }


    //显示通知内容
    public void showAppNotification() {

        Notification.Builder builder = new Notification.Builder(MainActivity.this);
        builder.setContentInfo("");
        builder.setContentText("匿名课堂有新消息");
        builder.setContentTitle("通知");
        builder.setSmallIcon(R.drawable.notification_flag);
        builder.setTicker("新消息");
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setWhen(System.currentTimeMillis());
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        nm.notify(Notification_ID_BASE, notification);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Toast.makeText(MainActivity.this,"Text",Toast.LENGTH_SHORT).show();

        if (this.loginFlag == 0) {

            Intent intent = new Intent(MainActivity.this, Login1.class);
            startActivity(intent);
            finish();

        } else if (this.loginFlag == 1) {
            if (getSelected() == 0) {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                startActivity(intent);
                finish();
            } else if (getSelected() == 1) {
                Intent intent = new Intent(MainActivity.this, UserTeacherActivity.class);
                startActivity(intent);
                finish();
            }
        }


        super.onOptionsItemSelected(item);
        return true;
    }


    /**************************************************************************/
    public void setLoginFlag(int loginFlag) {
        this.loginFlag = loginFlag;
    }

    public int getLoginFlag() {
        return this.loginFlag;
    }

    public void setJoin(int join) {
        this.join = join;
    }

    public int getJoin() {
        return this.join;
    }

    public ListView getDatalist() {
        return datalist;
    }


    /***********************/
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherName() {
        return this.teacherName;
    }

    /***********************/



    /*++++++++++++++++++++*/
    public void setSelected(int selected) {
        this.selected = selected;
    }

    public int getSelected() {
        return this.selected;
    }
    /*+++++++++++++++++++*/


    /*--------------------*/
    public void setHash_info(int hash_info) {
        this.hash_info = hash_info;
    }

    public int getHash_info() {
        return this.hash_info;
    }
    /*--------------------*/


    //判断课程编号是否正确
    public void setIfSuccess(boolean ifSuccess) {
        this.ifSuccess = ifSuccess;
    }

    public boolean getIfSuccess() {
        return this.ifSuccess;
    }


    //广播收到需要显示的组ID
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return this.groupId;
    }


    public void setSaveFlag(String saveFlag) {
        this.saveFlag = saveFlag;
    }

    public String getSaveFlag() {
        return this.saveFlag;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    //添加编号发的请求匹配
    private class AddClass extends Thread {
        private String inputkey = null;
        private static final String url = "http://115.28.80.81/app/check.php";


        public AddClass(String inputkey) {
            this.inputkey = inputkey;
        }

        @Override
        public void run() {

            try {

                HttpPost request = new HttpPost(url);
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("action", "getCourse"));
                params.add(new BasicNameValuePair("courseId", inputkey));

                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                HttpResponse response = new DefaultHttpClient().execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String str = EntityUtils.toString(response.getEntity());
                    System.out.println("JSON1-------->" + str);
                    JSONArray jsonArray = new JSONArray(str);
                    //System.out.println("STATUS------------------->" + jsonObject.getBoolean("status"));
                    if (str != null) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        System.out.println("courseName------------------->" + jsonObject.getString("courseName"));
                        System.out.println("teacherName------------------->" + jsonObject.getString("teacherName"));
                        setCourseName(jsonObject.getString("courseName"));
                        setTeacherName(jsonObject.getString("teacherName"));

                        MainActivity.this.handler.sendEmptyMessage(2);

                        setIfSuccess(true);
                        System.out.println("ifSuccess--------->" + getIfSuccess());
                    } else {
                        MainActivity.this.handler.sendEmptyMessage(3);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private class AddTalk extends Thread {
        private String inputkey = null;
        private static final String url = "http://115.28.80.81/app/check.php";


        public AddTalk(String inputkey) {
            this.inputkey = inputkey;
        }

        @Override
        public void run() {

            try {

                HttpPost request = new HttpPost(url);
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("action", "addTalk"));
                params.add(new BasicNameValuePair("gid", inputkey));

                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                HttpResponse response = new DefaultHttpClient().execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String str = EntityUtils.toString(response.getEntity());
                    System.out.println("JSON1-------->" + str);
                    JSONArray jsonArray = new JSONArray(str);
                    //System.out.println("STATUS------------------->" + jsonObject.getBoolean("status"));
                    if (str != null) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        System.out.println("talkName------------------->" + jsonObject.getString("groupName"));
                        System.out.println("teacherName------------------->" + jsonObject.getString("userName"));
                        setCourseName(jsonObject.getString("groupName"));
                        setTeacherName(jsonObject.getString("userName"));

                        MainActivity.this.handler.sendEmptyMessage(1);

                    } else {
                        MainActivity.this.handler.sendEmptyMessage(3);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    //读取表的信息
    private class lodingInfo extends Thread {
        private String url = "http://115.28.80.81/app/check.php";

        @Override
        public void run() {
            try {

                HttpPost request = new HttpPost(url);
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("action", "loadSession"));
                params.add(new BasicNameValuePair("userName", getDatabaseName()));


                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                HttpResponse response = new DefaultHttpClient().execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String str = EntityUtils.toString(response.getEntity());
                    System.out.println("JSONLoding-------->" + str);
                    JSONArray jsonArray = new JSONArray(str);
                    System.out.println("STATUS lodingInfo------------------->" + 2);

                    for (int x = 0; x < jsonArray.length(); x++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(x);
                        String name = jsonObject.getString("userName");
                        String groupName = jsonObject.getString("groupName");
                        String groupId = jsonObject.getString("gid");
                        int tempFlag = jsonObject.getInt("flag");
                        Log.e("//////////拉取信息","name = " + name +
                                "groupName = " + groupName + "groupId=" + groupId + "tempFlag" + tempFlag);
                        System.out.println("name = " + name + "group = " + groupName);
                        MainActivity.this.setCourseName(groupName);
                        MainActivity.this.setTeacherName(name);
                        MainActivity.this.setGroupId(groupId);
                        MainActivity.this.setSaveFlag(String.valueOf(tempFlag));
                        MainActivity.this.handler.sendEmptyMessage(4);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


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


    //插入日历
    public void InsertSystemDate(String mgsBody, String Hour, String Min) {
        if (mgsBody == null) {
            //Toast.makeText(CommitTeacher.this,"无事件插入",Toast.LENGTH_SHORT).show();
        } else {

            String calId = "";
            Cursor userCursor = getContentResolver().query(Uri.parse(calanderURL), null, null, null, null);

            if (userCursor.getCount() > 0) {
                userCursor.moveToLast();  //注意：是向最后一个账户添加，开发者可以根据需要改变添加事件 的账户
                calId = userCursor.getString(userCursor.getColumnIndex("_id"));
            } else {
                initCalendars();//添加账户
                calId = "1";
            }
            for (userCursor.moveToFirst(); !userCursor.isAfterLast(); userCursor.moveToNext()) {
                System.out.println("name: " + userCursor.getString(userCursor.getColumnIndex("ACCOUNT_NAME")));

                String userName1 = userCursor.getString(userCursor.getColumnIndex("name"));
                String userName0 = userCursor.getString(userCursor.getColumnIndex("ACCOUNT_NAME"));
                //Toast.makeText(CommitTeacher.this, "NAME: " + userName1 + " -- ACCOUNT_NAME: " + userName0, Toast.LENGTH_LONG).show();
            }

            ContentValues event = new ContentValues();
            SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyymmdd");
            event.put("title", "通知");//****
            event.put("description", mgsBody);//***
            // 插入账户
            event.put("calendar_id", 1);
            System.out.println("calId: " + calId);
            event.put("eventLocation", "地球-华夏");

            Calendar mCalendar = Calendar.getInstance();
            /*mCalendar.set(Calendar.DAY_OF_MONTH,mCalendar.get(Calendar.DAY_OF_MONTH));
            mCalendar.set(Calendar.MONTH,mCalendar.get(Calendar.MONTH)+1);
            mCalendar.set(Calendar.YEAR,mCalendar.get(Calendar.YEAR));*/
            mCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(Hour));//*****
            mCalendar.set(Calendar.MINUTE, Integer.parseInt(Min));//*****
            long start = mCalendar.getTime().getTime();
            mCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(Hour + 1));//****
            mCalendar.set(Calendar.MINUTE, Integer.parseInt(Min));
            long end = mCalendar.getTime().getTime();
            //mCalendar.add(Calendar.DATE, 1);
            //String dtUntill = yyyymmdd.format(mCalendar.getTime());

            //event.put(CalendarContract.Events.RRULE, "FREQ=DAILY;UNTIL="
            //  + dtUntill);

            event.put("dtstart", start);
            //event.put("dtend", end);
            event.put("hasAlarm", 1);
            event.put("allDay", 0);
            //event.put("hasAttendeeData",1);
            //event.put("eventStatus", 1);
            event.put(CalendarContract.Events.DURATION, "+P1H");
            event.put(CalendarContract.Events.HAS_ALARM, 1);

            event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai");  //这个是时区，必须有，

            //添加事件
            Uri newEvent = getContentResolver().insert(Uri.parse(calanderEventURL), event);
            //事件提醒的设定
            long id = Long.parseLong(newEvent.getLastPathSegment());
            ContentValues values = new ContentValues();
            values.put("event_id", id);
            // 提前10分钟有提醒
            values.put("minutes", 5);
            values.put("method", 1);
            getContentResolver().insert(Uri.parse(calanderRemiderURL), values);

            Toast.makeText(MainActivity.this, "插入日历成功", Toast.LENGTH_LONG).show();

        }

    }


}

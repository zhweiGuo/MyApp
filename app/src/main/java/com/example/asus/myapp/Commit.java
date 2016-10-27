package com.example.asus.myapp;

import com.example.asus.KMP.KMP;
import com.example.asus.myapp.MyMessage;
import com.example.asus.pushreflash.Course;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Commit extends AppCompatActivity {
    final String[] data = new String[]{"滚", "cao", "操", "滚蛋", "滚犊子",
            "傻瓜", "傻逼", "变态", "蠢",
            "草", "尼玛", "你妈", "你他妈",
            "妈的", "操你妈", "日", "日你妹", "问候你祖宗", "去死", "去死吧", "脑子有病", "猪脑",
            "奶", "奶奶的", "大爷", "畜生", "扑街",
            "猪", "傻", "妈的智障", "妈","混蛋","你爹","你妹","爸"};

    public static final String ACTION_UPDATEUI2 = "action.updateUI";
    UpdateUIBroadcastReceiver broadcastReceiver;

    private String[] dataWord = new String[]{
    };

    private Toolbar toolbar = null;
    //private static final String URL = "http://115.28.80.81/lazarus.space";
    private UserActivity userActivity = new UserActivity();
    private UserTeacherActivity userTeacherActivity = new UserTeacherActivity();


    private ListView msgListView;
    private MessageAdapter msgAdapter;
    private List<MyMessage> msgList = new ArrayList<MyMessage>();

    private  static String discuss = null;
    private  static String show_name = null;


    private EditText promble = null;
    private Button commit = null;
    private SimpleDateFormat simpleDateFormat = null;
    private Date date = null;

    private static String groupId = null;
    private static int isWho;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

                    MyMessage ms = new MyMessage(Commit.this.getDiscuss(), MyMessage.RECEIVE, new Date(), "", "");
                    msgList.add(ms);
                    msgAdapter.notifyDataSetChanged();
                    msgListView.setSelection(msgList.size());

                    MyDBHelp myDBHelp = new MyDBHelp(Commit.this, "record", null, 1);
                    SQLiteDatabase db = myDBHelp.getWritableDatabase();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("author", "RECEIVE");
                    contentValues.put("flag", discuss);
                    contentValues.put("_nameID", Commit.this.getGroupId());

                    db.insert("record", null, contentValues);
                    db.close();

                    break;
                case 1:

                    MyMessage ms1 = new MyMessage(Commit.this.getDiscuss(), MyMessage.RECEIVE, new Date(), getShow_name(), "");
                    msgList.add(ms1);
                    msgAdapter.notifyDataSetChanged();
                    msgListView.setSelection(msgList.size());

                    MyDBHelp myDBHelp1 = new MyDBHelp(Commit.this, "record", null, 1);
                    SQLiteDatabase db1 = myDBHelp1.getWritableDatabase();

                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("author", "RECEIVE");
                    contentValues1.put("flag", discuss);
                    contentValues1.put("_name",getShow_name());
                    contentValues1.put("_nameID", Commit.this.getGroupId());

                    db1.insert("record", null, contentValues1);
                    db1.close();

                    break;
            }
        }
    };



    //广播接收消息
    private class UpdateUIBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Commit.this.setDiscuss(String.valueOf(intent.getExtras().getString("discuss")));
            Commit.this.setShow_name(String.valueOf(intent.getExtras().getString("userName")));
            Commit.this.setGroupId(String.valueOf(intent.getExtras().getString("groupId")));

            String info = String.valueOf(intent.getExtras().getString("discuss"));

            MyDBHelp myDBHelp = new MyDBHelp(Commit.this,"record",null,1);
            SQLiteDatabase db = myDBHelp.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("author", "RECEIVE");
            contentValues.put("flag", discuss);
            contentValues.put("_name",getShow_name());
            contentValues.put("_nameID", Commit.this.getGroupId());

            db.insert("record", null, contentValues);
            db.close();

            switch (isWho){
                case 0 :
                    MyMessage ms = new MyMessage(Commit.this.getDiscuss(), MyMessage.RECEIVE, new Date(), "", "");
                    msgList.add(ms);
                    msgAdapter.notifyDataSetChanged();
                    msgListView.setSelection(msgList.size());

                    break;
                case 1:
                    MyMessage ms1 = new MyMessage(Commit.this.getDiscuss(), MyMessage.RECEIVE, new Date(), getShow_name(), "");
                    msgList.add(ms1);
                    msgAdapter.notifyDataSetChanged();
                    msgListView.setSelection(msgList.size());

                    break;
            }
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.commit);

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
                Intent intent = new Intent(Commit.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        this.commit = (Button) super.findViewById(R.id.commit);
        this.promble = (EditText) super.findViewById(R.id.problem_info);
        this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date = new Date(System.currentTimeMillis() / 1000);

        msgAdapter = new MessageAdapter(Commit.this, R.layout.message_item1, msgList);
        msgListView = (ListView) findViewById(R.id.msg_list_view);
        msgListView.setAdapter(msgAdapter);



        // 动态注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_UPDATEUI2);
        broadcastReceiver = new UpdateUIBroadcastReceiver();
        registerReceiver(broadcastReceiver, filter);

        //Intent intent = new Intent(Commit.this,MyService2.class);
        //Commit.this.startService(intent);


        this.commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    //URL url=new URL(urlPath);

                    String info = promble.getText().toString();
                    String time = simpleDateFormat.format(date);
                    String unix = simpleDateFormat.parse(time).getTime() + "";

                    int flag = -1;
                    for(int x = 0 ; x < data.length ; x++){
                        String s = info; // 主串
                        String t = data[x]; // 模式串
                        char[] ss = s.toCharArray();
                        char[] tt = t.toCharArray();
                        System.out.println("data---->" + data[x]);
                        System.out.println("flag == " + KMP.KMP_Index(ss, tt)); // KMP匹配字符串
                        flag = KMP.KMP_Index(ss, tt);
                        if (flag != -1)
                        {
                            System.out.println("含有敏感词");
                            break;
                        }
                    }

                    if(-1 == flag) {
                        SentProm sentProm = new SentProm(info, unix);
                        sentProm.start();

                        if (!"".equals(info)) {


                            switch (getIsWho()) {

                                case 0:

                                    MyMessage msg = new MyMessage(info, MyMessage.SEND, new Date(), "", "");
                                    msgList.add(msg);
                                    msgAdapter.notifyDataSetChanged();
                                    msgListView.setSelection(msgList.size());


                                    MyDBHelp myDBHelp = new MyDBHelp(Commit.this, "record", null, 1);
                                    SQLiteDatabase db = myDBHelp.getWritableDatabase();

                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("author", "SEND");
                                    contentValues.put("flag", info);
                                    contentValues.put("_nameId", Commit.this.getGroupId());
                                    contentValues.put("_name", userActivity.getUsername());

                                    db.insert("record", null, contentValues);
                                    db.close();

                                    break;

                                case 1:

                                    MyMessage msg1 = new MyMessage(info, MyMessage.SEND, new Date(), "", userTeacherActivity.getUsername());
                                    msgList.add(msg1);
                                    msgAdapter.notifyDataSetChanged();
                                    msgListView.setSelection(msgList.size());


                                    MyDBHelp myDBHelp1 = new MyDBHelp(Commit.this, "record", null, 1);
                                    SQLiteDatabase db1 = myDBHelp1.getWritableDatabase();

                                    ContentValues contentValues1 = new ContentValues();
                                    contentValues1.put("author", "SEND");
                                    contentValues1.put("flag", info);
                                    contentValues1.put("_nameId", Commit.this.getGroupId());
                                    contentValues1.put("_name", userTeacherActivity.getUsername());

                                    db1.insert("record", null, contentValues1);
                                    db1.close();

                                    break;
                            }
                        } else {
                            //System.out.println("输入的内容不能为空");
                        }
                    }else{
                        Toast.makeText(Commit.this,"含有敏感词",Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Commit.this.promble.setText("");

            }


        });


        //发送10秒轮询消息

        Receive receive = new Receive(date);
        receive.start();


        //显示聊天记录
        switch (getIsWho()) {

            case 0:
                MyDBHelp myDBHelp = new MyDBHelp(Commit.this, "record", null, 1);
                SQLiteDatabase db = myDBHelp.getReadableDatabase();
                Cursor cursor = db.query("record", new String[]{"author", "flag", "_nameId", "_name"}, null, null, null, null, null);

                while (cursor.moveToNext()) {
                    String author = cursor.getString(cursor.getColumnIndex("author"));
                    String _flag = cursor.getString(cursor.getColumnIndex("flag"));
                    String _nameId = cursor.getString(cursor.getColumnIndex("_nameId"));
                    String _name = cursor.getString(cursor.getColumnIndex("_name"));
                    if (getGroupId().equals(_nameId)) {
                        if ("SEND".equals(author)) {
                            MyMessage msg = new MyMessage(_flag, MyMessage.SEND, new Date(), "", "");
                            msgList.add(msg);
                            msgAdapter.notifyDataSetChanged();
                            msgListView.setSelection(msgList.size());
                        } else if ("RECEIVE".equals(author)) {
                            MyMessage msg = new MyMessage(_flag, MyMessage.RECEIVE, new Date(), "", "");
                            msgList.add(msg);
                            msgAdapter.notifyDataSetChanged();
                            msgListView.setSelection(msgList.size());
                        }
                    }
                }
//关闭数据库
                //cursor.moveToLast();
                db.close();
                break;

            case 1:


                MyDBHelp myDBHelp2 = new MyDBHelp(Commit.this, "record", null, 1);
                SQLiteDatabase db2 = myDBHelp2.getReadableDatabase();
                Cursor cursor2 = db2.query("record", new String[]{"author", "flag", "_nameId", "_name"}, null, null, null, null, null);

                while (cursor2.moveToNext()) {
                    String author = cursor2.getString(cursor2.getColumnIndex("author"));
                    String _flag = cursor2.getString(cursor2.getColumnIndex("flag"));
                    String _nameId = cursor2.getString(cursor2.getColumnIndex("_nameId"));
                    String _name = cursor2.getString(cursor2.getColumnIndex("_name"));
                    if (getGroupId().equals(_nameId)) {
                        if ("SEND".equals(author)) {
                            MyMessage msg = new MyMessage(_flag, MyMessage.SEND, new Date(), "", _name);
                            msgList.add(msg);
                            msgAdapter.notifyDataSetChanged();
                            msgListView.setSelection(msgList.size());
                        } else if ("RECEIVE".equals(author)) {
                            MyMessage msg = new MyMessage(_flag, MyMessage.RECEIVE, new Date(), _name, "");
                            msgList.add(msg);
                            msgAdapter.notifyDataSetChanged();
                            msgListView.setSelection(msgList.size());
                        }
                    }
                }
//关闭数据库
                //cursor.moveToLast();
                db2.close();
                break;
        }
    }


    @Override
    public boolean releaseInstance() {
        return super.releaseInstance();
    }

    private class SentProm extends Thread {

        private final String url = "http://115.28.80.81/app/check.php";
        private String promble = null;
        private String date = null;


        public SentProm(String promble, String date) {
            this.promble = promble;
            this.date = date;

        }

        @Override
        public void run() {


            try {

                System.out.println("date----->" + date);
                System.out.println("prom----->" + promble);

                HttpPost request = new HttpPost(url);

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("action", "commit"));
                params.add(new BasicNameValuePair("userName", userActivity.getUsername()));
                params.add(new BasicNameValuePair("discuss", promble));
                params.add(new BasicNameValuePair("teacherName", "1"));
                params.add(new BasicNameValuePair("courseId", Commit.this.getGroupId()));
                params.add(new BasicNameValuePair("time", date));
                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                HttpResponse response = new DefaultHttpClient().execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String str = EntityUtils.toString(response.getEntity());
                    System.out.println("JSON1-------->" + str);
                    JSONObject jsonObject = new JSONObject(str);
                    System.out.println("STATUS------------------->" + jsonObject.getBoolean("status"));
                    //flag = jsonObject.getInt("status");
                    //setFlag(1);
                    //System.out.println("方法内的--->" + getFlag());
                    //setFlag(jsonObject.getInt("status"));
                    // flag = getBool(str);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //判断是老师还是学生
    public void setIsWho(int isWho) {
        this.isWho = isWho;
    }

    public int getIsWho() {
        return this.isWho;
    }

    //
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return this.groupId;
    }

    //显示不显示名字
    public void setShow_name(String show_name) {
        this.show_name = show_name;
    }

    public String getShow_name() {
        return this.show_name;
    }
    //

    public Date getDate() {
        return this.date;
    }

    public void setDiscuss(String discuss) {
        this.discuss = discuss;
    }

    public String getDiscuss() {
        return this.discuss;
    }


    private class Receive extends Thread {
        private final String url = "http://115.28.80.81/app/check.php";
        private String date = null;
        private Date _date = null;

        @Override
        public void run() {


            try {
                String date = simpleDateFormat.format(_date);
                String unix = simpleDateFormat.parse(date).getTime() + "";

                while (true) {

                    Thread.sleep(3000);

                    System.out.println("循环接收消息");

                    HttpPost request = new HttpPost(url);

                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("action", "commit"));
                    params.add(new BasicNameValuePair("userName", userActivity.getUsername()));
                    params.add(new BasicNameValuePair("discuss", ""));
                    params.add(new BasicNameValuePair("teacherName", "1"));
                    params.add(new BasicNameValuePair("courseId", Commit.this.getGroupId()));
                    params.add(new BasicNameValuePair("time", unix));
                    request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                    System.out.println("test ++++ " + userActivity.getUsername() + "    " + Commit.this.getGroupId());

                    HttpResponse response = new DefaultHttpClient().execute(request);
                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        String str = EntityUtils.toString(response.getEntity());
                        System.out.println("str = " + str);
                        if (!str.equals(null)) {
                            JSONArray jsonArray = new JSONArray(str);

                            for (int x = 0; x < jsonArray.length(); x++) {


                                JSONObject jsonObject = jsonArray.getJSONObject(x);

                                String discuss = jsonObject.getString("discuss");
                                String show_name = jsonObject.getString("userName");
                                String time = formatData("yyyy-MM-dd HH:mm:ss", jsonObject.getLong("time"));

                                if (Commit.this.getIsWho() == 0) {
                                    Commit.this.setDiscuss(discuss);
                                    Commit.this.handler.sendEmptyMessage(0);
                                } else if (Commit.this.getIsWho() == 1) {
                                    Commit.this.setDiscuss(discuss);
                                    Commit.this.setShow_name(show_name);
                                    Commit.this.handler.sendEmptyMessage(1);
                                }
                            }
                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Receive(Date _date) {
            this._date = _date;
        }
    }

    public String formatData(String dataFormat, long timeStamp) {
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

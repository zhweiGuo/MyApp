package com.example.asus.myapp;

import com.example.asus.KMP.KMP;
import com.example.asus.myapp.DBOperate.DBContext;
import com.example.asus.myapp.DBOperate.DatabanseOperate;

import com.example.asus.myapp.SocketFunction.SocketFunction;
import com.example.asus.myapp._Commit.*;


import android.content.Intent;

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

import java.io.OutputStream;
import java.net.Socket;
import java.text.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Commit extends AppCompatActivity {
    final String[] data = new String[]{"滚", "cao", "操", "滚蛋", "滚犊子",
            "傻瓜", "傻逼", "变态", "蠢",
            "草", "尼玛", "你妈", "你他妈",
            "妈的", "操你妈", "日", "日你妹", "问候你祖宗", "去死", "去死吧", "脑子有病", "猪脑",
            "奶", "奶奶的", "大爷", "畜生", "扑街",
            "猪", "傻", "妈的智障", "妈","混蛋","你爹","你妹","爸"};
    //"115.28.80.81" 12345"192.168.56.1"
    private final static String URL = "115.28.80.81";
    private final static int PRO = 12345;



    private Toolbar toolbar = null;
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

    private  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //显示ListView
                    MyMessage ms = new MyMessage(Commit.this.getDiscuss(), MyMessage.RECEIVE, new Date(), "", "");
                    msgList.add(ms);
                    msgAdapter.notifyDataSetChanged();
                    msgListView.setSelection(msgList.size());

                    //插入数据库
                    DBContext dbContext = new DBContext(new DatabanseOperate(),
                            "record",Commit.this,"RECEIVE"
                            ,getGroupId(),discuss,"");
                    dbContext.doStractegy(0);


                    break;
                case 1:

                    MyMessage ms1 = new MyMessage(Commit.this.getDiscuss(), MyMessage.RECEIVE, new Date(), getShow_name(), "");
                    msgList.add(ms1);
                    msgAdapter.notifyDataSetChanged();
                    msgListView.setSelection(msgList.size());

                    //插入数据库
                    DBContext dbContext1 = new DBContext(new DatabanseOperate(),
                            "record",Commit.this,"RECEIVE"
                            ,getGroupId(),discuss,getShow_name());
                    dbContext1.doStractegy(0);

                    break;


                case 2:
                    Toast.makeText(Commit.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
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



        //启动子线程来接收数据
        final SocketFunction socketFunction = new SocketFunction(handler,URL,PRO);
        socketFunction.start();

        //发送弹幕，Socket编程
        this.commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {

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

                        if (!"".equals(info)) {


                            switch (getIsWho()) {   //判断是老师还是学生

                                case 0:
                                    //发送弹幕

                                    //Text text = new Text();
                                    //text.start();
                                    //SentProblem sentProblem = new SentProblem(info, unix,getGroupId(),userActivity.getUsername());
                                    //sentProblem.start();
                                    Message message = new Message();
                                    message.what = 0x123;
                                    message.obj = info;
                                    socketFunction.rehandler.sendMessage(message);
                                    //socketFunction.setSendInfo(info);

                                    MyMessage msg = new MyMessage(info, MyMessage.SEND, new Date(), "", "");
                                    msgList.add(msg);
                                    msgAdapter.notifyDataSetChanged();
                                    msgListView.setSelection(msgList.size());




                                    //插入数据库
                                    DBContext dbContext1 = new DBContext(new DatabanseOperate(),
                                            "record",Commit.this,"SEND"
                                            ,getGroupId(),info,userActivity.getUsername());
                                    dbContext1.doStractegy(0);

                                    break;

                                case 1:
                                    //发送弹幕
                                    SentProblem sentProblem1 = new SentProblem(info, unix,getGroupId(),userTeacherActivity.getUsername());
                                    sentProblem1.start();

                                    MyMessage msg1 = new MyMessage(info, MyMessage.SEND, new Date(), "", userTeacherActivity.getUsername());
                                    msgList.add(msg1);
                                    msgAdapter.notifyDataSetChanged();
                                    msgListView.setSelection(msgList.size());


                                    //插入数据库
                                    DBContext dbContext = new DBContext(new DatabanseOperate(),
                                            "record",Commit.this,"SEND"
                                            ,getGroupId(),info,userTeacherActivity.getUsername());
                                    dbContext.doStractegy(0);
                                    break;
                            }
                        } else {
                            Toast.makeText(Commit.this,"输入内容为空",Toast.LENGTH_SHORT).show();
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



        //显示聊天记录
        switch (getIsWho()) {

            case 0:
                //发送10秒轮询消息,获取收到的弹幕记录

                Receive receive = new Receive(date,userActivity.getUsername(),simpleDateFormat,date,handler);
                receive.start();
                MyDBHelp myDBHelp = new MyDBHelp(Commit.this, "record", null, 1);
                SQLiteDatabase db = myDBHelp.getReadableDatabase();
                Cursor cursor = db.query("record", new String[]{"author", "flag", "_nameId", "_name"}, null, null, null, null, null);

                //查询数据库
                //Cursor cursor = new DatabanseOperate().Query(Commit.this,"record",new String[]{"author", "flag", "_nameId", "_name"});
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
                //发送10秒轮询消息,获取收到的弹幕记录

                receive = new Receive(date,userTeacherActivity.getUsername(),simpleDateFormat,date,handler);
                receive.start();
                //查询数据库
                MyDBHelp myDBHelp2 = new MyDBHelp(Commit.this, "record", null, 1);
                SQLiteDatabase db2 = myDBHelp2.getReadableDatabase();
                Cursor cursor2 = db2.query("record", new String[]{"author", "flag", "_nameId", "_name"}, null, null, null, null, null);


                //Cursor cursor2 = new DatabanseOperate().Query(Commit.this,"record",new String[]{"author", "flag", "_nameId", "_name"});
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



    //测试线程
    private class Text extends Thread {
        @Override
        public void run() {
            try{

                Socket socket = new Socket(URL,PRO);
                socket.setSoTimeout(5000);
                OutputStream out = socket.getOutputStream();
                out.write("hello".getBytes("utf-8"));
                out.flush();

                /*BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                System.out.println("发送");
                String buff = "";

                buff = bufferedReader.readLine();
                System.out.println("接收+" + bufferedReader.readLine());

                System.out.println("服务器接收消息" + buff);

                bufferedReader.close();*/
                out.close();
                socket.close();

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

}

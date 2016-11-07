package com.example.asus.app.UI;

import android.app.TimePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import com.example.asus.KMP.KMP;
import com.example.asus.app.UI.SendDanmuActivityHelpClass.Thread.ReceiveMessage;
import com.example.asus.app.UI.SendDanmuActivityHelpClass.Thread.SendMessage;
import com.example.asus.myapp.DBOperate.DBContext;
import com.example.asus.myapp.DBOperate.DatabanseOperate;
import com.example.asus.myapp.MessageAdapter;
import com.example.asus.myapp.MyDBHelp;
import com.example.asus.myapp.MyMessage;
import com.example.asus.myapp.R;
import com.example.asus.myapp.SocketFunction.SocketFunction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.example.asus.app.UI.FunctionActivity.COURSE_NAME;
import static com.example.asus.app.UI.FunctionActivity.LESSON_NUMBER;
import static com.example.asus.app.UI.FunctionActivity.ISLESSON;
import static com.example.asus.app.UI.FunctionActivity.USER_NAME;

public class SendDanmuActivity extends AppCompatActivity implements View.OnClickListener {

    public List<MyMessage> mMessageList = new ArrayList<>();
    private MessageAdapter mMessageListAdapter;
    private Toolbar mtoolbar;
    private Button mSendButton;
    private EditText mSendContentEditText;
    private ListView mMessageListView;
    private String mLessonNumber;
    private String mUserName;
    private ReceiveMessage mReceiveMessage;
    private boolean misLesson = false;
    public static final int UPDATE_MESSAGE_LIST = 0;
    public static final int UPDATE_MESSAGE_LIST_RECIVE = 1;

    private static String SHA1 = null;//"115.28.80.81" 12345"192.168.56.1"
    private final static String URL = "115.28.80.81";
    private final static int PRO = 12345;
    public static int isWho;
    private ListView msgListView;
    private MessageAdapter msgAdapter;
    private List<MyMessage> msgList = new ArrayList<MyMessage>();

    public Handler updateListViewHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case UPDATE_MESSAGE_LIST:
                    mMessageListAdapter.notifyDataSetChanged();


                case UPDATE_MESSAGE_LIST_RECIVE :

                    String[] str = msg.obj.toString().split(":");//冒号分割 str[0] = 用户名 str[1]=消息

                    MyMessage myMessage = new MyMessage(MyMessage.RECEIVE, str[1],
                            new SimpleDateFormat("MM-dd EEE HH:mm", new Locale("ZH", "CN"))
                                    .format(new Date()), str[0],"");
                    mMessageList.add(myMessage);
                    updateListViewHandler.sendEmptyMessage(UPDATE_MESSAGE_LIST);

                    //插入数据库
                    DBContext dbContext1 = new DBContext(new DatabanseOperate(),
                            "record",SendDanmuActivity.this,"RECEIVE"
                            ,mLessonNumber,str[1],str[0]);//-----
                    dbContext1.doStractegy(0);
                    break;
            }
        }
    };

    //启动子线程来接收数据
    final SocketFunction socketFunction = new SocketFunction(updateListViewHandler,URL,PRO);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_send_danmu);
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        mSendButton = (Button) findViewById(R.id.send_button);
        mSendContentEditText = (EditText) findViewById(R.id.send_content_editText);
        mMessageListView = (ListView) findViewById(R.id.msg_list_view);
        mMessageListAdapter = new MessageAdapter(this, R.layout.message_item1, mMessageList);

        initActivityLayout();
        mSendButton.setOnClickListener(this);
        mMessageListView.setAdapter(mMessageListAdapter);


        msgAdapter = new MessageAdapter(SendDanmuActivity.this, R.layout.message_item1, msgList);
        msgListView = (ListView) findViewById(R.id.msg_list_view);
        msgListView.setAdapter(msgAdapter);

        //开启接受弹幕信息的线程
        String dateString = new SimpleDateFormat("MM-dd EEE HH:mm", new Locale("ZH", "CN"))
                .format(new Date());
        mReceiveMessage = new ReceiveMessage(mLessonNumber, dateString, this, mUserName);
        mReceiveMessage.start();


        socketFunction.start();


        //显示聊天记录
        switch (isWho) {

            case 0:
                //发送10秒轮询消息,获取收到的弹幕记录

                //Receive receive = new Receive(date,userActivity.getUsername(),simpleDateFormat,date,handler);
                //receive.start();
                MyDBHelp myDBHelp = new MyDBHelp(SendDanmuActivity.this, "record", null, 1);
                SQLiteDatabase db = myDBHelp.getReadableDatabase();
                Cursor cursor = db.query("record", new String[]{"author", "flag", "_nameId", "_name"}, null, null, null, null, null);

                //查询数据库
                //Cursor cursor = new DatabanseOperate().Query(Commit.this,"record",new String[]{"author", "flag", "_nameId", "_name"});
                while (cursor.moveToNext()) {
                    String author = cursor.getString(cursor.getColumnIndex("author"));
                    String _flag = cursor.getString(cursor.getColumnIndex("flag"));
                    String _nameId = cursor.getString(cursor.getColumnIndex("_nameId"));
                    String _name = cursor.getString(cursor.getColumnIndex("_name"));
                    if (mLessonNumber.equals(_nameId)) {
                        if ("SEND".equals(author)) {
                            MyMessage myMessage = new MyMessage(MyMessage.SEND, _flag,
                                    new SimpleDateFormat("MM-dd EEE HH:mm", new Locale("ZH", "CN"))
                                            .format(new Date()), "","");
                            mMessageList.add(myMessage);
                            updateListViewHandler.sendEmptyMessage(UPDATE_MESSAGE_LIST);
                        } else if ("RECEIVE".equals(author)) {
                            MyMessage myMessage = new MyMessage(MyMessage.RECEIVE, _flag,
                                    new SimpleDateFormat("MM-dd EEE HH:mm", new Locale("ZH", "CN"))
                                            .format(new Date()), "","");
                            mMessageList.add(myMessage);
                            updateListViewHandler.sendEmptyMessage(UPDATE_MESSAGE_LIST);
                        }
                    }
                }
                //关闭数据库
                //cursor.moveToLast();
                db.close();
                break;

            case 1:
                //发送10秒轮询消息,获取收到的弹幕记录

                //receive = new Receive(date,userTeacherActivity.getUsername(),simpleDateFormat,date,handler);
                //receive.start();
                //查询数据库
                MyDBHelp myDBHelp2 = new MyDBHelp(SendDanmuActivity.this, "record", null, 1);
                SQLiteDatabase db2 = myDBHelp2.getReadableDatabase();
                Cursor cursor2 = db2.query("record", new String[]{"author", "flag", "_nameId", "_name"}, null, null, null, null, null);


                //Cursor cursor2 = new DatabanseOperate().Query(Commit.this,"record",new String[]{"author", "flag", "_nameId", "_name"});
                while (cursor2.moveToNext()) {
                    String author = cursor2.getString(cursor2.getColumnIndex("author"));
                    String _flag = cursor2.getString(cursor2.getColumnIndex("flag"));
                    String _nameId = cursor2.getString(cursor2.getColumnIndex("_nameId"));
                    String _name = cursor2.getString(cursor2.getColumnIndex("_name"));
                    if (mLessonNumber.equals(_nameId)) {
                        if ("SEND".equals(author)) {
                            MyMessage myMessage = new MyMessage(MyMessage.SEND, _flag,
                                    new SimpleDateFormat("MM-dd EEE HH:mm", new Locale("ZH", "CN"))
                                            .format(new Date()), "",_name);
                            mMessageList.add(myMessage);
                            updateListViewHandler.sendEmptyMessage(UPDATE_MESSAGE_LIST);
                        } else if ("RECEIVE".equals(author)) {
                            MyMessage myMessage = new MyMessage(MyMessage.RECEIVE, _flag,
                                    new SimpleDateFormat("MM-dd EEE HH:mm", new Locale("ZH", "CN"))
                                            .format(new Date()),_name,"");
                            mMessageList.add(myMessage);
                            updateListViewHandler.sendEmptyMessage(UPDATE_MESSAGE_LIST);
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
    public void onClick(View v) {
        Log.i("/////发送弹幕的活动", "点击的send按钮");
        String content = mSendContentEditText.getText().toString();
        if ((!TextUtils.isEmpty(content)) && (!haveSensitiveWords(content))) {

            if (! misLesson) {
                showTimePickerDialog();
            }

            String dateString = new SimpleDateFormat("MM-dd EEE HH:mm", new Locale("ZH", "CN"))
                    .format(new Date());
            /*SendMessage sendMessage = new SendMessage(content, dateString, this,
                    mUserName, mLessonNumber);
            sendMessage.start();*/
            content = getSHA1()+":"+content+":"+mUserName;

            Message message = new Message();
            message.what = 0x123;
            message.obj = content;
            socketFunction.rehandler.sendMessage(message);

            MyMessage myMessage = new MyMessage(MyMessage.SEND, content, dateString, "", mUserName);
            mMessageList.add(myMessage);
            updateListViewHandler.sendEmptyMessage(UPDATE_MESSAGE_LIST);
        }
    }

    private boolean haveSensitiveWords(String content) {
        boolean haveSensitive = false;
        for (int i = 0; i < KMP.SENSITIVE_WORDS.length; i++) {
            String t = KMP.SENSITIVE_WORDS[i]; // 模式串
            char[] ss = content.toCharArray();
            char[] tt = t.toCharArray();
            int result = KMP.KMP_Index(ss, tt); //result == -1表示没有敏感词
            if (result != -1) {
                haveSensitive = true;
                break;
            }
        }
        return haveSensitive;
    }

    /**
     * 如果是发送会话组的通知，调用此方法显示时间选择会话框
     * @return Calendar
     */
    private Calendar showTimePickerDialog() {
        final Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        new TimePickerDialog(SendDanmuActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
            }
        }, hour, minute, true).show();
        return calendar;
    }

    private void initActivityLayout() {
        Bundle data = getIntent().getExtras();
        misLesson = data.getBoolean(ISLESSON);
        mLessonNumber = String.valueOf(data.getInt(LESSON_NUMBER));
        mtoolbar.setTitle(data.getString(COURSE_NAME));
        mUserName = data.getString(USER_NAME);
        Log.i("////发送弹幕的活动", mUserName);
    }


    //接受SHA1密匙
    public void setSHA1(String SHA1){
        this.SHA1 = SHA1;
    }
    public String getSHA1(){
        return this.SHA1;
    }

}

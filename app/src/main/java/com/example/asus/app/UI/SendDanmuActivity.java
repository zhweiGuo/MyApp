package com.example.asus.app.UI;

import android.app.TimePickerDialog;
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
import com.example.asus.myapp.MessageAdapter;
import com.example.asus.myapp.MyMessage;
import com.example.asus.myapp.R;

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

        //开启接受弹幕信息的线程
        String dateString = new SimpleDateFormat("MM-dd EEE HH:mm", new Locale("ZH", "CN"))
                .format(new Date());
        mReceiveMessage = new ReceiveMessage(mLessonNumber, dateString, this, mUserName);
        mReceiveMessage.start();

    }

    public Handler updateListViewHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_MESSAGE_LIST:
                    Log.e("/////handler//////", "handler执行");
                    mMessageListAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onClick(View v) {
        Log.e("/////发送弹幕的活动", "点击的send按钮");
        String content = mSendContentEditText.getText().toString();
        if ((!TextUtils.isEmpty(content)) && (!haveSensitiveWords(content))) {

            if (! misLesson) {
                showTimePickerDialog();
            }

            String dateString = new SimpleDateFormat("MM-dd EEE HH:mm", new Locale("ZH", "CN"))
                    .format(new Date());
            SendMessage sendMessage = new SendMessage(content, dateString, this,
                    mUserName, mLessonNumber);
            Log.e("/////发送弹幕的活动", "线程开始");
            sendMessage.start();
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
    }
}

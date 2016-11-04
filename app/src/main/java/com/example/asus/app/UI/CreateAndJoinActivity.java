package com.example.asus.app.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asus.app.UI.CreateAndJoinActivityHelperClass.Thread.AddClass;
import com.example.asus.app.UI.CreateAndJoinActivityHelperClass.Thread.AddTalk;
import com.example.asus.app.UI.CreateAndJoinActivityHelperClass.Thread.CreateClass;
import com.example.asus.app.UI.CreateAndJoinActivityHelperClass.Thread.CreateTalk;
import com.example.asus.myapp.R;

import static com.example.asus.app.UI.FunctionActivity.ACTIVITY_TYPE;
import static com.example.asus.app.UI.FunctionActivity.CREATECONVERSATION;
import static com.example.asus.app.UI.FunctionActivity.CREATECOURSE;
import static com.example.asus.app.UI.FunctionActivity.JOINCONVERSATION;
import static com.example.asus.app.UI.FunctionActivity.JOINCOURSE;
import static com.example.asus.app.UI.FunctionActivity.USER_NAME;

public class CreateAndJoinActivity extends AppCompatActivity implements View.OnClickListener {

    private int mActivityType;
    private String mUserName;
    private EditText mEditText;
    private Button mButton;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_and_join);

        mEditText = (EditText) findViewById(R.id.join_conversation_edit);
        mButton = (Button) findViewById(R.id.join_conversation_btn);
        mTitle = (TextView) findViewById(R.id.toolbar_danmu);
        mButton.setOnClickListener(this);

        initLayout();
    }


    @Override
    public void onClick(View v) {
        String number = mEditText.getText().toString();
        if (!TextUtils.isEmpty(number)) {
            if (mActivityType == JOINCOURSE) {
                AddClass addClass = new AddClass(number, this);
                addClass.start();
            } else if (mActivityType == JOINCONVERSATION) {
                AddTalk addTalk = new AddTalk(number, this);
                addTalk.start();
            } else if (mActivityType == CREATECOURSE) {
                CreateClass createClass = new CreateClass(number, mUserName,this);
                createClass.start();
            } else if (mActivityType == CREATECONVERSATION) {
                CreateTalk createTalk= new CreateTalk(number, mUserName,this);
                createTalk.start();
            }
        }
    }

    private void initLayout() {
        Bundle data = getIntent().getExtras();
//        if (data != null)
        mActivityType = data.getInt(ACTIVITY_TYPE);
        mUserName = data.getString(USER_NAME);

        if (mActivityType == CREATECOURSE) {
            mTitle.setText("创建课程");
            mEditText.setHint("课程名称");
            mButton.setText("创建");
        } else if (mActivityType == JOINCOURSE) {
            mTitle.setText("加入课程");
            mEditText.setHint("课程编号");
        }else if (mActivityType == CREATECONVERSATION) {
            mTitle.setText("创建会话");
            mEditText.setHint("会话名称");
            mButton.setText("创建");
        }else if (mActivityType == JOINCONVERSATION) {
            mTitle.setText("加入会话");
            mEditText.setHint("会话编号");
        }
    }
}

package com.example.asus.app.UI;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.myapp.R;
import com.example.asus.myapp.RegisterTeacher;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.asus.app.UI.FunctionActivity.USER_NAME;
import static com.example.asus.app.UI.FunctionActivity.USER_NUMBER;
import static com.example.asus.app.UI.Login2Activity.PEOPLE;

public class RegisterUserActivity extends AppCompatActivity {


    private EditText mUserNameView = null;
    private EditText mUserIdView = null;
    private EditText mPassWordView = null;
    private EditText mConfirmingPassWordView = null;
    private Button mRegisterButton = null;
    public static final int SHOW_ERROR_INFORMATION = 1;
    private String mPeople;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_ERROR_INFORMATION:
                    Toast.makeText(RegisterUserActivity.this, "出问题了，注册失败",Toast.LENGTH_SHORT)
                            .show();
                    mRegisterButton.setText("请稍后···");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mUserNameView = (EditText) findViewById(R.id.name);
        mUserIdView = (EditText) findViewById(R.id.school_number);
        mPassWordView = (EditText) findViewById(R.id.password);
        mConfirmingPassWordView = (EditText) findViewById(R.id.password_confirm);
        mRegisterButton = (Button) findViewById(R.id.sign_in_button);
        getUserInformation();

        mConfirmingPassWordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.login || actionId == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

    }

    private void attemptRegister() {

        mUserNameView.setError(null);
        mUserIdView.setError(null);
        mPassWordView.setError(null);
        mConfirmingPassWordView.setError(null);


        String name = mUserNameView.getText().toString();
        String id = mUserIdView.getText().toString();
        String pswd = mPassWordView.getText().toString();
        String pswdConfirm = mConfirmingPassWordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(name)) {
            mUserNameView.setError("此项必填！");
            focusView = mUserNameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(id)) {
            mUserIdView.setError("此项必填！");
            focusView = mUserIdView;
            cancel = true;
        }

        if (pswd.length() < 6) {
            mPassWordView.setError("密码长度太短！");
            focusView = mPassWordView;
            cancel = true;
        }

        if (!pswd.equals(pswdConfirm)) {
            mConfirmingPassWordView.setError("两次密码不一致！");
            focusView = mConfirmingPassWordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            new RegisterThread(name, pswd, id, mPeople).start();
//            if (getSelected() == 1) {
//                showProgress(true);
//                RegisterTeacher.RegisterThread sentInfo = new RegisterTeacher.RegisterThread(name, pswd, id);
//                sentInfo.start();
//            } else if (getSelected() == 0) {
//                showProgress(true);
//                RegisterTeacher.SentInfo_Student sentInfo_student = new RegisterTeacher.SentInfo_Student(name, pswd, id);
//                sentInfo_student.start();
//            }
        }
    }

    private void getUserInformation() {
        mPeople = getIntent().getExtras().getString(PEOPLE);
    }
    private class RegisterThread extends Thread {
        private static final String url = "http://115.28.80.81/app/check.php";
        private String name = null;
        private String password = null;
        private String number = null;
        private String people;

        public RegisterThread(String name, String password, String number, String people) {
            this.name = name;
            this.password = password;
            this.number = number;
            this.people = people;
        }

        @Override
        public void run() {
            try {
                boolean succeedRegister = false;
                HttpPost request = new HttpPost(url);

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("flag", people));
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("id", number));
                params.add(new BasicNameValuePair("action", "register"));
                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                HttpResponse response = new DefaultHttpClient().execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String str = EntityUtils.toString(response.getEntity());
                    JSONObject jsonObject = new JSONObject(str);
                    succeedRegister = jsonObject.getBoolean("status");
                    Log.e("////////线程/////////", str);
                    if (succeedRegister) {
                        Intent intent = new Intent(RegisterUserActivity.this, FunctionActivity.class);
                        intent.putExtra(USER_NAME, name);
                        intent.putExtra(USER_NUMBER, number);

                        startActivity(intent);
                        finish();
                    } else {
                        mHandler.sendEmptyMessage(SHOW_ERROR_INFORMATION);
                    }

                }

            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
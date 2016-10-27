package com.example.asus.myapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.app.UI.Login1;

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

public class RegisterTeacher extends AppCompatActivity {

    private final String teacher_info = "teacher_info";
    private static final int LoginFlag = 1;
    private EditText mUserNameView = null;
    private EditText mUserIdView = null;
    private EditText mPassWordView = null;
    private EditText mConfirmingPassWordView = null;
    private Button mSignInButton = null;
    private String tempID = null;
    private boolean flag = false;
    private View mLoginFormView;
    private ProgressBar mProgressView;

    private static int selected = -1;

    private UserTeacherActivity userTeacherActivity = new UserTeacherActivity();
    private UserActivity userActivity = new UserActivity();

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(RegisterTeacher.this, "注册成功", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(RegisterTeacher.this, "注册失败", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_student_login);

        this.mUserNameView = (EditText) super.findViewById(R.id.name);
        this.mUserIdView = (EditText) super.findViewById(R.id.school_number);
        this.mPassWordView = (EditText) super.findViewById(R.id.password);
        this.mConfirmingPassWordView = (EditText) super.findViewById(R.id.password_confirm);
        this.mSignInButton = (Button) super.findViewById(R.id.sign_in_button);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = (ProgressBar) findViewById(R.id.login_progress);

        mConfirmingPassWordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.login || actionId == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        if (flag) {

            SharedPreferences sharedPreferences = getSharedPreferences(teacher_info, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Username", mUserNameView.getText().toString());
            editor.putString("Passworld", mPassWordView.getText().toString());
            editor.putString("StudentId", mUserIdView.getText().toString());
            editor.commit();
            Intent intent = new Intent(RegisterTeacher.this, Login1.class);
            startActivity(intent);
            finish();
        }


        System.out.println("--------" + getSelected());

    }


    private class SentInfo extends Thread {
        private String tempname = null;
        private String temppass = null;
        private String tempId = null;

        public SentInfo(String tempname, String temppass, String tempId) {
            this.tempId = tempId;
            this.tempname = tempname;
            this.temppass = temppass;
        }


        @Override
        public void run() {

            final String url = "http://115.28.80.81/app/check.php";

            try {

                HttpPost request = new HttpPost(url);

                List<NameValuePair> params = new ArrayList<>();

                //添加老师的标志 "ls"
                params.add(new BasicNameValuePair("flag", String.valueOf(getSelected())));
                params.add(new BasicNameValuePair("name", tempname));
                params.add(new BasicNameValuePair("password", temppass));
                params.add(new BasicNameValuePair("id", tempID));
                params.add(new BasicNameValuePair("action", "register"));
                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                HttpResponse response = new DefaultHttpClient().execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String str = EntityUtils.toString(response.getEntity());
                    System.out.println("JSON1-------->" + str);
                    JSONObject jsonObject = new JSONObject(str);
                    flag = jsonObject.getBoolean("status");

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (flag == true) {

                SharedPreferences sharedPreferences = getSharedPreferences(teacher_info, Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Username", tempname);
                editor.putString("Passworld", temppass);
                editor.putString("StudentId", tempID);
                editor.commit();

                userTeacherActivity.setUserID(tempId);

                RegisterTeacher.this.handler.sendEmptyMessage(0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000); //休眠2秒
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(RegisterTeacher.this, Login1.class);
                        startActivity(intent);
                        finish();
                    }
                }).start();

            } else {
                RegisterTeacher.this.handler.sendEmptyMessage(1);
            }
        }
    }


    //注册学生
    private class SentInfo_Student extends Thread {
        private String tempname = null;
        private String temppass = null;
        private String tempId = null;

        public SentInfo_Student(String tempname, String temppass, String tempId) {
            this.tempId = tempId;
            this.tempname = tempname;
            this.temppass = temppass;
        }


        @Override
        public void run() {

            final String url = "http://115.28.80.81/app/check.php";

            try {

                HttpPost request = new HttpPost(url);

                List<NameValuePair> params = new ArrayList<>();

                //添加老师的标志 "ls"
                params.add(new BasicNameValuePair("flag", String.valueOf(getSelected())));
                params.add(new BasicNameValuePair("name", tempname));
                params.add(new BasicNameValuePair("password", temppass));
                params.add(new BasicNameValuePair("id", tempID));
                params.add(new BasicNameValuePair("action", "register"));
                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                HttpResponse response = new DefaultHttpClient().execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String str = EntityUtils.toString(response.getEntity());
                    System.out.println("JSON1-------->" + str);
                    JSONObject jsonObject = new JSONObject(str);
                    flag = jsonObject.getBoolean("status");

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (flag == true) {

                SharedPreferences sharedPreferences = getSharedPreferences(teacher_info, Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Username", tempname);
                editor.putString("Passworld", temppass);
                editor.putString("StudentId", tempID);
                editor.commit();

                userActivity.setUserID(tempId);

                RegisterTeacher.this.handler.sendEmptyMessage(0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000); //休眠2秒
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(RegisterTeacher.this, Login1.class);
                        startActivity(intent);
                        finish();
                    }
                }).start();

            } else {
                RegisterTeacher.this.handler.sendEmptyMessage(1);
            }
        }
    }

    private void attemptLogin() {

        mUserNameView.setError(null);
        mUserIdView.setError(null);
        mPassWordView.setError(null);
        mConfirmingPassWordView.setError(null);


        String name = mUserNameView.getText().toString();
        String id = mUserIdView.getText().toString();
        String pswd = mPassWordView.getText().toString();
        String pswdConfirm = mConfirmingPassWordView.getText().toString();

//        System.out.println(name + "--->>>" + id + "--->>>" + pswd + "--->>>" + pswdConfirm);

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

            if (getSelected() == 1) {
                showProgress(true);
                SentInfo sentInfo = new SentInfo(name, pswd, id);
                sentInfo.start();
            } else if (getSelected() == 0) {
                showProgress(true);
                SentInfo_Student sentInfo_student = new SentInfo_Student(name, pswd, id);
                sentInfo_student.start();
            }
        }
    }


    public void setSelected(int selected) {
        this.selected = selected;
    }

    public int getSelected() {
        return this.selected;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });//-------------输入框所在的mLoginFormView会被隐藏

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });//-------------进度条会被显示
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}

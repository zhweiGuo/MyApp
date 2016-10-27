package com.example.asus.app.UI;

/**
 * Login1
 *
 * @author: Allen
 * @time: 2016/10/20 23:17
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.MediaRouter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.asus.myapp.Check;
import com.example.asus.myapp.MainActivity;
import com.example.asus.myapp.MyDBHelp;
import com.example.asus.myapp.R;
import com.example.asus.myapp.RegisterTeacher;
import com.example.asus.myapp.UserActivity;
import com.example.asus.myapp.UserTeacherActivity;

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

public class Login1 extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{
    private static final String URL = "http://115.28.80.81";


    private int selectPeople = 1;
    private MainActivity mainActivity = null;
    private UserTeacherActivity userTeacherActivity = new UserTeacherActivity();
    private UserActivity userActivity = new UserActivity();
    private RegisterTeacher registerTeacher = new RegisterTeacher();

    private EditText username = null;
    private EditText userpass = null;
    private Button login = null;
    private Button register = null;
    private Check check = null;
    private Spinner spinner = null;
    private RadioGroup mRadioGroup;

    private View mLoginFormView;
    private ProgressBar mProgressView = null;

    private String DatabaseName = null;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){

                case -1 :

                    Toast.makeText(Login1.this, "用户名或密码、选项错误", Toast.LENGTH_SHORT).show();
                    break;

                case 0:
                    Toast.makeText(Login1.this, "学生登陆", Toast.LENGTH_SHORT).show();
                    mainActivity = new MainActivity();
                    mainActivity.setDatabaseName(userActivity.getUsername());
                    Intent intent = new Intent(Login1.this, UserActivity.class);
                    startActivity(intent);
                    finish();
                    Log.d("HTTP", "POST--->  " + 1);
                    break;
                case 1:
                    Toast.makeText(Login1.this, "老师登陆", Toast.LENGTH_SHORT).show();
                    mainActivity = new MainActivity();
                    mainActivity.setDatabaseName(userTeacherActivity.getUsername());
                    Intent intent1 = new Intent(Login1.this, UserTeacherActivity.class);
                    startActivity(intent1);
                    finish();
                    Log.d("HTTP", "POST--->  " + 2);

                    break;

                case 2 :
                    MyDBHelp myDBHelp = new MyDBHelp(Login1.this,getDatabaseName(),null,1);
                    SQLiteDatabase db = myDBHelp.getReadableDatabase();

                    MyDBHelp myDBHelp1 = new MyDBHelp(Login1.this,"record",null,1);
                    SQLiteDatabase db1 = myDBHelp1.getReadableDatabase();

                    MyDBHelp myDBHelp2 = new MyDBHelp(Login1.this,"notification",null,1);
                    SQLiteDatabase db2 = myDBHelp2.getReadableDatabase();
                    Log.i("DB","RUN HERE");
                    break;
            }
        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.login1);

        this.mainActivity = new MainActivity();
        this.username = (EditText) super.findViewById(R.id.edtuser1);
        this.userpass = (EditText) super.findViewById(R.id.edtpsd1);
        this.login = (Button) super.findViewById(R.id.login1);
        this.register = (Button) super.findViewById(R.id.register1);
//        teacherBtn = (RadioButton) findViewById(R.id.teacher_radio_btn);
//        studentBtn = (RadioButton) findViewById(R.id.student_radio_btn);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mProgressView = (ProgressBar)findViewById(R.id.login1_activity_progress);
        mLoginFormView = findViewById(R.id.name_login1_form);
        mRadioGroup.setOnCheckedChangeListener(this);
//        if (login != null)
        this.login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name = "";
                name = username.getText().toString();
                String pass = "";
                pass = userpass.getText().toString();

                if ("".equals(name) && "".equals(pass)) {
                    Toast.makeText(Login1.this, "用户名或者密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    mainActivity.setTeacherName(name);
                    Login1.PostThread postThread = new Login1.PostThread(name, pass);
                    postThread.start();
//                    mProgressView.setVisibility(View.VISIBLE);
                    showProgress(true);

                }

            }
        });


        this.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Dialog dialog = new AlertDialog.Builder(Login1.this)
                        .setTitle("                        人群")
                        .setItems(R.array.people, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position) {
                                switch (position) {
                                    case 0:
                                        registerTeacher.setSelected(1);
                                        Intent intent1 = new Intent(Login1.this, RegisterTeacher.class);
                                        startActivity(intent1);
                                        finish();
                                        break;
                                    case 1:
                                        registerTeacher.setSelected(0);
                                        Intent intent2 = new Intent(Login1.this, RegisterTeacher.class);
                                        startActivity(intent2);
                                        finish();
                                        break;
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create();

                dialog.show();

            }
        });

    }

    public void setDatabaseName(String DatabaseName){
        this.DatabaseName = DatabaseName;
    }
    public String getDatabaseName(){
        return this.DatabaseName;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.teacher_radio_btn) {
            selectPeople = 1;
            mainActivity.setSelected(selectPeople);
        } else if (checkedId == R.id.student_radio_btn) {
            selectPeople = 0;
            mainActivity.setSelected(selectPeople);
        }
    }

    private class PostThread extends Thread {
        private String name;
        private String pass;
        private  boolean flag = false;


        public PostThread(String name, String pass) {
            this.name = name;
            this.pass = pass;
        }

        @Override
        public void run() {
            final String url = "http://115.28.80.81/app/check.php";

            try {

                HttpPost request = new HttpPost(url);

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("action","sign"));
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("password", pass));
                params.add(new BasicNameValuePair("flag",String.valueOf(selectPeople)));
                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                HttpResponse response = new DefaultHttpClient().execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String str = EntityUtils.toString(response.getEntity());
                    System.out.println("JSON1-------->" + str);
                    JSONObject jsonObject = new JSONObject(str);
                    System.out.println("STATUS------------------->"+jsonObject.getBoolean("status"));
                    flag = jsonObject.getBoolean("status");


                }else{
                    //Toast.makeText(Login.this,"服务器未响应",Toast.LENGTH_SHORT).show();
                }

                if(flag == true){

                    setDatabaseName(name);
                    Login1.this.handler.sendEmptyMessage(2);
                    try {
                        Thread.sleep(1000);
                        mainActivity.setLoginFlag(1);
                        mainActivity.setJoin(1);

                        if (selectPeople == 0) {
                            userActivity.setUsername(name);
                            Login1.this.handler.sendEmptyMessage(0);
                            Intent intent = new Intent(Login1.this, UserActivity.class);
                            startActivity(intent);
                            finish();
                            Log.d("HTTP", "POST--->  " + 1);
                        } else if(selectPeople == 1) {
                            userTeacherActivity.setUsername(name);
                            Login1.this.handler.sendEmptyMessage(1);
                            Intent intent = new Intent(Login1.this, UserTeacherActivity.class);
                            startActivity(intent);
                            finish();
                            Log.d("HTTP", "POST--->  " + 2);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Login1.this.handler.sendEmptyMessage(-1);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

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




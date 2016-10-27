package com.example.asus.myapp;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Login extends AppCompatActivity {
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

    private ProgressBar progressBar = null;

    private String DatabaseName = null;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){

                case -1 :

                    Toast.makeText(Login.this, "用户名或密码、选项错误", Toast.LENGTH_SHORT).show();
                    break;

                case 0:
                    Toast.makeText(Login.this, "学生登陆", Toast.LENGTH_SHORT).show();
                    mainActivity = new MainActivity();
                    mainActivity.setDatabaseName(userActivity.getUsername());
                    Intent intent = new Intent(Login.this, UserActivity.class);
                    startActivity(intent);
                    finish();
                    Log.d("HTTP", "POST--->  " + 1);
                    break;
                case 1:
                    Toast.makeText(Login.this, "老师登陆", Toast.LENGTH_SHORT).show();
                    mainActivity = new MainActivity();
                    mainActivity.setDatabaseName(userTeacherActivity.getUsername());
                    Intent intent1 = new Intent(Login.this, UserTeacherActivity.class);
                    startActivity(intent1);
                    finish();
                    Log.d("HTTP", "POST--->  " + 2);

                    break;

                case 2 :
                    MyDBHelp myDBHelp = new MyDBHelp(Login.this,getDatabaseName(),null,1);
                    SQLiteDatabase db = myDBHelp.getReadableDatabase();

                    MyDBHelp myDBHelp1 = new MyDBHelp(Login.this,"record",null,1);
                    SQLiteDatabase db1 = myDBHelp1.getReadableDatabase();

                    MyDBHelp myDBHelp2 = new MyDBHelp(Login.this,"notification",null,1);
                    SQLiteDatabase db2 = myDBHelp2.getReadableDatabase();
                    Log.i("DB","RUN HERE");
                    break;
            }
        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.login);

        Resources resources = getResources();
        Drawable drawable = resources.getDrawable(R.drawable.registerWindowBackground);
        this.getWindow().setBackgroundDrawable(drawable);

        this.mainActivity = new MainActivity();
        this.username = (EditText) super.findViewById(R.id.edtuser);
        this.userpass = (EditText) super.findViewById(R.id.edtpsd);
        this.login = (Button) super.findViewById(R.id.login);
        this.register = (Button) super.findViewById(R.id.register);
        this.spinner = (Spinner)super.findViewById(R.id.people);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Login.this,R.array.people,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        selectPeople = 1;   //老师
                        mainActivity.setSelected(selectPeople);
                        //Toast.makeText(Login.this,"-1",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        selectPeople = 0;
                        mainActivity.setSelected(selectPeople);
                        //Toast.makeText(Login.this,"1",Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        this.progressBar = (ProgressBar)super.findViewById(R.id.prog);


        this.login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name = "";
                name = username.getText().toString();
                String pass = "";
                pass = userpass.getText().toString();

                if ("".equals(name) && "".equals(pass)) {
                    Toast.makeText(Login.this, "用户名或者密码不能为空", Toast.LENGTH_SHORT).show();
                } else {

                    //**********************************************

                    //if (Login.this.registerStudent.getLoginFlag() == 2) {
                    mainActivity.setTeacherName(name);

                    PostThread postThread = new PostThread(name, pass);
                    postThread.start();

                    progressBar.setVisibility(View.VISIBLE);

                }

            }
        });


        this.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Dialog dialog = new AlertDialog.Builder(Login.this)
                        .setTitle("                        人群")
                        .setItems(R.array.people, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position) {
                                switch (position) {
                                    case 0:
                                        registerTeacher.setSelected(1);
                                        Intent intent1 = new Intent(Login.this, RegisterTeacher.class);
                                        startActivity(intent1);
                                        finish();
                                        break;
                                    case 1:
                                        registerTeacher.setSelected(0);
                                        Intent intent2 = new Intent(Login.this, RegisterTeacher.class);
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
                    Login.this.handler.sendEmptyMessage(2);
                    try {
                        Thread.sleep(1000);
                        mainActivity.setLoginFlag(1);
                        mainActivity.setJoin(1);

                        if (selectPeople == 0) {
                            userActivity.setUsername(name);
                            Login.this.handler.sendEmptyMessage(0);
                            Intent intent = new Intent(Login.this, UserActivity.class);
                            startActivity(intent);
                            finish();
                            Log.d("HTTP", "POST--->  " + 1);
                        } else if(selectPeople == 1) {
                            userTeacherActivity.setUsername(name);
                            Login.this.handler.sendEmptyMessage(1);
                            Intent intent = new Intent(Login.this, UserTeacherActivity.class);
                            startActivity(intent);
                            finish();
                            Log.d("HTTP", "POST--->  " + 2);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Login.this.handler.sendEmptyMessage(-1);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}



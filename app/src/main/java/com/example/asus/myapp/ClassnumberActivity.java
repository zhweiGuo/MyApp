package com.example.asus.myapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClassnumberActivity extends AppCompatActivity {
    private EditText teacher_name = null;
    private EditText class_name = null;
    private EditText class_number = null;
    private Button creat = null;
    private Button cancel = null;

    private int flag = 0;
    private UserTeacherActivity userTeacherActivity= new UserTeacherActivity();

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    userTeacherActivity.setCreatFlag(0);//设置不显现
                    Toast.makeText(ClassnumberActivity.this,"创建失败，服务器未响应",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(ClassnumberActivity.this,"创建成功 编号为：" + flag,7).show();

                    //插入本地数据库
                    MyDBHelp myDBHelp = new MyDBHelp(ClassnumberActivity.this,"info",null,1);
                    SQLiteDatabase db =  myDBHelp.getWritableDatabase();
                    //生成插入数据对象
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("_name",class_name.getText().toString());
                    contentValues.put("author",teacher_name.getText().toString());
                    contentValues.put("_nameId",flag);

                    db.insert("info",null,contentValues);
                    db.close();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                Intent intent = new Intent(ClassnumberActivity.this,UserTeacherActivity.class);
                                finish();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                case 2:
                    break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classnumber_layout);
        this.cancel = (Button)super.findViewById(R.id.cancel);
        this.creat = (Button)super.findViewById(R.id.creat);
        this.teacher_name = (EditText)super.findViewById(R.id.teacher_name);
        this.class_name = (EditText)super.findViewById(R.id.class_name);

        Resources resources = getResources();
        Drawable drawable = resources.getDrawable(R.drawable.registerWindowBackground);
        this.getWindow().setBackgroundDrawable(drawable);

        this.creat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(teacher_name.getText().toString()) || "".equals(class_name.getText().toString())){
                    Toast.makeText(ClassnumberActivity.this,"信息不能有空",Toast.LENGTH_SHORT).show();
                }else{
                    CreatClass creatClass = new CreatClass(class_name.getText().toString(),teacher_name.getText().toString());
                    creatClass.start();

                }
            }
        });

        this.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassnumberActivity.this,UserTeacherActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



    private class CreatClass extends Thread{
        private static final String url = "http://115.28.80.81/app/check.php";
        private String classname = null;
        private String teachername = null;

        public CreatClass(String classname , String teachername){
            this. classname = classname ;
            this.teachername = teachername ;
        }
        @Override
        public void run() {
            try {


                HttpPost request = new HttpPost(url);

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("action", "setCourse"));
                params.add(new BasicNameValuePair("courseName", classname));
                params.add(new BasicNameValuePair("teacherName", teachername));

                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                HttpResponse response = new DefaultHttpClient().execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String str = EntityUtils.toString(response.getEntity());
                    System.out.println("JSON1-------->" + str);
                    JSONObject jsonObject = new JSONObject(str);
                    System.out.println("STATUS------------------->" + jsonObject.getInt("status"));
                    //ClassnumberActivity.this.flag = jsonObject.getBoolean("status");
                    //setFlag(jsonObject.getInt("status"));
                    flag = jsonObject.getInt("status");
                    if(jsonObject.getInt("status")!= 0){
                        userTeacherActivity.setCreatFlag(1);//设置在开始显示
                        userTeacherActivity.setLocalname(classname);
                        userTeacherActivity.setLocalteachername(teachername);
                        ClassnumberActivity.this.handler.sendEmptyMessage(1);
                    }else{
                        ClassnumberActivity.this.handler.sendEmptyMessage(0);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void setFlag(int flag){
        this.flag = flag;
    }

    public int getFlag(){
        return this.flag;
    }

}

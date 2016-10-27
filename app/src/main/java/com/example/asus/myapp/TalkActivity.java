package com.example.asus.myapp;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class TalkActivity extends AppCompatActivity {

    private EditText talk_name = null;

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
                    Toast.makeText(TalkActivity.this,"创建失败，服务器未响应",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(TalkActivity.this,"创建成功 编号为：" + flag,Toast.LENGTH_LONG).show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                Intent intent = new Intent(TalkActivity.this,MainActivity.class);
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

    private MainActivity mainActivity = new MainActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk_layout);
        this.cancel = (Button)super.findViewById(R.id.cancel);
        this.creat = (Button)super.findViewById(R.id.creat);

        this.talk_name = (EditText)super.findViewById(R.id.talk_name);

        Resources resources = getResources();
        Drawable drawable = resources.getDrawable(R.drawable.registerWindowBackground);
        this.getWindow().setBackgroundDrawable(drawable);

        this.creat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(talk_name.getText().toString())){
                    Toast.makeText(TalkActivity.this,"信息不能有空",Toast.LENGTH_SHORT).show();
                }else{
                    CreatTalk creatTalk = new CreatTalk(talk_name.getText().toString());
                    creatTalk.start();

                }
            }
        });

        this.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TalkActivity.this,UserTeacherActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



    private class CreatTalk extends Thread{
        private static final String url = "http://115.28.80.81/app/check.php";
        private String talkname = null;


        public CreatTalk(String talkname ){
            this. talkname = talkname ;

        }
        @Override
        public void run() {
            try {


                HttpPost request = new HttpPost(url);

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("action", "addSession"));
                params.add(new BasicNameValuePair("userName", userTeacherActivity.getUser()));
                params.add(new BasicNameValuePair("groupName", talkname));

                System.out.println("user-------name = " + userTeacherActivity.getUser());
                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                HttpResponse response = new DefaultHttpClient().execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String str = EntityUtils.toString(response.getEntity());
                    System.out.println("JSON1-------->" + str);
                    JSONObject jsonObject = new JSONObject(str);
                    System.out.println("STATUS------------------->" + jsonObject.getBoolean("status"));
                    //ClassnumberActivity.this.flag = jsonObject.getBoolean("status");
                    //setFlag(jsonObject.getInt("status"));
                    //flag = jsonObject.getInt("status");
                    if(jsonObject.getBoolean("status")){
                        mainActivity.setHash_info(1);
                        TalkActivity.this.handler.sendEmptyMessage(1);
                    }else{
                        TalkActivity.this.handler.sendEmptyMessage(0);
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

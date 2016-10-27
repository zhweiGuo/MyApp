package com.example.asus.myapp;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserTeacherActivity extends AppCompatActivity {
    private Toolbar loginToolbar = null;
    private MainActivity mainActivity= null;
    private int flag = 0;
    private int talkflag = 0;

    private static ListView teacherlistviwe = null;
    private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    private ImageButton _add = null;
    private TextView userName = null;

    private static String tempname = null;
    private static String username = null;
    private static String userID = null;
    //************创建课程data
    private static int creatFlag = 0;
    private static String localname = null;
    private static String localteachername = null;
    //************************************

    private ListView mListView1;
    private ListView mListView2;
    private ListView mListView3;
    private ItemArrayAdapter mAdapter;
    private List<SettingItem> mItemList1 = new ArrayList<SettingItem>();
    private List<SettingItem> mItemList2 = new ArrayList<SettingItem>();
    private List<SettingItem> mItemList3 = new ArrayList<SettingItem>();


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:

                    Toast.makeText(UserTeacherActivity.this,"创建失败，服务器未响应",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(UserTeacherActivity.this,"创建成功 编号为：" + flag, 7).show();
                    mainActivity = new MainActivity();
                    //插入本地数据库
                    MyDBHelp myDBHelp = new MyDBHelp(UserTeacherActivity.this,mainActivity.getDatabaseName(),null,1);
                    SQLiteDatabase db =  myDBHelp.getWritableDatabase();
                    //生成插入数据对象
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("_name",getLocalname());
                    contentValues.put("author",getLocalteachername());
                    contentValues.put("_nameId",flag);
                    contentValues.put("flag","（班课）");

                    db.insert(mainActivity.getDatabaseName(),null,contentValues);
                    db.close();
                    break;
                case 2:

                    Toast.makeText(UserTeacherActivity.this,"创建成功 编号为：" + talkflag, 7).show();
                    mainActivity = new MainActivity();
                    //插入本地数据库
                    MyDBHelp myDBHelp1 = new MyDBHelp(UserTeacherActivity.this,mainActivity.getDatabaseName(),null,1);
                    SQLiteDatabase db1 =  myDBHelp1.getWritableDatabase();
                    //生成插入数据对象
                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("_name",getLocalname());
                    contentValues1.put("author",getLocalteachername());
                    contentValues1.put("_nameId",talkflag);
                    contentValues1.put("flag","（会话）");

                    db1.insert(mainActivity.getDatabaseName(),null,contentValues1);
                    db1.close();
                    break;

                case 3:
                    Toast.makeText(UserTeacherActivity.this,"原密码错误",Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(UserTeacherActivity.this,"修改成功",Toast.LENGTH_SHORT).show();

                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_test2);

        this.loginToolbar = (Toolbar)super.findViewById(R.id.logintoolbar);
        this.loginToolbar.setTitle("");
        setSupportActionBar(this.loginToolbar);

        initList();
        mListView1 = (ListView) findViewById(R.id.info_show);
        mListView2 = (ListView) findViewById(R.id.quit_list);
        mListView3 = (ListView) findViewById(R.id.creat_list);
        mAdapter = new ItemArrayAdapter(UserTeacherActivity.this, R.layout.item, mItemList1);
        mListView1.setAdapter(mAdapter);
        mAdapter = new ItemArrayAdapter(UserTeacherActivity.this, R.layout.item, mItemList2);
        mListView2.setAdapter(mAdapter);
        mAdapter = new ItemArrayAdapter(UserTeacherActivity.this,R.layout.item, mItemList3);
        mListView3.setAdapter(mAdapter);

        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0 :


                    final Dialog dialog_pass = new Dialog(UserTeacherActivity.this,R.style.refactor_pass);
                        dialog_pass.setContentView(R.layout.refactorpass);

                        Button pass_cancel = (Button) dialog_pass.findViewById(R.id.pass_cancel);
                        Button pass_sure = (Button)dialog_pass.findViewById(R.id.pass_sure);

                        pass_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog_pass.hide();
                            }
                        });

                        pass_sure.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                EditText old_pass = (EditText)dialog_pass.findViewById(R.id.oldpass);
                                EditText new_pass = (EditText)dialog_pass.findViewById(R.id.newpass);
                                EditText check_pass = (EditText)dialog_pass.findViewById(R.id.newpass_check);

                                //两次新密码输入是否正确
                                if (new_pass.getText().toString().equals(check_pass.getText().toString())){

                                    ReFactor reFactor = new ReFactor(new_pass.getText().toString(),old_pass.getText().toString());
                                    reFactor.start();
                                }else{
                                    Toast.makeText(UserTeacherActivity.this,"两次新密码不一致",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        dialog_pass.show();
                        break;
                    case 1:
                        Dialog dialog = new AlertDialog.Builder(UserTeacherActivity.this)
                                .setTitle("             注销此用户吗？")
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        mainActivity.setLoginFlag(0);
                                        mainActivity.setJoin(0);
                                        //mainActivity.getDatalist().setAdapter(null);
                                        System.out.println("****************************" + mainActivity.getJoin());
                                        Intent intent = new Intent(UserTeacherActivity.this,Login1.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).create();
                        dialog.show();
                        break;
                }
            }
        });


        mListView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position){
                    case 0 :
                        final Dialog dialog1 = new Dialog(UserTeacherActivity.this, R.style.mycreatTalk);
                        dialog1.show();
                        WindowManager windowManager = getWindowManager();
                        Display display = windowManager.getDefaultDisplay();
                        WindowManager.LayoutParams lp = dialog1.getWindow().getAttributes();
                        lp.width = (int)(display.getWidth()*0.8); //设置宽度
                        dialog1.getWindow().setAttributes(lp);

                        dialog1.setContentView(R.layout.creat_talkstyle);

                        Button cancelBut1 = (Button) dialog1.findViewById(R.id.talkcancel);
                        cancelBut1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog1.hide();
                            }
                        });
                        Button sure1 = (Button) dialog1.findViewById(R.id.talksure);
                        sure1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                EditText inputalKey = (EditText) dialog1.findViewById(R.id.inputalKey);


                                if (inputalKey.getText().toString().equals("")) {
                                    Toast.makeText(UserTeacherActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                                } else {

                                    setLocalname(inputalKey.getText().toString());
                                    setLocalteachername(UserTeacherActivity.this.getUsername());
                                    CreatTalk creatTalk = new CreatTalk(inputalKey.getText().toString());
                                    creatTalk.start();
                                }
                                dialog1.hide();
                            }
                        });


                        break;

                    case 1:
                        final Dialog dialog2 = new Dialog(UserTeacherActivity.this, R.style.mycreatClass);
                        dialog2.show();

                        WindowManager windowManager1 = getWindowManager();
                        Display display1 = windowManager1.getDefaultDisplay();
                        WindowManager.LayoutParams lp1 = dialog2.getWindow().getAttributes();
                        lp1.width = (int)(display1.getWidth()*0.8); //设置宽度
                        dialog2.getWindow().setAttributes(lp1);


                        dialog2.setContentView(R.layout.dialogstyle2);
                        Button cancelBut = (Button) dialog2.findViewById(R.id.cancel);
                        cancelBut.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog2.hide();
                            }
                        });
                        Button sure = (Button) dialog2.findViewById(R.id.sure);
                        sure.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                EditText inputKey = (EditText) dialog2.findViewById(R.id.inputKey);
                                EditText inputValue = (EditText)dialog2.findViewById(R.id.inputValue);

                                if (inputKey.getText().toString().equals("") && inputValue.getText().toString().equals("")) {
                                    Toast.makeText(UserTeacherActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                                } else {

                                    setLocalname(inputKey.getText().toString());
                                    setLocalteachername(inputValue.getText().toString());
                                    CreatClass creatClass = new CreatClass(inputKey.getText().toString(), inputValue.getText().toString());
                                    creatClass.start();
                                }
                                dialog2.hide();
                            }
                        });


                        break;
                }
            }
        });





        this.mainActivity = new MainActivity();

        this.userName = (TextView)super.findViewById(R.id.user);
        //this.userName.setText(getUsername());
        //setTempName(userName.getText().toString());


        //添加会话
        this._add = (ImageButton)super.findViewById(R.id._add);
        /*this._add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserTeacherActivity.this,TalkActivity.class);
                startActivity(intent);
                finish();
            }
        });*/

        this.teacherlistviwe = (ListView)super.findViewById(R.id.teacher_layout);
        this.loginToolbar.setNavigationIcon(R.drawable.back_button);

        this.loginToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserTeacherActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }

    public void setFlag(int flag){
        this.flag = flag;
    }

    public int getFlag(){
        return this.flag;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_user,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){


        super.onOptionsItemSelected(item);
        return true;
    }

    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }



    /**********创建的标志******************/
    public void setCreatFlag(int creatFlag){
        this.creatFlag = creatFlag;
    }

    public int getCreatFlag(){
        return this.creatFlag;
    }
    /********************************/



    /***********获取本地所创建的名字*****/
    public void setLocalname(String localname){
        this.localname = localname;
    }

    public String getLocalname(){
        return this.localname;
    }

    public void setLocalteachername(String localteachername){
        this.localteachername = localteachername;
    }

    public String getLocalteachername(){
        return this.localteachername;
    }
    /***/


    public void setTempName(String tempname){
        this.tempname = tempname;
    }
    public String getUser(){
        return UserTeacherActivity.this.tempname;
    }

    public void setUserID(String userID){
        this.userID = userID;
    }
    public String getUserID(){
        return this.userID;
    }

    /**********************************/


    private void initList() {
        SettingItem item1 = new SettingItem(new String("姓名"), this.getUsername());
        mItemList1.add(item1);
        SettingItem item2 = new SettingItem(new String("工号"), this.getUserID());
        mItemList1.add(item2);
        SettingItem item5 = new SettingItem(new String("修改密码"), new String(""));
        mItemList2.add(item5);
        SettingItem item6 = new SettingItem(new String("退出"), new String(""));
        mItemList2.add(item6);

        SettingItem item7 = new SettingItem(new String("创建会话"), new String(""));
        mItemList3.add(item7);
        SettingItem item8 = new SettingItem(new String("创建班课"), new String(""));
        mItemList3.add(item8);
    }



    private class CreatClass extends Thread {
        private static final String url = "http://115.28.80.81/app/check.php";
        private String classname = null;
        private String teachername = null;

        public CreatClass(String classname, String teachername) {
            this.classname = classname;
            this.teachername = teachername;
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
                    if (jsonObject.getInt("status") != 0) {

                        UserTeacherActivity.this.handler.sendEmptyMessage(1);
                    } else {
                        UserTeacherActivity.this.handler.sendEmptyMessage(0);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //创建会话

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
                params.add(new BasicNameValuePair("userName", UserTeacherActivity.this.getUsername()));
                params.add(new BasicNameValuePair("groupName", talkname));

                System.out.println("user-------name = " + UserTeacherActivity.this.getUsername());
                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                HttpResponse response = new DefaultHttpClient().execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String str = EntityUtils.toString(response.getEntity());
                    System.out.println("JSON1-------->" + str);
                    JSONObject jsonObject = new JSONObject(str);
                    //System.out.println("STATUS------------------->" + jsonObject.getInt("status"));
                    //ClassnumberActivity.this.flag = jsonObject.getBoolean("status");
                    //setFlag(jsonObject.getInt("status"));
                    talkflag = jsonObject.getInt("groupId");
                    if(jsonObject.getInt("groupId") != 0){
                        mainActivity.setHash_info(1);
                        UserTeacherActivity.this.handler.sendEmptyMessage(2);
                    }else{
                        UserTeacherActivity.this.handler.sendEmptyMessage(0);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //修改密码

    private class ReFactor extends Thread{
        private String new_pass = null;
        private String old_pass = null;
        private static final String url = "http://115.28.80.81/app/check.php";

        public ReFactor(String new_pass,String old_pass){
            this.new_pass = new_pass;
            this.old_pass = old_pass;
        }

        @Override
        public void run() {

            try {


                HttpPost request = new HttpPost(url);

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("action", "refactor"));
                params.add(new BasicNameValuePair("password", old_pass));
                params.add(new BasicNameValuePair("newpassword", new_pass));
                params.add(new BasicNameValuePair("flag","1"));
                params.add(new BasicNameValuePair("userName", UserTeacherActivity.this.getUsername()));

                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                HttpResponse response = new DefaultHttpClient().execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                    String str = EntityUtils.toString(response.getEntity());

                    JSONObject jsonObject = new JSONObject(str);

                    System.out.println("jsonObject.getBoolean(status)" + jsonObject.getBoolean("status"));
                    if (jsonObject.getBoolean("status")){
                        UserTeacherActivity.this.handler.sendEmptyMessage(4);
                        Thread.sleep(2000);//2秒后跳转至登陆页面
                        mainActivity.setLoginFlag(0);
                        mainActivity.setJoin(0);
                        //mainActivity.getDatalist().setAdapter(null);
                        System.out.println("****************************" + mainActivity.getJoin());
                        Intent intent = new Intent(UserTeacherActivity.this,Login1.class);
                        startActivity(intent);
                        finish();
                    }else{
                        UserTeacherActivity.this.handler.sendEmptyMessage(3);
                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

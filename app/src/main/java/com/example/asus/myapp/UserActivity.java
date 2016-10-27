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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

public class UserActivity extends AppCompatActivity {
    private Toolbar loginToolbar = null;
    private MainActivity mainActivity = null;
    //private TextView name = null;

    private static String username = null;
    private static String userID = null;

    private ListView mListView1;
    private ListView mListView2;
    private ItemArrayAdapter mAdapter;
    private List<SettingItem> mItemList1 = new ArrayList<SettingItem>();
    private List<SettingItem> mItemList2 = new ArrayList<SettingItem>();

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

                    Toast.makeText(UserActivity.this, "创建失败，服务器未响应", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(UserActivity.this, "创建成功 编号为：", 7).show();


                    break;
                case 2:

                    Toast.makeText(UserActivity.this, "创建成功 编号为：", 7).show();


                    break;

                case 3:
                    Toast.makeText(UserActivity.this, "原密码错误", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(UserActivity.this, "修改成功", Toast.LENGTH_SHORT).show();

                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_test);

        this.loginToolbar = (Toolbar)super.findViewById(R.id.logintoolbar);
        this.loginToolbar.setTitle("");
        setSupportActionBar(this.loginToolbar);

        initList();

        mListView1 = (ListView) findViewById(R.id.info_show);
        mListView2 = (ListView) findViewById(R.id.quit_list);
        mAdapter = new ItemArrayAdapter(UserActivity.this, R.layout.item, mItemList1);
        mListView1.setAdapter(mAdapter);
        mAdapter = new ItemArrayAdapter(UserActivity.this, R.layout.item, mItemList2);
        mListView2.setAdapter(mAdapter);

        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0 :
                        //Toast.makeText(UserActivity.this,"0",Toast.LENGTH_SHORT).show();

                        final Dialog dialog_pass = new Dialog(UserActivity.this,R.style.refactor_pass);
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
                                    Toast.makeText(UserActivity.this,"两次新密码不一致",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        dialog_pass.show();



                        break;
                    case 1 :
                        Dialog dialog = new AlertDialog.Builder(UserActivity.this)
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
                                        Intent intent = new Intent(UserActivity.this,Login1.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).create();
                        dialog.show();
                        //Toast.makeText(UserActivity.this,"1",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        //this.name = (TextView)super.findViewById(R.id.user);
        //this.name.setText(getUsername());
        this.mainActivity = new MainActivity();


        this.loginToolbar.setNavigationIcon(R.drawable.back_button);
        this.loginToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }

    public void setUserID(String userID){
        this.userID = userID;
    }
    public String getUserID(){
        return this.userID;
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

    private void initList() {
        SettingItem item1 = new SettingItem(new String("姓名"), this.getUsername());
        mItemList1.add(item1);
        SettingItem item2 = new SettingItem(new String("学号"), this.getUserID());
        mItemList1.add(item2);
        SettingItem item5 = new SettingItem(new String("修改密码"), new String(""));
        mItemList2.add(item5);
        SettingItem item6 = new SettingItem(new String("退出"), new String(""));
        mItemList2.add(item6);

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
                params.add(new BasicNameValuePair("flag","0"));
                params.add(new BasicNameValuePair("userName", UserActivity.this.getUsername()));

                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                HttpResponse response = new DefaultHttpClient().execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                    String str = EntityUtils.toString(response.getEntity());

                    JSONObject jsonObject = new JSONObject(str);

                    System.out.println("jsonObject.getBoolean(status)" + jsonObject.getBoolean("status"));
                    if (jsonObject.getBoolean("status")){
                        UserActivity.this.handler.sendEmptyMessage(4);
                        Thread.sleep(2000);//2秒后跳转至登陆页面
                        mainActivity.setLoginFlag(0);
                        mainActivity.setJoin(0);
                        //mainActivity.getDatalist().setAdapter(null);
                        System.out.println("****************************" + mainActivity.getJoin());
                        Intent intent = new Intent(UserActivity.this,Login1.class);
                        startActivity(intent);
                        finish();
                    }else{
                        UserActivity.this.handler.sendEmptyMessage(3);
                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
